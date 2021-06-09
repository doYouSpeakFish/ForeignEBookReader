package com.example.foreignebookreader.DbEntities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntityBook {

    @PrimaryKey
    public String checksum;

    public int currentLocation;
    public long lastReadTimestamp;
    public String title;
    public String languageCode;
    public String filePath;
}
