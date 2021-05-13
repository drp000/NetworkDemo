package com.drp.networkdemo.beans;

import java.util.List;

/**
 * @author durui
 * @date 2021/3/29
 * @description
 */
public class GankData extends GankBaseResponse {
    public int count;
    public boolean error;
    public List<GankItem> results;

    @Override
    public String toString() {
        return "GankCategory{" +
                "count=" + count +
                ", error=" + error +
                ", results=" + results +
                '}';
    }
}