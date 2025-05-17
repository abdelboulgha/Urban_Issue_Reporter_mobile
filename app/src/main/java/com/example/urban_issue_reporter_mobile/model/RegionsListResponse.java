package com.example.urban_issue_reporter_mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RegionsListResponse {
    @SerializedName("regions")
    private List<Region> regions;

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
}