package com.iit.reword.roomdb.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "phrase", foreignKeys = {
                                                @ForeignKey(
                                                        entity = User.class,
                                                        parentColumns = "u_id",
                                                        childColumns = "user",
                                                        onDelete = ForeignKey.CASCADE,
                                                        onUpdate = ForeignKey.CASCADE
                                                )
        })
public class Phrase {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "p_id")
    public int pid;

    @ColumnInfo(name = "phrase")
    public String phrase;

    @ColumnInfo(name = "user")
    public int user;

    @Ignore
    public Boolean isSelected;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return "Phrase{" +
                "pid=" + pid +
                ", phrase='" + phrase + '\'' +
                ", user='" + user + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
