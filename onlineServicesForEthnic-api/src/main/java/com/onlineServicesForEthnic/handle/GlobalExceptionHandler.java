package com.onlineServicesForEthnic.handle;

import com.onlineServicesForEthnic.constant.Code;
import com.onlineServicesForEthnic.constant.ResultData;
import com.onlineServicesForEthnic.exception.CheckException;
import com.onlineServicesForEthnic.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResultData<?> handleAccessDeniedException(AccessDeniedException e) {
        log.info(e.getMessage());
        e.printStackTrace();
        return ResultData.error(Code.Forbidden, e.getMessage() + ",你无权访问");
    }

    @ExceptionHandler(ServiceException.class)
    public ResultData<?> handleServiceException(ServiceException e) {
        log.info(e.getMessage());
        e.printStackTrace();
        return ResultData.error(e.getMessage());
    }

    @ExceptionHandler(CheckException.class)
    public ResultData<?> handleCheckException(CheckException e) {
        log.info(e.getMessage());
        e.printStackTrace();
        return ResultData.error(Code.BadRequest, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultData<?> handleException(Exception e) {
        log.info(e.getMessage());
        e.printStackTrace();
        return ResultData.error("System exception, Please Wait...");
    }
}
