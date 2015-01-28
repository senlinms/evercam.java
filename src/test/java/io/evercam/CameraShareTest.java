package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class CameraShareTest
{
    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
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
        ArrayList<Camera> cameraList = Camera.getAll(sharedUser.getUsername(), true, false);
        Camera sharedCamera= Camera.getById(ownedCamera.getId(),false);
        assertEquals(2, cameraList.size());
        assertEquals(ownedCamera.getId(),sharedCamera.getId());

        //Then delete the camera share
        boolean cameraDeleted = CameraShare.delete(sharedCamera.getId(), sharedUser.getUsername());
        assertTrue(cameraDeleted);
        assertEquals(1,Camera.getAll(sharedUser.getUsername(), true, false).size());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testGetCameraShare() throws EvercamException
    {
        //Create camera owner and add a camera
        RandomUser owner = new RandomUser();
        ApiKeyPair ownerKeyPair = API.requestUserKeyPairFromEvercam(owner.getUsername(), owner.getPassword());
        Camera ownedCamera = owner.addRandomCamera(true);

        //Create user to share camera with
        RandomUser sharedUser1 = new RandomUser();
        ApiKeyPair sharedKeyPair1 = API.requestUserKeyPairFromEvercam(sharedUser1.getUsername(), sharedUser1.getPassword());
        RandomUser sharedUser2 = new RandomUser();
        ApiKeyPair sharedKeyPair2 = API.requestUserKeyPairFromEvercam(sharedUser2.getUsername(), sharedUser2.getPassword());

        //Owner share the camera
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), sharedUser1.getUsername(), "Snapshot,View,Edit,List");
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), sharedUser2.getUsername(), "Snapshot,View,Edit,List");

        //Test get share by camera
        ArrayList<CameraShare> shareListByCamera = CameraShare.getByCamera(ownedCamera.getId());
        assertEquals(2,shareListByCamera.size());

        //Test get specific share by user and camera and test share object details
        CameraShare share = CameraShare.get(ownedCamera.getId(), sharedUser1.getUsername());
        assertNotNull(share);
        assertEquals(ownedCamera.getId(),share.getCameraId());
        assertEquals(owner.getUsername(),share.getSharerId());
        assertEquals(sharedUser1.getUsername(),share.getUserId());
        assertEquals(sharedUser1.getEmail(), share.getUserEmail());
        assertTrue(share.getRights().canEdit());

        API.setUserKeyPair(null, null);
    }

    @AfterClass
    public static void destroyClass()
    {

    }
}
