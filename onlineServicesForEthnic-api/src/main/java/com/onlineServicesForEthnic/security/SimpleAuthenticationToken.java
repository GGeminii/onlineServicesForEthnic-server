package com.onlineServicesForEthnic.security;

import com.onlineServicesForEthnic.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * <p>继承了{@link UsernamePasswordAuthenticationToken}</p>
 * <p>意在简化{@link UsernamePasswordAuthenticationToken}的权限设置</p>
 */
public class SimpleAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public SimpleAuthenticationToken(Object principal, Object credentials, Role role) {
        super(principal, credentials, List.of(role));
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    public Role getRole() {
        return (Role) super.getAuthorities().stream().toList().get(0);
    }
}
