package com.example.loginaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private MaterialTextView userInfo;
    private Button logoutBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        userInfo = findViewById(R.id.userInfo);
        logoutBtn = findViewById(R.id.logoutBtn);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userInfo.setText("Bienvenido, " + user.getEmail());
        }

        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            finish();
        });
    }
}
