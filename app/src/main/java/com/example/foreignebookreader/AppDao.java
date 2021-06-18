package com.example.foreignebookreader;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foreignebookreader.DbEntities.EntityBook;
import com.example.foreignebookreader.DbEntities.EntityTranslation;

import java.util.List;

@Dao
public interface AppDao {

    @Insert
    public long insertBook(EntityBook entityBook);

    @Insert
    public void insertTranslation(EntityTranslation entityTranslation);

    @Query(value = "SELECT * FROM EntityBook ORDER BY lastReadTimeStamp DESC")
    public LiveData<List<EntityBook>> getBooks();

    @Query(value = "SELECT * FROM EntityTranslation " +
            "WHERE text = :text " +
            "AND sourceLang = :sourceLang " +
            "AND targetLang = :targetLang")
    public LiveData<EntityTranslation> getTranslation(String text, String sourceLang, String targetLang);

    @Query(value = "SELECT * FROM EntityBook WHERE id = :id LIMIT 1")
    public EntityBook getBook(long id);

    @Update
    void updateEntityBook(EntityBook entityBook);
}
