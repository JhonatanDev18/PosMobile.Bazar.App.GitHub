package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;

import java.util.List;

@Dao
public interface TEFContinguenciaDao {
    @Insert
    void insert(TEFContinguenciaEntity t);

    @Query("SELECT * FROM BPM_TEF_CONTINGUENCIA ORDER BY fecha")
    List<TEFContinguenciaEntity> getAll();

    @Query("SELECT * FROM BPM_TEF_CONTINGUENCIA ORDER BY franquicia,tipoCuenta")
    List<TEFContinguenciaEntity> getAllPrint();

    @Query("DELETE FROM BPM_TEF_CONTINGUENCIA")
    void delete();
}
