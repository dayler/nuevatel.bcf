package com.nuevatel.bcf;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ShutdownHook.
 *
 * Created by asalazar on 6/4/15.
 */
public class ShutdownHookTest {


    private ShutdownHook hook;

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty(".configurationFile", "log4j2-test.xml");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        //
    }

    @Before
    public void setUp() throws Exception {
        hook = new ShutdownHook(1, 1);
    }

    @After
    public void tearDown() throws Exception {
        hook = null;
    }

    @Test
    public void appendProcess() throws Exception {
        Processor p1 = mock(Processor.class);
        doNothing().when(p1).shutdown(anyInt());
        hook.appendProcess(p1);
        // Execute
        hook.run();
        Thread.sleep(500);
        // Verify
        verify(p1, times(1)).shutdown(anyInt());
    }

    @Test
    public void clear() throws Exception {
        Processor p1 = mock(Processor.class);
        doNothing().when(p1).shutdown(anyInt());
        hook.appendProcess(p1);
        hook.clear();
        // Execute
        hook.run();
        // Verify
        verify(p1, times(0)).shutdown(anyInt());
    }
}