package com.berstek.veripay.views.account_setup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.berstek.veripay.R;

import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.home.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveBtn;
    private EditText brgy, city, state, postal, phone;
    private UserDA userDA;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userDA = new UserDA();

        state = findViewById(R.id.state_edit);
        saveBtn = findViewById(R.id.save_btn);
        brgy = findViewById(R.id.title_edit);
        postal = findViewById(R.id.postal_code_edit);
        city = findViewById(R.id.city_edit);
        phone = findViewById(R.id.phone_edit);
        saveBtn.setOnClickListener(this);

        userDA.queryUserByUID(UserUtils.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user = child.getValue(User.class);
                    user.setKey(child.getKey());

                    if (user.getAddress_city() != null)
                        city.setText(user.getAddress_city());

                    if (user.getAddress_state() != null)
                        state.setText(user.getAddress_state());

                    if (user.getAddress_street_brgy() != null)
                        brgy.setText(user.getAddress_street_brgy());

                    if (user.getPhone_number() != null)
                        phone.setText(user.getPhone_number());

                    if (user.getPostal_code() != 0)
                        postal.setText(user.getPostal_code() + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        boolean noEmptyTextFields = !CustomUtils.editTextEmpty(city, state, brgy, postal, phone);

        if (noEmptyTextFields) {
            user.setAddress_city(city.getText().toString());
            user.setPostal_code(Integer.parseInt(postal.getText().toString()));
            user.setAddress_street_brgy(brgy.getText().toString());
            user.setAddress_state(state.getText().toString());
            user.setPhone_number(phone.getText().toString());
            user.setAccount_setup_finished(true);

            userDA.updateUser(user);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
