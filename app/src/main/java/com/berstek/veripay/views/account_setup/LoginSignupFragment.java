package com.berstek.veripay.views.account_setup;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.berstek.veripay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginSignupFragment extends Fragment {

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


        return view;
    }
}
