package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CameraShareRequestTest
{
    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Test
    public void testGetPatchDeleteCameraShareRequest() throws EvercamException
    {
        final String TEST_SHARER_EMAIL = "liuting+999@evercam.io";

        //TODO: Use the testing server / remove the commented code
        //API.resetUrl();

        //Create camera owner and add a camera
        RandomUser owner = new RandomUser();
        ApiKeyPair ownerKeyPair = API.requestUserKeyPairFromEvercam(owner.getUsername(), owner.getPassword());
        Camera ownedCamera = owner.addRandomCamera(true);

        //Owner share the camera with a user that doesn't exist
        API.setUserKeyPair(ownerKeyPair.getApiKey(), ownerKeyPair.getApiId());
        CameraShare.create(ownedCamera.getId(), TEST_SHARER_EMAIL, Right.FULL_RIGHTS, CameraShareTest.TEST_SHARE_MESSAGE);

        ArrayList<CameraShareRequest> shareRequestList = CameraShareRequest.get(ownedCamera.getId(),
                CameraShareRequest.STATUS_PENDING);

        assertEquals(1, shareRequestList.size());
        CameraShareRequest shareRequest = shareRequestList.get(0);
        assertEquals(ownedCamera.getId(), shareRequest.getCameraId());
        assertEquals(owner.getUser().getFirstName() + " " + owner.getUser().getLastName(), shareRequest.getSharerName());
        assertEquals(owner.getEmail(), shareRequest.getSharerEmail());
        assertEquals(Right.FULL_RIGHTS, shareRequest.getRights().toString());
        assertEquals(owner.getUsername(), shareRequest.getUserId());
        assertEquals(TEST_SHARER_EMAIL, shareRequest.getEmail());

        //Test patch share request
        CameraShareRequest patchedShareRequest = CameraShareRequest.patch(ownedCamera.getId(), TEST_SHARER_EMAIL,
                Right.READ_ONLY);
        assertEquals(Right.READ_ONLY, patchedShareRequest.getRights().toString());

        //Test delete/revoke share request
        assertTrue(CameraShareRequest.delete(ownedCamera.getId(), TEST_SHARER_EMAIL));
        ArrayList<CameraShareRequest> shareRequestListAfterDelete = CameraShareRequest.get(ownedCamera.getId(),
                CameraShareRequest.STATUS_PENDING);
        assertEquals(0, shareRequestListAfterDelete.size());

        //Delete the random user after testing
        assertTrue(User.delete(owner.getUsername()));

        //API.URL = TestURL.URL;
    }

    @AfterClass
    public static void destroyClass()
    {

    }
}
