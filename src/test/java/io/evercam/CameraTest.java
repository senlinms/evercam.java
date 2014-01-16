package io.evercam;


import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class CameraTest
{

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateStream() throws EvercamException {
        Map<String, Object> streamMap = new HashMap<String, Object>();
        streamMap.put("id", "testcamera");
        streamMap.put("is_public", true);
        streamMap.put("auth", "{\"basic\": {\"username\": \"user1\",\"password\": \"abcde\"}}");
        streamMap.put("snapshots", "{\"jpg\": \"/onvif/snapshot\"}");
        streamMap.put("endpoints", "[\"http://127.0.0.1:8080\"]");
        Camera camera = Camera.create(streamMap);
        assertEquals("testcamera", camera.getId());
    }
    @Test
    public void testGetByIdCamera() throws EvercamException {
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
    }
}
