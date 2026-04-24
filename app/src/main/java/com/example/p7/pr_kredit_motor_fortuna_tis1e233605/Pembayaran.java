package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.util.Log;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Pembayaran extends Koneksi {
    private static final String TAG = "PembayaranClass";
    Server server = new Server();
    
    private String getBaseURL() {
        return server.Isi_Koneksi() + "tbpembayaran.php";
    }

    String url = "";
    String response = "";

    public String tampil_query_pembayaran() {
        try {
            url = getBaseURL() + "?operasi=query_pembayaran";
            Log.d(TAG, "Memanggil URL View: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error View: " + e.getMessage());
            response = e.toString();
        }
        return response;
    }

    public String simpan_pembayaran(String invoice, String tglbayar, String angsuranke, String bayar) {
        try {
            String utf8 = StandardCharsets.UTF_8.name();
            url = getBaseURL() + "?operasi=simpan_pembayaran" +
                    "&invoice=" + URLEncoder.encode(invoice, utf8) +
                    "&tglbayar=" + URLEncoder.encode(tglbayar, utf8) + 
                    "&angsuranke=" + URLEncoder.encode(angsuranke, utf8) + 
                    "&bayar=" + URLEncoder.encode(bayar, utf8);
            Log.d(TAG, "Memanggil URL Simpan: " + url);
            response = call(url);
            Log.d(TAG, "Respon Server Simpan: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Error Simpan: " + e.getMessage());
            response = e.toString();
        }
        return response;
    }

    public String cek_status_angsuran(String invoice) {
        try {
            url = getBaseURL() + "?operasi=cek_status_angsuran&invoice=" + URLEncoder.encode(invoice, StandardCharsets.UTF_8.name());
            Log.d(TAG, "Cek Status URL: " + url);
            response = call(url);
            Log.d(TAG, "Respon Status: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Error Cek Status: " + e.getMessage());
            response = "";
        }
        return response;
    }

    public String hapus_pembayaran(String idbayar) {
        try {
            url = getBaseURL() + "?operasi=hapus_pembayaran&idbayar=" + idbayar;
            Log.d(TAG, "Memanggil URL Hapus: " + url);
            response = call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Hapus: " + e.getMessage());
            response = e.toString();
        }
        return response;
    }
}
