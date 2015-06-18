package com.nuevatel.bcf.domain;

import com.nuevatel.cf.appconn.Name;

/**
 * Created by asalazar on 6/5/15.
 */
public class Media {

    private Integer id;

    private String mediaName;

    private Name name;

    private Integer value;

    public Media(Integer id, String mediaName, Name name, Integer value) {
        this.id = id;
        this.mediaName = mediaName;
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Name getName() {
        return name;
    }

    public String getMediaName() {
        return mediaName;
    }
}
