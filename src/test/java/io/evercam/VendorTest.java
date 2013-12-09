package io.evercam;

import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * User: Liuting
 * Date: 03/12/13
 * Time: 14:32
 */
public class VendorTest
{
    String TEST_VENDOR_ID = "hikvision";
    String TEST_VENDOR_NAME = "Hikvision Tec";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetAuth() throws EvercamException
    {
        Vendor axis = new Vendor("axis");
        String actualUsername = axis.getFirmware("*").getAuth("basic").getUsername();
        String actualPassword = axis.getFirmware("*").getAuth("basic").getPassword();
        assertEquals("root", actualUsername);
        assertEquals("pass", actualPassword);
    }

    @Test
    public void tesBoundaryUnknownVendor() throws EvercamException
    {
        exception.expect(EvercamException.class);
   //     exception.expectMessage("Unknown Vendor");
        new Vendor("wrong_vendor");
    }

    @Test
    public void tesBoundaryUnknownFirmware() throws EvercamException
    {
        exception.expect(EvercamException.class);
   //     exception.expectMessage("Unknown Firmware");
        Vendor axis = new Vendor("axis");
        axis.getFirmware("");
    }

    @Test
    public void tesBoundaryUnknownAuth() throws EvercamException
    {
        exception.expect(EvercamException.class);
        Vendor axis = new Vendor("axis");
        axis.getFirmware("*").getAuth("");
    }


    @Test
    public void testGetVendorInfo() throws EvercamException
    {
        Vendor axis = new Vendor("axis");
        assertEquals("Axis Communications",axis.getName());
        assertEquals("axis",axis.getId());
        ArrayList<String> knownMacs = new ArrayList<String>();
        knownMacs.add("00:40:8C");
        assertEquals(knownMacs, axis.getKnownMacs());
        assertEquals(1,axis.getKnownMacs().size());
    }

    @Test
    public void testGetFirmwares() throws EvercamException
    {
        Vendor axis = new Vendor("axis");
        assertEquals(1,axis.getFirmwares().size());
        assertEquals("*",axis.getFirmware("*").getName());

    }

    @Test
    public void testOtherClass() throws EvercamException
    {
        Vendor axis = new Vendor("axis");
        assertEquals(axis.getFirmware("*").getAuth("basic").getType(), "basic");
    }


    @Test
    public void testGetAll() throws EvercamException
    {
        assertEquals(11, Vendor.getAll().size());
    }

    @Test
    public void testGetByMac() throws EvercamException
    {
        assertEquals(1, Vendor.getByMac("00:40:8C").size());
    }

}