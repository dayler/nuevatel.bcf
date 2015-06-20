package com.nuevatel.bcf.core.entity;

/**
 * Created by asalazar on 6/19/15.
 */
public enum ESessionRecord {
    name,
    regex_id,
    new_media_id,
    end_media_id,
    swap_id,
    start_timestamp,
    end_timestamp,
    resp_code,
    ;

    public static String tableName() {
        return "session_record";
    }
}
