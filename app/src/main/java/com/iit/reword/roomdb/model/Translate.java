package com.iit.reword.roomdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "translate", foreignKeys = {
        @ForeignKey(
                entity = Phrase.class,
                parentColumns = "p_id",
                childColumns = "p_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),

        @ForeignKey(
                entity = Language.class,
                parentColumns = "name",
                childColumns = "language_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Translate {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "t_id")
    public int tid;

    @ColumnInfo(name = "p_id")
    public int p_id;

    @ColumnInfo(name = "translate_phrase")
    public String translatePhrase;


    @ColumnInfo(name = "language_id")
    public String languageId;

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getTranslatePhrase() {
        return translatePhrase;
    }

    public void setTranslatePhrase(String translatePhrase) {
        this.translatePhrase = translatePhrase;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return "Translate{" +
                "tid=" + tid +
                ", p_id=" + p_id +
                ", translatePhrase='" + translatePhrase + '\'' +
                ", languageId='" + languageId + '\'' +
                '}';
    }
}
