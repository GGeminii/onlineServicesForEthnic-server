package com.onlineServicesForEthnic.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;


/**
 * 角色管理枚举类
 */
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ROLE_admin"),
    USER("ROLE_user");



    private final String authority;


    public static Optional<Role> of(String authority) {
        for (Role role : Role.values()) {
            if (role.name().toLowerCase().equals(authority)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }


    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }

}