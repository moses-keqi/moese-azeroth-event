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

import java.util.EventObject;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:21 AM
 **/

public abstract class BaseApplicationEvent extends EventObject {


    private final long timestamp;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BaseApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
}
