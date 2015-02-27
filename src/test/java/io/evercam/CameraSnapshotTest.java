package io.evercam;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.*;

public class CameraSnapshotTest
{
    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Test
    public void testSnapshotsWithPaging() throws EvercamException
    {
        final String snapshotListJsonString = "{\"snapshots\": [{\"created_at\": 1410768882,\"notes\": \"Evercam System\"}],\"timezone\":\"Europe/Dublin\",\"pages\": 11}";
        SnapshotsWithPaging snapshotsWithPaging = new SnapshotsWithPaging(new JSONObject(snapshotListJsonString));
        assertEquals(11, snapshotsWithPaging.getTotalPages());
        assertEquals(1,snapshotsWithPaging.getSnapshotsList().size());
    }

    @Test
    public void testGetSnapshots() throws EvercamException
    {
        //Target to production server only for this test case
        API.resetUrl();

        final int TIME_FROM = 1421692800;
        final int TIME_TO = 1421723075;

        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);

        //Get hours that contains snapshots
        assertEquals(24, Snapshot.getHoursContainSnapshots(LocalConstants.TEST_CAMERA_ID, 2015, 2, 4).size());

        //Get days that contains snapshots
        assertEquals(13, Snapshot.getDaysContainSnapshots(LocalConstants.TEST_CAMERA_ID, 2015, 1).size());

        //Get snapshots in the first page
        assertEquals(1, Snapshot.getSnapshotListWithPaging(LocalConstants.TEST_CAMERA_ID,TIME_FROM,TIME_TO, 1, 1).getSnapshotsList().size());

        //Get all snapshots in a certain time
        ArrayList<Snapshot> snapshotList = Snapshot.getRecordedSnapshots(LocalConstants.TEST_CAMERA_ID, TIME_FROM, TIME_TO);
        assertEquals(17, snapshotList.size());

        //Get snapshot by time
        Snapshot snapshot = Snapshot.getByTime(LocalConstants.TEST_CAMERA_ID, TIME_FROM, true, 1);
        assertEquals("Initial snapshot", snapshot.getNotes());
        assertNotNull(snapshot.getData());

        API.setUserKeyPair(null, null);
        API.URL = TestURL.URL;
    }

    @Test
    public void testStoreSnapshotAndGetLatest() throws EvercamException
    {
        final String SNAPSHOT_NOTE = "Java Wrapper";
        RandomUser randomUser = new RandomUser();
        Camera camera = randomUser.addRealCamera();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        Snapshot.record(camera.getId(), SNAPSHOT_NOTE);
        Snapshot.record(camera.getId(), SNAPSHOT_NOTE);

        Snapshot latestSnapshot = Snapshot.getLatest(camera.getId(), true);
        assertEquals(SNAPSHOT_NOTE, latestSnapshot.getNotes());

        assertNotNull(latestSnapshot.getCompleteData());
        assertNotNull(latestSnapshot.getBase64DataString());
        assertNotNull(latestSnapshot.getTimeStamp());

        Snapshot snapshotWithoutData = Snapshot.getLatest(camera.getId(), false);
        assertNull(snapshotWithoutData.getCompleteData());
        assertNull(snapshotWithoutData.getBase64DataString());
    }

    @AfterClass
    public static void destroyClass()
    {

    }
}
