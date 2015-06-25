package com.nuevatel.bcf.service;

import com.google.common.cache.LoadingCache;
import com.nuevatel.bcf.core.domain.Media;
import com.nuevatel.bcf.core.domain.Regex;
import com.nuevatel.bcf.core.domain.Swap;
import com.nuevatel.bcf.core.exception.RegexNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.regex.Pattern;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by asalazar on 6/8/15.
 */
public class RegexServiceImplTest {

    public static final String TEST_REGEX_NAME = "70000001";
    public static final int TEST_REGEX_ID = 10;
    public static final int TEST_UNEXISTED_REGEX_ID = 7777;
    private RegexService service;

    @Mock
    private RegexCacheLoader cacheLoader;

    private Pattern pattern;

    @Mock
    private Media newMedia;

    @Mock
    private Swap swap;

    @Mock
    private LoadingCache<Integer, Regex> loadingCache;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pattern = Pattern.compile("[1-9]");
        Regex testRegex = new Regex(TEST_REGEX_ID, TEST_REGEX_NAME, pattern, newMedia, swap);
        when(cacheLoader.load(TEST_REGEX_ID)).thenReturn(testRegex);
        when(cacheLoader.load(TEST_UNEXISTED_REGEX_ID)).thenThrow(RegexNotFoundException.class);
        RegexServiceFactory sFactory = new RegexServiceFactory(null);
        sFactory.setExpireAfterWrite(30000)
                .setExpireAfterAccess(300000*2)
                .setConcurrencyLevel(4)
                .setCacheLoader(cacheLoader);
        // get service
        service = sFactory.getCache();
    }

    @After
    public void tearDown() throws Exception {
        service = null;
    }

    @Test
    public void getRegex() throws Exception {
        Regex testRegex = service.getRegex(TEST_REGEX_ID);
        assertNotNull("Regex cannot be null", testRegex);
        assertEquals("Not match regex id", TEST_REGEX_ID, testRegex.getId().intValue());
        assertEquals("Not match regex name", TEST_REGEX_NAME, testRegex.getName());
        assertEquals("Not match new Media", newMedia, testRegex.getNewMedia());
        assertEquals("Not match Swap", swap, testRegex.getSwap());
        // Verify
        verify(cacheLoader, times(1)).load(TEST_REGEX_ID);
    }

    @Test
    public void getRegex__ifNotExists() throws Exception {
        Regex testRegex = service.getRegex(TEST_UNEXISTED_REGEX_ID);
        assertNull("Regex must null", testRegex);
        // Verify
        verify(cacheLoader, times(1)).load(TEST_UNEXISTED_REGEX_ID);
    }

    @Test
    public void invalidate() throws Exception {
        Regex testRegex = service.getRegex(TEST_REGEX_ID);
        assertNotNull("Regex cannot be null", testRegex);
        long size = service.getSize();
        assertEquals("Size does not match", 1, size);
        service.invalidate(TEST_REGEX_ID);
        size = service.getSize();
        assertEquals("Size does not match", 0, size);
        // Verify
        verify(cacheLoader, times(1)).load(TEST_REGEX_ID);
    }

    @Test
    public void getSizeEq0() throws Exception {
        long size = service.getSize();
        assertEquals("Size does not match", 0, size);
        // Verify
        verify(cacheLoader, times(0)).load(TEST_REGEX_ID);
        verify(cacheLoader, times(0)).load(TEST_UNEXISTED_REGEX_ID);
    }

    @Test
    public void getSize() throws Exception {
        Regex testRegex = service.getRegex(TEST_REGEX_ID);
        assertNotNull("Regex cannot be null", testRegex);
        long size = service.getSize();
        assertEquals("Size does not match", 1, size);
        // Verify
        verify(cacheLoader, times(1)).load(TEST_REGEX_ID);
    }

    @Test
    public void clear() throws Exception {
        Regex testRegex = service.getRegex(TEST_REGEX_ID);
        assertNotNull("Regex cannot be null", testRegex);
        long size = service.getSize();
        assertEquals("Size does not match", 1, size);
        service.clear();
        size = service.getSize();
        assertEquals("Size does not match", 0, size);
        // Verify
        verify(cacheLoader, times(1)).load(TEST_REGEX_ID);
    }

    @Test
    public void setCacheEngine() throws Exception {
        service.setCacheEngine(loadingCache);
    }
}