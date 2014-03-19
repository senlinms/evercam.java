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
    String internalUrl;
    String externalUrl;
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

    public PatchCameraBuilder setInternalUrl(String internalUrl)
    {
        this.internalUrl = internalUrl;
        return this;
    }

    public PatchCameraBuilder setExternalUrl(String externalUrl)
    {
        this.externalUrl = externalUrl;
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
