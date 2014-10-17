package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PublicCameraTest
{
    @BeforeClass
    public static void setUpClass()
    {
     //TODO: Test should target to test server, which is not updated yet.
     //   API.URL = TestURL.URL;
    }

    @Test
    public void testNearestCamera() throws EvercamException
    {
//        Camera camera = PublicCamera.getNearest(null);
//        assertEquals("stgreencam", camera.getId());
//
//        Camera cameraByAddress = PublicCamera.getNearest("stephen green");
//        assertEquals("stgreencam", cameraByAddress.getId());

//        Camera cameraByLocation = PublicCamera.getNearest("53.339953,-6.260698");
//        assertEquals("stgreencam", cameraByLocation.getId());
    }

    @AfterClass
    public static void destroyClass()
    {
    }
}
