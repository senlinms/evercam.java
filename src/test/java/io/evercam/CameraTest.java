package io.evercam;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;

public class CameraTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateCamera() throws EvercamException, JSONException
    {
        CameraInfo cameraInfo = new CameraInfo();
        cameraInfo.setId("testcamera");
        cameraInfo.setName("testcameraname");
        cameraInfo.setPublic(true);
        cameraInfo.setSnapshotJPG("/onvif/snapshot");
        cameraInfo.setBasicAuth(new String[]{"user1","abcde"});
        cameraInfo.setEndpoints(new String[]{"http://127.0.0.1:8080"});
        Camera camera = Camera.create(cameraInfo);
        assertEquals("testcamera",camera.getId());
    }

    @Test
    public void testGetByIdCamera() throws EvercamException, IOException
    {
        API.setAuth(null,null);
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
        assertEquals("joeyb",camera.getOwner());
        assertTrue(camera.isPublic());
        assertEquals("user1", camera.getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("abcde",camera.getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals(3, camera.getEndpoints().size());
        assertEquals("/snapshot.jpg",camera.getSnapshotPath("jpg"));
        assertEquals(105708, getBytes(camera.getSnapshotStream()).length);
        assertEquals("Public Camera", camera.getName());
        assertEquals("axis", camera.getVendor());
        assertEquals("null", camera.getModel());
        assertEquals("Etc/UTC",camera.getTimezone());
        assertEquals("null", camera.isOnline());

        API.setAuth("joeyb","12345");
        Camera cameraPrivate = Camera.getById("privatecamera");
        assertFalse(cameraPrivate.isPublic());
        assertEquals("privatecamera",cameraPrivate.getId());

    }

        @Test
        public void tesBoundaryUnknownAuth() throws EvercamException
        {
            Camera camera = Camera.getById("testcamera");
            exception.expect(EvercamException.class);
            camera.getAuth("");
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
