/**
 * Project Name:
 * Class Name:com.moses.eventbus.bean.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.bean;

import com.moses.eventbus.event.ApplicationEventListener;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:27 AM
 **/

public class EventListenerDomain {

    /**
     * 监听器
     */
    public final ApplicationEventListener listener;
    /**
     * 是否异步
     */
    public final boolean enableAsync;

    /**
     * 构造器
     *
     * @param listener
     * @param enableAsync
     * @since 1.0.0
     */
    public EventListenerDomain(ApplicationEventListener listener, boolean enableAsync) {
        this.listener = listener;
        this.enableAsync = enableAsync;
    }
}
