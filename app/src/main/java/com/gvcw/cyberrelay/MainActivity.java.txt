package com.gvcw.cyberrelay;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etTargetPhone, etFirebaseUrl;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTargetPhone = findViewById(R.id.etTargetPhone);
        etFirebaseUrl = findViewById(R.id.etFirebaseUrl);
        btnSave = findViewById(R.id.btnSave);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 101);
        }

        SharedPreferences prefs = getSharedPreferences("GVCW_CONFIG", MODE_PRIVATE);
        etTargetPhone.setText(prefs.getString("target_phone", ""));
        etFirebaseUrl.setText(prefs.getString("firebase_url", "https://gvcw-otp-fecher-app-default-rtdb.firebaseio.com"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etTargetPhone.getText().toString().trim();
                String url = etFirebaseUrl.getText().toString().trim();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("target_phone", phone);
                editor.putString("firebase_url", url);
                editor.apply();

                Toast.makeText(MainActivity.this, "Settings Saved Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}