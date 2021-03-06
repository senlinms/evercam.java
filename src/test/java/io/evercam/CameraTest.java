package io.evercam;


import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CameraTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateAndDeleteCamera() throws EvercamException {
        //TODO: Use the testing server / remove the commented code
        //API.resetUrl();

        RandomUser randomUser = new RandomUser();

        Camera randomCamera = randomUser.addRandomCamera(true);
        assertFalse(randomCamera.isOnline());
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        assertEquals(1, Camera.getAll(randomUser.getUsername(), false, false).size());

        //TODO: Wait for the bug fix to re-enable this, currently can not delete camera
        boolean deleteSuccess = Camera.delete(randomCamera.getId());
        Assert.assertTrue(deleteSuccess);
        assertEquals(0, Camera.getAll(randomUser.getUsername(), false, false).size());

        /**
         * Test create camera with location and online status
         */
        Camera camera = randomUser.addFullCamera();
        assertEquals(RandomUser.LOCATION_LNG, camera.getLocation().getLng(), 0);
        assertEquals(RandomUser.LOCATION_LAT, camera.getLocation().getLat(), 0);
        assertTrue(camera.isOnline());
        assertEquals(RandomUser.CAMERA_NAME, camera.getName());
        assertEquals(true, camera.isPublic());
        assertEquals(RandomUser.CAMERA_INTERNAL_HOST, camera.getInternalHost());
        assertEquals(RandomUser.CAMERA_INTERNAL_HTTP, camera.getInternalHttpPort());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP, camera.getInternalRtspPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HOST, camera.getExternalHost());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HTTP, camera.getExternalHttpPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP, camera.getExternalRtspPort());
        assertEquals(RandomUser.CAMERA_USERNAME, camera.getUsername());
        assertEquals(RandomUser.CAMERA_PASSWORD, camera.getPassword());
        //   assertEquals(RandomUser.CAMERA_JPG_URL, patchCamera.getJpgUrl());
        //   assertEquals(RandomUser.CAMERA_RTSP_URL, patchCamera.getRtspUrl());
        assertEquals(RandomUser.CAMERA_TIMEZONE, camera.getTimezone());
        assertEquals(RandomUser.CAMERA_VENDOR, camera.getVendorId());
        assertEquals(RandomUser.CAMERA_VENDOR_NAME, camera.getVendorName());
        assertEquals(RandomUser.CAMERA_MAC, camera.getMacAddress());
        assertEquals(RandomUser.CAMERA_MODEL_NAME, camera.getModelName());
        assertEquals(RandomUser.CAMERA_MODEL_ID, camera.getModelId());
        assertTrue(camera.isDiscoverable());

        assertEquals(RandomUser.LOCATION_LNG, camera.getLocation().getLng(), 0);
        assertEquals(RandomUser.LOCATION_LAT, camera.getLocation().getLat(), 0);

        assertNotNull(camera.getProxyUrl().getHls());
        assertNotNull(camera.getProxyUrl().getRtmp());

        assertEquals(RandomUser.CAMERA_INTERNAL_URL, camera.getInternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_EXTERNAL_URL, camera.getExternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_INTERNAL_JPG_URL, camera.getInternalJpgUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_JPG_URL, camera.getExternalJpgUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL, camera.getInternalH264Url());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL, camera.getExternalH264Url());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL_WITH_AUTH, camera.getInternalH264UrlWithCredential());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL_WITH_AUTH, camera.getExternalH264UrlWithCredential());
        assertEquals(RandomUser.CAMERA_INTERNAL_MJPG_URL, camera.getInternalObject().getHttp().getMjpgUrl());
        //assertEquals(RandomUser.CAMERA_EXTERNAL_MJPG_URL, camera.getExternalObject().getHttp().getMjpgUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_MPEG_URL, camera.getInternalObject().getRtsp().getMpegUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_MPEG_URL, camera.getExternalObject().getRtsp().getMpegUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_AUDIO_URL, camera.getInternalObject().getRtsp().getAudioUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_AUDIO_URL, camera.getExternalObject().getRtsp().getAudioUrl());

        //Delete the user after testing
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        assertTrue(User.delete(randomUser.getUsername()));
        API.setUserKeyPair(null, null);

        //API.URL = TestURL.URL;
    }

    @Test
    public void testGetAllCamerasWithCredentials() throws EvercamException {
        //TODO: Use the testing server or remove the commented code
        //API.resetUrl();

        RandomUser randomUser = new RandomUser();
        randomUser.addRandomCamera(true);
        randomUser.addRandomCamera(false);

        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        ArrayList<Camera> allCameras = Camera.getAll(randomUser.getUsername(), false, false);
        assertEquals(2, allCameras.size());

        ArrayList<Camera> allCamerasIncludingShared = Camera.getAll(randomUser.getUsername(), true, false);
        assertEquals(3, allCamerasIncludingShared.size());

        //Test camera thumbnail
        Camera demoCamera = Camera.getById("evercam-remembrance-camera", true);
        String thumbnailUrl = demoCamera.getThumbnailUrl();
        assertEquals("https://media.evercam.io/v1/cameras/evercam-remembrance-camera/thumbnail?api_id=" +
                apiKeyPair.getApiId() + "&api_key=" + apiKeyPair.getApiKey(), thumbnailUrl);

        //Delete the user after testing
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        assertTrue(User.delete(randomUser.getUsername()));
        API.setUserKeyPair(null, null);

        //API.URL = TestURL.URL;
    }

    @Test
    public void testGetCamerasWithoutUsername() throws EvercamException {
        RandomUser randomUser = new RandomUser();
        randomUser.addRandomCamera(true);
        randomUser.addRandomCamera(false);

        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        ArrayList<Camera> allCameras = Camera.getAll(null, false, false);
        assertEquals(2, allCameras.size());

        ArrayList<Camera> allCamerasIncludingShared = Camera.getAll(randomUser.getUsername(), true, false);
        assertEquals(3, allCamerasIncludingShared.size());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testPatchCamera() throws EvercamException {
        //TODO: Use the testing server / remove the commented code
        //API.resetUrl();

        final String PATCH_CAMERA_NAME = "Patch Camera";
        RandomUser randomUser = new RandomUser();
        Camera camera = randomUser.addBasicCamera();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        assertFalse(camera.isOnline());
        //FIXME: Patch camera only accept location data as string, not float
        CameraDetail detail = new PatchCameraBuilder(camera.getId()).setInternalHost(RandomUser.CAMERA_INTERNAL_HOST).setInternalHttpPort(RandomUser.
                CAMERA_INTERNAL_HTTP).setInternalRtspPort(RandomUser.CAMERA_INTERNAL_RTSP).setExternalHost(RandomUser.CAMERA_EXTERNAL_HOST)
                .setExternalHttpPort(RandomUser.CAMERA_EXTERNAL_HTTP).setExternalRtspPort(RandomUser.CAMERA_EXTERNAL_RTSP)
                .setCameraUsername(RandomUser.CAMERA_USERNAME).setCameraPassword(RandomUser.CAMERA_PASSWORD).setJpgUrl(RandomUser.CAMERA_JPG_URL)
                .setH264Url(RandomUser.CAMERA_H264_URL).setMjpgUrl(RandomUser.CAMERA_MJPG_URL).setMpegUrl(RandomUser.CAMERA_MPEG_URL)
                .setAudioUrl(RandomUser.CAMERA_AUDIO_URL).setTimeZone(RandomUser.CAMERA_TIMEZONE).setVendor(RandomUser.CAMERA_VENDOR)
                .setModel(RandomUser.CAMERA_MODEL_ID).setMacAddress(RandomUser.CAMERA_MAC).setName(PATCH_CAMERA_NAME).setPublic(false)
                .setOnline(true).setLocation(RandomUser.LOCATION_LAT_STRING, RandomUser.LOCATION_LNG_STRING)
                .setDiscoverable(true)
                .build();
        Camera patchCamera = Camera.patch(detail);
        assertEquals(PATCH_CAMERA_NAME, patchCamera.getName());
        assertTrue(patchCamera.isOnline());
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
        assertEquals(RandomUser.CAMERA_MODEL_NAME, patchCamera.getModelName());
        assertEquals(RandomUser.CAMERA_MODEL_ID, patchCamera.getModelId());
        assertTrue(patchCamera.isDiscoverable());

        assertEquals(RandomUser.LOCATION_LNG, patchCamera.getLocation().getLng(), 0);
        assertEquals(RandomUser.LOCATION_LAT, patchCamera.getLocation().getLat(), 0);

        assertNotNull(patchCamera.getProxyUrl().getHls());
        assertNotNull(patchCamera.getProxyUrl().getRtmp());
        //    assertTrue(patchCamera.isOwned()); FIXME!

        assertEquals(RandomUser.CAMERA_INTERNAL_URL, patchCamera.getInternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_EXTERNAL_URL, patchCamera.getExternalCameraEndpoint());
        assertEquals(RandomUser.CAMERA_INTERNAL_JPG_URL, patchCamera.getInternalJpgUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_JPG_URL, patchCamera.getExternalJpgUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL, patchCamera.getInternalH264Url());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL, patchCamera.getExternalH264Url());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP_URL_WITH_AUTH, patchCamera.getInternalH264UrlWithCredential());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP_URL_WITH_AUTH, patchCamera.getExternalH264UrlWithCredential());

        assertEquals(RandomUser.CAMERA_INTERNAL_MJPG_URL, patchCamera.getInternalObject().getHttp().getMjpgUrl());
        //No external MJPG url in API
        //assertEquals(RandomUser.CAMERA_EXTERNAL_MJPG_URL, patchCamera.getExternalObject().getHttp().getMjpgUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_MPEG_URL, patchCamera.getInternalObject().getRtsp().getMpegUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_MPEG_URL, patchCamera.getExternalObject().getRtsp().getMpegUrl());
        assertEquals(RandomUser.CAMERA_INTERNAL_AUDIO_URL, patchCamera.getInternalObject().getRtsp().getAudioUrl());
        assertEquals(RandomUser.CAMERA_EXTERNAL_AUDIO_URL, patchCamera.getExternalObject().getRtsp().getAudioUrl());

        //Delete the user after testing
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        assertTrue(User.delete(randomUser.getUsername()));

        API.setUserKeyPair(null, null);

        //API.URL = TestURL.URL;
    }

    @Test
    public void testCameraGetByIdAndIdSet() throws EvercamException {
        RandomUser randomUser = new RandomUser();
        Camera randomCamera1 = randomUser.addRandomCamera(true);
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        Camera camera = Camera.getById(randomCamera1.getId(), false);
        assertEquals(randomCamera1.getId(), camera.getId());
        assertEquals(randomUser.getUsername(), camera.getOwner());
        assertEquals(2, camera.getEndpoints().size());

        Camera randomCamera2 = randomUser.addRandomCamera(true);
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        String idSet = randomCamera1.getId() + "," + randomCamera2.getId();
        ArrayList<Camera> camerasSet = Camera.getByIdSet(idSet);
        Assert.assertEquals(2, camerasSet.size());
        API.setUserKeyPair(null, null);
    }

    //TODO: Test for get snapshot. Currently it's disabled because test server doesn't work well with snapshot
//    @Test
//    public void testGetSnapshot() throws EvercamException, IOException
//    {
//        API.setUserKeyPair(LocalConstants.API_KEY, LocalConstants.API_ID);
//        InputStream stream = Camera.getSnapshotByCameraId("evercam-remembrance-camera");
//        byte[] byteSnapshot = IOUtils.toByteArray(stream);
//        System.out.println(byteSnapshot.length);
//        Assert.assertTrue(byteSnapshot.length > 200);
//        API.setUserKeyPair(null, null);
//    }

    @Test
    public void testTestSnapshot() throws EvercamException {
        //FIXME: The test request is send to the production server
        API.resetUrl();
        String URL = "http://" + LocalConstants.IP + ":" + LocalConstants.HTTP_PORT;
        String ENDING = LocalConstants.SNAPSHOT_ENDING;
        Snapshot snapshot = Camera.testSnapshot(URL, ENDING, LocalConstants.USERNAME, LocalConstants.PASSWORD, LocalConstants.VENDOR_ID, LocalConstants.CAMERA_EXID);
        assertNotNull(snapshot);
        Assert.assertTrue(snapshot.getData().length > 0);
        API.URL = TestURL.URL;
    }

    @AfterClass
    public static void destroyClass() {

    }
}
