package com.berstek.veripay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.berstek.veripay.data_access.AccountBalanceDA;
import com.berstek.veripay.models.TransactionHistory;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.account_setup.LoginSignupFragment;
import com.berstek.veripay.views.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements LoginSignupFragment.AccountSetupListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

//        try {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        } catch (Exception e) {}

        if (auth.getCurrentUser() == null)
            //redirect to login page
            getSupportFragmentManager().beginTransaction().
                    add(R.id.main_container, new LoginSignupFragment()).commit();
        else {

//            TransactionHistory transactionHistory = new TransactionHistory();
//            transactionHistory.setAmount(20000.00);
//            transactionHistory.setType(TransactionHistory.CreditDebit.DEBIT);
//
//            AccountBalanceDA accountBalanceDA = new AccountBalanceDA();
//            accountBalanceDA.pushTransaction(transactionHistory, UserUtils.getUID());

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onLogin(String username, String password) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignUp(User user) {
        //TODO register user
    }

    //test commit
}

