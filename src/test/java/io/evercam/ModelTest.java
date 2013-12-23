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
    }

    @Test
    public void testGetByVendor() throws EvercamException {
        ArrayList<Vendor> vendors = Model.getByVendor("testid");
    }
    @Test
    public void testGetByModel() throws EvercamException {
        Model model = Model.getByModel("YCW005");
    }
}
