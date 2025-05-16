package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.crystal.bazarposmobile.db.entity.DetalleDescuentoEntity;
import com.crystal.bazarposmobile.db.entity.DocHeaderEntity;
import com.crystal.bazarposmobile.db.entity.DocLineEntity;
import com.crystal.bazarposmobile.db.entity.DocPaymentEntity;
import com.crystal.bazarposmobile.db.entity.PagosTEFEntity;

import java.util.List;

@Dao
public interface DocumentoDao {

    @Insert
    void insertHeader(DocHeaderEntity header);

    @Insert
    void insertLine(DocLineEntity line);

    @Insert
    void insertPayment(DocPaymentEntity payment);

    @Insert
    void insertPagoTEF(PagosTEFEntity pagosTEF);

    @Insert
    void insertDetalleDescuento(DetalleDescuentoEntity dd);

    @Query("SELECT COUNT(*) FROM BPM_DOCUMENTO_HEADER")
    Integer getHeaderCount();

    @Query("SELECT * FROM BPM_DOCUMENTO_HEADER ORDER BY fechahora ASC")
    List<DocHeaderEntity> getHeaderAll();

    @Query("SELECT * FROM BPM_DOCUMENTO_HEADER WHERE internalReference = :internalReference")
    DocHeaderEntity getHeader(String internalReference);

    @Query("SELECT * FROM BPM_DOCUMENTO_LINE WHERE internalReference = :internalReference")
    List<DocLineEntity> getLines(String internalReference);

    @Query("SELECT * FROM BPM_DOCUMENTO_PAYMENT WHERE internalReference = :internalReference")
    List<DocPaymentEntity> getPayments(String internalReference);

    @Query("SELECT * FROM BPM_PAGO_TEF WHERE internalReference = :internalReference")
    List<PagosTEFEntity> getPagosTEF(String internalReference);

    @Query("SELECT * FROM BPM_DETALLE_DESCUENTO WHERE internalReference = :internalReference")
    List<DetalleDescuentoEntity> getDetalleDescuento(String internalReference);

    @Delete
    void deleteHeader(DocHeaderEntity header);

    @Query("DELETE FROM BPM_DOCUMENTO_LINE WHERE internalReference = :internalReference")
    void deleteLines(String internalReference);

    @Query("DELETE FROM BPM_DOCUMENTO_PAYMENT WHERE internalReference = :internalReference")
    void deletePayments(String internalReference);

    @Query("DELETE FROM BPM_PAGO_TEF WHERE internalReference = :internalReference")
    void deletePagosTEF(String internalReference);

    @Query("DELETE FROM BPM_DETALLE_DESCUENTO WHERE internalReference = :internalReference")
    void deleteDetalleDescuento(String internalReference);
}
