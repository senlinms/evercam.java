package io.evercam;


import org.junit.BeforeClass;
import org.junit.Test;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class CameraTest
{

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

//    @Test
//    public void testCreateCamera() throws EvercamException {
//        Map<String, Object> cameraMap = new HashMap<String, Object>();
//        cameraMap.put("id", "testcamera");
//        cameraMap.put("is_public", true);
//        cameraMap.put("auth", "{\"basic\": {\"username\": \"user1\",\"password\": \"abcde\"}}");
//        cameraMap.put("snapshots", "{\"jpg\": \"/onvif/snapshot\"}");
//        cameraMap.put("endpoints", "[\"http://127.0.0.1:8080\"]");
//        Camera camera = Camera.create(cameraMap);
//        assertEquals("testcamera", camera.getId());
//    }
    @Test
    public void testGetByIdCamera() throws EvercamException, IOException
    {
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
        assertEquals("joeyb",camera.getOwner());
        assertTrue(camera.isPublic());
        assertEquals("user1", camera.getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("abcde",camera.getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals(3, camera.getEndpoints().size());
        assertEquals("/snapshot.jpg",camera.getSnapshotPath("jpg"));
        assertEquals(105708, getBytes(camera.getSnapshotStream()).length);

    }
    private static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

}
