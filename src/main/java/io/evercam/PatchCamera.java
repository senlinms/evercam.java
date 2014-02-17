package io.evercam;


class PatchCamera
{
    String id;
    String name;
    Boolean isPublic;
    String vendor;
    String model;
    String timezone;
    String macAddress;
    String snapshotJPG;
    String[] endpoints;
    String[] basicAuth;

    PatchCamera(PatchCameraBuilder builder)
    {
        id = builder.id;
        name = builder.name;
        isPublic = builder.isPublic;
        vendor = builder.vendor;
        model = builder.model;
        timezone = builder.timezone;
        macAddress = builder.macAddress;
        snapshotJPG = builder.snapshotJPG;
        endpoints = builder.endpoints;
        basicAuth = builder.basicAuth;
    }
}
