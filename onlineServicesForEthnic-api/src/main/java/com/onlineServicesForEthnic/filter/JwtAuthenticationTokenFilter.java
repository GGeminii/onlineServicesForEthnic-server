package com.onlineServicesForEthnic.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineServicesForEthnic.constant.Code;
import com.onlineServicesForEthnic.constant.MessageConstant;
import com.onlineServicesForEthnic.constant.Reference;
import com.onlineServicesForEthnic.constant.ResultData;
import com.onlineServicesForEthnic.entity.Person;
import com.onlineServicesForEthnic.security.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT 登录授权过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    // JWT 工具类
    private final JwtTokenUtil jwtTokenUtil;

    private final ObjectMapper objectMapper;

    /**
     * 从请求中获取 JWT 令牌，并根据令牌获取用户信息，最后将用户信息封装到 Authentication 中，方便后续校验（只会执行一次）
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet 异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 排除不需要认证的路径
        for (AntPathRequestMatcher antPathRequestMatcher : Reference.PERMIT_ALL_MATCHER) {
            if (antPathRequestMatcher.matches(request)) {
                log.info("放行{}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        }
        log.info("拦截到请求{}", request.getRequestURI());
        // 从请求中获取 JWT 令牌的请求头（即：Authorization）
        String authHeader = request.getHeader(MessageConstant.JWT_HEADER_NAME);

        // 如果请求头不为空，并且以 JWT 令牌前缀（即：Bearer）开头
        if (authHeader != null && authHeader.startsWith(MessageConstant.JWT_TYPE)) {
            // 获取 JWT 令牌的内容（即：去掉 JWT 令牌前缀后的内容）
            String authToken = authHeader.substring(MessageConstant.JWT_TYPE.length());
            // 从 JWT 令牌中获取用户名
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // 记录日志
            log.info("checking username:{}", username);

            // 如果用户名不为空，并且 SecurityContextHolder 中的 Authentication 为空（表示该用户未登录）
            if (username != null) {
                log.info("授权session为：{}", request.getSession().getId());
                Person person = null;
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof Person) {
                        person = (Person) principal;
                        // 这里可以获取用户的权限信息等
                        log.info("授权：用户已认证authentication为：{}", authentication);
                    }
                } else {
                    log.info("没有存入授权信息，认证失败");
                }
                // 如果 JWT 令牌有效
                if (person != null && jwtTokenUtil.validateToken(authToken, person)) {
                    // 记录日志
                    log.info("authenticated user:{}", username);
                    // 放行，执行下一个过滤器
                    filterChain.doFilter(request, response);
                    return;
                }
                log.info("令牌过期或无效");
            }
        }
        // 认证失败，返回401
        log.info("authenticated fail");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(MessageConstant.JWT_FAIL_HEADER, MessageConstant.JWT_TYPE);
        response.setContentType(MessageConstant.ContentType_JSON);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String jsonPayload = objectMapper.writeValueAsString(
            ResultData.error(Code.UnAuthorization
                , "无效的Authorization，请先登录后再访问")
        );
        response.getWriter().write(jsonPayload);
    }
}
