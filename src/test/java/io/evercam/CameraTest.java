package io.evercam;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        assertEquals(1, User.getCameras(randomUser.getUsername()).size());

        Camera.delete(camera.getId());
        assertEquals(0, User.getCameras(randomUser.getUsername()).size());

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

        CameraDetail detail = new PatchCameraBuilder(camera.getId()).setInternalHost(RandomUser.CAMERA_INTERNAL_HOST).setInternalHttpPort(RandomUser.
                CAMERA_INTERNAL_HTTP).setInternalRtspPort(RandomUser.CAMERA_INTERNAL_RTSP).setExternalHost(RandomUser.CAMERA_EXTERNAL_HOST).setExternalHttpPort(RandomUser.CAMERA_EXTERNAL_HTTP)
                .setExternalRtspPort(RandomUser.CAMERA_EXTERNAL_RTSP).setCameraUsername(RandomUser.CAMERA_USERNAME).setCameraPassword(RandomUser.CAMERA_PASSWORD)
                .setJpgUrl(RandomUser.CAMERA_JPG_URL).setTimeZone(RandomUser.CAMERA_TIMEZONE).setVendor(RandomUser.CAMERA_VENDOR).setModel(RandomUser.CAMERA_MODEL)
                .setMacAddress(RandomUser.CAMERA_MAC).setName(PATCH_CAMERA_NAME).setPublic(false).build();
        Camera patchCamera = Camera.patch(detail);
        assertEquals(PATCH_CAMERA_NAME, patchCamera.getName());
        assertEquals(false, patchCamera.isPublic());
        assertEquals(RandomUser.CAMERA_INTERNAL_HOST, patchCamera.getInternalHost());
        assertEquals(RandomUser.CAMERA_INTERNAL_HTTP, patchCamera.getInternalHttpPort());
        assertEquals(RandomUser.CAMERA_INTERNAL_RTSP, patchCamera.getInternalRtspPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HOST, patchCamera.getExternalHost());
        assertEquals(RandomUser.CAMERA_EXTERNAL_HTTP, patchCamera.getExternalHttpPort());
        assertEquals(RandomUser.CAMERA_EXTERNAL_RTSP, patchCamera.getExternalRtspPort());
        assertEquals(RandomUser.CAMERA_USERNAME, patchCamera.getCameraUsername());
        assertEquals(RandomUser.CAMERA_PASSWORD, patchCamera.getCameraPassword());
        assertEquals(RandomUser.CAMERA_JPG_URL, patchCamera.getJpgUrl());
        assertEquals(RandomUser.CAMERA_TIMEZONE, patchCamera.getTimezone());
        assertEquals(RandomUser.CAMERA_VENDOR, patchCamera.getVendor());
        assertEquals(RandomUser.CAMERA_MAC, patchCamera.getMacAddress());
        assertEquals(RandomUser.CAMERA_MODEL, patchCamera.getModel());
        assertFalse(patchCamera.isOnline());

        //tests for get full URLs
        String expectedInternalJpgUrl = "http://127.0.0.1:80/snapshot.jpg";
        String expectedExternalJpgUrl = "http://123.123.123.123:8080/snapshot.jpg";
        assertEquals(expectedInternalJpgUrl,patchCamera.getJpgInternalFullUrl());
        assertEquals(expectedExternalJpgUrl,patchCamera.getJpgExternalFullUrl());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testCameraGetById() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        Camera randomCamera = randomUser.addRandomCamera(true);
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        Camera camera = Camera.getById(randomCamera.getId());
        assertEquals(randomCamera.getId(), camera.getId());
        assertEquals(randomUser.getUsername(), camera.getOwner());
        assertEquals(2, camera.getEndpoints().size());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testCameraGetByIdWithoutUserKeyPair() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        Camera randomCamera = randomUser.addRandomCamera(true);
        Camera camera = Camera.getById(randomCamera.getId());

        exception.expect(EvercamException.class);
        camera.getOwner();
    }

    @Test
    public void testArchiveSnapshot() throws EvercamException
    {
        final String SNAPSHOT_NOTE = "note";
        RandomUser randomUser = new RandomUser();
        Camera camera = randomUser.addRealCamera();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        Camera.archiveSnapshot(camera.getId(), SNAPSHOT_NOTE);
        ArrayList<Snapshot> snapshots = Camera.getArchivedSnapshots(camera.getId());
        assertEquals(1, snapshots.size());
        Snapshot snapshot = snapshots.get(0);
        assertEquals(SNAPSHOT_NOTE, snapshot.getNotes());
        assertEquals(camera.getId(), snapshot.getCameraId());
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}
