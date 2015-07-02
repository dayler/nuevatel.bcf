package com.nuevatel.bcf.core.entity;

/**
 * Created by asalazar on 6/6/15.
 */
public enum SQLQuery {
    get_session_record_by_id(
            "select id, name, regex_id, new_media_id, end_media_id, swap_id, start_timestamp, end_timestamp, resp_code\n" +
            "\tfrom session_record\n" +
            "where id=?;"),

    update_session_record(
            "update session_record\n" +
            "set name=?,\n" +
            "\tregex_id=?,\n" +
            "\tnew_media_id=?,\n" +
            "\tend_media_id=?, \n" +
            "\tswap_id=?,\n" +
            "\tstart_timestamp=?,\n" +
            "\tend_timestamp=?,\n" +
            "\tresp_code=?\n" +
            "where id=?;"),

    insert_new_session_record(
            "insert session_record(id, name, regex_id, new_media_id, end_media_id, swap_id, start_timestamp, end_timestamp, resp_code)\n" +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?);"),

    update_session_record_end_timestamp("update session_record\n" +
                                        "set end_timestamp=?, resp_code=?\n" +
                                        "where id=?;"),

    /**
     *Query to retrieve Unit object from Unit.name.
     * <br/><br/>
     * select name, regex_id, start_timestamp, end_timestamp from unit where name='%s';
     */
    select_unit_by_name("select name, regex_id, start_timestamp, end_timestamp from unit where name=?;"),

    /**
     * Query to retrieve all fields for a Regex object. <b>Must provide regexId<b/>. The table fields are mapped on
     * ERegexById.
     * <br/><br/>
     * select <br/>
     *      r.regex_id as regex_id,<br/>
     *      r.regex_name as regex_name,<br/>
     *      r.new_media_id as new_media_id,<br/>
     *      nm.media_name as new_media_media_name,<br/>
     *      nm.name as new_media_name,<br/>
     *      nm.type as new_media_type,<br/>
     *      nm.value as new_media_value,<br/>
     *      r.new_media_id as end_media_id,<br/>
     *      em.media_name as end_media_media_name,<br/>
     *      em.name as end_media_name,<br/>
     *      em.type as end_media_type,<br/>
     *      em.value as end_media_value,<br/>
     *      r.swap_id as swap_id,<br/>
     *      concat('591', s.name) as swap_name,<br/>
     *      s.swap_name as swap_swap_name,<br/>
     *      s.type as swap_type<br/>
     *  from regex as r,<br/>
     *          media as nm,<br/>
     *          media as em,<br/>
     *          swap as s<br/>
     * where r.regex_id=0<br/>
     *      and r.new_media_id=nm.media_id<br/>
     *      and r.end_media_id=em.media_id<br/>
     *      and r.swap_id=s.swap_id;<br/>
     *
     */
    select_regex_by_id("select\n" +
            "      r.regex_id as regex_id,\n" +
            "      r.regex_name as regex_name,\n" +
            "      r.regex as regex_regex,\n" +
            "      r.new_media_id as new_media_id,\n" +
            "      nm.media_name as new_media_media_name,\n" +
            "      nm.name as new_media_name,\n" +
            "      nm.type as new_media_type,\n" +
            "      nm.value as new_media_value,\n" +
            "      r.swap_id as swap_id,\n" +
            "      if(s.type=3,concat('591', s.name), s.name) as swap_name,\n" +
            "      s.swap_name as swap_swap_name,\n" +
            "      s.type as swap_type\n" +
            "      from regex as r\n" +
            "      left outer join media as nm on r.new_media_id=nm.media_id\n" +
            "      left outer join swap as s on r.swap_id=s.swap_id\n" +
            "where\n" +
            "       r.regex_id=?;"),
    
    insert_new_unit("insert unit(name, regex_id, start_timestamp, end_timestamp)\n" +
            "values (?, ?, ?, ?);"),
    exists_unit_for_name_regex_id("select 1 from unit where name=? and regex_id=?"),


    // TODO remove me!!
    get_unit_by_name_and_regexId(
            "select name, regex_id, creation_timestamp, start_timestamp, end_timestamp\n" +
            "\tfrom unit\n" +
            "where name=? and regex_id=?;"),
    
    delete_unit_by_name_and_regex_id("delete from unit\n" +
                "\twhere name=? and regex_id=?;"),
    
    insert_new_wsi_record_0("insert wsi_record_0(id, name, regex_id, action, from_ip_address, response)\n" +
            "values (?, ?, ?, ?, ?, ?);"),
    
    delete_wsi_record_0("delete from wsi_record_0\n" +
                "\twhere id=?;"),

    query_for_config("select param, value from config;"),
    ;

    private String query;

    private SQLQuery(String query) {
        this.query = query;
    }

    public String query() {
        return query;
    }
}
