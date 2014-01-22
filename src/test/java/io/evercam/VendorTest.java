package io.evercam;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class VendorTest
{
    private static final String TEST_VENDOR_ID = "testid";
    private static final String TEST_VENDOR_MAC = "00:73:57";


    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testGetVendorInfo() throws EvercamException
    {
        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        assertEquals("Ubiquiti Networks", axis.getName());
        assertEquals(TEST_VENDOR_ID, axis.getId());
        assertEquals(true, axis.getKnownMacs().contains(TEST_VENDOR_MAC));
        assertEquals(5, axis.getKnownMacs().size());
        assertEquals(true, axis.isSupported());
    }

    @Test
    public void testGetAll() throws EvercamException
    {
        assertEquals(2, Vendor.getAll().size());
    }

    @Test
    public void testGetByMac() throws EvercamException
    {
        assertEquals(1, Vendor.getByMac(TEST_VENDOR_MAC).size());
    }

}