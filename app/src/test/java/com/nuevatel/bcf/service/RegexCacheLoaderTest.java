package com.nuevatel.bcf.service;

import com.nuevatel.bcf.core.dao.DatabaseHelper;
import com.nuevatel.bcf.domain.Regex;
import com.nuevatel.bcf.exception.RegexNotFoundException;
import com.nuevatel.bcf.stub.DatasourceUtils;
import com.nuevatel.common.ds.DataSourceManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by asalazar on 6/8/15.
 */
public class RegexCacheLoaderTest {

    private final static Integer TEST_REGEX_ID = 5;
    public static final String TEST_REGEX_NAME = "test_cotel_regex_5";
    public static final String TEST_REGEX_PATTERN = "0?0?14.+";
    public static final int TEST_REGEX_NEW_MEDIA_ID = 1;
    public static final int TEST_REGEX_END_MEDIA_ID = 1;
    public static final int TEST_REGEX_SWAP_ID = 1;


    private final static Integer TEST2_REGEX_ID = 1;
    public static final String TEST2_REGEX_NAME = "cotel_regex_1";
    public static final String TEST2_REGEX_PATTERN = "0?0?14.+|(((591)|0)?(2|3|4|6|7).+)";

    private static DataSourceManager ds = null;

    private RegexCacheLoader loader = null;

    @BeforeClass
    public static void beforeClass() throws Exception {
        // No op
    }

    @AfterClass
    public static void afterClass() throws Exception {
        // No op
    }

    @Before
    public void setUp() throws Exception {
        createDatasourceManager();
        DatasourceUtils.populateTestData(ds);
        loader = new RegexCacheLoader();
    }

    @After
    public void tearDown() throws Exception {
        DatasourceUtils.deleteTestData(ds);
        loader = null;
        shutdownDatasourceManager();
    }

    @Test
    public void load() throws Exception {
        Regex regex = loader.load(TEST_REGEX_ID);
        assertNotNull("Regex is null", regex);
        assertEquals("Regex Id not match", TEST_REGEX_ID.intValue(), regex.getId().intValue());
        assertEquals("Name not match", TEST_REGEX_NAME, regex.getName());
        assertEquals("Regex pattern not match", TEST_REGEX_PATTERN, regex.getPattern().pattern());
        // Check new media
        assertEquals(TEST_REGEX_NEW_MEDIA_ID, regex.getNewMedia().getId().intValue());
        // Check end media
        assertEquals(TEST_REGEX_END_MEDIA_ID, regex.getNewMedia().getId().intValue());
        // Check swap
        assertEquals(TEST_REGEX_SWAP_ID, regex.getSwap().getId().intValue());
    }

    @Test(expected = RegexNotFoundException.class)
    public void loadUnexistingRegex() throws Exception {
        Regex regex = loader.load(777);
    }

    @Test
    public void loadRegexpWithNullMediasAndSwap() throws Exception {
        Regex regex = loader.load(TEST2_REGEX_ID);
        assertNotNull("Regex is null", regex);
        assertEquals("Regex Id not match", TEST2_REGEX_ID.intValue(), regex.getId().intValue());
        assertEquals("Name not match", TEST2_REGEX_NAME, regex.getName());
        assertEquals("Regex pattern not match", TEST2_REGEX_PATTERN, regex.getPattern().pattern());
        // Check new media
        assertNull("New media id is not null", regex.getNewMedia());
        // Check end media
        assertNull("End media id is not null", regex.getNewMedia());
        // Check swap
        assertNull("Swap id is not null", regex.getSwap());
    }

//    @Test
    public void loadRegexpWithoutEndMedia() throws Exception {
        fail("No Impl");
    }

    private static void createDatasourceManager() throws Exception {
        InputStream is = null;
        try {
            Properties prop = new Properties();
            is = RegexCacheLoader.class.getResourceAsStream("/bcf-test.properties");
            prop.load(is);
            ds = DatasourceUtils.getTestConnection(prop);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static void shutdownDatasourceManager() throws Exception {
        DatabaseHelper.shutdownConnPool();
    }
}
