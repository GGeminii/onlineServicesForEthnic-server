package com.onlineServicesForEthnic.constant;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.onlineServicesForEthnic.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.Map;

//待完善的权限参考
@RequiredArgsConstructor
public class Reference {

    //角色信息
    public static final BiMap<Role, String> ROLE_MAP = HashBiMap.create(Map.of(
            Role.ADMIN, "admin",
            Role.USER, "user"
    ));

    //放行路径
    public static final List<AntPathRequestMatcher> PERMIT_ALL_MATCHER = List.of(
        AntPathRequestMatcher.antMatcher("/login/{type}"),
        AntPathRequestMatcher.antMatcher("/persons/user"),
        AntPathRequestMatcher.antMatcher("/doc.html"),
        AntPathRequestMatcher.antMatcher("/webjars/**"),
        AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
        AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
        AntPathRequestMatcher.antMatcher("/v2/api-docs"),
        AntPathRequestMatcher.antMatcher("/v2/api-docs-ext"),
        AntPathRequestMatcher.antMatcher("/favicon.ico")

    );
}