package server.event_manager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestEventsManagerData
{
    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before Class");
    }
 
    @Before
    public void before() {
        System.out.println("Before Test Case");
    }
 
    @Test
    public void isGreaterTest() {
        System.out.println("Test");
        EventsManagerData eventsManagerData = new EventsManagerData();
    }
    
    @After
    public void after() {
        System.out.println("After Test Case");
    }
 
    @AfterClass
    public static void afterClass() {
        System.out.println("After Class");
    }
}