package io.evercam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.*;

public class ModelTest {
    private static final String TEST_VENDOR_ID = "hikvision";
    private static final String TEST_MODEL_THUMBNAIL_URL = "http://evercam-public-assets.s3.amazonaws" +
            ".com/hikvision/hikvision_default/thumbnail.jpg";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        //TODO: Uncomment this to send test request to the test server
        //API.URL = TestURL.URL;
    }

    @Test
    public void testGetModelById() throws EvercamException {
        Model model = Model.getById(TEST_VENDOR_ID + Model.DEFAULT_MODEL_SUFFIX);
        assertEquals(TEST_VENDOR_ID, model.getVendorId());
        assertEquals(Model.DEFAULT_MODEL_NAME, model.getName());
        assertEquals("admin", model.getDefaults().getAuth(Auth.TYPE_BASIC).getUsername());
        assertEquals("12345", model.getDefaults().getAuth(Auth.TYPE_BASIC).getPassword());
        assertEquals("Streaming/Channels/1/picture", model.getDefaults().getJpgURL());
        assertEquals("h264/ch1/main/av_stream", model.getDefaults().getH264URL());
        assertEquals("", model.getDefaults().getLowresURL());
        assertEquals("mpeg4/ch1/main/av_stream", model.getDefaults().getMpeg4URL());
        assertEquals("", model.getDefaults().getMobileURL());
        assertEquals("", model.getDefaults().getMjpgURL());
        assertEquals(TEST_MODEL_THUMBNAIL_URL, model.getThumbnailUrl());
        assertTrue(model.isOnvif());
        assertFalse(model.isPTZ());

        //Test the static model thumbnail URL method
        assertEquals(TEST_MODEL_THUMBNAIL_URL,
                Model.getThumbnailUrl(TEST_VENDOR_ID, TEST_VENDOR_ID + Model.DEFAULT_MODEL_SUFFIX));
    }

    @Test
    public void testGetAllModelByVendor() throws EvercamException {
        ArrayList<Model> modelList = Model.getAllByVendorId(TEST_VENDOR_ID);
        assertEquals(148, modelList.size());
    }

    @Test
    public void testGetAllModelByName() throws EvercamException {
        ArrayList<Model> modelList = Model.getAllByName("Default");
        assertEquals(61, modelList.size());
    }

    @Test
    public void testGetAllWithVendorAndModel() throws EvercamException {
        ArrayList<Model> modelList = Model.getAll("Default", TEST_VENDOR_ID);
        assertEquals(1, modelList.size());
        assertEquals("hikvision" + Model.DEFAULT_MODEL_SUFFIX, modelList.get(0).getId());
    }

    @Test
    public void testGetDefaultModelByVendorId() throws EvercamException {
        Model defaultModel = Model.getDefaultModelByVendorId(TEST_VENDOR_ID);
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

    @Test
    public void testModelNotExists() throws EvercamException {
        exception.expect(EvercamException.class);
        Model.getAllByVendorId("no_model");
    }

    @AfterClass
    public static void destroyClass() {

    }
}
