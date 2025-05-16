package com.crystal.bazarposmobile.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.db.dao.DatafonoDao;
import com.crystal.bazarposmobile.db.dao.DocumentoDao;
import com.crystal.bazarposmobile.db.dao.LoginDao;
import com.crystal.bazarposmobile.db.dao.MediosPagoCajaDao;
import com.crystal.bazarposmobile.db.dao.ProductoDao;
import com.crystal.bazarposmobile.db.dao.SuspesionDao;
import com.crystal.bazarposmobile.db.dao.TEFContinguenciaDao;
import com.crystal.bazarposmobile.db.dao.TarjetaDao;
import com.crystal.bazarposmobile.db.entity.DatafonoEntity;
import com.crystal.bazarposmobile.db.entity.DetalleDescuentoEntity;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.DocLineEntity;
import com.crystal.bazarposmobile.db.entity.DocPaymentEntity;
import com.crystal.bazarposmobile.db.entity.LoginEntity;
import com.crystal.bazarposmobile.db.entity.MediosPagoCajaEntity;
import com.crystal.bazarposmobile.db.entity.PagosTEFEntity;
import com.crystal.bazarposmobile.db.entity.ProductoEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionHeaderEntity;
import com.crystal.bazarposmobile.db.entity.SuspencionLineEntity;
import com.crystal.bazarposmobile.db.entity.TEFContinguenciaEntity;
import com.crystal.bazarposmobile.db.entity.TarjetaEntity;

@Database(entities = {ProductoEntity.class,
        MediosPagoCajaEntity.class,
        TarjetaEntity.class,
        TEFContinguenciaEntity.class,
        LoginEntity.class,
        DocHeaderEntity.class,
        DocLineEntity.class,
        DocPaymentEntity.class,
        PagosTEFEntity.class,
        DetalleDescuentoEntity.class,
        DatafonoEntity.class,
        SuspencionHeaderEntity.class,
        SuspencionLineEntity.class},
        version = Constantes.BASE_DATOS_BAZARPOSMOBIL_VERSION, exportSchema = false)
public abstract class BazarPosMovilDB extends RoomDatabase {

    private static BazarPosMovilDB INSTANCE;
    public abstract ProductoDao productoDao();
    public abstract MediosPagoCajaDao mediosPagoCajaDao();
    public abstract TarjetaDao tarjetaDao();
    public abstract TEFContinguenciaDao tefContinguenciaDao();
    public abstract LoginDao loginDao();
    public abstract DocumentoDao documentoDao();
    public abstract DatafonoDao datafonoDao();
    public abstract SuspesionDao suspesionDao();

    public static BazarPosMovilDB getBD(Context ctx){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    ctx, BazarPosMovilDB.class, Constantes.BASE_DATOS_BAZARPOSMOBIL)
                    .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyBD(){
        INSTANCE = null;
    }
}