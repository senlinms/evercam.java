package io.evercam;


import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;

public class CameraTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateCamera() throws EvercamException, JSONException
    {
        CameraDetail cameraDetail = new CameraDetail();
        cameraDetail.setId("testcamera");
        cameraDetail.setName("testcameraname");
        cameraDetail.setPublic(true);
        cameraDetail.setSnapshotJPG("/onvif/snapshot");
        cameraDetail.setBasicAuth("user1", "abcde");
        cameraDetail.setEndpoints(new String[]{"http://127.0.0.1:8080"});
        API.setAuth("joeyb", "12345");
        Camera camera = Camera.create(cameraDetail);
        assertEquals("testcamera", camera.getId());

        API.setAuth(null, null);
        exception.expect(EvercamException.class);
        Camera.create(cameraDetail);
    }

    @Test
    public void testCreateBoundary() throws EvercamException, JSONException
    {
        CameraDetail cameraDetail = new CameraDetail();
        cameraDetail.setId("fail");
        cameraDetail.setName("name");
        cameraDetail.setBasicAuth("user1","abcde");
        cameraDetail.setSnapshotJPG("/jpg");
        cameraDetail.setEndpoints(new String[]{"http://127.0.0.1:8080"});
        cameraDetail.setPublic(true);
        API.setAuth("joeyb", "12345");
        exception.expect(EvercamException.class);
        Camera.create(cameraDetail);
    }

    @Test
    public void testGetByIdCamera() throws EvercamException, IOException
    {
        API.setAuth(null, null);
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
        assertEquals("joeyb", camera.getOwner());
        assertTrue(camera.isPublic());
        assertEquals("user1", camera.getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("abcde", camera.getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals(3, camera.getEndpoints().size());
        assertEquals("/snapshot.jpg", camera.getSnapshotPath("jpg"));
        assertEquals(105708, getBytes(camera.getSnapshotStream()).length);
        assertEquals("Public Camera", camera.getName());
        assertEquals("axis", camera.getVendor());
        assertEquals("null", camera.getModel());
        assertEquals("Etc/UTC", camera.getTimezone());
        assertEquals("null", camera.isOnline());

        API.setAuth("joeyb", "12345");
        Camera cameraPrivate = Camera.getById("privatecamera");
        assertFalse(cameraPrivate.isPublic());
        assertEquals("privatecamera", cameraPrivate.getId());

    }

    @Test
    public void tesBoundaryUnknownAuth() throws EvercamException
    {
        Camera camera = Camera.getById("testcamera");
        exception.expect(EvercamException.class);
        camera.getAuth("");
    }

    private static byte[] getBytes(InputStream is) throws IOException
    {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream)
        {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        }
        else
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

}
