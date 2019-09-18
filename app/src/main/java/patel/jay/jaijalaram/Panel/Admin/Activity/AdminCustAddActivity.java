package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.Login.SignUpFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.titleSet;

public class AdminCustAddActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tablayout);
        titleSet(this, "Add Customer");

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignUpFragment(), "Sign Up");

        viewPager.setAdapter(adapter);

    }
}
