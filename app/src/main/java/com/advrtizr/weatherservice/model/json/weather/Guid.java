package com.advrtizr.weatherservice.model.json.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Guid implements Serializable {

@SerializedName("isPermaLink")
@Expose
private String isPermaLink;

public String getIsPermaLink() {
return isPermaLink;
}

public void setIsPermaLink(String isPermaLink) {
this.isPermaLink = isPermaLink;
}

}