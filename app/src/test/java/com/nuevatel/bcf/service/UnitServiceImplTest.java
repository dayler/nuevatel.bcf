package com.nuevatel.bcf.service;

import com.nuevatel.bcf.domain.Unit;
import com.nuevatel.bcf.exception.UnitNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by asalazar on 6/8/15.
 */
public class UnitServiceImplTest {

    public static final String TEST_UNIT_NAME = "70000001";
    public static final Date TEST_DATE = new Date();
    public static final String TEST_UNEXISTING_UNIT = "11";
    private UnitService service;

    @Mock
    private UnitCacheLoader cacheLoader;

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
        MockitoAnnotations.initMocks(this);
        Unit unit = new Unit(TEST_UNIT_NAME);
        unit.addRegexId(0);
        Date t = TEST_DATE;
        unit.addTimespan(0, t, t);
        when(cacheLoader.load(TEST_UNIT_NAME)).thenReturn(unit);
        when(cacheLoader.load(TEST_UNEXISTING_UNIT)).thenThrow(UnitNotFoundException.class);

        // set up base properties
        UnitServiceFactory sFactory = new UnitServiceFactory(null);
        sFactory.setExpireAfterWrite(30000)
                .setExpireAfterAccess(300000*2)
                .setConcurrencyLevel(4)
                .setCacheLoader(cacheLoader);
        // get service
        service = sFactory.getCache();
    }

    @After
    public void teardown() throws Exception {
        service = null;
    }

    @Test
    public void getUnit() throws Exception {
        Unit testUnit = service.getUnit(TEST_UNIT_NAME);
        assertNotNull("Unit cannot be null", testUnit);
        assertEquals("Not match unit name", TEST_UNIT_NAME, testUnit.getName());
        assertEquals("Not match regexid", 0, testUnit.getRegexIds().get(0).intValue());
        assertEquals("Not match starttimestamp", TEST_DATE, testUnit.getStartTimestamp(0));
        assertEquals("Not match endtimestamp", TEST_DATE, testUnit.getEndTimestamp(0));

        // Verify
        verify(cacheLoader, times(1)).load(TEST_UNIT_NAME);
    }

    @Test
    public void getUnit_ifNotExists() throws Exception {
        Unit testUnit = service.getUnit(TEST_UNEXISTING_UNIT);
        assertNull("Unit must null", testUnit);
        // Verify
        verify(cacheLoader, times(1)).load(TEST_UNEXISTING_UNIT);
    }

    @Test
    public void getSize() throws Exception {
        Unit testUnit = service.getUnit(TEST_UNIT_NAME);
        assertNotNull("Unit cannot be null", testUnit);
        long size = service.getSize();
        assertEquals("Size does not match", 1, size);

        // Verify
        verify(cacheLoader, times(1)).load(TEST_UNIT_NAME);
    }

    @Test
    public void getSizeEq0() throws Exception {
        long size = service.getSize();

        assertEquals("Size does not match", 0, size);
        // Verify
        verify(cacheLoader, times(0)).load(anyString());
    }

    @Test
    public void clear() throws Exception {
        Unit testUnit = service.getUnit(TEST_UNIT_NAME);
        assertNotNull("Unit cannot be null", testUnit);
        long size = service.getSize();
        assertEquals("Size does not match", 1, size);
        service.clear();
        size = service.getSize();
        assertEquals("Size must 0", 0, size);

        // Verify
        verify(cacheLoader, times(1)).load(TEST_UNIT_NAME);
    }
}