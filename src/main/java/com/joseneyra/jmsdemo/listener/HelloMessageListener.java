package com.joseneyra.jmsdemo.listener;

import com.joseneyra.jmsdemo.config.JmsConfig;
import com.joseneyra.jmsdemo.model.HelloWorldMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message) {

//        System.out.println("I Got a Message!!!!!");
        System.out.println(helloWorldMessage);

//        throw new RuntimeException("Test"); // JMS will redeliver the message if the client throws an exception,
//                                            // the message will continue to be sent to the client until the message is
//                                            // processed correctly
    }

    @JmsListener(destination = JmsConfig.MY_SEND_RECEIVE_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage replyMessage = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("World!!!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), replyMessage);
    }
}
