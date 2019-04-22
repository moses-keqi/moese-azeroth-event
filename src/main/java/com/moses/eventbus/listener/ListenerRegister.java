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


import com.moses.eventbus.EventBus;
import com.moses.eventbus.bean.EventListenerDomain;
import com.moses.eventbus.commons.Constants;
import com.moses.eventbus.event.ApplicationEventListener;
import com.moses.eventbus.event.ApplicationEventType;
//import com.moses.eventbus.utils.ClassUtil;
import com.moses.eventbus.utils.ClassUtils;
import com.moses.eventbus.utils.CommonMultimap;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:41 AM
 **/

@Slf4j
public class ListenerRegister {

    private EventBus eventBus;

    public ListenerRegister(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * 注册订阅者[需要在event-bus启动之前]
     * <p>
     * 订阅者包括：1：包含有@Listener注解方法的类。2:实现ApplicationEventListener接口
     * </p>
     */
    public synchronized CommonMultimap<ApplicationEventType, EventListenerDomain> registerListener() {

        // 扫描注解
        Set<Class<?>> clazzSet = ClassUtils.getClasses(eventBus.getScanPackage());
                //ClassUtil.scanPackageByAnnotation(eventBus.getScanPackage(), eventBus.isScanJar(), Listener.class);

        if (clazzSet.isEmpty()) {
            log.error(Constants.Logger.EXCEPTION + "Listener is empty! Please check it!");
        }

        List<Class<? extends ApplicationEventListener>> allListeners = new ArrayList<>();
        // 装载所有 {@code ApplicationEventListener} 的子类
        Class superClass;
        for (Class<?> clazz : clazzSet) {
            superClass = ApplicationEventListener.class;
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                allListeners.add((Class<? extends ApplicationEventListener>) clazz);
            }
        }

        if (allListeners.isEmpty()) {
            log.error("{} Listener is empty! Please check @Listener is right?", Constants.Logger.EXCEPTION);
        }
        // 监听器排序
        sortListeners(allListeners);
        // 重复key的map，使用监听的type，取出所有的监听器
        CommonMultimap<ApplicationEventType, EventListenerDomain> map = new CommonMultimap<>();
        Type type;
        ApplicationEventListener listener;

        for (Class<? extends ApplicationEventListener> clazz : allListeners) {
            // 获取监听器上的泛型信息
            type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
            // 实例化监听器(改成用spring管理注解)
            listener = eventBus.getApplicationContext().getBean(clazz);
            // 监听器上的注解
            Listener annotation = clazz.getAnnotation(Listener.class);
            String tag = annotation.tag();
            ApplicationEventType applicationEventType = new ApplicationEventType(tag, type);
            map.put(applicationEventType, new EventListenerDomain(listener, annotation.enableAsync()));
            if (log.isDebugEnabled()) {
                log.debug(" {}{}init~", Constants.Logger.MESSAGE , clazz);
            }
        }


        return map;
    }

    /**
     * 对所有的监听器进行排序
     */
    private void sortListeners(List<Class<? extends ApplicationEventListener>> listeners) {
        //Collections.sort(listeners, new Comparator<Class<? extends ApplicationEventListener>>() {
        Collections.sort(listeners, (o1, o2) -> {

            int x = o1.getAnnotation(Listener.class).priority();
            int y = o2.getAnnotation(Listener.class).priority();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        });
    }

}
