package com.crystal.bazarposmobile.retrofit;

import com.crystal.bazarposmobile.retrofit.request.RequestActualizarCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.request.RequestBancolombiaQR;
import com.crystal.bazarposmobile.retrofit.request.RequestEanes;
import com.crystal.bazarposmobile.retrofit.request.RequestExtraerInfoDocumento;
import com.crystal.bazarposmobile.retrofit.request.RequestHeaderList;
import com.crystal.bazarposmobile.retrofit.request.RequestInsertarPerifericos;
import com.crystal.bazarposmobile.retrofit.request.RequestParametros;
import com.crystal.bazarposmobile.retrofit.request.RequestPerifericos;
import com.crystal.bazarposmobile.retrofit.request.RequestPostDocumento;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestActualizarTransaccion;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestAnularCompraN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestBaseN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestCompraN6;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestCompraN6SO;
import com.crystal.bazarposmobile.retrofit.request.datafonon6.RequestGuardarRespuestasTef;
import com.crystal.bazarposmobile.retrofit.request.detallesdescuento.RequestDetallesDescuento;
import com.crystal.bazarposmobile.retrofit.request.RequestLogin;
import com.crystal.bazarposmobile.retrofit.request.creardocumento.RequestSaleDocumentCreate;
import com.crystal.bazarposmobile.retrofit.request.facturaelectronica.RequestGenerarQRFE;
import com.crystal.bazarposmobile.retrofit.request.suspension.RequestSuspension;
import com.crystal.bazarposmobile.retrofit.request.tef.RequestTEF;
import com.crystal.bazarposmobile.retrofit.response.ResponseBase;
import com.crystal.bazarposmobile.retrofit.response.ResponseConsultarDocumento;
import com.crystal.bazarposmobile.retrofit.response.ResponseCupoEmpleado;
import com.crystal.bazarposmobile.retrofit.response.ResponseExtraerInfoDocumento;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.ConsultaPagoQr;
import com.crystal.bazarposmobile.retrofit.response.bancolombiaqr.ResponseBancolombiaQr;
import com.crystal.bazarposmobile.retrofit.response.cliente.ResponseCliente;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseActualizarTransaccion;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseAnularCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseBorrarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseConsultarCompraN6;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseGuardarRespuestasTef;
import com.crystal.bazarposmobile.retrofit.response.datafonon6.ResponseRespuestasDatafono;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDDList;
import com.crystal.bazarposmobile.retrofit.response.eanes.ResponseEanes;
import com.crystal.bazarposmobile.retrofit.response.ResponseValidarAperturaCaja;
import com.crystal.bazarposmobile.retrofit.response.consecutivofiscal.ResponseConsecutivoFiscal;
import com.crystal.bazarposmobile.retrofit.response.ResponseDomentoFiscal;
import com.crystal.bazarposmobile.retrofit.response.ResponseParametros;
import com.crystal.bazarposmobile.retrofit.response.caja.ResponseCajas;
import com.crystal.bazarposmobile.retrofit.response.cliente.ResponseClienteLista;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.RequestGuardarTrazaFE;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.ResponseGenerarQRFE;
import com.crystal.bazarposmobile.retrofit.response.facturaelectronica.ResponseGuardarTrazaFE;
import com.crystal.bazarposmobile.retrofit.response.login.ResponseLogin;
import com.crystal.bazarposmobile.retrofit.response.mediospagocaja.ResponseMediosCaja;
import com.crystal.bazarposmobile.retrofit.response.documentodetalle.ResponseDD;
import com.crystal.bazarposmobile.retrofit.response.perifericos.ResponseConsultarPerifericos;
import com.crystal.bazarposmobile.retrofit.response.perifericos.ResponseGuardarPerifericos;
import com.crystal.bazarposmobile.retrofit.response.postdocumento.ResponsePostDocumento;
import com.crystal.bazarposmobile.retrofit.response.suspensiones.ResponseSuspensiones;
import com.crystal.bazarposmobile.retrofit.response.tarjetasbancarias.ResponseTarjetasBancarias;
import com.crystal.bazarposmobile.retrofit.response.tienda.ResponseTienda;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<ResponseLogin> doLogin(@Body RequestLogin requestLogin);

    @GET("tienda/cajas")
    Call<ResponseCajas> doCajas(@Header("usuario") String usuario);

    @GET("mediospagocaja/{caja}")
    Call<ResponseMediosCaja> doMediosCaja(@Header("usuario") String usuario,@Path("caja") String caja);

    @GET("consecutivofiscal/{caja}")
    Call<ResponseConsecutivoFiscal> doConsecutivoFiscal(@Header("usuario") String usuario,@Path("caja") String caja);

    @GET("tienda/tienda/{codigo}")
    Call<ResponseTienda> doTienda(@Header("usuario") String usuario, @Path("codigo") String codigo);

    @POST("documento/crear/")
    Call<ResponseDomentoFiscal> doSaleDocument (@Header("usuario") String usuario,@Body RequestSaleDocumentCreate requestSaleDocumentCreate);

    @GET("validaraperturacaja/{caja}")
    Call<ResponseValidarAperturaCaja> doAperturaCaja(@Header("usuario") String usuario,@Path("caja") String caja);

    @GET("tarjetabancaria/{pais}")
    Call<ResponseTarjetasBancarias> doTarjetasBancarias(@Header("usuario") String usuario,@Path("pais") String pais);

    @POST("tef")
    Call<String> doRequestTEF(@Header("usuario") String usuario,@Body RequestTEF requestRequestTEF);

    @POST("parametros")
    Call<ResponseParametros> doParametrosCaja(@Header("usuario") String usuario,@Body RequestParametros requestParametros);

    @GET("cliente/buscar/{idcliente}")
    Call<ResponseClienteLista> doClienteBuscar(@Header("usuario") String usuario,@Path("idcliente") String idcliente);

    @POST("productos/eanes")
    Call<ResponseEanes> doEanes(@Header("usuario") String usuario,@Body RequestEanes requestEanes);

    @GET("documento/detalle/{referenciaInterna}")
    Call<ResponseDD> doConsultarDocumentoDetalle(@Header("usuario") String usuario,@Path("referenciaInterna") String documentoid);

    @POST("documento/postdocumento")
    Call<ResponsePostDocumento> doPostDocumento(@Header("usuario") String usuario,@Body RequestPostDocumento postDocumento);

    @POST("documento/detallesdescuento/")
    Call<String> doRequestDetallesDescuento(@Header("usuario") String usuario,@Body RequestDetallesDescuento requestDetallesDescuento);

    @GET("documento/{referenciaInterna}")
    Call<ResponseConsultarDocumento> doConsultarDocumento(@Header("usuario") String usuario, @Path("referenciaInterna") String documentoid);

    @POST("bazar/suspension/agregar")
    Call<ResponseBase> doSuspensionAgregar(@Header("usuario") String usuario, @Body RequestSuspension rSuspension);

    @GET("bazar/suspension/obtener/{tienda}")
    Call<ResponseSuspensiones> doObtenerSuspensiones(@Header("usuario") String usuario, @Path("tienda") String tienda);

    @GET("bazar/suspension/consumir/{referencia}")
    Call<ResponseBase> doConsumirSuspension(@Header("usuario") String usuario, @Path("referencia") String referencia);

    @GET("cliente/{codigo}")
    Call<ResponseCliente> doCliente(@Header("usuario") String usuario, @Path("codigo") String codigo);

    @POST("tef/compraN6")
    Call<ResponseCompraN6> doCompraN6(@Header("usuario") String usuario, @Body RequestCompraN6 requestCompraN6);
    @POST("tef/sobreescribirCompraN6")
    Call<ResponseCompraN6> doSobreescribirCompraN6(@Header("usuario") String usuario, @Body RequestCompraN6SO requestCompraN6SO);

    @POST("tef/consultarCompraN6")
    Call<ResponseConsultarCompraN6> doConsultarCompraN6(@Header("usuario") String usuario, @Body RequestBaseN6 requestBaseN6);

    @POST("tef/borrarCompraN6")
    Call<ResponseBorrarCompraN6> doBorrarCompraN6(@Header("usuario") String usuario, @Body RequestBaseN6 requestBaseN6);
    @POST("tef/actualizarRespuestaTef")
    Call<ResponseActualizarTransaccion> doActualizarAnulacionN6(@Header("usuario") String usuario, @Body RequestActualizarTransaccion requestActualizarTransaccion);

    @GET("tef/Respuestas")
    Call<ResponseRespuestasDatafono> doRespuestasDatafono(@Query("Fecha") String fecha,
                                                          @Query("Tienda") String tienda,
                                                          @Query("Caja") String caja);
    @POST("tef/guardarRespuestasTef")
    Call<ResponseGuardarRespuestasTef> doGuardarRespuestasTef(@Header("usuario") String usuario, @Body List<RequestGuardarRespuestasTef> respuestasTefs);
    @POST("tef/anularCompraN6")
    Call<ResponseAnularCompraN6> doAnularCompraN6(@Header("usuario") String usuario, @Body RequestAnularCompraN6 requestAnularCompraN6);
    @POST("tef/borrarAnulacionN6")
    Call<ResponseBorrarCompraN6> doBorrarAnulacionN6(@Header("usuario") String usuario, @Body RequestBaseN6 requestBaseN6);
    @POST("tef/consultarAnulacionN6")
    Call<ResponseConsultarCompraN6> doConsultarAnulacionN6(@Header("usuario") String usuario, @Body RequestBaseN6 requestBaseN6);

    @POST("documento/detalles")
    Call<ResponseDDList> doHeaderList(@Header("usuario") String usuario, @Body RequestHeaderList rHeaderList);

    @POST("bancolombia/qr-codes")
    Call<ResponseBancolombiaQr> doBancolombiaQr(@Header("usuario") String usuario, @Body RequestBancolombiaQR rBancolombiaQR);

    @GET("bancolombia/pagoqr/{referencia}")
    Call<ConsultaPagoQr> doConsultarPagoBancolombiaQr(@Header("usuario") String usuario, @Path("referencia") String referencia);

    @POST("tienda/caja/perifericos")
    Call<ResponseConsultarPerifericos> doConsultarPerifericos(@Header("usuario") String usuario, @Body RequestPerifericos requestPerifericos);

    @POST("tienda/caja/insertar/perifericos")
    Call<ResponseGuardarPerifericos> doGuardarPerifericos(@Header("usuario") String usuario, @Body List<RequestInsertarPerifericos> requestInsertarPerifericos);

    @POST("cliente/facturaelectronica/qr")
    Call<ResponseGenerarQRFE> doFacturaElectronicaQR(@Header("usuario") String usuario, @Body RequestGenerarQRFE requestGenerarQRFE);

    @POST("cliente/facturaelectronica/guardartraza")
    Call<ResponseGuardarTrazaFE> doFacturaElectronicaGuardarTraza(@Header("usuario") String usuario, @Body RequestGuardarTrazaFE requestGuardarTrazaFE);

    @POST("documento/extraerinfo")
    Call<ResponseExtraerInfoDocumento> doExtraerInfoDocumento(@Header("usuario") String usuario, @Body RequestExtraerInfoDocumento requestExtraerInfoDocumento);

    @GET("cupoempleado/{id}")
    Call<ResponseCupoEmpleado> doCupoEmpleado(@Header("usuario") String usuario, @Path("id") String id);

    @POST("cupoempleado/actualizar")
    Call<String> doActualizarCupoEmpleado(@Header("usuario") String usuario,
                                          @Body RequestActualizarCupoEmpleado requestActualizarCupoEmpleado);
}
