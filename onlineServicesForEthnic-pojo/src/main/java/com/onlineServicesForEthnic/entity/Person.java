package com.onlineServicesForEthnic.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.List;


/**
 * 针对本项目的一个UserDetails封装的Person
 */
@Table("person")
@SuperBuilder
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
public class Person extends EntityBase implements UserDetails {
    @Serial
    private static final long serialVersionUID = 520L;

    // 人员的登录名称
    private String username;

    // 民族
    private String ethnic;

    // 头像
    private String avatar;

    // 真实姓名
    private String realName;

    // 性别：1.男; 2.女; 0:未知
    private Integer gender;

    // 角色: admin; student; volunteer
    private String role;

    // 学校/行业
    private String workAt;

    // 学号
    private String studentId;

    // 状态：1.正常；2.锁定
    private Integer state;

    // 用户邮箱
    private String email;

    // 用户的手机号码
    private String phoneNum;

    // 用户的登录密码
    private String password;

    @Override
    public List<Role> getAuthorities() {
        return Role.of(role).isPresent() ? List.of(Role.of(role).get()) : null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return state == 1;
    }
}
