package com.berstek.veripay.views.home;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berstek.veripay.R;
import com.berstek.veripay.views.contacts.ContactsFragment;
import com.berstek.veripay.views.history.HistoryFragment;
import com.berstek.veripay.views.transactions.TransactionsFragment;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewPagerFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    public HomeViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_view_pager, container, false);
        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewpager);

        fragments = new ArrayList<>();

        Fragment transactionsFragment = new TransactionsFragment();
        Fragment historyFragment = new HistoryFragment();
        Fragment contactsFragment = new ContactsFragment();

        fragments.add(transactionsFragment);
        fragments.add(historyFragment);
        fragments.add(contactsFragment);

        tabLayout.removeAllTabs();


        for(int i = 0; i < fragments.size(); i ++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setText("History");
        tabLayout.getTabAt(2).setText("Contacts");

//        tabLayout.setTabTextColors(Color.parseColor("#737373"),
//                Color.parseColor("#4caf50"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
