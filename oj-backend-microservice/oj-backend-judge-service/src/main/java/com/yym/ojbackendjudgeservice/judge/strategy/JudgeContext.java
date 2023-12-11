package com.yym.ojbackendjudgeservice.judge.strategy;

import com.yym.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.yym.ojbackendmodel.model.dto.question.JudgeCase;
import com.yym.ojbackendmodel.model.entity.Question;
import com.yym.ojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文(用于定义在策略中传递的参数)
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    /**
     * 沙箱执行结果的输出
     */
    private List<String> outputList;

    /**
     * 题目的判题用例
     */
    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
