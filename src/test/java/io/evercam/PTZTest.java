package io.evercam;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PTZTest
{

    @Test
    public void testPTZRelativeMove() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        PTZRelative ptzRelative = new PTZRelativeBuilder("mobile-mast").left(10).build();
        assertTrue(ptzRelative.move());
    }

    @Test
    public void testPTZHome() throws PTZException
    {
        API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        assertTrue(new PTZHome("mobile-mast").move());
    }

    @Test
    public void testGetAllPresets() throws PTZException
    {
        //API.setUserKeyPair(LocalConstants.TEST_API_KEY, LocalConstants.TEST_API_ID);
        assertEquals(39, PTZPreset.getAllPresets("mobile-mast").size());
    }
}
