package com.onlineServicesForEthnic.exception;

import com.onlineServicesForEthnic.constant.Wrapper;
import lombok.Getter;

import java.util.List;

/**
 * <p>用来检查点什么东西</p>
 */
@Getter
public class CheckException extends IllegalArgumentException {

    //检查失败的原因
    private final String reason;

    private final Wrapper<?> attachment;

    public CheckException(String state, String reason, Wrapper<?> attachment) {
        //state是检查失败的状态，比如"无效的密码"
        super(state + ": " + reason);
        this.reason = reason;
        this.attachment = attachment;
    }

    public CheckException(String state, String reason) {
        this(state, reason, null);
    }

    /**
     * 检查候选者的状态是否通过验证
     *
     * @param state      失败状态信息
     * @param candidate  待检查对象
     * @param validators 验证器列表，如果通过验证则返回null，否则返回失败原因
     * @throws CheckException 如果候选者的状态未通过验证，则抛出异常
     */
    public static <T> void check(String state, T candidate, List<Validator<T>> validators) throws CheckException {
        for (Validator<T> validator : validators) {
            String reason = validator.validate(candidate);
            if (reason != null) {
                throw new CheckException(state, reason, Wrapper.of(candidate));
            }
        }
    }

    //检查器，类型需要满足输入一个T类型，返回一个String
    @FunctionalInterface
    public interface Validator<T> {
        String validate(T candidate);
    }

}
