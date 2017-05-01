package com.advrtizr.weatherservice.model.json.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Results implements Serializable {

@SerializedName("channel")
@Expose
private Channel channel;

public Channel getChannel() {
return channel;
}

public void setChannel(Channel channel) {
this.channel = channel;
}

}