package com.onlineServicesForEthnic.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;


/**
 * <p>纯粹的包装类</p>
 *
 * @param <T> 包装的数据类型
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wrapper<T> {
    private T data;

    public static <T> Wrapper<T> of(T data) {
        return new Wrapper<>(data);
    }

    public static <T> Wrapper<T> empty() {
        return new Wrapper<>(null);
    }

    public boolean isPresent() {
        return data != null;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(data);
    }

    public T orElse(T other) {
        return this.isPresent() ? data : other;
    }

    public <R> R orElse(Class<R> clazz, R other) {
        try {
            return this.isPresent() ? clazz.cast(this.getData()) : other;
        } catch (ClassCastException e) {
            return other;
        }
    }
}
