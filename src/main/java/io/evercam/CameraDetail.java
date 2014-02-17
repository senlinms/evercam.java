package io.evercam;

public class CameraDetail
{
    private String id;
    private String name;
    private String vendor;
    private String model;
    private String timezone;
    private Boolean isPublic;
    private String macAddress;
    private String snapshotJPG;
    private String[] endpoints;
    private String[] basicAuth;

    protected String getId() throws EvercamException
    {
        if (id == null)
        {
            throw new EvercamException("camera id required");
        }
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    protected String getName() throws EvercamException
    {
        if (name == null)
        {
            throw new EvercamException("camera name required");
        }
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    protected boolean isPublic() throws EvercamException
    {
        if (isPublic == null)
        {
            throw new EvercamException("camera public/private required");
        }
        return isPublic;
    }

    public void setPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }

    protected String getSnapshotJPG() throws EvercamException
    {
        return snapshotJPG;
    }

    public void setSnapshotJPG(String snapshotJPG)
    {
        this.snapshotJPG = snapshotJPG;
    }

    protected String[] getEndpoints() throws EvercamException
    {
        if (endpoints == null)
        {
            throw new EvercamException("endpoints required");
        }
        return endpoints;
    }

    public void setEndpoints(String[] endpoints)
    {
        this.endpoints = endpoints;
    }

    protected String[] getBasicAuth() throws EvercamException
    {
        if (basicAuth == null)
        {
            throw new EvercamException("basic auth required");
        }
        else if (basicAuth[0] == null || basicAuth[1] == null)
        {
            throw new EvercamException("basic auth required");
        }
        return basicAuth;
    }

    public void setBasicAuth(String username, String password)
    {
        this.basicAuth = new String[]{username, password};
    }

    protected String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    protected String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    protected String getTimezone() throws EvercamException
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(String macaddress)
    {
        this.macAddress = macaddress;
    }
}
