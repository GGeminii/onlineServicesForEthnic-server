package com.onlineServicesForEthnic.security;

import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.util.UpdateEntity;
import com.onlineServicesForEthnic.entity.Person;
import com.onlineServicesForEthnic.mapper.PersonMapper;
import com.onlineServicesForEthnic.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import static com.onlineServicesForEthnic.entity.table.PersonTableDef.PERSON;


/**
 * 重新定义的security认证逻辑
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DBUserDetailManager implements UserDetailsManager {
    private final PersonMapper personMapper;
    private final MapperUtil mapperUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("账号为{}的用户正在校验用户名", username);
        Person person = mapperUtil.getPersonByUsername(username);
        log.info("查找到{}的用户", person);
        if (person == null) {
            throw new UsernameNotFoundException("不存在" + username);
        }
        String role = person.getRole();
        log.info("用户{}的角色为{}", username, role);
        return person;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        Person person = (Person) userDetails;
        person.setState(1);
        personMapper.insert(person);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        Person person = (Person) userDetails;
        personMapper.update(person, true);
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //修改密码
        Person person = UpdateEntity.of(Person.class);
        person.setPassword(newPassword);
        personMapper.updateByCondition(person, QueryCondition.create(PERSON.USERNAME, username));
    }

    @Override
    public boolean userExists(String username) {
        Person person = mapperUtil.getPersonByUsername(username);
        return person != null && person.getState() == 1;
    }
}
