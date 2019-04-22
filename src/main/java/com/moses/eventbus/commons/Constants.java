/**
 * Project Name:
 * Class Name:com.moses.eventbus.commons.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.commons;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/19 11:30 AM
 **/

public class Constants {

    /**
     * 私有构造器
     */
    private Constants() {
        throw new IllegalAccessError();
    }


    /**
     * 日志信息
     */
    public static class Logger {
        public static final String EXCEPTION = "Exception:";
        public static final String MESSAGE = " ";

        private Logger() {
            throw new IllegalAccessError();
        }
    }
}
