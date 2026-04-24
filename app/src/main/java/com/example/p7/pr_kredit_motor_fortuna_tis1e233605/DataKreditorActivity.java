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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataKreditorActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "DataKreditorActivity";
    // Inisialisasi Objek + Variabel + Class
    Koneksi koneksi = new Koneksi();
    TableLayout tbKreditor;
    Button btTambahKreditor, btRefreshDataKreditor;
    ArrayList<Button> buttonEdit = new ArrayList<>();
    ArrayList<Button> buttonDelete = new ArrayList<>();
    JSONArray arrayKreditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kreditor);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Pemberian Nama komponen
        tbKreditor = findViewById(R.id.tbKreditor);
        btTambahKreditor = findViewById(R.id.btTambahKreditor);
        btRefreshDataKreditor = findViewById(R.id.btRefreshDataKreditor);

        tampildataKreditor();
    }

    public void KlikbtTambahKreditor(View v) {
        tambahKreditor();
    }

    public void KlikbtRefreshDataKreditor(View v) {
        tampildataKreditor();
    }

    public void KlikBack(View v) {
        finish();
    }

    public void KlikbtExitDataKreditor(View v) {
        finish(); // Menutup activity dan kembali ke halaman sebelumnya
    }

    // --- Fungsi Database (Pengganti Kreditor.java) ---
    private String tampilKreditor() {
        try {
            String url = koneksi.Isi_Koneksi() + "tbkreditor.php?operasi=view";
            return koneksi.call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Tampil Kreditor", e);
            return "";
        }
    }

    private String insertKreditor(String kdkreditor, String nama, String alamat, String telp, String pekerjaan) {
        try {
            String url = koneksi.Isi_Koneksi() + "tbkreditor.php?operasi=insert" +
                    "&kdkreditor=" + URLEncoder.encode(kdkreditor, StandardCharsets.UTF_8.name()) +
                    "&nama=" + URLEncoder.encode(nama, StandardCharsets.UTF_8.name()) +
                    "&alamat=" + URLEncoder.encode(alamat, StandardCharsets.UTF_8.name()) +
                    "&telp=" + URLEncoder.encode(telp, StandardCharsets.UTF_8.name()) +
                    "&pekerjaan=" + URLEncoder.encode(pekerjaan, StandardCharsets.UTF_8.name());
            return koneksi.call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Insert Kreditor", e);
            return "Error: " + e.getMessage();
        }
    }

    private String getKreditorByIdkreditor(int idkreditor) {
        try {
            String url = koneksi.Isi_Koneksi() + "tbkreditor.php?operasi=get_kreditor_by_idkreditor&idkreditor=" + idkreditor;
            return koneksi.call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Get Kreditor", e);
            return "";
        }
    }

    private String updateKreditor(String idkreditor, String kdkreditor, String nama, String alamat, String telp, String pekerjaan) {
        try {
            String url = koneksi.Isi_Koneksi() + "tbkreditor.php?operasi=update" +
                    "&idkreditor=" + idkreditor +
                    "&kdkreditor=" + URLEncoder.encode(kdkreditor, StandardCharsets.UTF_8.name()) +
                    "&nama=" + URLEncoder.encode(nama, StandardCharsets.UTF_8.name()) +
                    "&alamat=" + URLEncoder.encode(alamat, StandardCharsets.UTF_8.name()) +
                    "&telp=" + URLEncoder.encode(telp, StandardCharsets.UTF_8.name()) +
                    "&pekerjaan=" + URLEncoder.encode(pekerjaan, StandardCharsets.UTF_8.name());
            return koneksi.call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Update Kreditor", e);
            return "Error: " + e.getMessage();
        }
    }

    private String deleteKreditor(int idkreditor) {
        try {
            String url = koneksi.Isi_Koneksi() + "tbkreditor.php?operasi=delete&idkreditor=" + idkreditor;
            return koneksi.call(url);
        } catch (Exception e) {
            Log.e(TAG, "Error Delete Kreditor", e);
            return "";
        }
    }
    // ------------------------------------------------

    @SuppressLint("SetTextI18n")
    public void tampildataKreditor() {
        tbKreditor.removeAllViews();
        buttonEdit.clear();
        buttonDelete.clear();

        TableRow barisTabel = new TableRow(this);
        barisTabel.setBackgroundColor(Color.BLACK);

        TextView viewHeaderKdKreditor = new TextView(this);
        TextView viewHeaderNama = new TextView(this);
        TextView viewHeaderAlamat = new TextView(this);
        TextView viewHeaderTelp = new TextView(this);
        TextView viewHeaderPekerjaan = new TextView(this);
        TextView viewHeaderAction = new TextView(this);

        viewHeaderKdKreditor.setText("KdKreditor");
        viewHeaderNama.setText("Nama");
        viewHeaderAlamat.setText("Alamat");
        viewHeaderTelp.setText("Telp");
        viewHeaderPekerjaan.setText("Pekerjaan");
        viewHeaderAction.setText("Action");

        viewHeaderKdKreditor.setTextColor(Color.WHITE);
        viewHeaderNama.setTextColor(Color.WHITE);
        viewHeaderAlamat.setTextColor(Color.WHITE);
        viewHeaderTelp.setTextColor(Color.WHITE);
        viewHeaderPekerjaan.setTextColor(Color.WHITE);
        viewHeaderAction.setTextColor(Color.WHITE);

        int padding = 15;
        viewHeaderKdKreditor.setPadding(padding, padding, padding, padding);
        viewHeaderNama.setPadding(padding, padding, padding, padding);
        viewHeaderAlamat.setPadding(padding, padding, padding, padding);
        viewHeaderTelp.setPadding(padding, padding, padding, padding);
        viewHeaderPekerjaan.setPadding(padding, padding, padding, padding);
        viewHeaderAction.setPadding(padding, padding, padding, padding);

        barisTabel.addView(viewHeaderKdKreditor);
        barisTabel.addView(viewHeaderNama);
        barisTabel.addView(viewHeaderAlamat);
        barisTabel.addView(viewHeaderTelp);
        barisTabel.addView(viewHeaderPekerjaan);
        barisTabel.addView(viewHeaderAction);

        tbKreditor.addView(barisTabel, new TableLayout.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        try {
            String response = tampilKreditor();
            if (response == null || response.isEmpty()) return;
            arrayKreditor = new JSONArray(response);

            for (int i = 0; i < arrayKreditor.length(); i++) {
                JSONObject jsonChildNode = arrayKreditor.getJSONObject(i);
                String idkreditor = jsonChildNode.optString("idkreditor");
                String kdkreditor = jsonChildNode.optString("kdkreditor");
                String nama = jsonChildNode.optString("nama");
                String alamat = jsonChildNode.optString("alamat");
                String telp = jsonChildNode.optString("telp");
                String pekerjaan = jsonChildNode.optString("pekerjaan");

                barisTabel = new TableRow(this);
                if (i % 2 == 0) barisTabel.setBackgroundColor(Color.LTGRAY);

                TextView viewKdKreditor = new TextView(this);
                viewKdKreditor.setText(kdkreditor);
                viewKdKreditor.setPadding(5, 5, 5, 5);
                barisTabel.addView(viewKdKreditor);

                TextView viewNama = new TextView(this);
                viewNama.setText(nama);
                viewNama.setPadding(5, 5, 5, 5);
                barisTabel.addView(viewNama);

                TextView viewAlamat = new TextView(this);
                viewAlamat.setText(alamat);
                viewAlamat.setPadding(5, 5, 5, 5);
                barisTabel.addView(viewAlamat);

                TextView viewTelp = new TextView(this);
                viewTelp.setText(telp);
                viewTelp.setPadding(5, 5, 5, 5);
                barisTabel.addView(viewTelp);

                TextView viewPekerjaan = new TextView(this);
                viewPekerjaan.setText(pekerjaan);
                viewPekerjaan.setPadding(5, 5, 5, 5);
                barisTabel.addView(viewPekerjaan);

                buttonEdit.add(i, new Button(this));
                buttonEdit.get(i).setId(Integer.parseInt(idkreditor));
                buttonEdit.get(i).setTag("Edit");
                buttonEdit.get(i).setText("Edit");
                buttonEdit.get(i).setOnClickListener(this);
                barisTabel.addView(buttonEdit.get(i));

                buttonDelete.add(i, new Button(this));
                buttonDelete.get(i).setId(Integer.parseInt(idkreditor));
                buttonDelete.get(i).setTag("Delete");
                buttonDelete.get(i).setText("Delete");
                buttonDelete.get(i).setOnClickListener(this);
                barisTabel.addView(buttonDelete.get(i));

                tbKreditor.addView(barisTabel, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }

    public void tambahKreditor() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final EditText editKdKreditor = new EditText(this);
        editKdKreditor.setHint("KdKreditor");
        layoutInput.addView(editKdKreditor);

        final EditText editNama = new EditText(this);
        editNama.setHint("Nama");
        layoutInput.addView(editNama);

        final EditText editAlamat = new EditText(this);
        editAlamat.setHint("Alamat");
        layoutInput.addView(editAlamat);

        final EditText editTelp = new EditText(this);
        editTelp.setHint("Telp");
        layoutInput.addView(editTelp);

        final EditText editPekerjaan = new EditText(this);
        editPekerjaan.setHint("Pekerjaan");
        layoutInput.addView(editPekerjaan);

        new AlertDialog.Builder(this)
                .setTitle("Insert Kreditor")
                .setView(layoutInput)
                .setPositiveButton("Insert", (dialog, which) -> {
                    String laporan = insertKreditor(editKdKreditor.getText().toString(), editNama.getText().toString(), editAlamat.getText().toString(), editTelp.getText().toString(), editPekerjaan.getText().toString());
                    Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
                    tampildataKreditor();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editKreditor(int idkreditor) {
        String kdkreditorEdit = "", namaEdit = "", alamatEdit = "", telpEdit = "", pekerjaanEdit = "";
        try {
            JSONArray arrayPersonal = new JSONArray(getKreditorByIdkreditor(idkreditor));
            if (arrayPersonal.length() > 0) {
                JSONObject jsonChildNode = arrayPersonal.getJSONObject(0);
                kdkreditorEdit = jsonChildNode.optString("kdkreditor");
                namaEdit = jsonChildNode.optString("nama");
                alamatEdit = jsonChildNode.optString("alamat");
                telpEdit = jsonChildNode.optString("telp");
                pekerjaanEdit = jsonChildNode.optString("pekerjaan");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error fetching detail", e);
        }

        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final TextView viewKdkreditor = new TextView(this);
        viewKdkreditor.setText(getString(R.string.label_kode, kdkreditorEdit));
        viewKdkreditor.setTextSize(18);
        layoutInput.addView(viewKdkreditor);

        final EditText editNama = new EditText(this);
        editNama.setText(namaEdit);
        layoutInput.addView(editNama);

        final EditText editAlamat = new EditText(this);
        editAlamat.setText(alamatEdit);
        layoutInput.addView(editAlamat);

        final EditText editTelp = new EditText(this);
        editTelp.setText(telpEdit);
        layoutInput.addView(editTelp);

        final EditText editPekerjaan = new EditText(this);
        editPekerjaan.setText(pekerjaanEdit);
        layoutInput.addView(editPekerjaan);

        String finalKdkreditorEdit = kdkreditorEdit;
        new AlertDialog.Builder(this)
                .setTitle("Update Kreditor")
                .setView(layoutInput)
                .setPositiveButton("Update", (dialog, which) -> {
                    String laporan = updateKreditor(String.valueOf(idkreditor), finalKdkreditorEdit, editNama.getText().toString(), editAlamat.getText().toString(), editTelp.getText().toString(), editPekerjaan.getText().toString());
                    Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
                    tampildataKreditor();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String tag = view.getTag() != null ? view.getTag().toString() : "";
        if (tag.equals("Edit")) {
            editKreditor(id);
        } else if (tag.equals("Delete")) {
            String laporan = deleteKreditor(id);
            Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
            tampildataKreditor();
        }
    }
}
