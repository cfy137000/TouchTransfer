package com.maodou.touchtransferpc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description 返回
 * @Author Wang Yucui
 * @Date 9/4/2020 3:20 PM
 */
@Getter
@Setter
@ToString
public class RestResult<T> {
    private Integer code;
    private String message;
    private T data;

    public RestResult(final Integer code, final String message, final T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestResult(final Integer code, final String message) {
        super();
        this.code = code;
        this.message = message;
    }

}
