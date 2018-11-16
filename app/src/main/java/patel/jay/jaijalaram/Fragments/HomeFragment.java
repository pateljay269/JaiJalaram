package patel.jay.jaijalaram.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    final Handler handler = new Handler();
    View view;
    ViewPager viewPager, viewPager2;
    TabLayout tabLayout;
    MyPagerAdapter adapter;
    int currentPage = 0, NUM_PAGES = 0;
    Timer timer;
    Runnable vpScroll = new Runnable() {
        @Override
        public void run() {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager2.setCurrentItem(currentPage++, true);
        }
    };


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_tablayout, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager2 = (ViewPager) view.findViewById(R.id.viewpager2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
        viewPager2.setVisibility(View.VISIBLE);

        int i = 0;
        String[] urls = new String[]{"https://i.ytimg.com/vi/kKQ6OeUfcEQ/maxresdefault.jpg",
                "http://www.technobuffalo.com/wp-content/uploads/2014/04/fast-food.jpg",
                "https://www.bodypower.com/assets/uploads/images/800x320_861358e27e38452fde5ce47ea3da1d95.jpg"};

        adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ImageTransFragment(urls[i++]), "" + i);
        adapter.addFragment(new ImageTransFragment(urls[i++]), "" + i);
        adapter.addFragment(new ImageTransFragment(urls[i++]), "" + i);
        viewPager2.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        NUM_PAGES = adapter.getCount();
        currentPage = viewPager2.getCurrentItem();

        startTransition();
    }

    private void startTransition() {
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(vpScroll);
                }
            }, 1000, 2000);
            //delay for first starting point and period for changeing slide

        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }

    private void stopTransition() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTransition();
    }

}