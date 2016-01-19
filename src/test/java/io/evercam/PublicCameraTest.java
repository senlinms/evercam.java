package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PublicCameraTest {
    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testNearestCamera() throws EvercamException {
        Camera camera = PublicCamera.getNearest(null);
        assertEquals("stephens-green", camera.getId());

        Camera cameraByAddress = PublicCamera.getNearest("stephen green");
        assertEquals("stephens-green", cameraByAddress.getId());

        Camera cameraByLocation = PublicCamera.getNearest("53.343826,-6.247662");
        assertEquals("pearse-street", cameraByLocation.getId());
    }

    @Test
    public void testNearestJpg() throws EvercamException {
        //TODO: Tests for get nearest JPG
    }

    @AfterClass
    public static void destroyClass() {
    }
}
