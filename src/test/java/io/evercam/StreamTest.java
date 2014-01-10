package io.evercam;


import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class StreamTest {

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateStream() throws EvercamException {
        Map<String, Object> streamMap = new HashMap<String, Object>();
        streamMap.put("id", "teststream");
        streamMap.put("is_public", true);
        streamMap.put("auth", "{\"basic\": {\"username\": \"user1\",\"password\": \"abcde\"}}");
        streamMap.put("snapshots", "{\"jpg\": \"/onvif/snapshot\"}");
        streamMap.put("endpoints", "[\"http://127.0.0.1:8080\"]");
        Stream stream = Stream.create(streamMap);
        assertEquals("teststream", stream.getId());
    }
    @Test
    public void testGetByIdStream() throws EvercamException {
        Stream stream =Stream.getById("teststream");
        assertEquals("teststream", stream.getId());
    }
}