package io.evercam;

public class CameraInfo
{
    private String id;
    private String name;
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
}
