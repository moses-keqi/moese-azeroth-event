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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HanKeQi
 * @Description 实现的ConcurrentMultiMap 重复key的map，使用监听的type，取出所有的监听器
 * @date 2019/4/19 11:37 AM
 **/

public class ConcurrentMultiMap<K, V> {

    private transient final ConcurrentMap<K, List<V>> map;

    public ConcurrentMultiMap() {
        map = new ConcurrentHashMap<K, List<V>>();
    }

    List<V> createlist() {
        return new ArrayList<V>();
    }

    /**
     * put to ConcurrentMultiMap
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean put(K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = createlist();
            if (list.add(value)) {
                map.put(key, list);
                return true;
            } else {
                throw new AssertionError("New list violated the list spec");
            }
        } else if (list.add(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * put list to ConcurrentMultiMap
     *
     * @param key  键
     * @param list 值列表
     * @return boolean
     */
    public boolean putAll(K key, List<V> list) {
        if (list == null) {
            return false;
        } else {
            map.put(key, list);
            return true;
        }
    }

    /**
     * get List by key
     *
     * @param key 键
     * @return List
     */
    public List<V> get(K key) {
        List<V> list = map.get(key);
        return list;
    }

    /**
     * clear ConcurrentMultiMap
     */
    public void clear() {
        map.clear();
    }

}
