package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.util.Log;
import java.nio.charset.StandardCharsets;

public class Motor extends Koneksi {
    private static final String TAG = "MotorClass";
    Server server = new Server();
    
    private String getBaseURL() {
        return server.Isi_Koneksi() + "tbmotor.php";
    }

    String url = "";
    String response = "";

    public String tampilMotor() {
        try {
            url = getBaseURL() + "?operasi=view";
            Log.d(TAG, "Memanggil URL: " + url);
            response = call(url);
            if (response == null || response.isEmpty()) {
                Log.e(TAG, "Respon server KOSONG. Cek apakah Apache/XAMPP sudah jalan.");
            } else {
                Log.d(TAG, "Respon server: " + response);
            }
        } catch (Exception e) {
            Log.e(TAG, "Gagal memanggil tampilMotor: " + e.getMessage());
        }
        return response;
    }

    public String insertMotor(String kdmotor, String nama, String type, String harga) {
        try {
            String utf8 = StandardCharsets.UTF_8.name();
            url = getBaseURL() + "?operasi=insert&kdmotor=" + java.net.URLEncoder.encode(kdmotor, utf8) + 
                  "&nama=" + java.net.URLEncoder.encode(nama, utf8) + 
                  "&tipe=" + java.net.URLEncoder.encode(type, utf8) +
                  "&harga=" + java.net.URLEncoder.encode(harga, utf8);
            Log.d(TAG, "URL Insert Motor: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }

    public String getMotorByIdmotor(int idmotor) {
        try {
            url = getBaseURL() + "?operasi=get_motor_by_idmotor&idmotor=" + idmotor;
            Log.d(TAG, "URL Get Motor: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }

    public String updateMotor(String idmotor, String kdmotor, String nama, String type, String harga) {
        try {
            String utf8 = StandardCharsets.UTF_8.name();
            url = getBaseURL() + "?operasi=update&idmotor=" + idmotor + 
                  "&kdmotor=" + java.net.URLEncoder.encode(kdmotor, utf8) + 
                  "&nama=" + java.net.URLEncoder.encode(nama, utf8) + 
                  "&tipe=" + java.net.URLEncoder.encode(type, utf8) +
                  "&harga=" + java.net.URLEncoder.encode(harga, utf8);
            Log.d(TAG, "URL Update Motor: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }

    public String getMotorByKdmotor(String kdmotor) {
        try {
            url = getBaseURL() + "?operasi=get_motor_by_kdmotor&kdmotor=" + kdmotor;
            Log.d(TAG, "URL Get Motor by Code: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }

    public String deleteMotor(int idmotor) {
        try {
            url = getBaseURL() + "?operasi=delete&idmotor=" + idmotor;
            Log.d(TAG, "URL Delete Motor: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
        return response;
    }
}
