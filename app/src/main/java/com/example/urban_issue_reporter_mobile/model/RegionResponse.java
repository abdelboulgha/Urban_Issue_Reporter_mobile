// RegionResponse.java
package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionResponse {
    @SerializedName("region")
    private Region region;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
