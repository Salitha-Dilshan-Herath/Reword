package com.iit.reword.roomdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "language_subscription", primaryKeys = {"name", "user"})
public class LanguageSubscription {


    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "user")
    @NonNull
    public int u_id;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public int getU_id() {
        return u_id;
    }

    public void setU_id(@NonNull int u_id) {
        this.u_id = u_id;
    }
}
