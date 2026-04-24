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

public class DataPetugasActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "DataPetugasActivity";
    // Inisialisasi Objek + Variabel + Class
    Petugas petugas = new Petugas();
    TableLayout tbPetugas;
    Button btTambahPetugas, btRefreshDataPetugas;
    ArrayList<Button> buttonEdit = new ArrayList<>();
    ArrayList<Button> buttonDelete = new ArrayList<>();
    JSONArray arrayPetugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petugas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Pemberian Nama komponen
        tbPetugas = findViewById(R.id.tbPetugas);
        btTambahPetugas = findViewById(R.id.btTambahPetugas);
        btRefreshDataPetugas = findViewById(R.id.btRefreshDataPetugas);

        tampildataPetugas();
    }

    public void KlikbtTambahPetugas(View v) {
        tambahPetugas();
    }

    public void KlikbtRefreshDataPetugas(View v) {
        tampildataPetugas();
    }

    public void KlikBack(View v) {
        finish();
    }

    public void KlikbtExitDataPetugas(View v) {
        finish();
    }
    @SuppressLint("SetTextI18n")
    public void tampildataPetugas() {
        tbPetugas.removeAllViews();
        buttonEdit.clear();
        buttonDelete.clear();

        TableRow barisTabel = new TableRow(this);
        barisTabel.setBackgroundColor(Color.BLACK);

        // Memberi Nama kolom HEADER
        TextView viewHeaderKdPetugas = new TextView(this);
        TextView viewHeaderNama = new TextView(this);
        TextView viewHeaderJabatan = new TextView(this);
        TextView viewHeaderAction = new TextView(this);

        viewHeaderKdPetugas.setText("KdPetugas");
        viewHeaderNama.setText("Nama");
        viewHeaderJabatan.setText("Jabatan");
        viewHeaderAction.setText("Action");

        viewHeaderKdPetugas.setTextColor(Color.WHITE);
        viewHeaderNama.setTextColor(Color.WHITE);
        viewHeaderJabatan.setTextColor(Color.WHITE);
        viewHeaderAction.setTextColor(Color.WHITE);

        viewHeaderKdPetugas.setPadding(5, 1, 5, 1);
        viewHeaderNama.setPadding(5, 1, 5, 1);
        viewHeaderJabatan.setPadding(5, 1, 5, 1);
        viewHeaderAction.setPadding(5, 1, 5, 1);

        barisTabel.addView(viewHeaderKdPetugas);
        barisTabel.addView(viewHeaderNama);
        barisTabel.addView(viewHeaderJabatan);
        barisTabel.addView(viewHeaderAction);

        tbPetugas.addView(barisTabel, new TableLayout.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        try {
            arrayPetugas = new JSONArray(petugas.tampilPetugas());

            for (int i = 0; i < arrayPetugas.length(); i++) {
                JSONObject jsonChildNode = arrayPetugas.getJSONObject(i);
                String idpetugas = jsonChildNode.optString("idpetugas");
                String kdpetugas = jsonChildNode.optString("kdpetugas");
                String nama = jsonChildNode.optString("nama");
                String jabatan = jsonChildNode.optString("jabatan");

                Log.d(TAG, "idpetugas :" + idpetugas);
                Log.d(TAG, "kdpetugas :" + kdpetugas);
                Log.d(TAG, "nama :" + nama);
                Log.d(TAG, "jabatan :" + jabatan);

                barisTabel = new TableRow(this);
                if (i % 2 == 0) {
                    barisTabel.setBackgroundColor(Color.LTGRAY);
                }

                TextView viewKdPetugas = new TextView(this);
                viewKdPetugas.setText(kdpetugas);
                viewKdPetugas.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewKdPetugas);

                TextView viewNama = new TextView(this);
                viewNama.setText(nama);
                viewNama.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewNama);

                TextView viewJabatan = new TextView(this);
                viewJabatan.setText(jabatan);
                viewJabatan.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewJabatan);

                // Membuat Button Edit pada Baris
                buttonEdit.add(i, new Button(this));
                buttonEdit.get(i).setId(Integer.parseInt(idpetugas));
                buttonEdit.get(i).setTag("Edit");
                buttonEdit.get(i).setText("Edit");
                buttonEdit.get(i).setOnClickListener(this);
                barisTabel.addView(buttonEdit.get(i));

                // Membuat Button Delete pada Baris
                buttonDelete.add(i, new Button(this));
                buttonDelete.get(i).setId(Integer.parseInt(idpetugas));
                buttonDelete.get(i).setTag("Delete");
                buttonDelete.get(i).setText("Delete");
                buttonDelete.get(i).setOnClickListener(this);
                barisTabel.addView(buttonDelete.get(i));

                tbPetugas.addView(barisTabel, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }

    public void deletePetugas(int idpetugas) {
        String laporan = petugas.deletePetugas(idpetugas);
        Toast.makeText(this, laporan, Toast.LENGTH_SHORT).show();
        tampildataPetugas();
    }

    public void getPetugasByKdpetugas(int idpetugas) {
        String idpetugasEdit = null;
        String kdpetugasEdit = null;
        String namaEdit = null;
        String jabatanEdit = null;
        JSONArray arrayPersonal;
        try {
            arrayPersonal = new JSONArray(petugas.getPetugasByKdpetugas(idpetugas));
            for (int i = 0; i < arrayPersonal.length(); i++) {
                JSONObject jsonChildNode = arrayPersonal.getJSONObject(i);
                idpetugasEdit = jsonChildNode.optString("idpetugas");
                kdpetugasEdit = jsonChildNode.optString("kdpetugas");
                namaEdit = jsonChildNode.optString("nama");
                jabatanEdit = jsonChildNode.optString("jabatan");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error fetching petugas detail", e);
        }

        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final TextView viewKdpetugas = new TextView(this);
        viewKdpetugas.setText(String.format("Kode = %s", kdpetugasEdit));
        viewKdpetugas.setBackgroundColor(Color.TRANSPARENT);
        viewKdpetugas.setTextColor(Color.BLACK);
        viewKdpetugas.setTextSize(20);
        layoutInput.addView(viewKdpetugas);

        final EditText editIdPetugas = new EditText(this);
        editIdPetugas.setText(idpetugasEdit);
        editIdPetugas.setVisibility(View.GONE);
        layoutInput.addView(editIdPetugas);

        final EditText editNama = new EditText(this);
        editNama.setText(namaEdit);
        layoutInput.addView(editNama);

        final EditText editJabatan = new EditText(this);
        editJabatan.setText(jabatanEdit);
        layoutInput.addView(editJabatan);

        AlertDialog.Builder builderEditPetugas = new AlertDialog.Builder(this);
        builderEditPetugas.setTitle("Update Petugas");
        builderEditPetugas.setView(layoutInput);
        builderEditPetugas.setPositiveButton("Update", (dialog, which) -> {
            String idpetugasUpdate = editIdPetugas.getText().toString();
            String kdpetugas = viewKdpetugas.getText().toString().replace("Kode =", "");
            String nama = editNama.getText().toString();
            String jabatan = editJabatan.getText().toString();

            System.out.println("IdPetugas : " + idpetugasUpdate + " KdPetugas : " + kdpetugas + " Nama : " + nama + " Jabatan : " + jabatan);
            String laporan = petugas.updatePetugas(idpetugasUpdate, kdpetugas, nama, jabatan);
            Toast.makeText(DataPetugasActivity.this, laporan, Toast.LENGTH_SHORT).show();
            tampildataPetugas();
        });

        builderEditPetugas.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builderEditPetugas.show();
    }

    public void tambahPetugas() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final EditText editKdPetugas = new EditText(this);
        editKdPetugas.setHint("KdPetugas");
        layoutInput.addView(editKdPetugas);

        final EditText editNama = new EditText(this);
        editNama.setHint("Nama");
        layoutInput.addView(editNama);

        final EditText editJabatan = new EditText(this);
        editJabatan.setHint("jabatan");
        layoutInput.addView(editJabatan);

        AlertDialog.Builder builderInsertPetugas = new AlertDialog.Builder(this);
        builderInsertPetugas.setTitle("Insert Petugas");
        builderInsertPetugas.setView(layoutInput);
        builderInsertPetugas.setPositiveButton("Insert", (dialog, which) -> {
            String kdpetugas = editKdPetugas.getText().toString();
            String nama = editNama.getText().toString();
            String jabatan = editJabatan.getText().toString();

            System.out.println("KdPetugas : " + kdpetugas + " Nama : " + nama + " Jabatan : " + jabatan);
            String laporan = petugas.insertPetugas(kdpetugas, nama, jabatan);
            Toast.makeText(DataPetugasActivity.this, laporan, Toast.LENGTH_SHORT).show();
            tampildataPetugas();
        });

        builderInsertPetugas.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builderInsertPetugas.show();
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag() != null ? view.getTag().toString() : "";
        int id = view.getId();

        if (tag.equals("Edit")) {
            getPetugasByKdpetugas(id);
        } else if (tag.equals("Delete")) {
            deletePetugas(id);
        }
    }
}
