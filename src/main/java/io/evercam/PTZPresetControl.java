package io.evercam;

public class PTZPresetControl implements PTZControl
{
    private final String cameraId;
    private String moveToToken = "";

    public PTZPresetControl(String cameraId)
    {
        this.cameraId = cameraId;
    }

    @Override
    public boolean move() throws PTZException
    {
        return false;
    }

    protected static String getPresetsUrl(String cameraId)
    {
        return URL + '/' +  cameraId + "/ptz/presets";
    }
}
