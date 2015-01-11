package com.teamchief.petergok.teamchief.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Peter on 2015-01-10.
 */
public class GsonTeam {
    @SerializedName(value="_id")
    public String id;
    public String name;
    public long latestActive;
    public List<GsonMessage> messages;
    public List<GsonUser> users;
}

