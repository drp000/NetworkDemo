package com.drp.base.model;


import java.io.Serializable;

/**
 * @author durui
 * @date 2021/4/2
 * @description
 */
public class BaseCachedData<F> implements Serializable {
    /**
     * 缓存数据
     */
    public F data;
    /**
     * 缓存时间
     */
    public long updateTime;
}