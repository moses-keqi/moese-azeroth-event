/**
 * Project Name:
 * Class Name:com.moses.eventbus.channel.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.channel;

import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.command.CommandBus;
import com.moses.eventbus.event.ApplicationEventType;
import com.moses.eventbus.event.BaseApplicationEvent;

/**
 * @author HanKeQi
 * @Description 内存事件处理器
 * @date 2019/4/19 11:29 AM
 **/

public class MemoryChannel extends AbstractChannel{

    @Override
    public void handle(EventListenerDomain domain, BaseApplicationEvent event) {
        CommandBus.handle(domain, event);
    }

    @Override
    public void publish(String tag, BaseApplicationEvent event) {
        ApplicationEventType applicationEventType = new ApplicationEventType(tag, event.getClass());
        CommandBus.publish(applicationEventType, event);
    }

}
