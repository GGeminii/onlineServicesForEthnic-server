package com.onlineServicesForEthnic.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一个通用的entity抽象类
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Accessors(chain = true)
public abstract class EntityBase implements Serializable {
    @Id
    private Long id;
    @Column(onInsertValue = "now()")
    @Schema(title = "createdAt", description = "创建时间")
    private LocalDateTime createdAt;
    @Column(onUpdateValue = "now()", onInsertValue = "now()")
    @Schema(title = "updatedAt", description = "更新时间")
    private LocalDateTime updatedAt;
    @Column(isLogicDelete = true)
    @Schema(title = "isDelete", description = "是否删除：1(已删除)，0(未删除)", defaultValue = "0")
    private Integer isDelete;
}
