package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.common.DatafonoTotales;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;

import java.util.List;

@Dao
public interface DatafonoDao {

    @Insert
    void insert(DatafonoEntity login);

    @Query("SELECT * FROM BPM_DATAFONO WHERE esCierre == 1 ORDER BY franquicia, tipoCuenta, numRecibo")
    List<DatafonoEntity> getDetallado();

    @Query("SELECT franquicia, tipoCuenta, COUNT(tipoCuenta) as tipoCuentaCount, SUM(monto) as monto, SUM(iva) as iva, SUM(propina) as propina FROM BPM_DATAFONO WHERE monto > 0 AND esCierre == 1 GROUP BY franquicia,tipoCuenta ORDER BY franquicia,tipoCuenta")
    List<DatafonoTotales> getTotales();

    @Query("SELECT franquicia, tipoCuenta, COUNT(tipoCuenta) as tipoCuentaCount, SUM(monto) as monto, SUM(iva) as iva, SUM(propina) as propina FROM BPM_DATAFONO WHERE monto < 0 AND esCierre == 1 GROUP BY franquicia,tipoCuenta ORDER BY franquicia,tipoCuenta")
    List<DatafonoTotales> getTotalesCanceladas();

    @Query("DELETE FROM BPM_DATAFONO WHERE esCierre == 1")
    void delete_cierre();

    @Query("DELETE FROM BPM_DATAFONO")
    void trucate();

    @Query("SELECT * FROM BPM_DATAFONO WHERE esCierre == 0 ORDER BY numRecibo")
    List<DatafonoEntity> getComprbantes();
}
