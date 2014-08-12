package io.evercam;

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
        API.setDeveloperKeyPair(LocalConstants.DEVELOPER_KEY, LocalConstants.DEVELOPER_ID);
    }

    @Test
    public void testArchiveSnapshotAndGetSnapshot() throws EvercamException
    {
        final String SNAPSHOT_NOTE = "note";
        RandomUser randomUser = new RandomUser();
        Camera camera = randomUser.addRealCamera();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        //Test archive two Snapshot
        Camera.archiveSnapshot(camera.getId(), SNAPSHOT_NOTE);
        Camera.archiveSnapshot(camera.getId(), SNAPSHOT_NOTE);

        //Test get all archived snapshots
        ArrayList<Snapshot> snapshots = Camera.getArchivedSnapshots(camera.getId());
        assertEquals(2, snapshots.size());

        //Test get latest snapshot, and Snapshot 'get' methods
        Snapshot latestSnapshot = Camera.getLatestArchivedSnapshot(camera.getId(), true);
        assertEquals(SNAPSHOT_NOTE, latestSnapshot.getNotes());

        assertNotNull(latestSnapshot.getCompleteData());
        assertNotNull(latestSnapshot.getBase64DataString());
        assertNotNull(latestSnapshot.getTimeStamp());

        assertEquals(camera.getTimezone(), latestSnapshot.getTimeZone());
        Snapshot snapshotWithoutData = Camera.getLatestArchivedSnapshot(camera.getId(), false);
        assertNull(snapshotWithoutData.getCompleteData());
        assertNull(snapshotWithoutData.getBase64DataString());
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}
