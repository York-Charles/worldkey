package com.worldkey.controller;

import com.worldkey.exception.Z406Exception;
import com.worldkey.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * @author HP
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = {ConstraintViolationException.class, IllegalArgumentException.class})
    @ResponseStatus
    @ResponseBody
    public ResultUtil constraintViolationException(ConstraintViolationException ex) {
       /* ex.printStackTrace();*/
        return new ResultUtil(ResultUtil.PARAMETER_ERROR, "no", ex.getMessage());
    }
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultUtil noHandlerFoundException(NoHandlerFoundException ex) {
        return new ResultUtil(ResultUtil.NOT_FOUNT, "no", "404,请查看网址是否正确：" + ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus
    @ResponseBody
    public ResultUtil methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
         ex.printStackTrace();
        return new ResultUtil(ResultUtil.PARAMETER_ERROR, "no", "参数类型错误："+ex.getMessage());
    }
    @ExceptionHandler(value = {Z406Exception.class})
    @ResponseStatus
    @ResponseBody
    public ResultUtil z406Exception(Z406Exception ex) {
        log.info(ex.getMessage());
        return new ResultUtil(ResultUtil.SYSTEM_ERROR, "no", ex.getMessage());
    }
    @ExceptionHandler(value = {UnauthenticatedException.class})
    public String unauthenticatedException(){
        return "redirect:/admin/login";
    }

    @ResponseBody
    @ResponseStatus
    @ExceptionHandler(value = {Exception.class})
    public ResultUtil unknownException(Exception ex) {
        ex.printStackTrace();
        return new ResultUtil(ResultUtil.SYSTEM_ERROR, "no", ex.getMessage());
    }


}
