package com.klemar.android.factorytask.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "source_table")
public class SourceModel {

    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public SourceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
