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

import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.event.BaseApplicationEvent;

/**
 * @author HanKeQi
 * @Description 事件对象工具类用于，用于数据暂存对象
 * @date 2019/4/19 11:31 AM
 **/

public class CommandEvent {

    private BaseApplicationEvent applicationEvent;

    private EventListenerDomain eventListenerDomain;

    public EventListenerDomain getEventListenerDomain() {
        return eventListenerDomain;
    }

    public void setEventListenerDomain(EventListenerDomain eventListenerDomain) {
        this.eventListenerDomain = eventListenerDomain;
    }

    public BaseApplicationEvent getApplicationEvent() {
        return applicationEvent;
    }

    public void setApplicationEvent(BaseApplicationEvent applicationEvent) {
        this.applicationEvent = applicationEvent;
    }
}
