package com.nuevatel.bcf.service;

import com.google.common.cache.CacheLoader;
import com.nuevatel.bcf.core.domain.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * UnitServiceFactory's test class
 *
 * @author Ariel Salazar
 */
public class UnitServiceFactoryTest {

    private UnitServiceFactory sFactory;

    @Mock
    private CacheLoader<String, Unit>cacheLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // set up base properties
        sFactory = new UnitServiceFactory(null);
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
     * Test getCache after setup properties.
     *
     * @throws Exception
     */
    @Test
    public void getCache() throws Exception {
        UnitService us = sFactory.getCache();
        assertNotNull("UnitService is null", us);
    }
}