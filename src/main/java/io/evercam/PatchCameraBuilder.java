package io.evercam;

public class PatchCameraBuilder
{
    final String id;
    Boolean isPublic;
    String name;
    String vendor;
    String model;
    String timezone;
    String macAddress;
    String jpgUrl;
    String rtspUrl;
    String internalHost;
    int internalHttpPort;
    int internalRtspPort;
    String externalHost;
    int externalHttpPort;
    int externalRtspPort;
    String cameraUsername;
    String cameraPassword;

    public PatchCameraBuilder(String cameraId) throws EvercamException
    {
        if (cameraId != null)
        {
            id = cameraId;
        }
        else
        {
            throw new EvercamException("camera id can not be null");
        }
    }

    public PatchCameraBuilder setName(String cameraName)
    {
        name = cameraName;
        return this;
    }

    public PatchCameraBuilder setPublic(Boolean isPublic)
    {
        this.isPublic = isPublic;
        return this;
    }

    public PatchCameraBuilder setJpgUrl(String jpgUrl)
    {
        this.jpgUrl = jpgUrl;
        return this;
    }

    public PatchCameraBuilder setRtspUrl(String rtspUrl)
    {
        this.rtspUrl = rtspUrl;
        return this;
    }

    public PatchCameraBuilder setInternalHost(String internalHost)
    {
        this.internalHost = internalHost;
        return this;
    }

    public PatchCameraBuilder setInternalHttpPort(int internalHttpPort)
    {
        this.internalHttpPort = internalHttpPort;
        return this;
    }

    public PatchCameraBuilder setInternalRtspPort(int internalRtspPort)
    {
        this.internalRtspPort = internalRtspPort;
        return this;
    }

    public PatchCameraBuilder setExternalHost(String externalHost)
    {
        this.externalHost = externalHost;
        return this;
    }

    public PatchCameraBuilder setExternalHttpPort(int externalHttpPort)
    {
        this.externalHttpPort = externalHttpPort;
        return this;
    }

    public PatchCameraBuilder setExternalRtspPort(int externalRtspPort)
    {
        this.externalRtspPort = externalRtspPort;
        return this;
    }

    public PatchCameraBuilder setCameraUsername(String cameraUsername)
    {
        this.cameraUsername = cameraUsername;
        return this;
    }

    public PatchCameraBuilder setCameraPassword(String cameraPassword)
    {
        this.cameraPassword = cameraPassword;
        return this;
    }

    public PatchCameraBuilder setVendor(String vendor)
    {
        this.vendor = vendor;
        return this;
    }

    public PatchCameraBuilder setModel(String model)
    {
        this.model = model;
        return this;
    }

    public PatchCameraBuilder setTimeZone(String timezone)
    {
        this.timezone = timezone;
        return this;
    }

    public PatchCameraBuilder setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
        return this;
    }

    public CameraDetail build()
    {
        return new CameraDetail(this);
    }
}
