package com.zpc.mq;

import com.alibaba.fastjson.JSONObject;
import com.zpc.service.MailService;
import com.zpc.entity.MailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Created by zpc on 16/12/28.
 */
@Component
public class MailQueueMassageListener implements SessionAwareMessageListener<Message> {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination destination;

    @Autowired
    private MailService mailService;


    public synchronized void onMessage(Message message, Session session) throws JMSException {
        try {
            TextMessage textMessage = (TextMessage) message;

            final String ms = textMessage.getText();

            System.out.println("收到消息：" + ms);
            //转换成相应的对象

            MailEntity mail = JSONObject.parseObject(ms, MailEntity.class);

            if (mail == null) {
                return;
            }

            try {
                //执行发送邮件
                mailService.mailSend(mail);
            } catch (Exception e) {
                // 发送异常，重新放回队列
//				jmsTemplate.send(mailQueue, new MessageCreator() {
//					@Override
//					public Message createMessage(Session session) throws JMSException {
//						return session.createTextMessage(ms);
//					}
//				});
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
