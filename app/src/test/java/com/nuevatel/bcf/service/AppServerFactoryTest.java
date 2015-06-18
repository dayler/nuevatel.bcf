package com.nuevatel.bcf.service;

import com.nuevatel.base.appconn.AppServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * Test Class for BCFServer
 */
public class AppServerFactoryTest {

    private AppServerFactory bcfServerFactory = null;

    @Mock
    private AppServer server;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // Mock bcf server
        doNothing().when(server).start();
        doNothing().when(server).interrupt();
        bcfServerFactory = new AppServerFactory(server);
    }

    public void teardown() throws Exception {
        bcfServerFactory = null;
    }

    @Test
    public void testInterrupt() throws Exception {
        bcfServerFactory.interrupt();
        // Verify
        verify(server, times(0)).start();
        verify(server, times(1)).interrupt();
    }

    @Test
    public void testGet() throws Exception {
        assertNotNull("BCFServer is null", bcfServerFactory.get());
        // Verify
        verify(server, times(0)).start();
        verify(server, times(0)).interrupt();
    }
}