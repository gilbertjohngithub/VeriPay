package com.berstek.veripay.views.account_setup;


import android.animation.Animator;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.berstek.veripay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginSignupFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button signupBtn, loginBtn;
    private ConstraintLayout signupForm, loginForm;
    private EditText username, loginPassword,
            email, signupPassword, signupPasswordConfirm;

    private ImageView googleLoginBtn, fbLoginBtn, logo;



    public LoginSignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login_signup, container, false);

        //Wire views
        signupBtn = view.findViewById(R.id.signup_btn);
        signupForm = view.findViewById(R.id.form_signup);
        loginForm = view.findViewById(R.id.form_login);
        loginBtn = view.findViewById(R.id.login_btn);
        username = view.findViewById(R.id.login_username);
        loginPassword = view.findViewById(R.id.login_password);
        email = view.findViewById(R.id.signup_email);
        signupPassword = view.findViewById(R.id.signup_password);
        signupPasswordConfirm = view.findViewById(R.id.signup_password_confirm);
        googleLoginBtn = view.findViewById(R.id.login_google);
        logo = view.findViewById(R.id.logo);

        signupBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        googleLoginBtn.setOnClickListener(this);


        return view;
    }

    private void rippleLogin() {
        signupForm.setVisibility(View.VISIBLE);
        loginForm.setVisibility(View.VISIBLE);
        loginForm.bringToFront();

        int cx = 0 + (signupBtn.getWidth() / 2);
        int cy = view.getHeight() - ((signupBtn.getHeight() / 2) + 16);
        int rad = Math.max(view.getWidth(), view.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(loginForm, cx, cy, 0, rad);
        animator.setDuration(300);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                signupForm.setVisibility(View.GONE);
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void rippleSignUp() {
        signupForm.setVisibility(View.INVISIBLE);
        signupForm.setVisibility(View.VISIBLE);
        signupForm.bringToFront();

        int cx = view.getWidth() - (loginBtn.getWidth() / 2);
        int cy = view.getHeight() - ((loginBtn.getHeight() / 2) + 16);
        int rad = Math.max(view.getWidth(), view.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(signupForm, cx, cy, 0, rad);
        animator.setDuration(300);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                loginForm.setVisibility(View.GONE);
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_btn:
                rippleSignUp();
                break;
            case R.id.login_btn:
                rippleLogin();
                break;
            case R.id.login_google:
                //TODO google login
                break;
            default:
                break;
        }
    }
}
