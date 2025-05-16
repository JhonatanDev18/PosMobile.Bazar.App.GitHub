package com.crystal.bazarposmobile.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.crystal.bazarposmobile.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

    private LogFile() {}

    public static void adjuntarLog(String text) {

        if (ContextCompat.checkSelfPermission(MyApp.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) MyApp.getContext(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constantes.PERMISO_WRITE_EXTERNAL_STORAGE);
        }else{
            //Obtener fecha y hora
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd");
            Date todayDate = new Date();

            //Crear ruta "Nombre a elegir (NOMBRE_CARPETA)" si no existe en el almacenamiento interno
            File dirPosMobile = new File(Environment.getExternalStorageDirectory(), Constantes.NOMBRE_CARPETA);
            if (!dirPosMobile.exists())
                if (!dirPosMobile.mkdirs())
                    android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.no_directorio));

            //Creando archivo txt por dia de nombre yyyyMMddlog.txt
            String ruta = dirPosMobile.toString() + "/" + currentDate.format(todayDate) + "log.txt";
            File logFile = new File(ruta);

            //Abriendo el archivo
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                    android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.log_no_creado)+" - "+e);
                }
            }

            //Escriendo en el archivo y despues cerrandolo
            try{
                //Fecha y hora de la escritura
                currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                todayDate = new Date();
                String fechahora = currentDate.format(todayDate);

                text = fechahora +": "+text;

                //BufferedWriter para establecer agregar al indicador de archivo
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            }catch (IOException e){
                e.printStackTrace();
                android.util.Log.e("LogFile", Resources.getSystem().getString(R.string.log_no_escrito)+" - "+e);
            }
        }
    }
}
