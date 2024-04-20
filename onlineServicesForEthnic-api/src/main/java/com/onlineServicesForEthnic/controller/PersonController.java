package com.onlineServicesForEthnic.controller;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.onlineServicesForEthnic.constant.Reference;
import com.onlineServicesForEthnic.constant.ResultData;
import com.onlineServicesForEthnic.dto.JsonRequestDto;
import com.onlineServicesForEthnic.entity.Person;
import com.onlineServicesForEthnic.entity.Role;
import com.onlineServicesForEthnic.exception.CheckException;
import com.onlineServicesForEthnic.security.DBUserDetailManager;
import com.onlineServicesForEthnic.service.PersonService;
import com.onlineServicesForEthnic.utils.CheckUtil;
import com.onlineServicesForEthnic.utils.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.onlineServicesForEthnic.entity.table.PersonTableDef.PERSON;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/persons")
@Tag(name = "人员管理")
public class PersonController {
    private final DBUserDetailManager dbUserDetailManager;
    private final CheckUtil checkUtil;
    private final MapperUtil mapperUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonService personService;

    @Value("${onlineServicesForEthnic.default.image-url}")
    private String defaultAvatar ;
    /**
     * 用户注册
     *
     * @param role   角色类型：admin或user
     * @param person 用户信息
     * @return 注册结果
     */
    @PostMapping("/{role}")
    @Operation(summary = "注册用户")
    public ResultData<?> register(@RequestBody Person person, @PathVariable String role) {
        log.info("注册{}：{}", role, person);
        //检查
        checkUtil.checkUsernameSyntax(person.getUsername(), true);
        checkUtil.checkPasswordSyntax(person.getPassword());
        checkUtil.checkPhoneNumSyntax(person.getPhoneNum(), true);
        checkUtil.checkEmailSyntax(person.getEmail(), true);
        if (role.equals(Reference.ROLE_MAP.get(Role.ADMIN)) || role.equals(Reference.ROLE_MAP.get(Role.USER))) {
            person.setRole(role);
        }else {
            throw new CheckException("非法角色", "不存在该角色");
        }
        //设置默认字段
        person.setAvatar(this.defaultAvatar);
        if(person.getGender()==null){
            person.setGender(0);
        }
        //加密
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        //注册
        dbUserDetailManager.createUser(person);
        return ResultData.ok("注册成功");
    }

    /**
     * 更新用户信息
     *
     * @param id     用户id
     * @param person 用户更新信息(除角色和密码)
     * @return 更新结果
     */
    @PutMapping("/info/{id}")
    @Operation(summary = "更新当前用户信息")
    public ResultData<?> update(@PathVariable Long id, @RequestBody Person person) {
        //检查
        Person originPerson = mapperUtil.getPersonById(id);
        String originName = originPerson.getUsername();
        String originEmail = originPerson.getEmail();
        String originPhoneNum = originPerson.getPhoneNum();
        String username = person.getUsername();
        String phoneNum = person.getPhoneNum();
        String email = person.getEmail();
        if (!originName.equals(username)) {
            checkUtil.checkUsernameSyntax(username, true);
        }
        if(!originEmail.equals(email)){
            checkUtil.checkEmailSyntax(person.getEmail(), true);
        }
        if(!originPhoneNum.equals(phoneNum)){
            checkUtil.checkPhoneNumSyntax(person.getPhoneNum(), true);
        }
        //设置默认值
        if(StrUtil.isBlank(person.getAvatar())){
            person.setAvatar(this.defaultAvatar);
        }
        //更新
        person.setId(id);
        dbUserDetailManager.updateUser(person);
        return ResultData.ok("更新成功");
    }

    /**
     * 修改密码
     *
     * @param passwordRequest 密码JSON
     * @return ResultData
     * @throws CheckException 密码格式or匹配错误
     */
    @PutMapping("/password")
    @Operation(summary = "修改当前用户密码")
    public ResultData<?> updatePassword(@RequestBody JsonRequestDto.PasswordRequest passwordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();
        //检验
        checkUtil.checkPasswordSyntax(newPassword);
        checkUtil.checkPasswordSyntax(oldPassword);
        checkUtil.checkPasswordMatcher(oldPassword, username);
        //修改
        dbUserDetailManager.changePassword(oldPassword, passwordEncoder.encode(newPassword));
        return ResultData.ok("修改密码成功");
    }

    /**
     * 根据用户名获取用户个人信息
     *
     * @return ResultData<Person>
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public ResultData<Person> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResultData.ok((Person) authentication.getPrincipal());
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "根据id获取用户信息")
    public ResultData<Person> getById(@PathVariable Long id) {
        return ResultData.ok(mapperUtil.getPersonById(id));
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/pageInfo")
    @Operation(summary = "分页获取用户列表")
    public ResultData<Page<Person>> getList(JsonRequestDto.PagePersonRequest pagePersonRequest) {
        log.info("分页获取用户列表请求：{}", pagePersonRequest);
        QueryChain<Person> queryChain = personService.queryChain()
            .select(PERSON.DEFAULT_COLUMNS)
            .from(PERSON)
            .eq(Person::getRole, pagePersonRequest.getRole(), StrUtil.isNotBlank(pagePersonRequest.getRole()))
            .eq(Person::getState, pagePersonRequest.getState(), pagePersonRequest.getState() != null)
            .eq(Person::getGender, pagePersonRequest.getGender(), pagePersonRequest.getGender() != null);

        //关键字搜索
        String searchWord = pagePersonRequest.getSearchWord();
        if (StrUtil.isNotBlank(searchWord)) {
            queryChain
                    .and(PERSON.USERNAME.like(searchWord))
                    .or(PERSON.ID.like(searchWord))
                    .or(PERSON.ETHNIC.like(searchWord))
                    .or(PERSON.STUDENT_ID.like(searchWord))
                    .or(PERSON.WORK_AT.like(searchWord))
                    .or(PERSON.REAL_NAME.like(searchWord));
        }
        queryChain.orderBy(PERSON.UPDATED_AT.desc());
        Page<Person> page = queryChain.page(Page.of(pagePersonRequest.getPageNum(), pagePersonRequest.getPageSize()));
        return ResultData.ok(page);
    }
}
