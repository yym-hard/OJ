package com.yym.ojbackendjudgeservice.judge;

import com.yym.ojbackendmodel.model.entity.QuestionSubmit;

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
