package io.evercam;

public class CameraDetail
{
    private String id;
    private String name;
    private String vendor;
    private String model;
    private String timezone;
    private boolean isPublic;
    private String snapshotJPG;
    private String[] endpoints;
    private String[] basicAuth;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }

    public String getSnapshotJPG()
    {
        return snapshotJPG;
    }

    public void setSnapshotJPG(String snapshotJPG)
    {
        this.snapshotJPG = snapshotJPG;
    }

    public String[] getEndpoints()
    {
        return endpoints;
    }

    public void setEndpoints(String[] endpoints)
    {
        this.endpoints = endpoints;
    }

    public String[] getBasicAuth()
    {
        return basicAuth;
    }

    public void setBasicAuth(String[] basicAuth)
    {
        this.basicAuth = basicAuth;
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }
}
