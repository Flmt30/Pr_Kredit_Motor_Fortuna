package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataPengajuanKreditActivity extends AppCompatActivity {
    private static final String TAG = "DataPengajuanKredit";
    
    Kredit kredit = new Kredit();
    TableLayout tbQueryKredit;
    Button btRefreshKredit, btKembaliKredit;
    JSONArray arrayQueryKredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pengajuan_kredit);

        // Bypass Network On Main Thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tbQueryKredit = findViewById(R.id.tbQueryKredit);
        btRefreshKredit = findViewById(R.id.btRefreshKredit);
        btKembaliKredit = findViewById(R.id.btKembaliKredit);

        btRefreshKredit.setOnClickListener(v -> {
            tampilQueryKredit();
            Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show();
        });

        btKembaliKredit.setOnClickListener(v -> finish());

        tampilQueryKredit();
    }

    public void KlikBack(View v) {
        finish();
    }

    private String formatRupiah(double amount) {
        return "Rp " + String.format("%,.0f", amount);
    }

    @SuppressLint("SetTextI18n")
    public void tampilQueryKredit() {
        tbQueryKredit.removeAllViews();

        // Header Tabel
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.BLACK);
        rowHeader.setPadding(0, 10, 0, 10);

        String[] headers = {"Invoice", "Tgl", "Kreditor", "Nama", "Motor", "Tunai", "DP", "Kredit", "Bunga", "Lama", "Total", "Angsuran", "Action"};
        
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(10, 10, 10, 10);
            tv.setGravity(Gravity.CENTER);
            rowHeader.addView(tv);
        }
        tbQueryKredit.addView(rowHeader);

        try {
            String response = kredit.tampil_query_kredit();
            if (response == null || response.isEmpty() || response.equals("null")) {
                return;
            }

            arrayQueryKredit = new JSONArray(response);
            for (int i = 0; i < arrayQueryKredit.length(); i++) {
                JSONObject obj = arrayQueryKredit.getJSONObject(i);
                
                // Ambil data dengan kunci yang sesuai dengan alias di tbkredit.php (view)
                final String invoice = obj.optString("invoice");
                final String tgl = obj.optString("tanggal");
                final String idkred = obj.optString("idkreditor");
                final String nmKred = obj.optString("nama");
                final String nMot = obj.optString("nmotor");
                
                // Format Rupiah untuk angka-angka
                final String hTunai = formatRupiah(obj.optDouble("hrgtunai", 0));
                final String dpVal = formatRupiah(obj.optDouble("dp", 0));
                final String hKred = formatRupiah(obj.optDouble("hrgkredit", 0));
                final String bungaVal = obj.optString("bunga");
                final String lamaVal = obj.optString("lama");
                final String totKred = formatRupiah(obj.optDouble("totalkredit", 0));
                final String angsVal = formatRupiah(obj.optDouble("angsuran", 0));

                TableRow rowData = new TableRow(this);
                if (i % 2 == 0) rowData.setBackgroundColor(Color.WHITE);
                else rowData.setBackgroundColor(Color.parseColor("#F5F5F5"));

                String[] data = {
                        invoice, tgl, idkred, nmKred, nMot, hTunai, dpVal, hKred, bungaVal + "%", lamaVal, totKred, angsVal
                };

                for (String d : data) {
                    TextView tv = new TextView(this);
                    tv.setText(d);
                    tv.setPadding(10, 20, 10, 20);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    rowData.addView(tv);
                }

                // Layout untuk tombol aksi
                android.widget.LinearLayout actionLayout = new android.widget.LinearLayout(this);
                actionLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);

                // Tombol PDF
                Button btnPdf = new Button(this);
                btnPdf.setText("PDF");
                btnPdf.setTextSize(10);
                btnPdf.setOnClickListener(v -> Toast.makeText(this, "Cetak PDF Invoice: " + invoice, Toast.LENGTH_SHORT).show());
                actionLayout.addView(btnPdf);

                // Tombol Delete
                Button btnDel = new Button(this);
                btnDel.setText("DEL");
                btnDel.setTextSize(10);
                btnDel.setBackgroundColor(Color.RED);
                btnDel.setTextColor(Color.WHITE);
                btnDel.setOnClickListener(v -> {
                    String res = kredit.hapus_kredit(invoice);
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                    tampilQueryKredit(); // Refresh
                });
                actionLayout.addView(btnDel);

                rowData.addView(actionLayout);
                tbQueryKredit.addView(rowData);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Error", e);
        }
    }
}
