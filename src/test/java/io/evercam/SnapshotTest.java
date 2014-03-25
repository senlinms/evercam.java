package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class SnapshotTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
        API.setAuth("joeyb", "12345");
        API.setDeveloperKeyPair("apikey", "apiid");
    }

    @Test
    public void testSaveSnapshot() throws EvercamException
    {
        Snapshot snapshot = Camera.archiveSnapshot("testid", null);
        assertEquals("testid", snapshot.getCameraId());
        assertEquals("null", snapshot.getNotes());
        assertEquals(1394712493, snapshot.getTimeStamp());
        assertEquals("Europe/Dublin", snapshot.getTimeZone());
    }

    @Test
    public void testSaveSnapshotCameraNotExists() throws EvercamException
    {
        exception.expect(EvercamException.class);
        Camera.archiveSnapshot("fail", null);
    }

    @Test
    public void testSaveSnapshotCameraOffline() throws EvercamException
    {
        exception.expect(EvercamException.class);
        Camera.archiveSnapshot("offline", null);
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setAuth(null, null);
        API.setDeveloperKeyPair(null, null);
    }
}
