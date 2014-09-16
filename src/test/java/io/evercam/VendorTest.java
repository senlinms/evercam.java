package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @AfterClass
    public static void destroyClass()
    {
        API.setDeveloperKeyPair(null, null);
    }
}