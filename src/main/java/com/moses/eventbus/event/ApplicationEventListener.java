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
package com.moses.eventbus.event;

import java.util.EventListener;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:24 AM
 **/

public interface ApplicationEventListener<E extends BaseApplicationEvent> extends EventListener {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    void onApplicationEvent(E event);

}
