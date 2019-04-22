package com.moses.eventbus.channel;

import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.event.BaseApplicationEvent;

/**
 * @author HanKeQi
 * @Description 事件通道接口
 * @date 2019/4/19 11:28 AM
 **/

public interface Channel {

    /**
     * bean handle event for async
     *
     * @param helper 事件处理器
     * @param event  事件
     * @return
     */
    void handle(final EventListenerDomain helper, final BaseApplicationEvent event);


    /**
     * eventBus publish event
     *
     * @param tag   事件标签
     * @param event 事件
     * @return
     */
    void publish(final String tag, final BaseApplicationEvent event);
}
