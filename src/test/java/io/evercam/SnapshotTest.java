package io.evercam;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

public class SnapshotTest {


    @BeforeClass
    public static void setUpClass() {
        API.URL = "http://127.0.0.1:3000/v1/";
    }

    @Test
    public void testGetSnapshot() throws EvercamException, JSONException {
        Snapshot snapshot = Snapshot.getSnapshot("test");
        JSONObject uris = snapshot.getUris();
        JSONObject formats = snapshot.getFormats();
        JSONObject auth = snapshot.getAuth();
    }

}
