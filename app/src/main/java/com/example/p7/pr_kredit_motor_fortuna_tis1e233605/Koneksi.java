package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Koneksi extends Server {
    private static final String TAG = "Koneksi";

    public String Isi_Koneksi() {
        return urlServer;
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(10000); // 10 detik
            httpConn.setReadTimeout(10000);
            httpConn.setUseCaches(false); // Jangan gunakan cache
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            } else {
                Log.e(TAG, "Gagal! Kode Respon Server: " + response + " untuk URL: " + urlString);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Gagal koneksi ke: " + urlString + " | Error: " + ex.getMessage());
            throw new IOException("Error connecting: " + ex.getMessage());
        }
        return in;
    }

    public String call(String url) {
        int BUFFER_SIZE = 2000;
        InputStream in;
        try {
            in = OpenHttpConnection(url);
            if (in == null) return "";
        } catch (IOException e) {
            Log.e(TAG, "Gagal membuka koneksi HTTP", e);
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        StringBuilder sb = new StringBuilder();
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                sb.append(inputBuffer, 0, charRead);
            }
            in.close();
        } catch (IOException e) {
            Log.e(TAG, "Gagal membaca data dari stream", e);
            return "";
        }
        return sb.toString();
    }
}