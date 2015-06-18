package com.nuevatel.bcf.core.entity;

/**
 * Created by asalazar on 6/6/15.
 */
public enum ERegex {
    regex_id,
    regex_name,
    regex,
    new_media_id,
    end_media_id,
    swap_id,
    ;

    public static String tableName() {
        return "regex";
    }
}
