package com.example.foreignebookreader.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"text", "sourceLang", "targetLang"})
public class EntityTranslation {

    @ColumnInfo(name = "text")
    private final String mText;

    @ColumnInfo(name = "sourceLang")
    private final String mSourceLang;

    @ColumnInfo(name = "targetLang")
    private final String mTargetLang;

    @ColumnInfo(name = "translation")
    private String mTranslation;

    public EntityTranslation(String text, String sourceLang, String targetLang, String translation) {
        mText = text;
        mSourceLang = sourceLang;
        mTargetLang = targetLang;
        mTranslation = translation;
    }

    public String getText() {
        return mText;
    }

    public String getSourceLang() {
        return mSourceLang;
    }

    public String getTargetLang() {
        return mTargetLang;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }
}
