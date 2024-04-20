package com.onlineServicesForEthnic.constant;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static com.onlineServicesForEthnic.constant.MessageConstant.SUCCESS;


@Data
@Accessors(chain = true)
//封装返回值
public class ResultData<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    private ResultData() {
    }


//------------------------------------------------------------------------------------------------------

    /**
     * 专用于与硬件的成功结果返回
     */
    public static ResultData<?> success() {
        return new ResultData<>().setMessage(SUCCESS).setCode(Code.Success.getCode());
    }

    public static <T> ResultData<T> success(T data) {
        return new ResultData<T>().setMessage(SUCCESS).setCode(Code.Success.getCode()).setData(data);
    }

//----------------------------------------------------------------------------------------------------------

    /**
     * 与前端交互使用以下返回方法
     */
    public static ResultData<?> ok(String message) {
        return new ResultData<>().setMessage(message).setCode(Code.OK.getCode());
    }

    public static <T> ResultData<T> ok(T data) {
        return new ResultData<T>().setCode(Code.OK.getCode()).setData(data).setMessage(SUCCESS);
    }

    public static <T> ResultData<T> ok(String message, T data) {
        return new ResultData<T>().setMessage(message).setData(data).setCode(Code.OK.getCode());
    }

    public static ResultData<?> error(String message) {
        return new ResultData<>().setMessage(message).setCode(Code.Fail.getCode());
    }

    public static ResultData<?> error(Code code, String message) {
        return new ResultData<>().setMessage(message).setCode(code.getCode());
    }
}
