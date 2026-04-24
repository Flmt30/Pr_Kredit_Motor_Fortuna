package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TransaksiActivity extends AppCompatActivity {
    Button btPengajuanKredit, btPembayaranAngsuran, btDataPengajuanKredit, btDataPembayaranAngsuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        btPengajuanKredit = findViewById(R.id.btPengajuanKredit);
        btPembayaranAngsuran = findViewById(R.id.btPembayaranAngsuran);
        btDataPengajuanKredit = findViewById(R.id.btDataPengajuanKredit);
        btDataPembayaranAngsuran = findViewById(R.id.btDataPembayaranAngsuran);

        btPengajuanKredit.setOnClickListener(view -> KlikbtPengajuanKredit());
        btPembayaranAngsuran.setOnClickListener(view -> KlikbtPembayaranAngsuran());
        btDataPengajuanKredit.setOnClickListener(view -> KlikbtDataPengajuanKredit());
        btDataPembayaranAngsuran.setOnClickListener(view -> KlikbtDataPembayaranAngsuran());
    }

    public void KlikbtPengajuanKredit() {
        Intent i = new Intent(getApplicationContext(), PengajuanKreditActivity.class);
        startActivity(i);
    }

    public void KlikbtPembayaranAngsuran() {
        Intent i = new Intent(getApplicationContext(), PembayaranAngsuranActivity.class);
        startActivity(i);
    }

    public void KlikbtDataPengajuanKredit() {
        Intent i = new Intent(getApplicationContext(), DataPengajuanKreditActivity.class);
        startActivity(i);
    }

    public void KlikbtDataPembayaranAngsuran() {
        // Ganti dengan Activity yang sesuai, contoh: DataPembayaranActivity.class
        Intent i = new Intent(getApplicationContext(), DataPembayaranActivity.class);
        startActivity(i);
    }

    public void KlikBack(View v) {
        finish();
    }
}
