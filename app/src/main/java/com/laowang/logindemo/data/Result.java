package com.laowang.logindemo.data;

import java.io.Serializable;

public class Result<T> {

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