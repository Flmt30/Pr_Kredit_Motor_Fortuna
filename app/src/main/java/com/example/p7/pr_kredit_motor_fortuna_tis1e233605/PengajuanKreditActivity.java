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

import java.util.ArrayList;

public class PengajuanKreditActivity extends AppCompatActivity {
    private static final String TAG = "PengajuanKreditActivity";

    // Inisialisasi Class Backend
    private final Kreditor kreditor = new Kreditor();
    private final Motor motor = new Motor();
    private final Kredit kredit = new Kredit();

    // Komponen UI
    private Spinner SpinnerNamaKreditor, SpinnerNamaMotor;
    private EditText editUangMuka, editBunga, editLamaAngsuran;
    private TextView textNamaMotor, textAlamatKrditor, textNamaKreditor, textHargaMotor, textHargaKredit, textTotalKredit, textAngsuranPerbulan;
    private TableLayout tbKredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_kredit);

        // Mengizinkan koneksi network di main thread (untuk keperluan praktikum)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        initUI();
        loadSpinners();
        tampildataKredit();
    }

    private void initUI() {
        tbKredit = findViewById(R.id.tbKredit);
        textNamaKreditor = findViewById(R.id.TextNamaKreditor);
        textAlamatKrditor = findViewById(R.id.TextAlamatKreditor);
        textNamaMotor = findViewById(R.id.TextNamaMotor);
        textHargaMotor = findViewById(R.id.textHargaMotor);
        textHargaKredit = findViewById(R.id.textHargaKredit);
        textTotalKredit = findViewById(R.id.textTotalKredit);
        textAngsuranPerbulan = findViewById(R.id.textAngsuranPerbulan);

        editBunga = findViewById(R.id.editBunga);
        editUangMuka = findViewById(R.id.editUangMuka);
        editLamaAngsuran = findViewById(R.id.editLamaAngsuran);

        SpinnerNamaKreditor = findViewById(R.id.SpinnerNamaKreditor);
        SpinnerNamaMotor = findViewById(R.id.SpinnerNamaMotor);

        SpinnerNamaKreditor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                getNamaKreditor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        SpinnerNamaMotor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                getKdmotorKredit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    private void loadSpinners() {
        try {
            // Load Kreditor
            String resKreditor = kreditor.tampilKreditorbyIdNama();
            if (resKreditor != null && !resKreditor.isEmpty()) {
                JSONArray array = new JSONArray(resKreditor);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getJSONObject(i).optString("idkreditor"));
                }
                SpinnerNamaKreditor.setAdapter(createAdapter(list));
            }

            // Load Motor
            String resMotor = motor.tampilMotor();
            if (resMotor != null && !resMotor.isEmpty()) {
                JSONArray array = new JSONArray(resMotor);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getJSONObject(i).optString("kdmotor"));
                }
                SpinnerNamaMotor.setAdapter(createAdapter(list));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error loading spinners", e);
        }
    }

    private ArrayAdapter<String> createAdapter(ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void getNamaKreditor() {
        if (SpinnerNamaKreditor.getSelectedItem() == null) return;
        try {
            String response = kreditor.select_by_Idkreditor(SpinnerNamaKreditor.getSelectedItem().toString());
            JSONArray array = new JSONArray(response);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                textNamaKreditor.setText(obj.optString("nama"));
                textAlamatKrditor.setText(obj.optString("alamat"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getNamaKreditor", e);
        }
    }

    public void getKdmotorKredit() {
        if (SpinnerNamaMotor.getSelectedItem() == null) return;
        try {
            String response = motor.getMotorByKdmotor(SpinnerNamaMotor.getSelectedItem().toString());
            JSONArray array = new JSONArray(response);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                textNamaMotor.setText(obj.optString("nama"));
                
                double harga = obj.optDouble("harga", 0);
                textHargaMotor.setText("Rp " + String.format("%,.0f", harga));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getKdmotorKredit", e);
        }
    }

    @SuppressLint("DefaultLocale")
    public void HitungKredit() {
        try {
            // Bersihkan format mata uang (hapus Rp dan pemisah ribuan)
            String sharga = textHargaMotor.getText().toString().replaceAll("[^0-9]", "");
            String sdp = editUangMuka.getText().toString().replaceAll("[^0-9]", "");
            String sbunga = editBunga.getText().toString().replaceAll("[^0-9]", "");
            String slama = editLamaAngsuran.getText().toString().replaceAll("[^0-9]", "");

            if (sharga.isEmpty() || sdp.isEmpty() || sbunga.isEmpty() || slama.isEmpty()) {
                Toast.makeText(this, "Silahkan Lengkapi Data !", Toast.LENGTH_SHORT).show();
                return;
            }

            double harga = Double.parseDouble(sharga);
            double dp = Double.parseDouble(sdp);
            double bunga = Double.parseDouble(sbunga);
            double lama = Double.parseDouble(slama);

            double hargaKredit = harga - dp;
            double totalKredit = hargaKredit + (hargaKredit * (bunga / 100) * (lama / 12));
            double angsuran = totalKredit / lama;

            // Menambahkan format Rp dan pemisah ribuan
            textHargaKredit.setText("Harga Kredit: Rp " + String.format("%,.0f", hargaKredit));
            textTotalKredit.setText("Total Kredit: Rp " + String.format("%,.0f", totalKredit));
            textAngsuranPerbulan.setText("Rp " + String.format("%,.0f", angsuran) + " /Bulan");
        } catch (Exception e) {
            Log.e(TAG, "Error HitungKredit", e);
        }
    }

    public void KlikbuttonProsesPengajuanKredit(View v) {
        HitungKredit();
    }

    public void KlikBack(View v) {
        finish();
    }

    public void KlikbuttonResetKredit(View v) {
        editUangMuka.setText("");
        editBunga.setText("");
        editLamaAngsuran.setText("");
        textHargaKredit.setText("0");
        textTotalKredit.setText("0");
        textAngsuranPerbulan.setText("0");
    }

    public void KlikbuttonSimpanPengajuanKredit(View v) {
        if (SpinnerNamaKreditor.getSelectedItem() == null || SpinnerNamaMotor.getSelectedItem() == null) {
            Toast.makeText(this, "Pilih Kreditor dan Motor!", Toast.LENGTH_SHORT).show();
            return;
        }

        String idkreditor = SpinnerNamaKreditor.getSelectedItem().toString();
        String kdmotor = SpinnerNamaMotor.getSelectedItem().toString();
        
        // Ambil hanya angka dari TextView/EditText
        String hrgtunai = textHargaMotor.getText().toString().replaceAll("[^0-9]", "");
        String dp = editUangMuka.getText().toString().replaceAll("[^0-9]", "");
        
        // Membersihkan string dari format "Harga Kredit: Rp 15,000" menjadi "15000"
        String hrgkredit = textHargaKredit.getText().toString().replaceAll("[^0-9]", "");
        String bunga = editBunga.getText().toString();
        String lama = editLamaAngsuran.getText().toString();
        String totalkredit = textTotalKredit.getText().toString().replaceAll("[^0-9]", "");
        String angsuran = textAngsuranPerbulan.getText().toString().replaceAll("[^0-9]", "");

        if (dp.isEmpty() || lama.isEmpty() || angsuran.equals("0") || angsuran.isEmpty()) {
            Toast.makeText(this, "Hitung Kredit Terlebih Dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Panggil fungsi simpan
        String laporan = kredit.simpan_kredit(idkreditor, kdmotor, hrgtunai, dp, hrgkredit, bunga, lama, totalkredit, angsuran);
        Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
        
        // Refresh tabel
        tampildataKredit();
    }

    @SuppressLint("SetTextI18n")
    public void tampildataKredit() {
        if (tbKredit == null) return;
        tbKredit.removeAllViews();
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.BLACK);

        String[] headers = {"Inv", "Kreditor", "Motor", "Angsuran", "Action"};
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(10, 10, 10, 10);
            rowHeader.addView(tv);
        }
        tbKredit.addView(rowHeader);

        try {
            String res = kredit.tampil_query_kredit();
            if (res == null || res.isEmpty()) return;
            JSONArray array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                TableRow row = new TableRow(this);
                if (i % 2 != 0) row.setBackgroundColor(Color.LTGRAY);

                // Sesuaikan kunci dengan alias di PHP (invoice, idkreditor, kdmotor, angsuran)
                String invoice = obj.optString("invoice");
                String idkreditor = obj.optString("idkreditor");
                String kdmotor = obj.optString("kdmotor");
                String angsuran = "Rp " + String.format("%,.0f", obj.optDouble("angsuran"));

                String[] columns = {invoice, idkreditor, kdmotor, angsuran};
                for (String text : columns) {
                    TextView tv = new TextView(this);
                    tv.setText(text);
                    tv.setPadding(10, 10, 10, 10);
                    row.addView(tv);
                }

                Button btnDel = new Button(this);
                btnDel.setText("X");
                btnDel.setBackgroundColor(Color.RED);
                btnDel.setOnClickListener(v -> {
                    kredit.hapus_kredit(obj.optString("invoice"));
                    tampildataKredit();
                });
                row.addView(btnDel);
                tbKredit.addView(row);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error tampildataKredit", e);
        }
    }
}
