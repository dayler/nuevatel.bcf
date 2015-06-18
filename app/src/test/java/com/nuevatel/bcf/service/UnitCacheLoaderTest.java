package com.nuevatel.bcf.service;

import com.nuevatel.bcf.domain.Unit;
import com.nuevatel.bcf.exception.UnitNotFoundException;
import com.nuevatel.bcf.stub.DatasourceUtils;
import com.nuevatel.common.ds.DataSourceManager;
import com.nuevatel.common.exception.OperationException;
import com.nuevatel.common.util.date.DateFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by asalazar on 6/8/15.
 */
public class UnitCacheLoaderTest {

    public static final String TEST_UNIT_NAME = "22210695";
    public static final int TEST_REGEXID = 4;
    public static final Date TEST_STARTTIMESTAMP = DateFormatter.CUSTOM.parse("2013-11-18 00:00:00", "yyyy-MM-dd HH:mm:ss");
    private DataSourceManager ds = null;
    private UnitCacheLoader loader = null;

    @Before
    public void setUp() throws Exception {
        createDatasourceManager();
        DatasourceUtils.populateTestData(ds);
        loader = new UnitCacheLoader();
    }

    @After
    public void teardown() throws Exception {
        DatasourceUtils.deleteTestData(ds);
        loader = null;
        shutdownDatasourceManager();
    }

    /**
     * Test Load single unit from DB.
     * @throws Exception
     */
    @Test
    public void load() throws Exception {
        Unit testUnit = loader.load(TEST_UNIT_NAME);
        assertNotNull("Unit is null", testUnit);
        assertEquals("Unit name not match", TEST_UNIT_NAME, testUnit.getName());
        assertEquals(TEST_REGEXID, testUnit.getRegexIds().get(0).intValue());
        assertTrue("Not the same date", testUnit.getStartTimestamp(TEST_REGEXID).compareTo(TEST_STARTTIMESTAMP) == 0);
        assertNull("End timestamp must null", testUnit.getEndTimestamp(TEST_REGEXID));
    }

    /**
     * Test Exception when missing is requested.
     * @throws Exception
     */
    @Test(expected = UnitNotFoundException.class)
    public void loadUnexistingUnit() throws Exception {
        Unit testUnit = loader.load("");
        assertNull("Units is not exists", testUnit);
    }

    private void createDatasourceManager() throws Exception {
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

    private void shutdownDatasourceManager() throws Exception {
        if (ds != null) {
            ds.shutdownConnPool();
        }
    }
}
