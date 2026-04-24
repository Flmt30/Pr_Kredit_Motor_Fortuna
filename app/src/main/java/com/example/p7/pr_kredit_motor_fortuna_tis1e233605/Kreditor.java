package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.util.Log;

public class Kreditor extends Koneksi {
    private static final String TAG = "KreditorClass";
    Server server = new Server();
    String SERVER = server.urlDatabase1();
    String URL = "http://" + SERVER + "/jskreditmotor/tbkreditor.php";
    String url = "";
    String response = "";

    public String tampilKreditor() {
        try {
            url = URL + "?operasi=view";
            Log.d(TAG, "URL Tampil Kreditor: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }

    public String tampilKreditorbyIdNama() {
        return tampilKreditor();
    }

    public String select_by_Idkreditor(String idkreditor) {
        try {
            url = URL + "?operasi=get_kreditor_by_idkreditor&idkreditor=" + idkreditor;
            Log.d(TAG, "URL Get Kreditor by ID: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }
}
