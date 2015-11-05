package io.evercam;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class CameraShareTest
{
    public final static String TEST_SHARE_MESSAGE = "Java Wrapper Test Message";

    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateAndDeleteShare() throws EvercamException
    {
        //TODO: Use the testing server / remove the commented code
        //API.resetUrl();

        //Create camera owner and add a camera
        RandomUser owner = new RandomUser();
        ApiKeyPair ownerKeyPair = API.requestUserKeyPairFromEvercam(owner.getUsername(), owner.getPassword());
        Camera ownedCamera = owner.addRandomCamera(true);

        //Create user to share camera with
        RandomUser sharedUser = new RandomUser();
        ApiKeyPair sharedKeyPair = API.requestUserKeyPairFromEvercam(sharedUser.getUsername(), sharedUser.getPassword());

        //Owner share the camera
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), sharedUser.getUsername(), Right.FULL_RIGHTS, TEST_SHARE_MESSAGE);

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

        //Delete the random users
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        assertTrue(User.delete(owner.getUsername()));
        API.setUserKeyPair(sharedKeyPair.getApiKey(), sharedKeyPair.getApiId());
        assertTrue(User.delete(sharedUser.getUsername()));

        API.setUserKeyPair(null, null);

        //API.URL = TestURL.URL;
    }

    @Test
    public void testGetAndPatchCameraShare() throws EvercamException
    {
        //TODO: Use the testing server / remove the commented code
        //API.resetUrl();

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
        CameraShare.create(ownedCamera.getId(), sharedUser1.getUsername(), Right.FULL_RIGHTS, TEST_SHARE_MESSAGE);
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), sharedUser2.getUsername(), Right.FULL_RIGHTS, TEST_SHARE_MESSAGE);

        //Test get share by camera
        ArrayList<CameraShare> shareListByCamera = CameraShare.getByCamera(ownedCamera.getId());
        assertEquals(2,shareListByCamera.size());

        //Test get specific share by user and camera and test share object details
        CameraShare share = CameraShare.get(ownedCamera.getId(), sharedUser1.getUsername());
        assertNotNull(share);
        assertEquals(ownedCamera.getId(),share.getCameraId());
        assertEquals(owner.getUsername(),share.getSharerId());
        assertEquals(owner.getUser().getFullName(), share.getSharerFullName());
        assertEquals(owner.getEmail(), share.getSharerEmail());
        assertEquals(sharedUser1.getUsername(),share.getUserId());
        assertEquals(sharedUser1.getEmail(), share.getUserEmail());
        assertEquals(sharedUser1.getUser().getFullName(), share.getFullName());
        assertTrue(share.getRights().canEdit());
        assertEquals("private", share.getKind());

        //Test for patch camera share
        CameraShare patchedShare = CameraShare.patch(ownedCamera.getId(), sharedUser1.getUsername(), Right.READ_ONLY);
        assertEquals(Right.READ_ONLY.toLowerCase(), patchedShare.getRights().toString().toLowerCase());
        //Test patch share with Email address
        CameraShare patchedShareWithEmail = CameraShare.patch(ownedCamera.getId(), sharedUser1.getEmail(),
                Right.FULL_RIGHTS);
        assertEquals(Right.FULL_RIGHTS.toLowerCase(), patchedShareWithEmail.getRights().toString().toLowerCase());

        //Test transfer owner
        Camera transferredCameraByUsername = Camera.transfer(ownedCamera.getId(), sharedUser1.getUsername());
        assertEquals(sharedUser1.getUsername(), transferredCameraByUsername.getOwner());
        API.setUserKeyPair(sharedKeyPair1.getApiKey(), sharedKeyPair1.getApiId());
        Camera transferredCameraByEmail = Camera.transfer(ownedCamera.getId(), sharedUser2.getUsername());
        assertEquals(sharedUser2.getUsername(), transferredCameraByEmail.getOwner());

        //Delete the random users
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        assertTrue(User.delete(owner.getUsername()));
        API.setUserKeyPair(sharedKeyPair1.getApiKey(), sharedKeyPair1.getApiId());
        assertTrue(User.delete(sharedUser1.getUsername()));
        API.setUserKeyPair(sharedKeyPair2.getApiKey(), sharedKeyPair2.getApiId());
        assertTrue(User.delete(sharedUser2.getUsername()));

        API.setUserKeyPair(null, null);

        //API.URL = TestURL.URL;
    }

    @AfterClass
    public static void destroyClass()
    {

    }
}
