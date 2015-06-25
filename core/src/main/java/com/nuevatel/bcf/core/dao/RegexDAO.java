package com.nuevatel.bcf.core.dao;

import com.nuevatel.bcf.core.domain.Media;
import com.nuevatel.bcf.core.domain.Regex;
import com.nuevatel.bcf.core.domain.Swap;
import com.nuevatel.bcf.core.entity.ERegexById;
import com.nuevatel.bcf.core.entity.SQLQuery;
import com.nuevatel.cf.appconn.Name;
import com.nuevatel.common.ds.DataSourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by asalazar on 6/24/15.
 */
public class RegexDAO implements DAO<Integer, Regex> {

    private static Logger logger = LogManager.getLogger(RegexDAO.class);

    private DataSourceManager ds = DatabaseHelper.getBcfDatasource();

    @Override
    public void insert(Regex record) throws SQLException {
        logger.warn("No implemented");
    }

    @Override
    public Regex findById(Integer regexId) throws SQLException {
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
                regex = new Regex(regexId, rxName, pattern, newMedia, swap);
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

    @Override
    public void update(Regex record) throws SQLException {
        logger.warn("No implemented");
    }

    @Override
    public void deleteByPK(Integer key) throws SQLException {
        logger.warn("No implemented");
    }
}
