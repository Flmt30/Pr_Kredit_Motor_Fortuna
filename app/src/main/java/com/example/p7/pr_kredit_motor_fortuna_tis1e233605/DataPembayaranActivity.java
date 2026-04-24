package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataPembayaranActivity extends AppCompatActivity {
    private static final String TAG = "DataPembayaranActivity";
    private final Pembayaran pembayaran = new Pembayaran();
    private final Kredit kredit = new Kredit();
    private final Kreditor kreditor = new Kreditor();
    private final Motor motor = new Motor();
    private TableLayout tbLaporanPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pembayaran);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        tbLaporanPembayaran = findViewById(R.id.tbLaporanPembayaran);

        tampilDataLaporan();
    }

    public void KlikBack(View v) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void tampilDataLaporan() {
        if (tbLaporanPembayaran == null) return;
        tbLaporanPembayaran.removeAllViews();

        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.BLACK);
        String[] headers = {"ID", "Invoice", "Tgl", "Ke", "Bayar", "Action"};
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(10, 10, 10, 10);
            rowHeader.addView(tv);
        }
        tbLaporanPembayaran.addView(rowHeader);

        try {
            String res = pembayaran.tampil_query_pembayaran();
            if (res == null || res.isEmpty()) return;
            JSONArray array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                TableRow row = new TableRow(this);
                if (i % 2 != 0) row.setBackgroundColor(Color.LTGRAY);

                String idbayar = obj.optString("idbayar");
                String invoice = obj.optString("invoice");
                String tglbayar = obj.optString("tglbayar");
                String angsuranke = obj.optString("angsuranke");
                
                double bayarDouble = obj.optDouble("bayar", 0);
                String bayarFormatted = "Rp " + String.format("%,.0f", bayarDouble);

                String[] columns = {idbayar, invoice, tglbayar, angsuranke, bayarFormatted};
                for (String text : columns) {
                    TextView tv = new TextView(this);
                    tv.setText(text);
                    tv.setPadding(10, 10, 10, 10);
                    row.addView(tv);
                }

                // Tombol PDF
                Button btnPdf = new Button(this);
                btnPdf.setText("PDF");
                btnPdf.setTextSize(10); // Perkecil ukuran teks
                btnPdf.setBackgroundColor(Color.BLUE);
                btnPdf.setTextColor(Color.WHITE);
                btnPdf.setPadding(5, 5, 5, 5);
                btnPdf.setLayoutParams(new TableRow.LayoutParams(100, 80)); // Atur ukuran pasti
                btnPdf.setOnClickListener(v -> preparePdfData(invoice, tglbayar, angsuranke, bayarFormatted));
                row.addView(btnPdf);

                // Tombol Delete
                Button btnDel = new Button(this);
                btnDel.setText("X");
                btnDel.setTextSize(10);
                btnDel.setBackgroundColor(Color.RED);
                btnDel.setTextColor(Color.WHITE);
                btnDel.setPadding(5, 5, 5, 5);
                btnDel.setLayoutParams(new TableRow.LayoutParams(80, 80));
                btnDel.setOnClickListener(v -> {
                    pembayaran.hapus_pembayaran(idbayar);
                    tampilDataLaporan();
                });
                row.addView(btnDel);

                tbLaporanPembayaran.addView(row);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error tampilDataLaporan", e);
        }
    }

    private void preparePdfData(String invoice, String tgl, String ke, String bayarRaw) {
        try {
            // Bersihkan format mata uang untuk perhitungan jika perlu, 
            // tapi untuk PDF kita gunakan yang sudah terformat
            double bayarVal = Double.parseDouble(bayarRaw.replaceAll("[^0-9]", ""));
            String bayarFormatted = "Rp " + String.format("%,.0f", bayarVal);
            String resKredit = kredit.tampil_query_kredit();
            JSONArray arrayKredit = new JSONArray(resKredit);
            String idKreditor = "", kdMotor = "";
            
            for (int i = 0; i < arrayKredit.length(); i++) {
                JSONObject obj = arrayKredit.getJSONObject(i);
                if (obj.optString("invoice").equals(invoice)) {
                    idKreditor = obj.optString("idkreditor");
                    kdMotor = obj.optString("kdmotor");
                    break;
                }
            }

            // 2. Ambil data Customer (Kreditor)
            String resCustomer = kreditor.select_by_Idkreditor(idKreditor);
            JSONObject customerObj = new JSONArray(resCustomer).getJSONObject(0);
            String namaCust = customerObj.optString("nama");
            String alamatCust = customerObj.optString("alamat");
            String telfonCust = customerObj.optString("telepon"); // Pastikan field ini ada di DB

            // 3. Ambil data Motor
            String resMotor = motor.getMotorByKdmotor(kdMotor);
            JSONObject motorObj = new JSONArray(resMotor).getJSONObject(0);
            String tipeMotor = motorObj.optString("nama");

            createFullPdf(invoice, tgl, ke, bayarFormatted, namaCust, telfonCust, alamatCust, tipeMotor);

        } catch (Exception e) {
            Log.e(TAG, "Error preparePdfData", e);
            Toast.makeText(this, "Gagal memuat data pendukung: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createFullPdf(String invoice, String tgl, String ke, String bayar, String nama, String telfon, String alamat, String motor) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 Size
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        
        // Header Perusahaan
        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("FORTUNA MOTOR - DEALER RESMI", 50, 50, paint);
        paint.setTextSize(10);
        paint.setFakeBoldText(false);
        canvas.drawText("Jl. Raya Utama No. 123, Jakarta", 50, 70, paint);
        canvas.drawText("Telp: (021) 12345678", 50, 85, paint);
        
        canvas.drawLine(50, 100, 545, 100, paint);

        // Judul Invoice
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        canvas.drawText("INVOICE PEMBAYARAN ANGSURAN", 200, 130, paint);

        // Data Pembayaran
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText("No. Invoice  : " + invoice, 50, 170, paint);
        canvas.drawText("Tanggal      : " + tgl, 50, 190, paint);
        
        // Data Customer
        paint.setFakeBoldText(true);
        canvas.drawText("DATA CUSTOMER:", 50, 230, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("Nama         : " + nama, 50, 250, paint);
        canvas.drawText("Telepon      : " + telfon, 50, 270, paint);
        canvas.drawText("Alamat       : " + alamat, 50, 290, paint);
        
        // Data Kendaraan
        paint.setFakeBoldText(true);
        canvas.drawText("DATA KENDARAAN:", 50, 330, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("Tipe Motor   : " + motor, 50, 350, paint);
        
        // Rincian Pembayaran
        paint.setFakeBoldText(true);
        canvas.drawText("RINCIAN PEMBAYARAN:", 50, 390, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("Angsuran Ke  : " + ke, 50, 410, paint);
        canvas.drawText("Jumlah Bayar : " + bayar, 50, 430, paint);
        
        canvas.drawLine(50, 450, 545, 450, paint);

        // Tanda Tangan
        canvas.drawText("Tanda Tangan Customer,", 50, 550, paint);
        canvas.drawText("(..................................)", 50, 620, paint);
        canvas.drawText(nama, 70, 635, paint);

        canvas.drawText("Hormat Kami,", 400, 550, paint);
        canvas.drawText("FORTUNA MOTOR", 400, 565, paint);
        canvas.drawText("(..................................)", 400, 620, paint);
        canvas.drawText("Kasir", 435, 635, paint);

        document.finishPage(page);

        String fileName = "Invoice_Lengkap_" + invoice + "_" + ke + ".pdf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Berhasil disimpan di Folder Downloads", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error writing PDF", e);
            Toast.makeText(this, "Gagal download PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}
