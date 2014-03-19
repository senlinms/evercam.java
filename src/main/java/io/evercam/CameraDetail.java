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
    String internalUrl;
    String externalUrl;
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
        internalUrl = builder.internalUrl;
        externalUrl = builder.externalUrl;
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
        internalUrl = builder.internalUrl;
        externalUrl = builder.externalUrl;
        cameraUsername = builder.cameraUsername;
        cameraPassword = builder.cameraPassword;
    }
}
