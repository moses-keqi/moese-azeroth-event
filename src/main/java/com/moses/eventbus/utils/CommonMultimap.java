/**
 * Project Name:
 * Class Name:com.moses.eventbus.utils.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.moses.eventbus.utils;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HanKeQi
 * @Description 重复key的map，使用监听的type，取出所有的监听器
 * @date 2019/4/19 11:36 AM
 **/

public class CommonMultimap<K, V> {

    private final Map<K, List<V>> map;

    /**
     * 构造器
     */
    public CommonMultimap() {
        map = new HashMap<>();
    }

    List<V> createList() {
        return new ArrayList<>();
    }

    /**
     * put to map
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean put(K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = createList();
            map.put(key, list);
        }
        return list.add(value);
    }

    /**
     * get List by key
     *
     * @param key 键
     * @return List
     */
    public List<V> get(K key) {
        List<V> list = map.get(key);
        if (list == null) {
            list = createList();
        }
        return list;
    }

    /**
     * map大小
     *
     * @return
     */
    public int size() {
        return map.size();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(map);
    }

    /**
     * clear map
     */
    public void clear() {
        map.clear();
    }
}
