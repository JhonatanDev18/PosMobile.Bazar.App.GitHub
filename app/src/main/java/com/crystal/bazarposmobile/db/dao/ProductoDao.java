package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.ProductoEntity;

import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insert(ProductoEntity p);

    @Query("SELECT * FROM BPM_PRODUCTO ORDER BY line")
    List<ProductoEntity> getAll();

    @Query("DELETE FROM BPM_PRODUCTO")
    void delete();

    @Query("SELECT * FROM BPM_PRODUCTO WHERE line = :line")
    ProductoEntity getLine(Integer line);

    @Query("SELECT * FROM BPM_PRODUCTO WHERE ean = :ean")
    ProductoEntity getEAN(String ean);
}
