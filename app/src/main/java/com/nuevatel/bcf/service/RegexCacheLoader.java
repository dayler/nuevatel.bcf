package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.domain.Media;
import com.nuevatel.bcf.domain.Regex;
import com.nuevatel.bcf.domain.Swap;
import com.nuevatel.bcf.core.entity.ERegexById;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.bcf.exception.RegexNotFoundException;
import com.nuevatel.cf.appconn.Name;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.ds.DataSourceManagerImpl;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Load single Regex object from the database.
 *
 * @author Ariel Salazar
 */
public class RegexCacheLoader extends CacheLoader<Integer, Regex> {

    private static Logger logger = LogManager.getLogger(RegexCacheLoader.class);

    /**
     *
     * @param regexId Id to identify the RegexId.
     * @return The unit to corresponds with the regexId.
     * @throws Exception If the Regex could not be retrieved.
     */
    @Override
    public Regex load(Integer regexId) throws Exception {
        if (regexId == null) {
            return  null;
        }

        DataSourceManager ds = new DataSourceManagerImpl();
        Connection conn = null;
        CallableStatement stm = null;
        ResultSet rs = null;

        try {
            Regex regex = null;
            conn = ds.getConnection();
            stm = ds.makeStatement(conn, SQLQuery.select_regex_by_id.query(), regexId);
            rs = stm.executeQuery();
            while (rs.next()) {
                String rxName = rs.getString(ERegexById.regex_name.name());
                String tmpRegex = rs.getString(ERegexById.regex_regex.name());
                // Build new media
                Integer newMediaId = (Integer)rs.getObject(ERegexById.new_media_id.name());
                String newMediaMediaName = rs.getString(ERegexById.new_media_media_name.name());
                String newMediaName = rs.getString(ERegexById.new_media_name.name());
                Byte newMediaType = rs.getByte(ERegexById.new_media_type.name());
                Integer newMediaValue = rs.getInt(ERegexById.new_media_value.name());
                Media newMedia = newMediaId == null ? null : new Media(newMediaId, newMediaMediaName, new Name(newMediaName, newMediaType), newMediaValue);
                // Build end media
                Integer endMediaId = (Integer)rs.getObject(ERegexById.end_media_id.name());
                String endMediaMediaName = rs.getString(ERegexById.end_media_media_name.name());
                String endMediaName = rs.getString(ERegexById.end_media_name.name());
                Byte endMediaType = rs.getByte(ERegexById.end_media_type.name());
                Integer endMediaValue = (Integer)rs.getObject(ERegexById.end_media_value.name());
                Media endMedia = endMediaId == null ? null : new Media(endMediaId, endMediaMediaName, new Name(endMediaName, endMediaType), endMediaValue);
                // Build swap
                Integer swapId = (Integer)rs.getObject(ERegexById.swap_id.name());
                String swapSwapName = rs.getString(ERegexById.swap_swap_name.name());
                String swapName = rs.getString(ERegexById.swap_name.name());
                Byte swapType = rs.getByte(ERegexById.swap_type.name());
                Swap swap = swapId == null ? null : new Swap(swapId, swapSwapName, swapName, swapType);
                // Build Pattern
                Pattern pattern = null;
                try {
                    pattern = Pattern.compile(tmpRegex);
                } catch (PatternSyntaxException ex) {
                    logger.warn("Failed to compile pattern '{}' for regexId '{}'", tmpRegex, regexId);
                }
                // Build Regex
                regex = new Regex(regexId, rxName, pattern, newMedia, endMedia, swap);
            }

            if (regex == null) {
                throw new RegexNotFoundException(regexId);
            }

            return regex;
        } finally {
             if (conn != null) {
                 conn.close();
             }

            if (stm != null) {
                stm.close();
            }

            if (rs != null) {
                rs.close();
            }
        }
    }
}
