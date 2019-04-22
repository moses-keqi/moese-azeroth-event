/**
 * Project Name:
 * Class Name:com.moses.eventbus.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextAware;

import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.channel.Channel;
import com.moses.eventbus.channel.MemoryChannel;
import com.moses.eventbus.command.CommandBus;
import com.moses.eventbus.event.ApplicationEventType;
import com.moses.eventbus.event.BaseApplicationEvent;
import com.moses.eventbus.listener.ListenerRegister;
import com.moses.eventbus.utils.CommonMultimap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author HanKeQi
 * @Description 注解标记需要扫描的监听器
 * <pre>
 *       (1) 消息的发布入口；
 *      (2) 订阅者的注册(采用扫描注解自动注册);
 *      (3) 事件们与订阅者们的结构组装;
 *      (4) 重试服务开关(待实现)
 *      (5) 消息处理异步分发器启停开关
 * </pre>
 * @date 2019/4/19 11:42 AM
 **/
@Slf4j
public class EventBus implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    /**
     * 通道
     */
    private Channel channel;
    /**
     * 重复key的map，使用监听的type，取出所有的监听器
     */
    private static CommonMultimap<ApplicationEventType, EventListenerDomain> map = null;
    /**
     * 默认不扫描jar包
     */
    private boolean scanJar = false;
    /**
     * 默认扫描所有的包
     */
    private String scanPackage = "";
    /**
     * 默认不开启全局异步
     */
    private boolean enableAsync = false;
    /**
     * 队列大小
     */
    private int conBufferSize;
    /**
     * 异步线程大小
     */
    private int asyncThreads;
    /**
     * 是否已经启动
     */
    private AtomicBoolean started;
    /**
     * 分发器
     */
    private Dispatcher dispatcher;

    /**
     * 构造EventBus
     */
    public EventBus() {
        this(new MemoryChannel(), false, "");
    }

    /**
     * 构造EventBus
     *
     * @param scanJar     是否扫描jar
     * @param scanPackage 扫描的包名
     */
    public EventBus(boolean scanJar, String scanPackage) {
        this(new MemoryChannel(), scanJar, scanPackage);
    }

    /**
     * 构造EventBus
     *
     * @param channel 事件处理通道
     */
    public EventBus(Channel channel) {
        this(channel, false, "");
    }

    /**
     * 构造EventBus
     *
     * @param channel     事件处理通道
     * @param scanJar     是否扫描Jar
     * @param scanPackage 扫描的jar包位置
     */
    public EventBus(Channel channel, boolean scanJar, String scanPackage) {
        this.channel = channel;
        this.scanJar = scanJar;
        this.scanPackage = scanPackage;
        this.started = new AtomicBoolean(false);
    }

    /**
     * 注册事件通道
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus registerChannel() {
        return registerChannel(new MemoryChannel());
    }

    /**
     * 注册事件通道
     * @param channel 事件处理通道
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus registerChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * 开启全局异步
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus async() {
        return async(0, 0);
    }

    /**
     * 开启全局异步
     *
     * @param conBufferSize bufferSize大小 默认1024 2的幂次方
     * @param asyncThreads  线程池的大小，不传或小于1时默认为8
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus async(int conBufferSize, int asyncThreads) {
        this.enableAsync = true;
        setAsyncThreads(asyncThreads);
        setConBufferSize(conBufferSize);
        return this;
    }

    /**
     * 从jar包中搜索监听器
     *
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus scanJar() {
        this.scanJar = true;
        return this;
    }

    /**
     * 指定扫描的包
     *
     * @param scanPackage 指定扫描的包
     * @return EventBus
     * @since 1.0.0
     */
    public EventBus scanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
        return this;
    }

    /**
     * eventBus启动
     * @return
     * @since 1.0.0
     */
    public synchronized boolean start() {
        if (started.get()) {
            log.error("Event Bus is Running!");
            return false;
        }

        if (null == map) {
            map = new ListenerRegister(this).registerListener();
        }

        if (enableAsync) {
            if (dispatcher != null) {
                dispatcher.stop();
            }
            dispatcher = new Dispatcher(this).start();
            CommandBus.init(enableAsync, map, dispatcher.getConRingBuffer());
        } else {
            CommandBus.init(enableAsync, map);
        }
        started.set(true);
        log.info("Event Bus started!");
        return true;
    }

    /**
     * eventBus优雅停机
     *
     * @return
     * @since 1.0.0
     */
    public synchronized boolean stop() {
        //确保新消息无法发送
        started.set(false);

        if (null != map) {
            map.clear();
            map = null;
        }
        if (dispatcher != null) {
            dispatcher.stop();
        }

        log.info("Event Bus Terminated !");
        return true;
    }

    /**
     * 发布事件
     *
     * @param event 事件
     * @return
     * @since 1.0.0
     */
    public boolean publish(final BaseApplicationEvent event) {
        publish(ApplicationEventType.DEFAULT_TAG, event);
        return true;
    }

    /**
     * 发布事件
     *
     * @param tag   事件标签
     * @param event 事件
     * @return
     * @since 1.0.0
     */
    public boolean publish(final String tag, final BaseApplicationEvent event) {
        if (started.get()) {
            if (event == null) {
                return false;
            }
            if (channel == null) {
                channel = new MemoryChannel();
            }
            channel.publish(tag, event);
        }
        return true;
    }


    public Channel getChannel() {
        return channel;
    }

    public int getConBufferSize() {
        return conBufferSize;
    }

    public void setConBufferSize(int conBufferSize) {
        this.conBufferSize = conBufferSize;
    }

    public int getAsyncThreads() {
        return asyncThreads;
    }

    public void setAsyncThreads(int asyncThreads) {
        this.asyncThreads = asyncThreads;
    }

    public boolean isScanJar() {
        return scanJar;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
