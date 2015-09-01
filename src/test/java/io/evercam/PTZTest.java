package io.evercam;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PTZTest
{
    private final String PTZ_CAMERA_ID = "mobile-mast";

    @BeforeClass
    public static void setUpClass()
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
    }

    @Test
    public void testPTZRelativeMove() throws PTZException
    {
        PTZRelative ptzRelative = new PTZRelativeBuilder(PTZ_CAMERA_ID).left(10).build();
        assertTrue(ptzRelative.move());
    }

    @Test
    public void testPTZHome() throws PTZException
    {
        assertTrue(new PTZHome(PTZ_CAMERA_ID).move());
    }

    @Test
    public void testGetAllPresets() throws PTZException
    {
        assertEquals(39, PTZPreset.getAllPresets(PTZ_CAMERA_ID).size());
    }

    @Test
    public void testPresetMove() throws PTZException
    {
        assertTrue(new PTZPresetControl(PTZ_CAMERA_ID, "1").move());
    }

    @Test
    public void testCreatePreset() throws PTZException
    {
        assertNotEquals("", PTZPreset.create(PTZ_CAMERA_ID, "JavaWrapperTest"));
    }
}
