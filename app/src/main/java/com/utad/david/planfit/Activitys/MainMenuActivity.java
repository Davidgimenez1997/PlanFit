package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button button = findViewById(R.id.cerrarsesion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
                Intent I=new Intent(MainMenuActivity.this,FirstActivity.class);
                startActivity(I);
            }
        });
    }

}
