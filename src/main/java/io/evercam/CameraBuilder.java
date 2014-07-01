package io.evercam;

public class CameraBuilder
{
    final String id;
    final Boolean isPublic;
    final String name;

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

    public CameraBuilder(String cameraId, String cameraName, Boolean isPublic) throws EvercamException
    {
        if (cameraId != null)
        {
            id = cameraId;
        }
        else
        {
            throw new EvercamException("camera id can not be null");
        }
        if (cameraName != null)
        {
            name = cameraName;
        }
        else
        {
            throw new EvercamException("camera name can not be null");
        }
        if (isPublic != null)
        {
            this.isPublic = isPublic;
        }
        else
        {
            throw new EvercamException("camera public/private can not be null");
        }
    }

    public CameraBuilder setJpgUrl(String jpgUrl)
    {
        this.jpgUrl = jpgUrl;
        return this;
    }

    public CameraBuilder setRtspUrl(String rtspUrl)
    {
        this.rtspUrl = rtspUrl;
        return this;
    }

    public CameraBuilder setInternalHost(String internalHost)
    {
        this.internalHost = internalHost;
        return this;
    }

    public CameraBuilder setInternalHttpPort(int internalHttpPort)
    {
        this.internalHttpPort = internalHttpPort;
        return this;
    }

    public CameraBuilder setInternalRtspPort(int internalRtspPort)
    {
        this.internalRtspPort = internalRtspPort;
        return this;
    }

    public CameraBuilder setExternalHost(String externalHost)
    {
        this.externalHost = externalHost;
        return this;
    }

    public CameraBuilder setExternalHttpPort(int externalHttpPort)
    {
        this.externalHttpPort = externalHttpPort;
        return this;
    }

    public CameraBuilder setExternalRtspPort(int externalRtspPort)
    {
        this.externalRtspPort = externalRtspPort;
        return this;
    }

    public CameraBuilder setCameraUsername(String cameraUsername)
    {
        this.cameraUsername = cameraUsername;
        return this;
    }

    public CameraBuilder setCameraPassword(String cameraPassword)
    {
        this.cameraPassword = cameraPassword;
        return this;
    }

    public CameraBuilder setVendor(String vendor)
    {
        this.vendor = vendor;
        return this;
    }

    public CameraBuilder setModel(String model)
    {
        this.model = model;
        return this;
    }

    public CameraBuilder setTimeZone(String timezone)
    {
        this.timezone = timezone;
        return this;
    }

    public CameraBuilder setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
        return this;
    }

    public CameraDetail build()
    {
        return new CameraDetail(this);
    }
}
