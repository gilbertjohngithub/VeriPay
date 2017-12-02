package com.berstek.veripay.views.account_setup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berstek.veripay.R;
import com.berstek.veripay.callbacks.OnUserQueriedListener;
import com.berstek.veripay.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements OnUserQueriedListener {


    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }


    //allows to pass the data of the logged in user to this fragment
    @Override
    public void onUserQueried(User user) {

    }
}
