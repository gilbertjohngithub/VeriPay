package com.berstek.veripay.views.user_profile;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;

import com.berstek.veripay.data_access.ContactDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Feedback;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomImageUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.feedback.FeedbacksAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView dp, dpBlurred, backImg;
    private TextView nameTxt, addressTxt, phoneTxt;
    private ImageView addContactBtn;

    private CustomImageUtils customImageUtils;

    private UserDA userDA;

    private RecyclerView recyclerView;
    private String key = "";

    private ContactDA contactDA;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        userDA = new UserDA();
        contactDA = new ContactDA();

        key = getIntent().getExtras().getString("key");

        customImageUtils = new CustomImageUtils();

        dp = findViewById(R.id.dp);
        dpBlurred = findViewById(R.id.dp_blurred);
        backImg = findViewById(R.id.back_img);
        nameTxt = findViewById(R.id.name_txt);
        addContactBtn = findViewById(R.id.add_contact_btn);
        addressTxt = findViewById(R.id.address_edit);
        phoneTxt = findViewById(R.id.phone_txt);

        BitmapDrawable img = (BitmapDrawable) dpBlurred.getDrawable();
        Bitmap bitmap = img.getBitmap();
        Bitmap blurred = customImageUtils.blurRenderScript(bitmap,
                15, ProfileActivity.this);
        dpBlurred.setImageBitmap(blurred);

        recyclerView = findViewById(R.id.recview_feedbacks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backImg.setOnClickListener(this);
        addContactBtn.setOnClickListener(this);

        loadUserData();
        loadFeedbacks();
    }

    private void loadUserData() {
        userDA.queryUserByUID(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user = child.getValue(User.class);
                    user.setKey(child.getKey());
                    nameTxt.setText(user.getFullName());
                    addressTxt.setText("  " + user.getAddress_city() + ", Philippines");
                    phoneTxt.setText("  " + user.getPhone_number());
                    Glide.with(ProfileActivity.this).
                            load(user.getPhoto_url()).
                            skipMemoryCache(true).into(dp);

                    new CustomImageUtils().blurImage(ProfileActivity.this, user.getPhoto_url(),
                            dpBlurred, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadFeedbacks() {
        //TODO fetch data from database
        ArrayList<Feedback> feedbacks = new ArrayList<>();
        Feedback feedback = new Feedback();

        for (int i = 0; i < 10; i++) {
            feedbacks.add(feedback);
        }

        FeedbacksAdapter adapter = new FeedbacksAdapter(this, feedbacks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.back_img)
            super.onBackPressed();
        else if (id == R.id.add_contact_btn) {
            contactDA.addContact(user.getKey(), UserUtils.getUID());
        }
    }
}
