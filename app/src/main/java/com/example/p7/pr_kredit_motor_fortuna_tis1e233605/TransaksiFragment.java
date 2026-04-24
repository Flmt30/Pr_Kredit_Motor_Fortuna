package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TransaksiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaksi, container, false);

        // Pengajuan Kredit Baru
        Button btnPengajuan = view.findViewById(R.id.btPengajuanKredit);
        if (btnPengajuan != null) {
            btnPengajuan.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), PengajuanKreditActivity.class));
            });
        }

        // Lihat Data Pengajuan
        Button btnDataPengajuan = view.findViewById(R.id.btDataPengajuanKredit);
        if (btnDataPengajuan != null) {
            btnDataPengajuan.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), DataPengajuanKreditActivity.class));
            });
        }

        // Pembayaran Angsuran
        Button btnPembayaran = view.findViewById(R.id.btPembayaranAngsuran);
        if (btnPembayaran != null) {
            btnPembayaran.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), PembayaranAngsuranActivity.class));
            });
        }

        // Lihat Data Pembayaran
        Button btnDataPembayaran = view.findViewById(R.id.btDataPembayaranAngsuran);
        if (btnDataPembayaran != null) {
            btnDataPembayaran.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), DataPembayaranActivity.class));
            });
        }

        return view;
    }
}
