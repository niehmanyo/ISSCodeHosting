package com.iss.common.result;

import com.iss.common.constant.HttpStatusMessageConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * Done by CHEN WEIJIAN
 * Return the standard result to frontend
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    /**
     * code: HTTP status code
     * msg: the error message
     * data: vo data
     */
    private Integer code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * Return the successful notification
     * Not need to return data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        return new Result<>(200, HttpStatusMessageConstant.OK);
    }

    /**
     * Return the successful notification
     * Need to return data
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, HttpStatusMessageConstant.OK, data);
    }

    /**
     * Return the successful notification
     * Need to return data and message
     * @param msg
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    /**
     * Return the error message
     * @param code
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * Return the error message with data
     * @param code
     * @param msg
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    // Convenience methods for common status codes

    public static <T> Result<T> badRequest() {
        return new Result<>(400, HttpStatusMessageConstant.BAD_REQUEST);
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(401, HttpStatusMessageConstant.UNAUTHORIZED);
    }

    public static <T> Result<T> notFound() {
        return new Result<>(404, HttpStatusMessageConstant.NOT_FOUND);
    }

    public static <T> Result<T> internalServerError() {
        return new Result<>(500, HttpStatusMessageConstant.INTERNAL_SERVER_ERROR);
    }
}
