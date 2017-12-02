package com.berstek.veripay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.berstek.veripay.views.account_setup.LoginSignupFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //enable offline mode and syn when online
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {}

        if (auth.getCurrentUser() == null)
            //redirect to login page
            getSupportFragmentManager().beginTransaction().
                    add(R.id.main_container, new LoginSignupFragment()).commit();
        else {
            //TODO display welcome screen
        }
    }
}
