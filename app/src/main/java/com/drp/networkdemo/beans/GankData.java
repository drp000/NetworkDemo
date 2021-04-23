package com.drp.networkdemo.beans;

/**
 * @author durui
 * @date 2021/3/29
 * @description
 */
public class GankData<T> extends GankBaseResponse{
    public int count;
    public boolean error;
    //    public List<GankItem> results;
    public T results;

    @Override
    public String toString() {
        return "GankCategory{" +
                "count=" + count +
                ", error=" + error +
                ", results=" + results +
                '}';
    }
}