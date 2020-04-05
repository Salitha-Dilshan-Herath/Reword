package com.iit.reword.roomdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "language")
public class Language {

    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "code")
    public String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "Language{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
