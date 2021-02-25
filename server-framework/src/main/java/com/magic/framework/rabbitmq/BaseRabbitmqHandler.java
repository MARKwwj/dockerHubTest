package com.magic.framework.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;

public abstract class BaseRabbitmqHandler<T extends BaseRabbitmqMessage> implements BeanFactoryAware {
    @RabbitHandler
    public void processMessage(T message) {
        processMessageImpl(message);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        //this.getClass().getAnnotations()
        RabbitListener rabbitListener = this.getClass().getAnnotation(RabbitListener.class);
        if (rabbitListener == null) {
            throw new RuntimeException(this.getClass().getName() + "类未加@RabbitListener注解");
        }
        try {
            ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class clazz = (Class<T>) ptype.getActualTypeArguments()[0];
            T obj = (T) clazz.newInstance();

            InvocationHandler h = Proxy.getInvocationHandler(rabbitListener);
            Field hField = h.getClass().getDeclaredField("memberValues");
            hField.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>)hField.get(h);
            String[] queues = new String[] { obj.getQueueName() };
            memberValues.put("queues", queues);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    protected abstract void processMessageImpl(T message);
}
