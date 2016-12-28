package com.zpc.service;

import com.zpc.entity.MailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


/**
 * Created by zpc on 16/12/28.
 */
@Service("mailService")
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void mailSend(final MailEntity mailEntity) {
        threadPoolTaskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    simpleMailMessage.setFrom(mailEntity.getFrom());
                    simpleMailMessage.setTo(mailEntity.getTo());
                    simpleMailMessage.setSubject(mailEntity.getSubject());
                    simpleMailMessage.setText(mailEntity.getContent());

                    javaMailSender.send(simpleMailMessage);
                } catch (MailException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }
}
