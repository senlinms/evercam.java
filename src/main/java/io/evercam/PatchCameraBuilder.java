package io.evercam;

import javafx.util.Builder;

public class PatchCameraBuilder implements Builder<PatchCamera>
{
    final String id;
    Boolean isPublic;
    String name;
    String vendor;
    String model;
    String timezone;
    String macAddress;
    String snapshotJPG;
    String[] endpoints;
    String[] basicAuth;

    PatchCameraBuilder(String cameraId)
    {
        id = cameraId;
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

    public PatchCameraBuilder setEndpoints(String[] endpoints)
    {
       this.endpoints = endpoints;
       return this;
    }

    public PatchCameraBuilder setSnapshotJPG(String snapshotJPG)
    {
        this.snapshotJPG = snapshotJPG;
        return this;
    }

    public PatchCameraBuilder setBasicAuth(String[] basicAuth)
    {
        this.basicAuth = basicAuth;
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

    @Override
    public PatchCamera build()
    {
        return new PatchCamera(this);
    }
}
