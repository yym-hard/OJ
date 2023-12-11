package com.yym.ojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.yym.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yym.ojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.yym.ojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.yym.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yym.ojbackendmodel.common.ErrorCode;
import com.yym.ojbackendmodel.exception.BusinessException;
import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yym.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.yym.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.yym.ojbackendmodel.model.dto.question.JudgeCase;
import com.yym.ojbackendmodel.model.entity.Question;
import com.yym.ojbackendmodel.model.entity.QuestionSubmit;
import com.yym.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.yym.ojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;
    @Value("${codesandbox.type:example}")
    private String type;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1.根据传入的题目提交id,查询提交信息(包含代码、编程语言等)
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        //获取题目id
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        //2.判断题目提交信息的判题状态是否为等待中，若不为等待中，就不用重复执行了。
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目题目正在判题中");
        }
        //3.更新题目提交状态为判题中，避免后续重复执行代码
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        //4.调用代码沙箱，获取到执行结果
        //根据沙箱类型在沙箱工厂中获取对应沙箱实例
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        //获取当前沙箱实例的代理类对象
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inPutList = judgeCaseList.stream().map(JudgeCase::getInPut).collect(Collectors.toList());
        //构建执行代码的请求对象
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inPutList)
                .build();
        //调用沙箱执行代码
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        //5.调用策略模式的判题策略，将题目的正确结果与运行结果进行比对
        //设置上下文环境（需要的参数）
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inPutList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        //调用judgeManager，根据语言去选择判题策略
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //6.修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
