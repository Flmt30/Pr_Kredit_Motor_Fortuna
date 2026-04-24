package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PembayaranAngsuranActivity extends AppCompatActivity {
    private static final String TAG = "PembayaranActivity";

    private final Pembayaran pembayaran = new Pembayaran();
    private final Kredit kredit = new Kredit();

    private Spinner SpinnerInvoice;
    private EditText editTglBayar, editAngsuranKe, editBayar;
    private TextView txtSisaAngsuran; // Tambahan untuk info sisa
    private TableLayout tbPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_angsuran);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        initUI();
        setTanggalOtomatis();
        loadInvoices();
        tampilDataPembayaran();
        
        // Listener saat memilih Invoice di Spinner
        SpinnerInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cekStatusPembayaran();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initUI() {
        SpinnerInvoice = findViewById(R.id.SpinnerInvoice);
        editTglBayar = findViewById(R.id.editTglBayar);
        editAngsuranKe = findViewById(R.id.editAngsuranKe);
        editBayar = findViewById(R.id.editBayar);
        tbPembayaran = findViewById(R.id.tbPembayaran);
        
        // Cari TextView sisa angsuran jika ada di layout, jika tidak kita buat sementara di log
        txtSisaAngsuran = findViewById(R.id.txtInfoSisa); // Pastikan ID ini ada di XML atau sesuaikan
    }

    private void setTanggalOtomatis() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editTglBayar.setText(date);
    }

    private void loadInvoices() {
        String res = kredit.tampil_query_kredit();
        if (res == null || res.isEmpty()) return;

        ArrayList<String> list = parseInvoiceList(res);
        if (list.isEmpty()) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerInvoice.setAdapter(adapter);
    }

    private ArrayList<String> parseInvoiceList(String jsonResponse) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonResponse);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String invoice = obj.optString("invoice");
                String nama = obj.optString("namakreditor");
                if (nama.isEmpty()) nama = obj.optString("idkreditor");
                list.add(invoice + "_" + nama);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing invoice list", e);
        }
        return list;
    }

    private void cekStatusPembayaran() {
        if (SpinnerInvoice.getSelectedItem() == null) return;
        String fullSelection = SpinnerInvoice.getSelectedItem().toString();
        
        // Ambil hanya angka dari bagian sebelum garis bawah (misal "4_Budi" -> "4")
        String invPart = fullSelection.split("_")[0];
        String invoice = invPart.replaceAll("[^0-9]", "");

        if (invoice.isEmpty()) return;

        // Memanggil fungsi cek_status_angsuran di Pembayaran.java
        String res = pembayaran.cek_status_angsuran(invoice);
        try {
            if (res != null && !res.isEmpty()) {
                JSONObject obj = new JSONObject(res);
                int terakhir = obj.optInt("angsuran_terakhir", 0);
                int totalTenor = obj.optInt("total_tenor", 0);
                String nominal = obj.optString("nominal_angsuran", "");

                // 1. OTOMATIS ISI ANGSURAN KE & NOMINAL
                int angsuranBaru = terakhir + 1;
                editAngsuranKe.setText(String.valueOf(angsuranBaru));
                
                double nominalDouble = obj.optDouble("nominal_angsuran", 0);
                editBayar.setText("Rp " + String.format("%,.0f", nominalDouble));
                
                setTanggalOtomatis(); // Refresh tanggal ke hari ini

                // 2. CEK STATUS SISA / LUNAS
                if (txtSisaAngsuran != null) {
                    int sisa = totalTenor - terakhir;
                    View btnSimpan = findViewById(R.id.btnSimpanBayar);

                    if (totalTenor > 0 && terakhir >= totalTenor) {
                        txtSisaAngsuran.setText(getString(R.string.status_lunas, terakhir));
                        txtSisaAngsuran.setTextColor(Color.GREEN);
                        if (btnSimpan != null) btnSimpan.setEnabled(false); // Matikan tombol jika sudah lunas
                        editAngsuranKe.setText("-");
                        editBayar.setText("0");
                    } else {
                        txtSisaAngsuran.setText(getString(R.string.sisa_angsuran, sisa, totalTenor));
                        txtSisaAngsuran.setTextColor(Color.RED);
                        if (btnSimpan != null) btnSimpan.setEnabled(true); // Aktifkan jika masih ada sisa
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing status angsuran", e);
        }
    }

    public void KlikSimpanPembayaran(View v) {
        if (SpinnerInvoice.getSelectedItem() == null) return;

        String fullSelection = SpinnerInvoice.getSelectedItem().toString();
        // Pastikan hanya angka ID yang diambil (misal "4_Budi" -> "4")
        String invoice = fullSelection.split("_")[0].replaceAll("[^0-9]", "");

        String tgl = editTglBayar.getText().toString();
        String angsuranke = editAngsuranKe.getText().toString();
        // Bersihkan format mata uang sebelum kirim ke server
        String bayar = editBayar.getText().toString().replaceAll("[^0-9]", "");

        if (tgl.isEmpty() || angsuranke.isEmpty() || bayar.isEmpty() || invoice.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_lengkapi_data), Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan data
        String res = pembayaran.simpan_pembayaran(invoice, tgl, angsuranke, bayar);
        
        // Jika respons mengandung kode RTF (error), bersihkan atau beri peringatan
        if (res.contains("{\\rtf")) {
            Toast.makeText(this, "Error Server: Format file PHP salah (RTF). Harap bersihkan file PHP!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
            tampilDataPembayaran();
            cekStatusPembayaran(); // Update info sisa
        }
    }

    public void KlikResetPembayaran(View v) {
        editAngsuranKe.setText("");
        editBayar.setText("");
        setTanggalOtomatis();
    }

    public void KlikBack(View v) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void tampilDataPembayaran() {
        if (tbPembayaran == null) return;
        tbPembayaran.removeAllViews();

        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.BLACK);
        String[] headers = {"Kreditor", "Inv", "Tanggal", "Ke", "Bayar", "Action"};
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(10, 10, 10, 10);
            rowHeader.addView(tv);
        }
        tbPembayaran.addView(rowHeader);

        try {
            String res = pembayaran.tampil_query_pembayaran();
            if (res == null || res.isEmpty()) return;
            JSONArray array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                TableRow row = new TableRow(this);
                if (i % 2 != 0) row.setBackgroundColor(Color.LTGRAY);

                String[] columns = {
                        obj.optString("namakreditor"),
                        obj.optString("invoice"),
                        obj.optString("tglbayar"),
                        obj.optString("angsuranke"),
                        "Rp " + String.format("%,.0f", obj.optDouble("bayar", 0))
                };
                for (String text : columns) {
                    TextView tv = new TextView(this);
                    tv.setText(text);
                    tv.setPadding(10, 10, 10, 10);
                    row.addView(tv);
                }

                Button btnDel = new Button(this);
                btnDel.setText("X");
                btnDel.setBackgroundColor(Color.RED);
                btnDel.setTextColor(Color.WHITE);
                btnDel.setOnClickListener(v -> {
                    pembayaran.hapus_pembayaran(obj.optString("idbayar"));
                    tampilDataPembayaran();
                    cekStatusPembayaran();
                });
                row.addView(btnDel);
                tbPembayaran.addView(row);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error tampilDataPembayaran", e);
        }
    }
}
