package com.drp.networkdemo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author KG on 2017/6/14.
 */

public class GankItem implements Serializable{
   /* {
        "_id": "5bf22fd69d21223ddba8ca25",
            "createdAt": "2018-11-19T03:36:54.950Z",
            "desc": "2018-11-19",
            "publishedAt": "2018-11-19T00:00:00.0Z",
            "source": "web",
            "type": "福利",
            "url": "https://img.lijinshan.site/images/c9e4d064cfe340fea3f41e2ef5034596",
            "used": true,
            "who": "lijinshanmx"
    }*/
    public String _id;
    public String desc;
    public String createdAt;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public String who;
    public boolean used;
    public List<String> images;

 @Override
 public String toString() {
  return "GankItem{" +
          "_id='" + _id + '\'' +
          ", desc='" + desc + '\'' +
          ", createdAt='" + createdAt + '\'' +
          ", publishedAt='" + publishedAt + '\'' +
          ", source='" + source + '\'' +
          ", type='" + type + '\'' +
          ", url='" + url + '\'' +
          ", who='" + who + '\'' +
          ", used=" + used +
          ", images=" + images +
          '}';
 }
}
