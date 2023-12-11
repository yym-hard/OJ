package com.yym.ojbackendserviceclient.service;

import com.yym.ojbackendmodel.model.entity.Question;
import com.yym.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author 86157
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2023-11-03 21:29:05
 */
@FeignClient(name = "oj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient{


    /**
     * 根据id获取题目信息
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据题目提交id获取提交信息
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    /**
     * 根据id修改题目提交信息
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
