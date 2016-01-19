package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class VendorTest {
    private static final String TEST_VENDOR_ID = "hikvision";
    private static final String TEST_VENDOR_MAC = "8c:e7:48";
    private static final String TEST_VENDOR_NAME = "Hikvision Digital Technology";
    private static final String TEST_LOGO_URL = "http://evercam-public-assets.s3.amazonaws.com/hikvision/logo.jpg";


    @BeforeClass
    public static void setUpClass() {
        //TODO: Uncomment this to send test request to the test server
        //API.URL = TestURL.URL;
    }

    @Test
    public void testGetVendorByMac() throws EvercamException {
        assertEquals(1, Vendor.getByMac(TEST_VENDOR_MAC).size());
        Vendor hikvision = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(TEST_LOGO_URL, hikvision.getLogoUrl());
        assertEquals(5, hikvision.getKnownMacs().size());
    }

    @Test
    public void testGetAllVendors() throws EvercamException {
        assertEquals(59, Vendor.getAll().size());
    }

    @Test
    public void testGetVendorById() throws EvercamException {
        Vendor hikvision = Vendor.getById(TEST_VENDOR_ID);
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(6, hikvision.getKnownMacs().size());
        assertEquals(152, hikvision.getAllModels().size());

        //Test the static vendor logo URL method
        assertEquals(TEST_LOGO_URL, Vendor.getLogoUrl("hikvision"));
    }

    @Test
    public void testGetVendorByName() throws EvercamException {
        Vendor hikvision = Vendor.getByName(TEST_VENDOR_NAME).get(0);
        assertEquals(TEST_VENDOR_NAME, hikvision.getName());
        assertEquals(TEST_VENDOR_ID, hikvision.getId());
        assertEquals(4, hikvision.getKnownMacs().size());
    }

    @Test
    public void testGetDefaultModel() throws EvercamException {
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
    public static void destroyClass() {

    }
}