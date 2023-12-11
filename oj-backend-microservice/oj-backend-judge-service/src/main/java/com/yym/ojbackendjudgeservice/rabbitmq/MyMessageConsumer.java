package com.yym.ojbackendjudgeservice.rabbitmq;

import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.Channel;
import com.yym.ojbackendjudgeservice.judge.JudgeService;
import com.yym.ojbackendmodel.common.ErrorCode;
import com.yym.ojbackendmodel.exception.BusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")//ackMode = "MANUAL":手动确认消息是否消费成功
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        if (StrUtil.isBlank(message)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交id不存在");
        }
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionSubmitId);
            //确认消息消费成功
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //第二个参数设置为true：失败后不重新处理
            channel.basicNack(deliveryTag, false, false);
        }
    }
}