package com.example.foreignebookreader.DbEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"text", "sourceLang", "targetLang"})
public class EntityTranslation {

    @ColumnInfo(name = "text")
    @NonNull
    private final String mText;

    @ColumnInfo(name = "sourceLang")
    @NonNull
    private final String mSourceLang;

    @ColumnInfo(name = "targetLang")
    @NonNull
    private final String mTargetLang;

    @ColumnInfo(name = "translation")
    private String mTranslation;

    public EntityTranslation(@NonNull String text, @NonNull String sourceLang, @NonNull String targetLang, String translation) {
        mText = text;
        mSourceLang = sourceLang;
        mTargetLang = targetLang;
        mTranslation = translation;
    }

    public @NonNull String getText() {
        return mText;
    }

    public @NonNull String getSourceLang() {
        return mSourceLang;
    }

    public @NonNull String getTargetLang() {
        return mTargetLang;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }
}
