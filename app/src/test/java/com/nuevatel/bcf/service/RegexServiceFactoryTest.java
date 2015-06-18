package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.domain.Regex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Test class for RegexServiceFactory
 *
 * @author Ariel Salazar
 */
public class RegexServiceFactoryTest {

    private RegexServiceFactory sFactory;

    @Mock
    private CacheLoader<Integer, Regex>cacheLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // set factory service properties
        sFactory = new RegexServiceFactory(null);
        sFactory.setExpireAfterWrite(30000)
                .setExpireAfterAccess(300000*2)
                .setConcurrencyLevel(4)
                .setCacheLoader(cacheLoader);
    }

    @After
    public void tearDown() throws Exception {
        sFactory = null;
    }

    /**
     * Test getCache after setup properties
     *
     * @throws Exception
     */
    @Test
    public void getCache() throws Exception {
        RegexService rs = sFactory.getCache();
        assertNotNull("RegexService is null", rs);
    }
}