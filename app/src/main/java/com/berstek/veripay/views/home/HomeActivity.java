package com.berstek.veripay.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.MainActivity;
import com.berstek.veripay.R;
import com.berstek.veripay.data_access.TestDA;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.Contact;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomImageUtils;
import com.berstek.veripay.utils.UserUtils;
import com.berstek.veripay.views.account_setup.EditUserInfoActivity;
import com.berstek.veripay.views.search.SearchContactsAdapter;
import com.berstek.veripay.views.search.SearchResultsAdapter;
import com.berstek.veripay.views.search.SearchUserDialogFragment;
import com.berstek.veripay.views.user_profile.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        SearchContactsAdapter.OnContactSelectedListener,
        SearchResultsAdapter.OnResultSelectedListener {

    private ImageView searchBtn;
    private TextView appTitle, nameTxt, addressTxt, phoneTxt,
            payIDTxt, ratingTxt, verifiedTxt;
    private ImageView dpBlurred, dp;
    private ConstraintLayout navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new TestDA().writeToConsole1("HOME ACTIVITY");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Wire views

        navHeader = (ConstraintLayout) navigationView.getHeaderView(0);

        searchBtn = findViewById(R.id.search_btn);
        appTitle = findViewById(R.id.app_title);
        dpBlurred = navHeader.findViewById(R.id.dp_blurred);
        dp = navHeader.findViewById(R.id.dp);
        nameTxt = navHeader.findViewById(R.id.name_txt);
        addressTxt = navHeader.findViewById(R.id.address_edit);
        phoneTxt = navHeader.findViewById(R.id.phone_txt);
        payIDTxt = navHeader.findViewById(R.id.pay_id);
        ratingTxt = navHeader.findViewById(R.id.rating_txt);
        verifiedTxt = navHeader.findViewById(R.id.verified_txt);

        searchBtn.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().
                add(R.id.home_content, new HomeViewPagerFragment()).commit();

        loadUserInfo();
    }


    private void loadUserInfo() {
        UserDA userDA = new UserDA();

        Query query = userDA.queryUserByUID(UserUtils.getUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final User user = child.getValue(User.class);
                    nameTxt.setText(user.getFullName());
                    payIDTxt.setText("PAY ID: " + user.getPay_id());
                    addressTxt.setText(user.getAddress_city() + ", Philippines");
                    Glide.with(HomeActivity.this).
                            load(user.getPhoto_url()).skipMemoryCache(true).into(dp);

                    new CustomImageUtils().blurImage(HomeActivity.this,
                            user.getPhoto_url(), dpBlurred, false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, EditUserInfoActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.search_btn) {
            SearchUserDialogFragment fragment = new SearchUserDialogFragment();
            fragment.setContactSelectedListener(this);
            fragment.setResultSelectedListener(this);
            fragment.show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onContactSelected(Contact contact) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("key", contact.getKey());
        startActivity(intent);
    }

    @Override
    public void onResultSelected(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("key", user.getKey());
        startActivity(intent);
    }
}
