package io.evercam;


public class CameraDetail
{
    public String id;
    String name;
    Boolean isPublic;
    String vendor;
    String model;
    String timezone;
    String macAddress;
    String jpgUrl;
    String internalHost;
    int internalHttpPort;
    int internalRtspPort;
    String externalHost;
    int externalHttpPort;
    int externalRtspPort;
    String cameraUsername;
    String cameraPassword;

    public CameraDetail(PatchCameraBuilder builder)
    {
        id = builder.id;
        name = builder.name;
        isPublic = builder.isPublic;
        vendor = builder.vendor;
        model = builder.model;
        timezone = builder.timezone;
        macAddress = builder.macAddress;
        jpgUrl = builder.jpgUrl;
        internalHost = builder.internalHost;
        internalHttpPort = builder.internalHttpPort;
        internalRtspPort = builder.internalRtspPort;
        externalHost = builder.externalHost;
        externalHttpPort = builder.externalHttpPort;
        externalRtspPort = builder.externalRtspPort;
        cameraUsername = builder.cameraUsername;
        cameraPassword = builder.cameraPassword;
    }

    public CameraDetail(CameraBuilder builder)
    {
        id = builder.id;
        name = builder.name;
        isPublic = builder.isPublic;
        vendor = builder.vendor;
        model = builder.model;
        timezone = builder.timezone;
        macAddress = builder.macAddress;
        jpgUrl = builder.jpgUrl;
        internalHost = builder.internalHost;
        internalHttpPort = builder.internalHttpPort;
        internalRtspPort = builder.internalRtspPort;
        externalHost = builder.externalHost;
        externalHttpPort = builder.externalHttpPort;
        externalRtspPort = builder.externalRtspPort;
        cameraUsername = builder.cameraUsername;
        cameraPassword = builder.cameraPassword;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Boolean getIsPublic()
    {
        return isPublic;
    }

    public String getVendor()
    {
        return vendor;
    }

    public String getModel()
    {
        return model;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public String getJpgUrl()
    {
        return jpgUrl;
    }

    public String getInternalHost()
    {
        return internalHost;
    }

    public int getInternalHttpPort()
    {
        return internalHttpPort;
    }

    public int getExternalHttpPort()
    {
        return externalHttpPort;
    }

    public String getExternalHost()
    {
        return externalHost;
    }

    public int getInternalRtspPort()
    {
        return internalRtspPort;
    }

    public int getExternalRtspPort()
    {
        return externalRtspPort;
    }

    public String getCameraUsername()
    {
        return cameraUsername;
    }

    public String getCameraPassword()
    {
        return cameraPassword;
    }

    @Override
    public String toString()
    {
        return "CameraDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isPublic=" + isPublic +
                ", vendor='" + vendor + '\'' +
                ", model='" + model + '\'' +
                ", timezone='" + timezone + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", jpgUrl='" + jpgUrl + '\'' +
                ", internalHost='" + internalHost + '\'' +
                ", internalHttpPort=" + internalHttpPort +
                ", internalRtspPort=" + internalRtspPort +
                ", externalHost='" + externalHost + '\'' +
                ", externalHttpPort=" + externalHttpPort +
                ", externalRtspPort=" + externalRtspPort +
                ", cameraUsername='" + cameraUsername + '\'' +
                ", cameraPassword='" + cameraPassword + '\'' +
                '}';
    }
}
