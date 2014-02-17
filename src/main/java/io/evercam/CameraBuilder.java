package io.evercam;

import javafx.util.Builder;

public class CameraBuilder implements Builder<CameraDetail>
{
    final String id;
    final Boolean isPublic;
    final String name;
    final String[] endpoints;

    String vendor;
    String model;
    String timezone;
    String macAddress;
    String snapshotJPG;
    String[] basicAuth;

    CameraBuilder(String cameraId, String cameraName, Boolean isPublic, String[] endpoints) throws EvercamException
    {
        if(cameraId!=null)
        {
            id = cameraId;
        }
        else
        {
            throw new EvercamException("camera id can not be null");
        }
        if(cameraName!=null)
        {
            name = cameraName;
        }
        else
        {
            throw new EvercamException("camera name can not be null");
        }
        if(isPublic!=null)
        {
            this.isPublic =  isPublic;
        }
        else
        {
            throw new EvercamException("camera public/private can not be null");
        }
        if(endpoints!=null)
        {
            this.endpoints = endpoints;
        }
        else
        {
            throw new EvercamException("camera endpoints can not be null");
        }
    }

    public CameraBuilder setSnapshotJPG(String snapshotJPG)
    {
        this.snapshotJPG = snapshotJPG;
        return this;
    }

    public CameraBuilder setBasicAuth(String username, String password)
    {
        this.basicAuth = new String[] {username, password};
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

    @Override
    public CameraDetail build()
    {
        return new CameraDetail(this);
    }
}
