package patel.jay.jaijalaram.Panel.Customer.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import patel.jay.jaijalaram.Adapter.MyIPagerAdapter;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Models.Items;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Handler handlerUp = new Handler();
    View view;

    ViewPager viewPagerAuto;//, viewPager;

    TabLayout tabLayout;
    MyIPagerAdapter adapter;
    int curPageUp = 0, pageUp = 0;
    Timer timerUp;

    ArrayList<Items> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_tablayout, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
//        viewPager = view.findViewById(R.id.viewpager);
        view.findViewById(R.id.viewpager).setVisibility(View.GONE);

        viewPagerAuto = view.findViewById(R.id.viewpagerUp);
        viewPagerAuto.setVisibility(View.VISIBLE);

        tabLayout.setBackgroundColor(getResources().getColor(R.color.trans));
//        tabLayout.setVisibility(View.GONE);

        items = Items.favItems(getActivity(), PrefConst.HOMEITEM);

        adapter = new MyIPagerAdapter(getActivity(), items);
        viewPagerAuto.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPagerAuto);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        pageUp = adapter.getCount();
        curPageUp = viewPagerAuto.getCurrentItem();

        startTransition();
    }

    Runnable vpUpScroll = new Runnable() {
        @Override
        public void run() {
            if (curPageUp == pageUp)
                curPageUp = 0;
            viewPagerAuto.setCurrentItem(curPageUp++, true);
        }
    };

    private void startTransition() {
        try {
            timerUp = new Timer();
            timerUp.schedule(new TimerTask() {
                @Override
                public void run() {
                    handlerUp.post(vpUpScroll);
                }
            }, 1000, 5000);
            //delay for first starting point and period for changeing slide

        } catch (Exception e) {
            toast(getActivity(), e.getMessage());
        }
    }

    private void stopTransition() {
        if (timerUp != null) {
            timerUp.cancel();
            timerUp = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTransition();
    }
}