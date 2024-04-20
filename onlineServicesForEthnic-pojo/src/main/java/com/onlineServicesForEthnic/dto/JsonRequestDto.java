package com.onlineServicesForEthnic.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

/**
 * <p>用于Controller接收Json转换为对象的DTO</p>
 */
@Component
@RequiredArgsConstructor
public class JsonRequestDto {

    /**
     * 一个抽象JSON请求
     */
    public static abstract class AbstractRequest {
    }

    /**
     * 用于分页的抽象JSON请求
     */
    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class AbstractPageRequest {
        private Long pageNum;
        private Long pageSize;
    }


    /**
     * 修改密码请求
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordRequest extends AbstractRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class PagePersonRequest extends AbstractPageRequest {
        private String searchWord;
        private Integer gender;
        private Integer state;
        private String role;
    }

}
