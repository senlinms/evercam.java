package io.evercam;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CameraShareTest
{
    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
        API.setDeveloperKeyPair(LocalConstants.DEVELOPER_KEY, LocalConstants.DEVELOPER_ID);
    }

    @Test
    public void testCreateAndDeleteShare() throws EvercamException
    {
        //Create camera owner and add a camera
        RandomUser owner = new RandomUser();
        ApiKeyPair ownerKeyPair = API.requestUserKeyPairFromEvercam(owner.getUsername(), owner.getPassword());
        Camera ownedCamera = owner.addRandomCamera(true);

        //Create user to share camera with
        RandomUser sharedUser = new RandomUser();
        ApiKeyPair sharedKeyPair = API.requestUserKeyPairFromEvercam(sharedUser.getUsername(), sharedUser.getPassword());

        //Owner share the camera
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), sharedUser.getUsername(), "Snapshot,View,Edit,List");

        //Validate camera is successfully shared
        API.setUserKeyPair(sharedKeyPair.getApiKey(), sharedKeyPair.getApiId());
        ArrayList<Camera> cameraList = User.getCameras(sharedUser.getUsername(), true, false);
        Camera sharedCamera= Camera.getById(ownedCamera.getId(),false);
        assertEquals(1, cameraList.size());
        assertEquals(ownedCamera.getId(),sharedCamera.getId());

        //Then delete the camera share
        boolean cameraDeleted = CameraShare.delete(sharedCamera.getId());
        assertTrue(cameraDeleted);
        assertEquals(0,User.getCameras(sharedUser.getUsername(), true, false).size());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testGetCameraShare() throws EvercamException
    {

    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}
