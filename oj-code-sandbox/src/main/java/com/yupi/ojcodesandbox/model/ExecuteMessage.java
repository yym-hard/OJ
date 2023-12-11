package com.yupi.ojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {
    /**
     * 程序退出码
     */
    private Integer exitValue;
    /**
     * 执行成功消息
     */
    private String message;
    /**
     * 执行错误信息
     */
    private String errorMessage;
    /**
     * 程序执行时间
     */
    private Long time;
    /**
     * 执行内存
     */
    private Long memory;
}
