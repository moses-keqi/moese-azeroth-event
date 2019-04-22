/**
 * Project Name:
 * Class Name:com.moses.eventbus.command.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.command;

import com.moses.eventbus.commons.Constants;
import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.event.ApplicationEventType;
import com.moses.eventbus.event.BaseApplicationEvent;
import com.moses.eventbus.utils.CommonMultimap;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author HanKeQi
 * @Description 事件工具类
 * @date 2019/4/19 11:31 AM
 **/

@Slf4j
public class CommandBus {

    private static RingBuffer<CommandEvent> conRingBuffer;

    private static CommonMultimap<ApplicationEventType, EventListenerDomain> map;

    private static boolean enableAsync;

    /**
     * 构造器
     */
    private CommandBus() {

    }

    /**
     * 初始化事件发生器
     *
     * @param enableAsync   是否异步
     * @param map           注册器存储map
     * @param conRingBuffer 队列操作
     * @return EventBus
     * @since 1.0.0
     */
    public static void init(boolean enableAsync, CommonMultimap<ApplicationEventType, EventListenerDomain> map, RingBuffer<CommandEvent> conRingBuffer) {
        CommandBus.conRingBuffer = conRingBuffer;
        CommandBus.map = map;
        CommandBus.enableAsync = enableAsync;
    }

    /**
     * 初始化事件发生器
     *
     * @param enableAsync 是否异步
     * @param map         注册器存储map
     * @return EventBus
     * @since 1.0.0
     */
    public static void init(boolean enableAsync, CommonMultimap<ApplicationEventType, EventListenerDomain> map) {
        CommandBus.map = map;
        CommandBus.enableAsync = enableAsync;
    }

    /**
     * 发布事件
     * @param helper 处理器
     * @param event  事件
     * @since 1.0.0
     */
    private static boolean publish(final EventListenerDomain helper, final BaseApplicationEvent event) {
        try {
            long seq = conRingBuffer.tryNext();
            //the remaining capacity of the buffer < the size of the buffer * 0.2
            //队列超出阀值得时候 取消队列并发消费 日志输出提示告警
            if (conRingBuffer.remainingCapacity() < conRingBuffer.getBufferSize() * 0.2) {
                enableAsync = false;
                log.warn(Constants.Logger.MESSAGE + "commandBus consume warn message, remainingCapacity size:" + conRingBuffer.remainingCapacity() + ",conRingBuffer size:" + conRingBuffer.getBufferSize());
            }
            CommandEvent commandEvent = conRingBuffer.get(seq);
            commandEvent.setApplicationEvent(event);
            commandEvent.setEventListenerDomain(helper);
            conRingBuffer.publish(seq);
        } catch (InsufficientCapacityException e) {
            log.error(Constants.Logger.EXCEPTION + "conRingBuffer too late to consume error message,you may increase conBufferSize/asyncThreads " + e.toString());
            return false;
        }
        return true;
    }

    /**
     * 发布事件
     * @param applicationEventType 事件封装
     * @param event                事件
     * @since 1.0.0
     */
    public static void publish(final ApplicationEventType applicationEventType, final BaseApplicationEvent event) {
        Collection<EventListenerDomain> listenerList = map.get(applicationEventType);
        if (listenerList != null && !listenerList.isEmpty()) {
            for (final EventListenerDomain domain : listenerList) {
                if (enableAsync && domain.enableAsync) {
                    publish(domain, event);
                } else {
                    handle(domain, event);
                }
            }
        }
    }

    /**
     * 处理事件
     * @param domain 事件处理器
     * @param event  事件
     * @since 1.0.0
     */
    public static void handle(final EventListenerDomain domain, final BaseApplicationEvent event) {
        try {
            domain.listener.onApplicationEvent(event);
        } catch (Exception e) {
            log.error(Constants.Logger.EXCEPTION + "commandBus handle event error message" + e.toString());
        }
    }

}
