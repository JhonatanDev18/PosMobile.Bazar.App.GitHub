package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.SuspencionHeaderEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionLineEntity;

import java.util.List;

@Dao
public interface SuspesionDao {

    @Insert
    void insertHeader(SuspencionHeaderEntity header);

    @Insert
    void insertLine(SuspencionLineEntity line);

    @Query("SELECT * FROM BPM_SUSPENSION_HEADER ORDER BY fechahora ASC")
    List<SuspencionHeaderEntity> getHeaderAll();

    @Query("SELECT * FROM BPM_SUSPENSION_LINE WHERE referencia = :referencia")
    List<SuspencionLineEntity> getLines(String referencia);

    @Query("DELETE FROM BPM_SUSPENSION_HEADER WHERE referencia = :referencia")
    void deleteHeader(String referencia);

    @Query("DELETE FROM BPM_SUSPENSION_LINE WHERE referencia = :referencia")
    void deleteLines(String referencia);

    @Query("DELETE FROM BPM_SUSPENSION_HEADER")
    void deleteHeaderAll();

    @Query("DELETE FROM BPM_SUSPENSION_LINE")
    void deleteLinesAll();
}
