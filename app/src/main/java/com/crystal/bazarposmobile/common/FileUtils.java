package com.crystal.bazarposmobile.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        // Verificar si el archivo fuente existe
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new IOException("Archivo fuente no encontrado");
        }

        // Crear el directorio de destino si no existe
        File destinationDirectory = destinationFile.getParentFile();
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        // Mover el archivo
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            // Cerrar los streams de entrada y salida
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
