package io.evercam;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PTZTest
{
    private final String PTZ_CAMERA_ID = "mobile-mast";

    @Test
    public void testPTZRelativeMove() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        PTZRelative ptzRelative = new PTZRelativeBuilder(PTZ_CAMERA_ID).left(10).build();
        assertTrue(ptzRelative.move());
    }

    @Test
    public void testPTZHome() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        assertTrue(new PTZHome(PTZ_CAMERA_ID).move());
    }

    @Test
    public void testGetAllPresets() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        assertEquals(39, PTZPreset.getAllPresets(PTZ_CAMERA_ID).size());
    }

    @Test
    public void testPresetMove() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        assertTrue(new PTZPresetControl(PTZ_CAMERA_ID, "1").move());
    }
}
