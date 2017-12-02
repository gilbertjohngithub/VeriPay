package com.berstek.veripay.views.account_setup;


import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.berstek.veripay.R;

import com.berstek.veripay.callbacks.OnUserQueriedListener;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomAnimator;
import com.berstek.veripay.views.home.HomeActivity;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements OnUserQueriedListener,
        View.OnClickListener {

    private View view;
    private ImageView dp, logo;
    private TextView welcome;
    private Button continueBtn;
    private User user;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    private AnimatorSet welcomeAnimation;
    private ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        constraintLayout = (ConstraintLayout) view;
        logo = view.findViewById(R.id.logo);
        continueBtn = view.findViewById(R.id.continue_btn);
        dp = view.findViewById(R.id.dp);
        welcome = view.findViewById(R.id.txt_welcome);
        welcomeAnimation = CustomAnimator.animateAlpha(logo);

        continueBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onUserQueried(User user) {
        this.user = user;

        if(!user.isAccount_setup_finished())
            continueBtn.setText("SETUP ACCOUNT");

        Glide.with(getContext()).load(user.getPhoto_url()).skipMemoryCache(true).into(dp);
        welcome.setText("Welcome, " + user.getFirstname() + "!");

        TransitionManager.beginDelayedTransition(constraintLayout);
        ConstraintLayout.LayoutParams lparams = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        lparams.verticalBias = 0.05f;
        logo.setLayoutParams(lparams);

        dp.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);
        welcomeAnimation.removeAllListeners();
        welcomeAnimation.end();
        welcomeAnimation.cancel();
        continueBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

        if (user.isAccount_setup_finished()) {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), EditUserInfoActivity.class);
            startActivity(intent);
        }
    }
}
