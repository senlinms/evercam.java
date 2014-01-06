package io.evercam;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SnapshotTest {


    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testGetSnapshot() throws EvercamException, JSONException {
        Snapshot snapshot = Snapshot.getSnapshot("test");
        JSONObject uris = snapshot.getUris();
        assertEquals("http://mystic-mountains-1471.evr.cm", uris.getString("external"));
        JSONObject formats = snapshot.getFormats();
        assertEquals("/streaming/channels/1/picture", formats.getJSONObject("jpg").getString("path"));
        JSONObject auth = snapshot.getAuth();
        assertEquals("user1", auth.getJSONObject("basic").getString("username"));
    }

}
