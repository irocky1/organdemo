package com.rocky.me.research.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by rocky on 2018/3/10.
 */
public class CommonResult<T> {

    private final static String RETURN_CODE_SUCCESS = "0";
    private final static String RETURN_CODE_FAIL = "-1";
    private final static String RETURN_MSG_SUCCESS = "操作成功";
    private final static String RETURN_MSG_FAIL = "操作失败";

    private T data;
    private CodeMessage meta = new CodeMessage();

    public CommonResult(){}

    public CommonResult(T data) {
        this(RETURN_CODE_SUCCESS,RETURN_MSG_SUCCESS,data);
    }

    public CommonResult(String code, String message) {
        this(code, message, null);
    }

    public CommonResult(String code, String message, T data) {
        this.meta.setCode(code);
        this.meta.setMessage(message);
        this.data = data;
    }

    public static <T> CommonResult success(T data){
        return new CommonResult(data);
    }
    public static <T> CommonResult success(){
        return new CommonResult(null);
    }

    public static <T> CommonResult fail(T data){
        return new CommonResult(RETURN_CODE_FAIL,RETURN_MSG_FAIL,data);
    }
    public static <T> CommonResult fail(){
        return new CommonResult(RETURN_CODE_FAIL,RETURN_MSG_FAIL,null);
    }
    public static <T> CommonResult fail(String code,String message,T data){
        return new CommonResult(code,message,data);
    }
    public static <T> CommonResult fail(String message){
        return new CommonResult(RETURN_CODE_FAIL,message,null);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CodeMessage getMeta() {
        return meta;
    }

    public void setMeta(CodeMessage meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE, false);
    }

    public class CodeMessage{
        private String code;
        private String message;

        public CodeMessage(String code, String message) {
            this.code = code;
            this.message = message;
        }
        public CodeMessage(){}

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE, false);
        }
    }
}
