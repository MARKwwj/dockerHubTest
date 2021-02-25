package com.magic.framework.rabbitmq;

import com.magic.framework.utils.RabbitmqUtil;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.io.Serializable;

public abstract class BaseRabbitmqMessage implements Serializable, BeanFactoryAware {
    private final String queueName;

    public BaseRabbitmqMessage(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void send() {
        RabbitmqUtil.sendMessage(this);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        listableBeanFactory.registerSingleton(getQueueName(), new Queue(getQueueName()));
    }
}
