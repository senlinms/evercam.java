package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class VendorTest
{
    private static final String TEST_VENDOR_ID = "hikvision";
    private static final String TEST_VENDOR_MAC = "8c:e7:48";
    private static final String TEST_VENDOR_NAME = "Hikvision Digital Technology";


    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
        API.setDeveloperKeyPair(LocalConstants.DEVELOPER_KEY, LocalConstants.DEVELOPER_ID);
    }

    @Test
    public void testGetVendorByMac() throws EvercamException
    {
        assertEquals(1, Vendor.getByMac(TEST_VENDOR_MAC).size());
        Vendor hikvision = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(3, hikvision.getKnownMacs().size());
        assertEquals(true, hikvision.isSupported());
    }

    @Test
    public void testGetAllVendors() throws EvercamException
    {
        assertEquals(41, Vendor.getAll().size());
    }

    @Test
    public void testGetVendorById() throws EvercamException
    {
        Vendor hikvision = Vendor.getById(TEST_VENDOR_ID);
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(1, hikvision.getModelNames().size());
        assertEquals("*", hikvision.getModelNames().get(0));
    }

    @Test
    public void testGetModel() throws EvercamException
    {
        Model model = Vendor.getById(TEST_VENDOR_ID).getModel("hikvision" + Model.DEFAULT_MODEL_SUFFIX);
        ArrayList<String> models = model.getKnownModels();
        assertEquals(1, models.size());
        assertEquals(TEST_VENDOR_ID, model.getVendor());
        assertEquals(Model.DEFAULT_MODEL_NAME, model.getName());
        assertEquals(1, model.getKnownModels().size());
        assertEquals("admin", model.getDefaults().getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("12345", model.getDefaults().getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals("Streaming/Channels/1/picture", model.getDefaults().getJpgURL());
        assertEquals("h264/ch1/main/av_stream", model.getDefaults().getH264URL());
        assertEquals("", model.getDefaults().getLowresURL());
        assertEquals("mpeg4/ch1/main/av_stream", model.getDefaults().getMpeg4URL());
        assertEquals("", model.getDefaults().getMobileURL());
        assertEquals("", model.getDefaults().getMjpgURL());
    }

    @Test
    public void testGetSupportedVendors() throws EvercamException
    {
        ArrayList<Vendor> vendors = Vendor.getSupportedVendors();
        assertEquals(41, vendors.size());
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}