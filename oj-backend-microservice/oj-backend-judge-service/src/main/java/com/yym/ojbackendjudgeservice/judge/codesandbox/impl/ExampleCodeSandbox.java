package com.yym.ojbackendjudgeservice.judge.codesandbox.impl;


import com.yym.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.yym.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.yym.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.yym.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        //获取输入用例
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        //模拟判题，设置执行的响应结果
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.Accepted.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        //返回
        return executeCodeResponse;
    }
}
