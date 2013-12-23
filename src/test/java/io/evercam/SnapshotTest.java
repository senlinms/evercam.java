package io.evercam;


import org.junit.BeforeClass;
import org.junit.Test;

public class SnapshotTest {


    @BeforeClass
    public static void setUpClass() {
        API.URL = "http://127.0.0.1:3000/v1/";
    }

    @Test
    public void testGetSnapshot() throws EvercamException {
        Snapshot snapshot = Snapshot.getSnapshot("test");
    }

}
