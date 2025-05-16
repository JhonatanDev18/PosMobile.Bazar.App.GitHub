package com.crystal.bazarposmobile.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.bazarposmobile.R;
import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.LogFile;
import com.crystal.bazarposmobile.common.SPM;
import com.crystal.bazarposmobile.db.BazarPosMovilDB;
import com.crystal.bazarposmobile.db.entity.LoginEntity;
import com.crystal.bazarposmobile.retrofit.AppCliente;
import com.crystal.bazarposmobile.retrofit.ApiService;
import com.crystal.bazarposmobile.retrofit.request.RequestLogin;
import com.crystal.bazarposmobile.retrofit.request.RequestParametros;
import com.crystal.bazarposmobile.retrofit.response.ResponseValidarAperturaCaja;
import com.crystal.bazarposmobile.retrofit.response.ResponseParametros;
import com.crystal.bazarposmobile.retrofit.response.login.ResponseLogin;
import com.crystal.bazarposmobile.retrofit.response.login.Usuario;
import com.crystal.bazarposmobile.ui.dialogfragmen.CambiarUrlDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Activity Principal de la Aplicación
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaración del Cliente REST
    ApiService apiService;

    //Declaración de los objetos de la interfaz del activity
    Button btnlogin;
    EditText tvuser,tvpassword;
    ProgressBar pb;
    TextView tvmodoautonomo;
    ImageView ivcamarausuario,ivcamarapass;

    //Declaración de la variables del activity
    String caja, tienda;
    Boolean scanUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        int estadoPermiso = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(estadoPermiso == PackageManager.PERMISSION_GRANTED){
            inicio();
        }else{
            dialogPermisos();
        }
    }

    private void inicio() {
        //Asignando la URL Base para la App
        if(SPM.getString(Constantes.API_POSSERVICE_URL) == null){
            SPM.setString(Constantes.API_POSSERVICE_URL, "https://pvp.crystal.com.co:3000/");
            //SPM.setString(Constantes.API_POSSERVICE_URL, "http://172.18.171.199:3000/");
            SPM.setBoolean(Constantes.MODO_AUTONOMO, false);
            SPM.setBoolean(Constantes.LINTERNA, false);
        }

        getVariablesActivity();
        retrofitInit();
        findViews();
        events();

        if(SPM.getBoolean(Constantes.MODO_AUTONOMO)){
            tvmodoautonomo.setVisibility(View.VISIBLE);
        }
    }

    private void dialogPermisos() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);

        alertDialog.setTitleText(getResources().getString(R.string.informacion))
                .setCustomImage(R.drawable.crystal)
                .setContentText(getResources().getString(R.string.alertaPermisosMemoriaInterna))
                .setCancelButton(getResources().getString(R.string.salir), sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    finish();
                })
                .setConfirmButton(getResources().getString(R.string.aceptar), sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constantes.PERMISO_WRITE_EXTERNAL_STORAGE);
                })
                .setCancelable(false);

        alertDialog.show();
    }

    //Obtener variable de sesion caja
    private void getVariablesActivity() {
        caja = SPM.getString(Constantes.CAJA_CODE);
        tienda = SPM.getString(Constantes.TIENDA_CODE);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constantes.PERMISO_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                inicio();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Obtendremos resultados de escaneo aquí
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Validar si la respuest es vacia
        if (result != null) {
            if (result.getContents() == null) {
                msjToast(getResources().getString(R.string.escaneo_cancelado));
            } else {
                if(scanUser){
                    tvuser.setText(result.getContents());
                }else{
                    tvpassword.setText(result.getContents());
                }
            }
        }
    }

    //Instanciar el Cliebte REST
    private void retrofitInit() {
        AppCliente appCliente = AppCliente.getInstance();
        apiService = appCliente.getApiService();
    }

    //Asignacion de Referencias
    private void findViews() {
        btnlogin = findViewById(R.id.btnIniciarSesionLoginA);
        tvuser = findViewById(R.id.etUsuarioLoginA);
        tvpassword = findViewById(R.id.etPassLoginA);

        pb = findViewById(R.id.pbLoginA);
        pb.setVisibility(View.GONE);

        tvmodoautonomo = findViewById(R.id.tvModoAutonomoLoginA);
        tvmodoautonomo.setVisibility(View.GONE);

        ivcamarausuario = findViewById(R.id.ivLecturaCamaraUsuarioLoginA);
        ivcamarapass = findViewById(R.id.ivLecturaCamaraPassLoginA);

        tvuser.requestFocus();
    }

    //Asignacion de eventos
    private void events() {
        btnlogin.setOnClickListener(this);
        ivcamarausuario.setOnClickListener(this);
        ivcamarapass.setOnClickListener(this);

        //Evento sobre el EditText password
        tvpassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnlogin.callOnClick();
                    return true;
                }
                return false;
            }
        });
        tvpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnlogin.callOnClick();
                }
                return handled;
            }
        });
        tvuser.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        camara_user();
                        return false;
                    }
                }
        );
        tvpassword.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        camara_pass();
                        return false;
                    }
                }
        );
    }

    private void camara_user() {
        scanUser = true;
        new IntentIntegrator(LoginActivity.this).setCaptureActivity(LectorCodigosCamaraActivity.class).initiateScan();
    }

    private void camara_pass() {
        scanUser = false;
        new IntentIntegrator(LoginActivity.this).setCaptureActivity(LectorCodigosCamaraActivity.class).initiateScan();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnIniciarSesionLoginA:
                iPB();

                final String usuario = tvuser.getText().toString().toLowerCase();
                final String pass = tvpassword.getText().toString();

                //Validar que usuario y contraseña no sean vacio
                if(usuario.isEmpty()){
                    tvuser.setError(getResources().getString(R.string.usurio_requerido));
                    pPB();
                }else if(pass.isEmpty()){
                    tvpassword.setError(getResources().getString(R.string.pass_requerido));
                    pPB();
                }else if(usuario.equals("1234") && pass.equals("1234")){
                    cambiarURL();
                    pPB();
                }else{
                    if(isNetDisponible()){
                        iniciarSesion(usuario,pass);
                    }else{
                        new LoginAsyncTask(usuario,pass).execute();
                    }
                }
                break;
            case R.id.ivLecturaCamaraUsuarioLoginA:
                camara_user();
                break;
            case R.id.ivLecturaCamaraPassLoginA:
                camara_pass();
                break;
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void,Void, LoginEntity> {

        String usuario;
        String contrasena;

        LoginAsyncTask(String usuario, String contrasena) {
            this.usuario = usuario;
            this.contrasena = contrasena;
        }

        @Override
        protected LoginEntity doInBackground(Void... voids) {
            return BazarPosMovilDB.getBD(getApplication()).loginDao().getLogin(usuario,contrasena);
        }

        @Override
        protected void onPostExecute(LoginEntity loginEntity) {
            super.onPostExecute(loginEntity);

            if(loginEntity == null){
                mensajeSimpleDialog(getResources().getString(R.string.error),"Usuario y/o contraseña inválida, o el usuario nunca ha iniciado sesión en modo autonómo.");
            }else{
                String usuarioCajero = SPM.getString(Constantes.USUARIO_CAJERO);
                String usuarioConf = SPM.getString(Constantes.USUARIO_CONFIGURADOR);

                //usuario es igual a Cajero (CJT)
                if(usuarioCajero.contains(loginEntity.getGrupo())){
                    //Validar que el dispositivo tenga una caja configurada
                    if(caja != null) {
                        //Validar que el usuario pertenezca a la tienda que esta configurada en el dispositivo
                        if(tienda.equals(loginEntity.getTiendaPorDefecto())){
                            pasarConsultaCliente(true);
                        }else{
                            String msjbase = getResources().getString(R.string.usuario_no_tienda);
                            String msjbaseFormateada = String.format(msjbase, SPM.getString(Constantes.NOMBRE_TIENDA));
                            mensajeSimpleDialog(getResources().getString(R.string.error),msjbaseFormateada);
                            tvuser.setText("");
                            tvpassword.setText("");
                            tvuser.requestFocus();
                        }
                    }else{
                        tvuser.setText("");
                        tvpassword.setText("");
                        tvuser.requestFocus();
                        mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.config_caja_msj));
                    }
                }
                //usuario es igual algun tipo configurador
                else if(usuarioConf.contains(loginEntity.getGrupo())){
                    mensajeSimpleDialog(getResources().getString(R.string.error),"En modo autónomo no puedes cambiar la configuración del equipo");
                    tvuser.setText("");
                    tvpassword.setText("");
                    tvuser.requestFocus();
                }
                //usuario no valido para la aplicacion
                else {
                    tvuser.setText("");
                    tvpassword.setText("");
                    tvuser.requestFocus();
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.tipo_usuario_invalido));
                }
            }
            pPB();
        }
    }

    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    //Dialog Fragment para la gestion del cambio de la URL Base
    private void cambiarURL() {
        CambiarUrlDialogFragment dialogFragment = new CambiarUrlDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"CambiarUrlDialogFragment");
    }

    private void iniciarSesion(final String usuario,final String pass) {
        //Guardar variable de sesion del nombre del usuario
        SPM.setString(Constantes.USER_NAME, usuario);
        //Ocultar el teclado de pantalla
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvpassword.getWindowToken(), 0);
        //Consultar la Api de login
        RequestLogin requestLogin = new RequestLogin(usuario, pass);
        Call<ResponseLogin> call = apiService.doLogin(requestLogin);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getEsValida()){
                        //Obtener el grupo al que pertenece el usuario
                        String grupo = response.body().getDatos().getGrupo();
                        String tiendaUsuario = response.body().getDatos().getTiendaPorDefecto();
                        final Usuario userData = response.body().getDatos();
                        //Guardar variable de sesion
                        SPM.setString(Constantes.GRUPO_AUTH_USER, response.body().getDatos().getGrupo());
                        //Consultar la Api de parametros para obtener los grupos de usurios para los rol en el pos movil
                        RequestParametros requestParametros = new RequestParametros("all",caja,tienda);
                        Call<ResponseParametros> callRPC = apiService.doParametrosCaja(usuario,requestParametros);
                        callRPC.enqueue(new Callback<ResponseParametros>() {
                            @Override
                            public void onResponse(Call<ResponseParametros> call, Response<ResponseParametros> response) {

                                if(response.isSuccessful()){
                                    assert response.body() != null;
                                    if(response.body().getError()){
                                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                                    }else{
                                        boolean errorPametro = false;

                                        if(response.body().getUsuarioCajero() != null){
                                            SPM.setString(Constantes.USUARIO_CAJERO, response.body().getUsuarioCajero());
                                        }else{
                                            errorPametro = true;
                                        }
                                        if(response.body().getUsuarioAdministrador() != null){
                                            SPM.setString(Constantes.USUARIO_ADMINISTRADOR, response.body().getUsuarioAdministrador());
                                        }else{
                                            errorPametro = true;
                                        }
                                        if(response.body().getUsuarioConfigurador() != null){
                                            SPM.setString(Constantes.USUARIO_CONFIGURADOR, response.body().getUsuarioConfigurador());
                                        }else{
                                            errorPametro = true;
                                        }

                                        if (response.body().getDatafonosCredibanCo() != null) {
                                            SPM.setString(Constantes.DATAFONOS_CREDIBANCO, response.body().getDatafonosCredibanCo());
                                        } else {
                                            errorPametro = true;
                                        }

                                        if (response.body().getDatafonosRedeban() != null) {
                                            SPM.setString(Constantes.DATAFONOS_REDEBAN, response.body().getDatafonosRedeban());
                                        } else {
                                            errorPametro = true;
                                        }

                                        if (response.body().getTimeoutDatafono() != null) {
                                            SPM.setString(Constantes.TIMEOUT_DATAFONO_CD, response.body().getTimeoutDatafono());
                                            SPM.setString(Constantes.TIMEOUT_DATAFONO_RB, response.body().getTimeoutDatafono());
                                        } else {
                                            errorPametro = true;
                                        }

                                        if (response.body().getTimeoutDatafonoEspera() != null) {
                                            SPM.setString(Constantes.TIMEOUT_ESPERA_DATAFONO_CD, response.body().getTimeoutDatafonoEspera());
                                            SPM.setString(Constantes.TIMEOUT_ESPERA_DATAFONO_RB, response.body().getTimeoutDatafonoEspera());
                                        } else {
                                            errorPametro = true;
                                        }

                                        if(errorPametro){
                                            mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_lect_caja_msj));
                                        }else{
                                            validarInicioSesion(grupo,tiendaUsuario,userData,usuario,pass);
                                        }
                                    }
                                }else{
                                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb));
                                }
                                pPB();
                            }

                            @Override
                            public void onFailure(Call<ResponseParametros> call, Throwable t) {
                                pPB();
                                LogFile.adjuntarLog( "ResponseParametros: "+call + t);
                                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                            }
                        });
                    }else{
                        mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                        SPM.setString(Constantes.USER_NAME, "");
                        pPB();
                        tvuser.setText("");
                        tvpassword.setText("");
                        tvuser.requestFocus();
                    }
                }else{
                    mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb));
                    SPM.setString(Constantes.USER_NAME, "");
                    tvuser.setText("");
                    tvpassword.setText("");
                    tvuser.requestFocus();
                    pPB();
                    preguntarInicioMA(usuario,pass);
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                pPB();
                SPM.setString(Constantes.USER_NAME, "");
                LogFile.adjuntarLog("ErrorResponseLogin: " + call + t);
                msjToast(getResources().getString(R.string.error_conexion) + t.getMessage());
                preguntarInicioMA(usuario,pass);
            }
        });
    }

    private void preguntarInicioMA(final String usuario,final String pass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.iniciar_sesion)
                .setMessage("Quieres intentar iniciar sesión mediante modo autónomo")
                .setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new LoginAsyncTask(usuario,pass).execute();
                            }
                        })
                .setNegativeButton(R.string.volver,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void validarInicioSesion(String grupo, String tiendaUsuario, final Usuario usuarioData, final String usuario, final String pass) {

        String usuarioCajero = SPM.getString(Constantes.USUARIO_CAJERO);
        String usuarioConf = SPM.getString(Constantes.USUARIO_CONFIGURADOR);

        //usuario es igual a Cajero (CJT)
        if(usuarioCajero.contains(grupo)){
            //Validar que el dispositivo tenga una caja configurada
            if(caja != null) {
                //Validar que el usuario pertenezca a la tienda que esta configurada en el dispositivo
                if(tienda.equals(tiendaUsuario)){
                    //Consultar la Api de Apertura de Caja
                    Call<ResponseValidarAperturaCaja> call2 = apiService.doAperturaCaja(usuario,caja);
                    call2.enqueue(new Callback<ResponseValidarAperturaCaja>() {
                        @Override
                        public void onResponse(Call<ResponseValidarAperturaCaja> call, Response<ResponseValidarAperturaCaja> response) {
                            //Validar que la respuesta de Api sea correcta
                            if (response.isSuccessful()) {
                                //Validar que la caja tiene apertura
                                assert response.body() != null;
                                if(response.body().getEsValida()){
                                    //Validar que la caja este abierta
                                    if(response.body().getEstadoCaja().equals("ABIERTA")) {
                                        //Guardar variable de sesion del cajero
                                        SPM.setString(Constantes.CAJERO_CODE, response.body().getCajero());
                                        SPM.setString(Constantes.CAJERO_NAME, response.body().getNombreVendedor());
                                        SPM.setInt(Constantes.NUMERO_DIA_APERTURA, response.body().getNumeroDia());
                                        regitrarLogin(usuarioData,usuario,pass,true);
                                    }else{
                                        String msjbase = getResources().getString(R.string.caja_n_cerrada);
                                        String msjbaseFormateada = String.format(msjbase, caja);
                                        mensajeSimpleDialog(getResources().getString(R.string.error),msjbaseFormateada);
                                        tvuser.setText("");
                                        tvpassword.setText("");
                                        tvuser.requestFocus();
                                        pPB();
                                    }
                                }else{
                                    mensajeSimpleDialog(getResources().getString(R.string.error),response.body().getMensaje());
                                    tvuser.setText("");
                                    tvpassword.setText("");
                                    tvuser.requestFocus();
                                    pPB();
                                }
                            } else {
                                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion_sb));
                                tvuser.setText("");
                                tvpassword.setText("");
                                tvuser.requestFocus();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseValidarAperturaCaja> call, Throwable t) {
                            LogFile.adjuntarLog( "ResponseValidarAperturaCaja: "+call + t);
                            pPB();
                            mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.error_conexion) + t.getMessage());
                        }
                    });
                }else{
                    String msjbase = getResources().getString(R.string.usuario_no_tienda);
                    String msjbaseFormateada = String.format(msjbase, SPM.getString(Constantes.NOMBRE_TIENDA));
                    mensajeSimpleDialog(getResources().getString(R.string.error),msjbaseFormateada);
                    tvuser.setText("");
                    tvpassword.setText("");
                    tvuser.requestFocus();
                    pPB();
                }
            }else{
                tvuser.setText("");
                tvpassword.setText("");
                tvuser.requestFocus();
                mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.config_caja_msj));
                pPB();
            }
        }
        //usuario es igual algun tipo configurador
        else if(usuarioConf.contains(grupo)){
            regitrarLogin(usuarioData,usuario,pass,false);
            pPB();
            tvuser.setText("");
            tvpassword.setText("");
            tvuser.requestFocus();
        }
        //usuario no valido para la aplicacion
        else {
            tvuser.setText("");
            tvpassword.setText("");
            tvuser.requestFocus();
            SPM.setString(Constantes.USER_NAME, "");
            mensajeSimpleDialog(getResources().getString(R.string.error),getResources().getString(R.string.tipo_usuario_invalido));
            pPB();
        }
    }

    private void regitrarLogin(Usuario user, String usuario, String pass, Boolean esCajero) {
        LoginEntity login = new LoginEntity(usuario,pass,user.getEstado(),user.getGrupo(),user.getUsuarioUtil(),user.getTiendaPorDefecto(),user.getPais());
        new VaidarLoginAsyncTask(esCajero).execute(login);
    }

    protected class VaidarLoginAsyncTask extends AsyncTask<LoginEntity,Void, LoginEntity> {
        Boolean esCajero;
        LoginEntity login;
        public VaidarLoginAsyncTask(Boolean esCajero) {
            this.esCajero = esCajero;
        }

        @Override
        protected LoginEntity doInBackground(LoginEntity... loginEntity) {
            login = loginEntity[0];
            return BazarPosMovilDB.getBD(getApplication()).loginDao().getUser(login.getUsuario());
        }

        @Override
        protected void onPostExecute(LoginEntity loginEntity) {
            if(loginEntity == null){
                new LoginInsertAsyncTask(esCajero).execute(login);
            }else{
                new LoginUpdateAsyncTask(esCajero).execute(login);
            }
        }
    }

    private class LoginUpdateAsyncTask extends AsyncTask<LoginEntity,Void,Void>{

        Boolean esCajero;
        public LoginUpdateAsyncTask(Boolean esCajero) {
            this.esCajero = esCajero;
        }

        @Override
        protected Void doInBackground(LoginEntity... loginEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).loginDao().update(loginEntity[0]);
            }catch (Exception ex){
                LogFile.adjuntarLog("update: "+ex.getMessage());
            }
            pasarConsultaCliente(esCajero);
            return null;
        }
    }

    private class LoginInsertAsyncTask extends AsyncTask<LoginEntity,Void,Void>{

        Boolean esCajero;
        public LoginInsertAsyncTask(Boolean esCajero) {
            this.esCajero = esCajero;
        }

        @Override
        protected Void doInBackground(LoginEntity... loginEntity) {
            try{
                BazarPosMovilDB.getBD(getApplication()).loginDao().insert(loginEntity[0]);
            }catch (Exception ex){
                LogFile.adjuntarLog("insert: "+ex.getMessage());
            }
            pasarConsultaCliente(esCajero);
            return null;
        }
    }

    private void pasarConsultaCliente(Boolean esCajero) {
        Intent i;
        if(esCajero){
            //Pasar al activity de Consultar clientes
            i = new Intent(LoginActivity.this, ClienteConsultaActivity.class);
            i.putExtra("actualizarcaja",  true);
        }else{
            //Pasar al activity de Configuracion del Dispositivo
            i = new Intent(LoginActivity.this, ConfigActivity.class);
        }
        startActivity(i);
        finish();
    }

    private void iPB() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void pPB() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.GONE);
    }

    private void msjToast(String msj) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Alert Dialog para mostrar mensaje de Error o alerta
    public void mensajeSimpleDialog(String titulo, String msj) {

        int icon = R.drawable.msj_alert_30;
        if (titulo.equals(getResources().getString(R.string.error))) {
            icon = R.drawable.msj_error_30;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(msj)
                .setIcon(icon)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
