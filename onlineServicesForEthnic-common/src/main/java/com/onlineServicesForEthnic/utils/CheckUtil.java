package com.onlineServicesForEthnic.utils;

import cn.hutool.core.util.StrUtil;
import com.onlineServicesForEthnic.constant.RegexPatterns;
import com.onlineServicesForEthnic.exception.CheckException;
import com.onlineServicesForEthnic.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.onlineServicesForEthnic.entity.table.PersonTableDef.PERSON;


/**
 * <p>检查工具类</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    private final MapperUtil mapperUtil;

    public final PersonMapper personMapper;

    /**
     * 用户名的校验
     *
     * @param username      用户名
     * @param isCheckUnique 是否要验证唯一
     */
    public void checkUsernameSyntax(String username, Boolean isCheckUnique) throws CheckException {
        List<CheckException.Validator<String>> validators = List.of(
            candidate -> (candidate == null || candidate.isEmpty()) ? "用户名不能为空" : null,
            candidate -> (isCheckUnique && mapperUtil.getPersonByUsername(username) != null) ? "用户名已存在" : null
        );
        CheckException.check("无效的用户名", username, validators);
    }

    /**
     * 单纯的密码格式的校验
     *
     * @param password 密码
     */
    public void checkPasswordSyntax(String password) throws CheckException {
        List<CheckException.Validator<String>> validators = List.of(
            candidate -> (candidate == null || candidate.isEmpty()) ? "密码不能为空" : null,
            candidate -> !candidate.matches(RegexPatterns.PASSWORD_REGEX) ? "密码长度不能小于8且必须包含数字和字母" : null
        );
        CheckException.check("无效的密码", password, validators);
    }

    /**
     * 密码校验加匹配
     *
     * @param password 密码
     * @param username 用户名
     */
    public void checkPasswordMatcher(String password, String username) throws CheckException {
        List<CheckException.Validator<String>> validators = List.of(
            candidate -> (!passwordEncoder.matches(password, mapperUtil.getPersonByUsername(username).getPassword()) ? "密码输入错误" : null)
        );
        CheckException.check("无效的密码", password, validators);
    }

    /**
     * 电话号码验证
     * @param phoneNum 电话号码
     * @param isCheckUnique 是否要验证唯一
     * @throws CheckException 异常
     */
    public void checkPhoneNumSyntax(String phoneNum,Boolean isCheckUnique) throws CheckException {
        List<CheckException.Validator<String>> validators = List.of(
                candidate -> (StrUtil.isNotBlank(candidate) && !candidate.matches(RegexPatterns.PHONE_REGEX)) ? "电话号码格式错误" : null,
                candidate -> isCheckUnique && (personMapper.selectOneByCondition(PERSON.PHONE_NUM.eq(candidate)) != null) ? "电话号码已存在" : null
        );
        CheckException.check("无效的电话号码", phoneNum, validators);
    }

    /**
     * 电话号码验证
     * @param email 邮箱
     * @param isCheckUnique 是否要验证唯一
     * @throws CheckException 异常
     */
    public void checkEmailSyntax(String email,Boolean isCheckUnique) throws CheckException {
        List<CheckException.Validator<String>> validators = List.of(
                candidate -> ((StrUtil.isNotBlank(candidate) && !candidate.matches(RegexPatterns.EMAIL_REGEX))) ? "邮箱格式错误" : null,
                candidate -> isCheckUnique && (personMapper.selectOneByCondition(PERSON.EMAIL.eq(candidate)) != null) ? "邮箱已存在" : null
        );
        CheckException.check("无效的邮箱", email, validators);
    }

}
