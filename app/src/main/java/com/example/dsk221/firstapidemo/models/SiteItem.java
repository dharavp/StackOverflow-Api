package com.example.dsk221.firstapidemo.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dsk-221 on 24/3/17.
 */

@DatabaseTable(tableName = "sites")
public class SiteItem implements Serializable {
    public SiteItem() {
    }

    @DatabaseField(columnName = "icon_url")
    @SerializedName("icon_url")
    private String iconUrl;

    @DatabaseField(columnName = "audience")
    private String audience;

    @DatabaseField(columnName = "site_url")
    @SerializedName("site_url")
    private String siteUrl;

    @DatabaseField(columnName = "api_site_parameter")
    @SerializedName("api_site_parameter")
    private String apiSiteParameter;

    @DatabaseField(columnName = "name")
    private String name;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getApiSiteParameter() {
        return apiSiteParameter;
    }

    public void setApiSiteParameter(String apiSiteParameter) {
        this.apiSiteParameter = apiSiteParameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
