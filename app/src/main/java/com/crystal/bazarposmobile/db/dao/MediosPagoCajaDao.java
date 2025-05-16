package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.MediosPagoCajaEntity;

import java.util.List;

@Dao
public interface MediosPagoCajaDao {
    @Insert
    void insert(MediosPagoCajaEntity mpc);

    @Query("SELECT * FROM BPM_MEDIO_PAGO_CAJA ORDER BY codigo")
    List<MediosPagoCajaEntity> getAll();

    @Query("DELETE FROM BPM_MEDIO_PAGO_CAJA")
    void delete();

    @Query("SELECT * FROM BPM_MEDIO_PAGO_CAJA WHERE codigo = :codigo")
    MediosPagoCajaEntity getMedio(String codigo);

}
