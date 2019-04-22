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


import com.lmax.disruptor.ExceptionHandler;
import com.moses.eventbus.commons.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:33 AM
 **/
@Slf4j
public class CommandEventExceptionHandler<E extends CommandEvent> implements ExceptionHandler<E> {

    private final String disruptor;

    /**
     * 构造器
     *
     * @param disruptor 分发器
     */
    public CommandEventExceptionHandler(String disruptor) {
        this.disruptor = disruptor;
    }

    /**
     * handle异常
     *
     * @param ex    异常
     * @param event 事件
     * @since 1.0.0
     */
    @Override
    public void handleEventException(Throwable ex, long sequence, E event) {
        log.error(Constants.Logger.EXCEPTION + "[{}] Event Exception:{},event:{}", disruptor, ex, event);
    }

    /**
     * start异常
     *
     * @param ex 异常
     * @since 1.0.0
     */
    @Override
    public void handleOnStartException(Throwable ex) {
        log.error(Constants.Logger.EXCEPTION + "[{}] on start Exception:{}", disruptor, ex);
    }

    /**
     * shutdown异常
     *
     * @param ex 异常
     * @since 1.0.0
     */
    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error(Constants.Logger.EXCEPTION + "[{}] on shutdown Exception :", disruptor, ex);
    }
}
