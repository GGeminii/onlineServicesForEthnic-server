package com.onlineServicesForEthnic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineServicesForEthnic.constant.Code;
import com.onlineServicesForEthnic.constant.MessageConstant;
import com.onlineServicesForEthnic.constant.Reference;
import com.onlineServicesForEthnic.constant.ResultData;
import com.onlineServicesForEthnic.entity.Role;
import com.onlineServicesForEthnic.filter.JwtAuthenticationTokenFilter;
import com.onlineServicesForEthnic.security.DBUserDetailManager;
import com.onlineServicesForEthnic.security.JwtTokenUtil;
import com.onlineServicesForEthnic.security.SimpleAuthenticationToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final DataSource dataSource;

    private final ObjectMapper objectMapper;

    private final DBUserDetailManager dbUserDetailManager;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final JwtTokenUtil jwtTokenUtil;


    @Value("${onlineServicesForEthnic.remember-me.tokenValiditySeconds}")
    private int second;

    @Value("${onlineServicesForEthnic.session.maximumSessions}")
    private int maximumSessions;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        //授权配置
        security.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/doc.html").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v2/api-docs").permitAll()
                    .requestMatchers("/v2/api-docs-ext").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    //管理员独有功能权限
                    .requestMatchers("/admin/**").hasRole(Reference.ROLE_MAP.get(Role.ADMIN))
                    //学生独有功能权限
                    .requestMatchers("/student/**").hasAnyRole(Reference.ROLE_MAP.get(Role.USER))
                    //注册权限
                    .requestMatchers("/persons/user").anonymous()
                    .requestMatchers("/persons/admin").hasRole(Reference.ROLE_MAP.get(Role.ADMIN))
                    .anyRequest().authenticated()
            )
            //登录配置
            .formLogin(formLoginConfigurer ->
                formLoginConfigurer
                    .loginProcessingUrl("/login/{type}")
                    .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        SimpleAuthenticationToken simpleAuthenticationToken = new SimpleAuthenticationToken(
                            authentication.getPrincipal(),
                            authentication.getCredentials(),
                            (Role) authentication.getAuthorities().stream().toList().get(0)
                        );
                        String authority = Reference.ROLE_MAP.get(simpleAuthenticationToken.getRole());
                        //获取URI验证身份
                        String requestURI = httpServletRequest.getRequestURI();
                        if (!requestURI.substring(requestURI.lastIndexOf("/") + 1).equals(authority)) {
                            httpServletResponse.setContentType(MessageConstant.ContentType_JSON);
                            httpServletResponse.getWriter()
                                .write(objectMapper.writeValueAsString(ResultData.error(
                                    Code.UnAuthorization, "用户名或密码错误"
                                )));
                            return;
                        }
                        //验证成功
                        simpleAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        // 生成token
                        String token = jwtTokenUtil.generateToken(simpleAuthenticationToken);
                        //设置Authorization头部
                        httpServletResponse.setHeader(MessageConstant.JWT_HEADER_NAME, MessageConstant.JWT_TYPE + token);
                        httpServletResponse.setContentType(MessageConstant.ContentType_JSON);
                        log.info("登录存入认证信息{}", SecurityContextHolder.getContext().getAuthentication());
                        log.info("登录session为：{}", httpServletRequest.getSession().getId());
                        // 返回登录成功结果
                        httpServletResponse
                            .getWriter()
                            .write(objectMapper
                                .writeValueAsString(ResultData
                                    .ok("登录成功"
                                        , (authentication.getPrincipal())
                                    )
                                )
                            );
                    })
                    .failureHandler((httpServletRequest, httpServletResponse, exception) -> {
                        String localizedMessage = exception.getLocalizedMessage();
                        httpServletResponse.setContentType(MessageConstant.ContentType_JSON);
                        httpServletResponse.getWriter()
                            .write(objectMapper.writeValueAsString(ResultData.error(Code.UnAuthorization, localizedMessage)));
                    })
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            //remember-me配置
            .rememberMe(rememberMeConfigurer ->
                rememberMeConfigurer
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(second)
                    .userDetailsService(dbUserDetailManager)
            )
            //登出配置
            .logout(logoutConfigurer ->
                logoutConfigurer
                    .logoutUrl("/logout")
                    .logoutSuccessHandler((req, resp, ao) -> {
                        resp.setContentType(MessageConstant.ContentType_JSON);
                        resp.getWriter().write(objectMapper.writeValueAsString(ResultData.ok("退出成功")));
                    })
            )
            //异常配置
            .exceptionHandling(exceptingHandlingConfigurer ->
                exceptingHandlingConfigurer
                    //无权限访问处理
                    .accessDeniedHandler((req, resp, exception) -> {
                        resp.setContentType(MessageConstant.ContentType_JSON);
                        resp.getWriter().write(objectMapper.writeValueAsString(ResultData.error(Code.Forbidden, "你无权限访问")));
                    })
            )
            //会话配置
            .sessionManagement(session -> session
                .maximumSessions(maximumSessions)
                .expiredSessionStrategy(sessionInformationExpiredEvent -> {
                    HttpServletResponse resp = sessionInformationExpiredEvent.getResponse();
                    resp.setContentType(MessageConstant.ContentType_JSON);
                    resp.getWriter().write(objectMapper.writeValueAsString(ResultData.error("另一个设备登陆了你的账号，即将下线")));
                })
            );
        //开启允许跨域访问
        security.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        security.csrf(AbstractHttpConfigurer::disable);
        security.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

    /**
     * remember-me持久化
     *
     * @return PersistentTokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        //设置数据源
        repository.setDataSource(dataSource);
        //首次初始化，仅在首次开启，用于创建数据库
//        repository.setCreateTableOnStartup(true);
        return repository;
    }


    /**
     * 启用Session并发控制
     *
     * @return HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    /**
     * 加密器
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    /**
     * 跨域配置
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader(MessageConstant.JWT_HEADER_NAME);
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}
