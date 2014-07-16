package io.evercam;


import junit.framework.*;
import org.junit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CameraTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
        API.setDeveloperKeyPair(LocalConstants.DEVELOPER_KEY, LocalConstants.DEVELOPER_ID);
    }

    @Test
    public void testCreateAndDeleteCamera() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();

        Camera camera = randomUser.addRandomCamera(true);
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        assertEquals(1, User.getCameras(randomUser.getUsername(), false).size());

        Camera.delete(camera.getId());
        assertEquals(0, User.getCameras(randomUser.getUsername(), false).size());

        /**
         * Test create camera with location
         */
        Camera cameraWithLocation = randomUser.addBasicCameraWithLocation();
        assertEquals(RandomUser.LOCATION_LNG, cameraWithLocation.getLocation().getLng(), 0);
        assertEquals(RandomUser.LOCATION_LAT, cameraWithLocation.getLocation().getLat(), 0);

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testPatchCamera() throws EvercamException
    {
        final String PATCH_CAMERA_NAME = "Patch Camera";
        RandomUser randomUser = new RandomUser();
        Camera camera = randomUser.addBasicCamera();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        assertFalse(camera.isOnline());
        CameraDetail detail = new PatchCameraBuilder(camera.getId()).setInternalHost(RandomUser.CAMERA_INTERNAL_HOST).setInternalHttpPort(RandomUser.
                CAMERA_INTERNAL_HTTP).setInternalRtspPort(RandomUser.CAMERA_INTERNAL_RTSP).setExternalHost(RandomUser.CAMERA_EXTERNAL_HOST).setExternalHttpPort(RandomUser.CAMERA_EXTERNAL_HTTP)
                .setExternalRtspPort(RandomUser.CAMERA_EXTERNAL_RTSP).setCameraUsername(RandomUser.CAMERA_USERNAME).setCameraPassword(RandomUser.CAMERA_PASSWORD).setJpgUrl(RandomUser.CAMERA_JPG_URL)
                .setRtspUrl(RandomUser.CAMERA_RTSP_URL).setTimeZone(RandomUser.CAMERA_TIMEZONE).setVendor(RandomUser.CAMERA_VENDOR).setModel(RandomUser.CAMERA_MODEL).setMacAddress(RandomUser.CAMERA_MAC)
                .setName(PATCH_CAMERA_NAME).setPublic(false).setOnline(true).setLocation(RandomUser.LOCATION_LNG, RandomUser.LOCATION_LAT).build();
        Camera patchCamera = Camera.patch(detail);
        assertEquals(PATCH_CAMERA_NAME, patchCamera.getName());
        assertTrue(patchCamera.isOnline()); //Test patch camera 'is_online'
        assertEquals(false, patchCamera.isPublic());
        assertEquals(RandomUser.CAMERA_INTERNAL_HOST, patchCamera.getInternalHost());
        assertEquals(RandomUser.CAMERA_INTERNAL_HTTP, patchCamera.getInternalHttpPort());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP, patchCamera.getInternalRtspPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HOST, patchCamera.getExternalHost());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HTTP, patchCamera.getExternalHttpPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP, patchCamera.getExternalRtspPort());
        assertEquals(RandomUser.CAMERA_USERNAME, patchCamera.getUsername());
        assertEquals(RandomUser.CAMERA_PASSWORD, patchCamera.getPassword());
        //   assertEquals(RandomUser.CAMERA_JPG_URL, patchCamera.getJpgUrl());
        //   assertEquals(RandomUser.CAMERA_RTSP_URL, patchCamera.getRtspUrl());
        assertEquals(RandomUser.CAMERA_TIMEZONE, patchCamera.getTimezone());
        assertEquals(RandomUser.CAMERA_VENDOR, patchCamera.getVendorId());
        assertEquals(RandomUser.CAMERA_VENDOR_NAME, patchCamera.getVendorName());
        assertEquals(RandomUser.CAMERA_MAC, patchCamera.getMacAddress());
        assertEquals(RandomUser.CAMERA_MODEL, patchCamera.getModel());
        assertFalse(patchCamera.isDiscoverable());

        assertEquals(RandomUser.LOCATION_LNG, patchCamera.getLocation().getLng(), 0);
        assertEquals(RandomUser.LOCATION_LAT, patchCamera.getLocation().getLat(), 0);

        assertNotNull(patchCamera.getDynamicDns());
        assertNotNull(patchCamera.getProxyUrl().getJpg());
        //    assertTrue(patchCamera.isOwned()); FIXME!

        assertEquals(RandomUser.CAMERA_INTERNAL_URL, patchCamera.getInternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_EXTERNAL_URL, patchCamera.getExternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_INTERNAL_JPG_URL, patchCamera.getInternalJpgUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_JPG_URL, patchCamera.getExternalJpgUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL, patchCamera.getInternalH264Url());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL, patchCamera.getExternalH264Url());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL_WITH_AUTH, patchCamera.getInternalH264UrlWithCredential());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL_WITH_AUTH, patchCamera.getExternalH264UrlWithCredential());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testCameraGetByIdAndIdSet() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        Camera randomCamera1 = randomUser.addRandomCamera(true);
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        Camera camera = Camera.getById(randomCamera1.getId());
        assertEquals(randomCamera1.getId(), camera.getId());
        assertEquals(randomUser.getUsername(), camera.getOwner());
        assertEquals(2, camera.getEndpoints().size());

        Camera randomCamera2 = randomUser.addRandomCamera(true);
        String idSet = randomCamera1.getId() + "," + randomCamera2.getId();
        ArrayList<Camera> camerasSet = Camera.getByIdSet(idSet);
        Assert.assertEquals(2, camerasSet.size());
        API.setUserKeyPair(null, null);
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}
