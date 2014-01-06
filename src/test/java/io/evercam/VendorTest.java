package io.evercam;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;

/**
 * User: Liuting
 * Date: 03/12/13
 * Time: 14:32
 */
public class VendorTest
{
    private static final String TEST_VENDOR_ID = "testid";
    private static final String TEST_VENDOR_MAC = "00:73:57";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testGetAuth() throws EvercamException
    {

        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        String actualUsername = axis.getFirmware("*").getAuth("basic").getUsername();
        String actualPassword = axis.getFirmware("*").getAuth("basic").getPassword();
        assertEquals("root", actualUsername);
        assertEquals("pass", actualPassword);
    }

    @Test
    public void tesBoundaryUnknownFirmware() throws EvercamException
    {
        exception.expect(EvercamException.class);
        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        axis.getFirmware("");
    }

    @Test
    public void tesBoundaryUnknownAuth() throws EvercamException
    {
        exception.expect(EvercamException.class);
        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        axis.getFirmware("*").getAuth("");
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
    public void testGetFirmwares() throws EvercamException
    {
        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        assertEquals(1,axis.getFirmwares().size());
        assertEquals("*",axis.getFirmware("*").getName());

    }

    @Test
    public void testOtherClass() throws EvercamException
    {
        Vendor axis = Vendor.getByMac(TEST_VENDOR_MAC).get(0);
        assertEquals(axis.getFirmware("*").getAuth("basic").getType(), "basic");
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