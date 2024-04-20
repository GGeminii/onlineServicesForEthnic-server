package com.onlineServicesForEthnic.security;
import com.onlineServicesForEthnic.constant.MessageConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtTokenUtil {

    // JWT 密钥
    @Value("${onlineServicesForEthnic.jwt.secret}")
    private String secret;

    // JWT 过期时间
    @Value("${onlineServicesForEthnic.jwt.expiration}")
    private Long expiration;

    //================ private methods ==================

    /**
     * 根据负载生成 JWT 的 token
     *
     * @param claims 负载
     * @return JWT 的 token
     */
    private String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)  // 设置负载
            .setExpiration(generateExpirationDate())  // 设置过期时间
            .setSubject(username)//最后设置所有者，否则被覆盖
            .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))  // 设置签名使用的签名算法和签名使用的秘钥
            .compact();
    }

    /**
     * 生成 token 的过期时间
     *
     * @return token 的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从 token 中获取 JWT 中的负载
     *
     * @param token JWT 的 token
     * @return JWT 中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser() // 解析 JWT 的 token
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8)) // 指定签名使用的密钥
                .parseClaimsJws(token) // 解析 JWT 的 token
                .getBody(); // 获取 JWT 的负载
        } catch (Exception e) {
            log.info("JWT 格式验证失败：{}", token);
        }
        return claims;
    }

    /**
     * 验证 token 是否过期
     *
     * @param token JWT 的 token
     * @return token 是否过期 true：过期 false：未过期
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从 token 中获取过期时间
     *
     * @param token JWT 的 token
     * @return 过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    //================ public methods ==================

    /**
     * 从 token 中获取登录用户名
     *
     * @param token JWT 的 token
     * @return 登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            // 从 token 中获取 JWT 中的负载
            Claims claims = getClaimsFromToken(token);
            // 从负载中获取用户名
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            log.info("获取用户名出错{}", e.getMessage());
        }
        return username;
    }

    /**
     * 验证 token 是否有效
     *
     * @param token       JWT 的 token
     * @param userDetails 从数据库中查询出来的用户信息（需要自定义 UserDetailsService 和 UserDetails）
     * @return token 是否有效 true：有效 false：无效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        // 从 token 中获取用户名
        String username = getUserNameFromToken(token);
        // 条件一：用户名不为 null
        // 条件二：用户名和 UserDetails 中的用户名一致
        // 条件三：token 未过期
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 根据用户信息生成 token
     *
     * @param authentication 用户名
     * @return token 字符串
     */
    public String generateToken(SimpleAuthenticationToken authentication) {
        // 创建负载
        Map<String, Object> claims = new HashMap<>();
        // 设置负载中的用户角色
        claims.put(MessageConstant.CLAM_KEY_ROLE, authentication.getRole().getAuthority());
        // 根据负载生成 token
        return this.generateToken(authentication.getName(), claims);
    }

}
