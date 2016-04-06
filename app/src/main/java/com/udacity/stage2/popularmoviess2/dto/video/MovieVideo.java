package com.udacity.stage2.popularmoviess2.dto.video;

/**
 * Created by Rashida on 2/29/2016.
 */
public class MovieVideo {

    private String id;

    private Results[] results;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", results = "+results+"]";
    }
}
