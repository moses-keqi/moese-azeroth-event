/**
 * Project Name:
 * Class Name:com.moses.eventbus.listener.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.listener;


import com.moses.eventbus.event.ApplicationEventType;

import java.lang.annotation.*;


/**
 * @author HanKeQi
 * @Description 注解标记需要扫描的监听器
 * @date 2019/4/19 11:40 AM
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Listener {

    /**
     * The priority value. Default is {@link Integer#MAX_VALUE}.
     * 监听器优先处理权 只能保证提交次序/不能保证执行完成的次序
     *
     * @return priority
     * @since 1.0.0
     */
    int priority() default Integer.MAX_VALUE;

    /**
     * 标记Listener是否为异步，Default is false
     *
     * @return async
     * @since 1.0.0
     */
    boolean enableAsync() default false;

    /**
     * 事件的tag，事件的标识符
     *
     * @return tag
     * @since 1.0.0
     */
    String tag() default ApplicationEventType.DEFAULT_TAG;
}
