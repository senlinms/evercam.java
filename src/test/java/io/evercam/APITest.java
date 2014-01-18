package io.evercam;

import org.junit.Test;

import static org.junit.Assert.*;

public class APITest {

    String TEST_USERNAME = "username";
    String TEST_PASSWORD = "password";

    @Test
    public void testAuth(){
        assertFalse(API.isAuth());
        API.setAuth(TEST_USERNAME,TEST_PASSWORD);
        String[] auth = API.getAuth();
        assertEquals(auth[0],TEST_USERNAME);
        assertEquals(auth[1],TEST_PASSWORD);
        assertTrue(API.isAuth());
    }
}
