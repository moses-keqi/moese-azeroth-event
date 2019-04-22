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

import java.lang.reflect.Type;

/**
 * @author HanKeQi
 * @Description 事件类型
 * @date 2019/4/19 11:23 AM
 **/

public class ApplicationEventType {

    /**
     * 默认的tag
     */
    public static final String DEFAULT_TAG = "default_tag";

    /**
     * 事件的tag
     */
    private String tag = DEFAULT_TAG;

    /**
     * 事件class type
     */
    private final Type type;

    /**
     * 构造器
     *
     * @param tag  标签
     * @param type 类型
     */
    public ApplicationEventType(String tag, Type type) {
        this.tag = tag;
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        ApplicationEventType other = (ApplicationEventType) obj;
        if (tag == null) {
            if (other.tag != null) {
                return false;
            }
        } else if (!tag.equals(other.tag)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
