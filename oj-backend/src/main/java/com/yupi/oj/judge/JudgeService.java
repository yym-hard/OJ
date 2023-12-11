package com.yupi.oj.judge;

import com.yupi.oj.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param QuestionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long QuestionSubmitId);
}
