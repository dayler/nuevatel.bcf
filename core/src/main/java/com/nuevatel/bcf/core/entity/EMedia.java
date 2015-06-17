package com.nuevatel.bcf.core.entity;

/**
 * Created by asalazar on 6/6/15.
 */
public enum EMedia {
    media_id,
    media_name,
    name,
    type,
    value,
    ;

    public static String tableName() {
        return "media";
    }
}
