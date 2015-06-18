package com.nuevatel.bcf.entity;

/**
 * Created by asalazar on 6/6/15.
 */
public enum ESwap {
    swap_id,
    swap_name,
    name,
    type,
    ;

    public static String tableName() {
        return "swap";
    }
}
