package com.example.foreignebookreader.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nl.siegmann.epublib.domain.Book;

@Entity(indices = {@Index(value = {"checksum"}, unique = true)})
public class EntityBook {

    @Ignore
    public static final String UNKNOWN = "unknown";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "checksum")
    private String mChecksum;

    @ColumnInfo(name = "currentLocation")
    private int mCurrentLocation;

    @ColumnInfo(name = "lastReadTimeStamp")
    private long mLastReadTimestamp;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "languageCode")
    private String mLanguageCode;

    @Ignore
    private Book mBook;

    public EntityBook() { }

    @Ignore
    public EntityBook(String title, String checksum) {
        mChecksum = checksum;
        mCurrentLocation = 0;
        mLastReadTimestamp = Calendar.getInstance().getTimeInMillis();
        mTitle = title;
        mLanguageCode = UNKNOWN;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getChecksum() {
        return mChecksum;
    }

    public void setChecksum(String checksum) {
        mChecksum = checksum;
    }

    public int getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(int currentLocation) {
        mCurrentLocation = currentLocation;
    }

    public long getLastReadTimestamp() {
        return mLastReadTimestamp;
    }

    public void setLastReadTimestamp(long lastReadTimestamp) {
        mLastReadTimestamp = lastReadTimestamp;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        mLanguageCode = languageCode;
    }

    @Ignore
    public Book getBook() {
        return mBook;
    }

    @Ignore
    public void setBook(Book book) {
        mBook = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityBook that = (EntityBook) o;
        return mId == that.mId &&
                mCurrentLocation == that.mCurrentLocation &&
                mLastReadTimestamp == that.mLastReadTimestamp &&
                Objects.equals(mChecksum, that.mChecksum) &&
                Objects.equals(mTitle, that.mTitle) &&
                Objects.equals(mLanguageCode, that.mLanguageCode) &&
                Objects.equals(mBook, that.mBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mChecksum, mCurrentLocation, mLastReadTimestamp, mTitle, mLanguageCode, mBook);
    }
}
