package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.TarjetaEntity;

import java.util.List;

@Dao
public interface TarjetaDao {
    @Insert
    void insert(TarjetaEntity t);

    @Query("SELECT * FROM BPM_TARJETA_BANCARIA ORDER BY nombre")
    List<TarjetaEntity> getAll();

    @Query("DELETE FROM BPM_TARJETA_BANCARIA")
    void delete();

    @Query("SELECT * FROM BPM_TARJETA_BANCARIA WHERE codigo = :codigo")
    TarjetaEntity getTB(String codigo);
}
