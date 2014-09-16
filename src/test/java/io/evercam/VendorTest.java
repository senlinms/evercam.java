package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(3, hikvision.getKnownMacs().size());
        assertEquals(126, hikvision.getAllModels().size());
    }

    @Test
    public void testGetVendorByName() throws EvercamException
    {
        Vendor hikvision = Vendor.getByName(TEST_VENDOR_NAME).get(0);
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(3, hikvision.getKnownMacs().size());
    }

    @Test
    public void testGetDefaultModel() throws EvercamException
    {
        Vendor hikvision = Vendor.getById(TEST_VENDOR_ID);
        Model defaultModel = hikvision.getDefaultModel();
        assertNotNull(defaultModel);
        Defaults hikvisionDefaults = defaultModel.getDefaults();
        assertEquals("admin", hikvisionDefaults.getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("12345", hikvisionDefaults.getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals("Streaming/Channels/1/picture", hikvisionDefaults.getJpgURL());
        assertEquals("h264/ch1/main/av_stream", hikvisionDefaults.getH264URL());
        assertEquals("", hikvisionDefaults.getLowresURL());
        assertEquals("mpeg4/ch1/main/av_stream", hikvisionDefaults.getMpeg4URL());
        assertEquals("", hikvisionDefaults.getMobileURL());
        assertEquals("", hikvisionDefaults.getMjpgURL());
    }

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}