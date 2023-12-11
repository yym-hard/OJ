package com.yupi.oj.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String inPut;

    /**
     * 输出用例
     */
    private String outPut;

}
