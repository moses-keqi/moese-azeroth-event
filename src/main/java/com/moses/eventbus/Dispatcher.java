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

import com.moses.eventbus.channel.Channel;
import com.moses.eventbus.command.CommandEvent;
import com.moses.eventbus.command.CommandEventExceptionHandler;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HanKeQi
 * @Description 异步事件处理分发器
 * @date 2019/4/19 11:44 AM
 **/

public class Dispatcher {

    private final EventBus eventBus;

    private static ExecutorService pool = null;
    /**
     * 并发消费的Disruptor Big Ring
     * <p>
     * 事件被消费的顺序是不确定的
     */
    private static Disruptor<CommandEvent> conDisruptor;

    /**
     * 并发消费 RingBuffer
     */
    private RingBuffer<CommandEvent> conRingBuffer;

    private volatile boolean running;

    protected Dispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
        this.running = false;
    }

    /**
     * 分发器start
     *
     * @return Dispatcher
     * @since 1.0.0
     */
    public synchronized Dispatcher start() {
        if (!this.running) {
            this.running = true;
            int conBufferSize = eventBus.getConBufferSize();
            int asyncThreads = eventBus.getAsyncThreads();
            final Channel channel = eventBus.getChannel();
            conBufferSize = conBufferSize > 0 ? conBufferSize : 1024;
            asyncThreads = asyncThreads > 0 ? asyncThreads : 8;
            pool = Executors.newFixedThreadPool(asyncThreads, new ThreadFactory() {
                final AtomicInteger seq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "EventBus-concurrency-Consumer-" + seq.getAndIncrement());
                }
            });
            conDisruptor = new Disruptor<>(
                    () -> new CommandEvent(), conBufferSize, pool, ProducerType.MULTI, new LiteBlockingWaitStrategy()
            );
            WorkHandler[] handlers = new WorkHandler[asyncThreads];
            Arrays.fill(handlers, (WorkHandler<CommandEvent>) commandEvent -> channel.handle(commandEvent.getEventListenerDomain(), commandEvent.getApplicationEvent()));
            conDisruptor.handleEventsWithWorkerPool(handlers);
            conDisruptor.handleExceptionsWith(new CommandEventExceptionHandler<>("commandBus-Concurrency-Disruptor"));
            conRingBuffer = conDisruptor.start();
            return this;
        }
        return this;
    }

    /**
     * 分发器stop
     *
     * @return Dispatcher
     * @since 1.0.0
     */
    public synchronized void stop() {
        this.running = false;
        if (null != conDisruptor) {
            conDisruptor.shutdown();
            conDisruptor = null;
        }
    }

    public RingBuffer<CommandEvent> getConRingBuffer() {
        return conRingBuffer;
    }

}
