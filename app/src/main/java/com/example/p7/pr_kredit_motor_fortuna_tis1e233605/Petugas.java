package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class Petugas extends Koneksi {
    private static final String TAG = "PetugasClass";
    private String url = "";
    private String response = "";

    public String tampilPetugas() {
        try {
            url = Isi_Koneksi() + "tbpetugas.php?operasi=view";
            Log.d(TAG, "URL Tampil Petugas: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Tampil Petugas", e);
        }
        return response;
    }

    public String insertPetugas(String kdpetugas, String nama, String jabatan) {
        try {
            url = Isi_Koneksi() + "tbpetugas.php?operasi=insert&kdpetugas=" + URLEncoder.encode(kdpetugas, StandardCharsets.UTF_8.name()) + 
                  "&nama=" + URLEncoder.encode(nama, StandardCharsets.UTF_8.name()) + 
                  "&jabatan=" + URLEncoder.encode(jabatan, StandardCharsets.UTF_8.name());
            Log.d(TAG, "URL Insert Petugas: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Insert Petugas", e);
        }
        return response;
    }

    public String getPetugasByKdpetugas(int idpetugas) {
        try {
            url = Isi_Koneksi() + "tbpetugas.php?operasi=get_petugas_by_kdpetugas&idpetugas=" + idpetugas;
            Log.d(TAG, "URL Get Petugas: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Get Petugas", e);
        }
        return response;
    }

    public String updatePetugas(String idpetugas, String kdpetugas, String nama, String jabatan) {
        try {
            url = Isi_Koneksi() + "tbpetugas.php?operasi=update&idpetugas=" + idpetugas + 
                  "&kdpetugas=" + URLEncoder.encode(kdpetugas, StandardCharsets.UTF_8.name()) + 
                  "&nama=" + URLEncoder.encode(nama, StandardCharsets.UTF_8.name()) + 
                  "&jabatan=" + URLEncoder.encode(jabatan, StandardCharsets.UTF_8.name());
            Log.d(TAG, "URL Update Petugas: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Update Petugas", e);
        }
        return response;
    }

    public String deletePetugas(int idpetugas) {
        try {
            url = Isi_Koneksi() + "tbpetugas.php?operasi=delete&idpetugas=" + idpetugas;
            Log.d(TAG, "URL Delete Petugas: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Delete Petugas", e);
        }
        return response;
    }
}
