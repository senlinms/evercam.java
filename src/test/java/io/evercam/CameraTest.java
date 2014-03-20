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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CameraTest
{
    private final String MOCK_URL = "http://ec2-54-194-83-178.eu-west-1.compute.amazonaws.com:3000/";

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
        API.setAuth("joeyb", "12345");
        API.setKeyPair("apikey", "apiid");
        CameraDetail cameraDetail = new CameraBuilder("testcamera", "testcameraname", true).setExternalUrl("http://127.0.0.1:8080").setJpgUrl("/onvif/snapshot").setCameraUsername("user1").setCameraPassword("abcde").build();
        Camera camera = Camera.create(cameraDetail);
        assertEquals("testcamera", camera.getId());

        API.setAuth(null, null);
        API.setKeyPair(null, null);
        exception.expect(EvercamException.class);
        Camera.create(cameraDetail);
    }

    @Test
    public void testCreateBoundary() throws EvercamException, JSONException
    {
        CameraDetail failDetail = new CameraBuilder("fail", "name", true).setExternalUrl("http://127.0.0.1:8080").setJpgUrl("/onvif/snapshot").setCameraUsername("user1").setCameraPassword("abcde").build();
        API.setAuth("joeyb", "12345");
        exception.expect(EvercamException.class);
        Camera.create(failDetail);
    }

    @Test
    public void testDeleteCamera()
    {
        //FIXME: Missing camera delete tests.
    }

    @Test
    public void testPatchCamera()
    {
        //FIXME: Missing camera patch tests.
    }

    @Test
    public void testGetByIdCamera() throws EvercamException, IOException
    {
        API.setAuth(null, null);
        API.setKeyPair("apikey", "apiid");
        Camera camera = Camera.getById("testcamera");
        assertEquals("testcamera", camera.getId());
        assertEquals("joeyb", camera.getOwner());
        assertTrue(camera.isPublic());
        assertEquals("user1", camera.getCameraUsername());
        assertEquals("abcde", camera.getCameraPassword());
        assertEquals(MOCK_URL + "basicauth", camera.getInternalUrl());
        assertEquals(MOCK_URL + "noauth", camera.getExternalUrl());
        assertEquals(2, camera.getEndpoints().size());
        assertEquals("/snapshot.jpg", camera.getJpgUrl());
        assertEquals(105708, getBytes(camera.getSnapshotImage()).length);
        assertEquals("Public Camera", camera.getName());
        assertEquals("axis", camera.getVendor());
        assertEquals("null", camera.getModel());
        assertEquals("Etc/UTC", camera.getTimezone());
        assertEquals("null", camera.isOnline());
        assertEquals("null", camera.getMacAddress());

        API.setAuth("joeyb", "12345");
        Camera cameraPrivate = Camera.getById("privatecamera");
        assertFalse(cameraPrivate.isPublic());
        assertEquals("privatecamera", cameraPrivate.getId());
        API.setKeyPair(null, null);
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
