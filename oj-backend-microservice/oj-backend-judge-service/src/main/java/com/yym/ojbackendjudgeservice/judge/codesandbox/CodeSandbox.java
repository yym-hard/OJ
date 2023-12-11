package com.yym.ojbackendjudgeservice.judge.codesandbox;

import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
