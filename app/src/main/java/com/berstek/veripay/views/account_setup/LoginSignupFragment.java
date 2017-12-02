package com.berstek.veripay.views.account_setup;


import com.berstek.veripay.R;
import com.berstek.veripay.data_access.UserDA;
import com.berstek.veripay.models.User;
import com.berstek.veripay.utils.CustomUtils;
import com.berstek.veripay.utils.IntentMarker;
import com.google.android.gms.auth.api.Auth;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginSignupFragment extends Fragment implements View.OnClickListener,
        TextView.OnEditorActionListener, GoogleApiClient.OnConnectionFailedListener {

    private View view;
    private Button signupBtn, loginBtn;
    private ConstraintLayout signupForm, loginForm;
    private EditText username, loginPassword,
            email, signupPassword, signupPasswordConfirm;

    private ImageView googleLoginBtn, fbLoginBtn, logo;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CustomUtils.getGoogleAuthKey())
            .requestEmail()
            .build();

    private GoogleApiClient googleApiClient;
    private AccountSetupListener accountSetupListener;
    private UserDA userDA = new UserDA();

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

        loginPassword.setOnEditorActionListener(this);
        signupPasswordConfirm.setOnEditorActionListener(this);

        //init google login
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return view;
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
                googleLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                || actionId == EditorInfo.IME_ACTION_DONE) {

            if (textView.getId() == R.id.login_password)
                accountSetupListener.onLogin(username.getText().toString(), loginPassword.getText().toString());
            else {
                //TODO manual signup
            }

            return true;
        }
        return false;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        accountSetupListener = (AccountSetupListener) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentMarker.RC_GOGGLE_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private WelcomeFragment welcomeFragment;

    private void handleSignInResult(GoogleSignInResult result) {

        welcomeFragment = new WelcomeFragment();
        if (result.isSuccess()) {

            setSharedElementReturnTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_trans));
            setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.move));

            welcomeFragment.setSharedElementEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_trans));
            welcomeFragment.setEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.move));

            getFragmentManager().beginTransaction().
                    addSharedElement(logo, "logo_trans").
                    addToBackStack(null).
                    replace(R.id.main_container, welcomeFragment).
                    commit();

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);

            Log.wtf(null, "LOGGED IN");
        } else {
            // Signed out, show unauthenticated UI.
            Log.wtf(null, "LOGGED OUT");

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(null, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser firebaseUser = auth.getCurrentUser();
                            Log.wtf(null, firebaseUser.getPhotoUrl().toString());

                            //check if user exists, if false, add user info to database
                            userDA.queryUserByUID(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //means the user does not exist so create it
                                    if (dataSnapshot.getChildrenCount() == 0) {
                                        User user = new User();
                                        user.setFirstname(acct.getGivenName());
                                        user.setLastname(acct.getFamilyName());
                                        user.setDate_created(System.currentTimeMillis());
                                        user.setEmail(firebaseUser.getEmail());
                                        user.setPhoto_url(acct.getPhotoUrl().toString());
                                        //TOCO check for collision
                                        user.setPay_id(RandomStringUtils.randomAlphanumeric(8).toUpperCase());

                                        userDA.addUser(firebaseUser.getUid(), user);

                                        welcomeFragment.onUserQueried(user);
                                    } else {
                                        //user exists, just query data
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            User user = child.getValue(User.class);
                                            welcomeFragment.onUserQueried(user);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
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


    private void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, IntentMarker.RC_GOGGLE_SIGNIN);
    }

    private void fbLogin() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public interface AccountSetupListener {
        void onLogin(String username, String password);

        void onSignUp(User user);
    }

}
