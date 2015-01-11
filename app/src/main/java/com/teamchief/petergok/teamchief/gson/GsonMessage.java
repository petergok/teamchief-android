package com.teamchief.petergok.teamchief.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Peter on 2015-01-10.
 */
public class GsonMessage {
    @SerializedName(value="_id")
    public String id;
    public GsonUser sender;
    public long sendTime;
    public String text;
}
