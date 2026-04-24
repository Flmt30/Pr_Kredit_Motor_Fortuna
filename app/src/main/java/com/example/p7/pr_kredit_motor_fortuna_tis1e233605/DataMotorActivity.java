package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataMotorActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "DataMotorActivity";
    Motor motor = new Motor();
    TableLayout tbMotor;
    Button btTambahMotor, btRefreshDataMotor;
    ArrayList<Button> buttonEdit = new ArrayList<>();
    ArrayList<Button> buttonDelete = new ArrayList<>();
    JSONArray arrayMotor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_motor);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tbMotor = findViewById(R.id.tbMotor);
        btTambahMotor = findViewById(R.id.btTambahMotor);
        btRefreshDataMotor = findViewById(R.id.btRefreshDataMotor);

        tampildataMotor();
    }

    public void KlikbtTambahMotor(View v) {
        tambahMotor();
    }

    public void KlikbtRefreshDataMotor(View v) {
        tampildataMotor();
    }

    public void KlikBack(View v) {
        finish();
    }

    public void KlikbtExitDataMotor(View v) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void tampildataMotor() {
        tbMotor.removeAllViews();
        buttonEdit.clear();
        buttonDelete.clear();

        // Tambahkan Header
        tbMotor.addView(createTableHeader());

        try {
            String result = motor.tampilMotor();
            Log.d(TAG, "Hasil Response Server: " + result);

            if (result == null || result.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(DataMotorActivity.this, "Gagal koneksi ke server (Kosong)", Toast.LENGTH_LONG).show());
                return;
            }

            if (result.trim().equals("[]")) {
                Toast.makeText(this, "Data Motor masih kosong di database", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi JSON
            if (!result.trim().startsWith("[")) {
                Log.e(TAG, "Respon bukan JSON: " + result);
                Toast.makeText(this, "Format data server tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            arrayMotor = new JSONArray(result);

            for (int i = 0; i < arrayMotor.length(); i++) {
                JSONObject jsonChildNode = arrayMotor.getJSONObject(i);
                String idmotor = jsonChildNode.optString("idmotor");
                String kdmotor = jsonChildNode.optString("kdmotor");
                String nama = jsonChildNode.optString("nama");
                
                // Cek kolom 'tipe' atau 'type'
                String tipe = jsonChildNode.optString("tipe");
                if (tipe.isEmpty()) {
                    tipe = jsonChildNode.optString("type");
                }

                double hargaDouble = jsonChildNode.optDouble("harga", 0);
                String hargaFormatted = "Rp " + String.format("%,.0f", hargaDouble);

                TableRow barisData = new TableRow(this);
                if (i % 2 == 0) {
                    barisData.setBackgroundColor(Color.WHITE);
                } else {
                    barisData.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }

                String[] data = {kdmotor, nama, tipe, hargaFormatted};
                for (String d : data) {
                    TextView tv = new TextView(this);
                    tv.setText(d);
                    tv.setPadding(20, 20, 20, 20);
                    tv.setTextColor(Color.BLACK);
                    barisData.addView(tv);
                }

                // Layout untuk tombol aksi
                LinearLayout layoutAksi = new LinearLayout(this);
                layoutAksi.setOrientation(LinearLayout.HORIZONTAL);

                Button btnEdit = new Button(this);
                btnEdit.setText("EDIT");
                btnEdit.setTextSize(10);
                btnEdit.setId(Integer.parseInt(idmotor));
                btnEdit.setTag("Edit");
                btnEdit.setOnClickListener(this);
                layoutAksi.addView(btnEdit);

                Button btnDel = new Button(this);
                btnDel.setText("DEL");
                btnDel.setTextSize(10);
                btnDel.setBackgroundColor(Color.RED);
                btnDel.setTextColor(Color.WHITE);
                btnDel.setId(Integer.parseInt(idmotor));
                btnDel.setTag("Delete");
                btnDel.setOnClickListener(this);
                layoutAksi.addView(btnDel);

                barisData.addView(layoutAksi);
                tbMotor.addView(barisData);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
            Toast.makeText(this, "Terjadi kesalahan memproses data", Toast.LENGTH_SHORT).show();
        }
    }

    private TableRow createTableHeader() {
        TableRow barisHeader = new TableRow(this);
        barisHeader.setBackgroundColor(Color.DKGRAY);
        barisHeader.setPadding(0, 10, 0, 10);

        String[] headers = {"Kd Motor", "Nama Motor", "Tipe", "Harga", "Aksi"};
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(20, 15, 20, 15);
            tv.setGravity(android.view.Gravity.CENTER);
            barisHeader.addView(tv);
        }
        return barisHeader;
    }

    public void deleteMotor(int idmotor) {
        String laporan = motor.deleteMotor(idmotor);
        Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
        tampildataMotor();
    }

    @SuppressLint("SetTextI18n")
    public void getMotorByIdmotor(int idmotor) {
        String idmotorVal = "";
        String kdmotorVal = "";
        String namaVal = "";
        String tipeVal = "";
        String hargaVal = "";

        try {
            JSONArray arrayPersonal = new JSONArray(motor.getMotorByIdmotor(idmotor));
            if (arrayPersonal.length() > 0) {
                JSONObject obj = arrayPersonal.getJSONObject(0);
                idmotorVal = obj.optString("idmotor");
                kdmotorVal = obj.optString("kdmotor");
                namaVal = obj.optString("nama");
                tipeVal = obj.optString("tipe");
                hargaVal = obj.optString("harga");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error fetching motor detail", e);
        }

        // Variabel final untuk digunakan di dalam lambda
        final String finalIdMotor = idmotorVal;
        final String finalKdMotor = kdmotorVal;

        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        layoutInput.setPadding(40, 20, 40, 20);

        final TextView viewKdmotor = new TextView(this);
        viewKdmotor.setText("Kode Motor: " + finalKdMotor);
        viewKdmotor.setTextSize(18);
        viewKdmotor.setPadding(0, 10, 0, 20);
        layoutInput.addView(viewKdmotor);

        final EditText editNama = new EditText(this);
        editNama.setHint("Nama Motor");
        editNama.setText(namaVal);
        layoutInput.addView(editNama);

        final EditText editType = new EditText(this);
        editType.setHint("Tipe");
        editType.setText(tipeVal);
        layoutInput.addView(editType);

        final EditText editHarga = new EditText(this);
        editHarga.setHint("Harga");
        editHarga.setText(hargaVal);
        editHarga.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layoutInput.addView(editHarga);

        new AlertDialog.Builder(this)
                .setTitle("Update Motor")
                .setView(layoutInput)
                .setPositiveButton("Update", (dialog, which) -> {
                    // Ambil data langsung dari EditText saat tombol diklik
                    String n = editNama.getText().toString();
                    String t = editType.getText().toString();
                    String h = editHarga.getText().toString();

                    // Gunakan variabel finalKdMotor & finalIdMotor
                    String laporan = motor.updateMotor(finalIdMotor, finalKdMotor, n, t, h);
                    Toast.makeText(DataMotorActivity.this, laporan, Toast.LENGTH_SHORT).show();
                    tampildataMotor();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void tambahMotor() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        layoutInput.setPadding(50, 20, 50, 20);

        final EditText editKdMotor = new EditText(this);
        editKdMotor.setHint("KdMotor (Contoh: M001)");
        layoutInput.addView(editKdMotor);

        final EditText editNama = new EditText(this);
        editNama.setHint("Nama Motor");
        layoutInput.addView(editNama);

        final EditText editType = new EditText(this);
        editType.setHint("Tipe Motor");
        layoutInput.addView(editType);

        final EditText editHarga = new EditText(this);
        editHarga.setHint("Harga (Angka saja)");
        editHarga.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layoutInput.addView(editHarga);

        AlertDialog.Builder builderInsertMotor = new AlertDialog.Builder(this);
        builderInsertMotor.setTitle("Tambah Data Motor Baru");
        builderInsertMotor.setView(layoutInput);
        builderInsertMotor.setPositiveButton("Simpan", (dialog, which) -> {
            String kdmotor = editKdMotor.getText().toString().trim();
            String nama = editNama.getText().toString().trim();
            String type = editType.getText().toString().trim();
            String harga = editHarga.getText().toString().trim();

            if (kdmotor.isEmpty() || nama.isEmpty() || type.isEmpty() || harga.isEmpty()) {
                Toast.makeText(DataMotorActivity.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            } else {
                String laporan = motor.insertMotor(kdmotor, nama, type, harga);
                Toast.makeText(DataMotorActivity.this, laporan, Toast.LENGTH_SHORT).show();
                tampildataMotor();
            }
        });

        builderInsertMotor.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builderInsertMotor.show();
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag() != null ? view.getTag().toString() : "";
        int id = view.getId();

        if (tag.equals("Edit")) {
            getMotorByIdmotor(id);
        } else if (tag.equals("Delete")) {
            deleteMotor(id);
        }
    }
}
