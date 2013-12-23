package io.evercam;


import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class StreamTest {

    @BeforeClass
    public static void setUpClass() {
        API.URL = "http://127.0.0.1:3000/v1/";
    }

    @Test
    public void testCreateStream() throws EvercamException, JSONException {
        Map<String, Object> streamMap = new HashMap<String, Object>();

    }
}
