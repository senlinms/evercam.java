package io.evercam;

public class CameraBuilder
{
    final String id;
    final Boolean isPublic;
    final String name;
    final String externalUrl;

    String vendor;
    String model;
    String timezone;
    String macAddress;
    String jpgUrl;
    String internalUrl;
    String cameraUsername;
    String cameraPassword;

    public CameraBuilder(String cameraId, String cameraName, Boolean isPublic, String externalUrl) throws EvercamException
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
        if (externalUrl != null)
        {
            this.externalUrl= externalUrl;
        }
        else
        {
            throw new EvercamException("external url can not be null");
        }
    }

    public CameraBuilder setJpgUrl(String jpgUrl)
    {
        this.jpgUrl = jpgUrl;
        return this;
    }

    public CameraBuilder setInternalUrl(String internalUrl)
    {
        this.internalUrl = internalUrl;
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
