package com.example.p7.pr_kredit_motor_fortuna_tis1e233605;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Finding buttons and setting click listeners using lambdas
        findViewById(R.id.btDataPetugasHome).setOnClickListener(v -> navigateToPetugas());
        findViewById(R.id.btDataMotorHome).setOnClickListener(v -> navigateToMotor());
        findViewById(R.id.btDataKreditorHome).setOnClickListener(v -> navigateToKreditor());
    }

    private void navigateToAbout() {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    private void navigateToPetugas() {
        // NOTE: Create DataPetugasActivity to fix the error below
        Intent i = new Intent(this, DataPetugasActivity.class);
        startActivity(i);
    }

    private void navigateToMotor() {
        // NOTE: Create DataMotorActivity to fix the error below
        Intent i = new Intent(this, DataMotorActivity.class);
        startActivity(i);
    }

    private void navigateToKreditor() {
        // NOTE: Create DataKreditorActivity to fix the error below
        Intent i = new Intent(this, DataKreditorActivity.class);
        startActivity(i);
    }

    public void KlikbtKeluar(View v) {
        finish();
    }
}
