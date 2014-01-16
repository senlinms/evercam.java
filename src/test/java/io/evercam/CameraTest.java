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
    public void testCreateCamera() throws EvercamException {
        Map<String, Object> cameraMap = new HashMap<String, Object>();
        cameraMap.put("id", "testcamera");
        cameraMap.put("is_public", true);
        cameraMap.put("auth", "{\"basic\": {\"username\": \"user1\",\"password\": \"abcde\"}}");
        cameraMap.put("snapshots", "{\"jpg\": \"/onvif/snapshot\"}");
        cameraMap.put("endpoints", "[\"http://127.0.0.1:8080\"]");
        Camera camera = Camera.create(cameraMap);
        assertEquals("testcamera", camera.getId());
    }
    @Test
    public void testGetByIdCamera() throws EvercamException {
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
    }
}
