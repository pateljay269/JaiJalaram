package patel.jay.jaijalaram.Admin.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import patel.jay.jaijalaram.Adapter.CatAdAdapter;
import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.Admin.Fragments.CategoryFragment;
import patel.jay.jaijalaram.Admin.Fragments.ItemsFragment;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.R;

public class CatItemActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_tablayout);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategoryFragment(), PrefConst.CATEGORY_S);
        adapter.addFragment(new ItemsFragment(), PrefConst.ITEM_S);

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CatAdAdapter.CATEGORY_ADMIN = null;
        ItemAdAdapter.ITEM_ADMIN = null;
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_home:
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

}