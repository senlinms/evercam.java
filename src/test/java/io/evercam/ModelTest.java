package io.evercam;


import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class ModelTest {

    @BeforeClass
    public static void setUpClass() {
        API.URL = "http://127.0.0.1:3000/v1/";
    }

    @Test
    public void testGetAll() throws EvercamException {
        ArrayList<Vendor> vendors = Model.getAll();
        assertEquals(2, vendors.size());
    }

    @Test
    public void testGetByVendor() throws EvercamException {
        ArrayList<Vendor> vendors = Model.getByVendor("testid");
        assertEquals(1, vendors.size());
    }
    @Test
    public void testGetByModel() throws EvercamException {
        Model model = Model.getByModel("testid", "YCW005");
    }
}
