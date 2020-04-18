package com.iit.reword.roomdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "language_subscription")
public class LanguageSubscription {

    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "lan_code")
    public String lan_code;

    @ColumnInfo(name = "isSub")
    public boolean isSub;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }


    public String getLan_code() {
        return lan_code;
    }

    public void setLan_code(String lan_code) {
        this.lan_code = lan_code;
    }

    public boolean getIsSub() {
        return isSub;
    }

    public void setIsSub(boolean isSub) {
        this.isSub = isSub;
    }
}
