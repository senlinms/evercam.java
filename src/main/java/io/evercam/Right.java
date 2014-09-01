package io.evercam;

import java.util.ArrayList;
import java.util.Arrays;

public class Right
{
    private String rightsString;
    private final String SNAPSHOT = "snapshot";
    private final String VIEW = "view";
    private final String EDIT  = "edit";
    private final String DELETE  = "delete";
    private final String LIST = "list";
    private final String GRANT_SNAPSHOT = "grant~snapshot";
    private final String GRANT_VIEW = "grant~view";
    private final String GRANT_EDIT  = "grant~edit";
    private final String GRANT_DELETE  = "grant~delete";
    private final String GRANT_LIST = "grant~list";


    protected Right(String rightsString)
    {
         this.rightsString = rightsString;
    }

    private Right()
    {
    }

    public ArrayList<String> toArray()
    {
        String[] rightsArray = rightsString.split(",");
        return new ArrayList<String>(Arrays.asList(rightsArray));
    }

    public boolean canGetSnapshot()
    {
        return toArray().contains(EDIT);
    }

    public boolean canEdit()
    {
        return toArray().contains(EDIT);
    }

    public boolean canView()
    {
        return toArray().contains(VIEW);
    }

    public boolean canDelete()
    {
        return toArray().contains(DELETE);
    }

    public boolean canList()
    {
        return toArray().contains(LIST);
    }

    public boolean canGrantSnapshot()
    {
        return toArray().contains(GRANT_SNAPSHOT);
    }

    public boolean canGrantEdit()
    {
        return toArray().contains(GRANT_EDIT);
    }

    public boolean canGrantView()
    {
        return toArray().contains(GRANT_VIEW);
    }

    public boolean canGrantDelete()
    {
        return toArray().contains(GRANT_DELETE);
    }

    public boolean canGrantList()
    {
        return toArray().contains(GRANT_LIST);
    }

    @Override
    public String toString()
    {
        return rightsString;
    }
}
