package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);
        
        v.findViewById(R.id.btDataPetugasHome).setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), DataPetugasActivity.class);
            startActivity(i);
        });
        
        v.findViewById(R.id.btDataMotorHome).setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), DataMotorActivity.class);
            startActivity(i);
        });
        
        v.findViewById(R.id.btDataKreditorHome).setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), DataKreditorActivity.class);
            startActivity(i);
        });
        
        return v;
    }
}
