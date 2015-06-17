package com.nuevatel.bcf.core.entity;

/**
 * Created by asalazar on 6/6/15.
 */
public enum EUnit {
    name,
    regex_id,
    creation_timestamp,
    start_timestamp,
    end_timestamp,
    ;

    public static String tableName() {
        return "unit";
    }
}
