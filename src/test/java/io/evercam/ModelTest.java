package io.evercam;


import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ModelTest
{

    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Test
    public void testGetAll() throws EvercamException
    {
        ArrayList<Vendor> vendors = Model.getAll();
        assertEquals(2, vendors.size());
        ArrayList<String> models = vendors.get(1).getModels();
        assertEquals(4, models.size());
    }

//    @Test
//    public void testGetByVendor() throws EvercamException
//    {
//        ArrayList<Vendor> vendors = Model.getByVendor("testid");
//        assertEquals(1, vendors.size());
//        ArrayList<String> models = vendors.get(0).getModels();
//        assertEquals(4, models.size());
//    }

    @Test
    public void testGetByModel() throws EvercamException
    {
        Model model = Model.getByModel("testid", "YCW005");
        ArrayList<String> models = model.getKnownModels();
        assertEquals(1, models.size());
        assertEquals("testid", model.getVendor());
        assertEquals("YCW005", model.getName());
    }
}
