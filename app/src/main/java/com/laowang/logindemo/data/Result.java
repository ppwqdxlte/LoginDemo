package com.laowang.logindemo.data;

import java.io.Serializable;

/**
 * A generic class that holds a result success w/ data or an error exception.
 * 不需要序列化版本UID因为安卓app不用保存result。。
 */
public class Result<T> {

    //    private static final Long serialVersionUID = 1L;
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString()
                    + " , message=" + success.getMsg()
                    + " , code=" + success.getCode() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString()
                    + " , message=" + error.getMsg()
                    + " , code=" + error.getCode() + "]";
        } else if (this instanceof Result.Problem) {
            Result.Problem problem = (Result.Problem) this;
            return "Problem[data=" + problem.getData().toString()
                    + " , message=" + problem.getMsg()
                    + " , code=" + problem.getCode() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;
        private String msg;
        private Integer code;

        public Success(T data, String msg) {
            this.data = data;
            this.msg = msg;
        }

        public Success(T data, String msg, Integer code) {
            this.data = data;
            this.msg = msg;
            this.code = code;
        }

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }

        public String getMsg() {
            return msg;
        }

        public Integer getCode() {
            return code;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private Exception error;
        private String msg;
        private Integer code;

        public Error(Exception error) {
            this.error = error;
        }

        public Error(String msg, Exception error) {
            msg = msg;
            this.error = error;
        }

        public Error(Exception error, String msg, Integer code) {
            this.error = error;
            this.msg = msg;
            this.code = code;
        }

        public Error(String msg, Integer code) {
            this.msg = msg;
            this.code = code;
        }

        public Exception getError() {
            return this.error;
        }

        public String getMsg() {
            return msg;
        }

        public Integer getCode() {
            return code;
        }
    }

    // Problem sub-class eg.problem response rather than an exception
    public final static class Problem<T> extends Result {
        private T data;
        private String msg;
        private Integer code;

        public Problem(T data, String msg) {
            this.data = data;
            this.msg = msg;
        }

        public Problem(T data, String msg, Integer code) {
            this.data = data;
            this.msg = msg;
            this.code = code;
        }

        public Problem(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }

        public String getMsg() {
            return msg;
        }

        public Integer getCode() {
            return code;
        }
    }
}