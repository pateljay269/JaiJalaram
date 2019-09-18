package patel.jay.jaijalaram.Panel.Admin.Reports;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.AttendFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.CustAvgFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.CustSaleFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.CustomRepoFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.DateWiseAttendFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.DateWiseFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.GrossRepoFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.MaxSaleFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.SaleByCustFragment;
import patel.jay.jaijalaram.Panel.Admin.Reports.Fragments.SpecificFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Constants.Screenshot.share;
import static patel.jay.jaijalaram.Constants.Screenshot.take;

public class ReportsNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Activity activity = ReportsNavActivity.this;
    ActionBarDrawerToggle toggle;
    NavigationView navView;
    DrawerLayout drawer;
    Toolbar toolbar;
    View view;
    private final int STORAGE_PERM = 1001;

    FragmentTransaction trans;
    String screenName = "";

    private static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_nav);

        setTitle("Reports");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(activity, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        trans = getSupportFragmentManager().beginTransaction();
        if (isFirst) {
            isFirst = false;
            GrossRepoFragment.isCal = true;
            trans.add(R.id.framelayout, new GrossRepoFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        trans = getSupportFragmentManager().beginTransaction();

        String title = "";

        switch (item.getItemId()) {
            case R.id.repo_gross:
                GrossRepoFragment.isCal = true;
                trans.replace(R.id.framelayout, new GrossRepoFragment()).commit();
                title = getString(R.string.periodic_reports);
                break;

            case R.id.repo_item_sale:
                GrossRepoFragment.isCal = false;
                trans.replace(R.id.framelayout, new GrossRepoFragment()).commit();
                title = getString(R.string.items_sale_reports);
                break;

            case R.id.repo_cust_sale:
                trans.replace(R.id.framelayout, new CustSaleFragment()).commit();
                title = getString(R.string.customer_sale_reports);
                break;

            case R.id.repo_cust_avg:
                trans.replace(R.id.framelayout, new CustAvgFragment()).commit();
                title = getString(R.string.customer_avg_reports);
                break;

            case R.id.repo_max_sale:
                trans.replace(R.id.framelayout, new MaxSaleFragment()).commit();
                title = getString(R.string.max_sale_by_customer);
                break;

            case R.id.repo_sale_by:
                trans.replace(R.id.framelayout, new SaleByCustFragment()).commit();
                title = getString(R.string.sale_by_customer);
                break;

            case R.id.repo_custom:
                trans.replace(R.id.framelayout, new CustomRepoFragment()).commit();
                title = getString(R.string.custom_reports);
                break;

            case R.id.repo_specific:
                trans.replace(R.id.framelayout, new SpecificFragment()).commit();
                title = getString(R.string.specific_search_reports);
                break;

            case R.id.repo_datewise:
                trans.replace(R.id.framelayout, new DateWiseFragment()).commit();
                title = getString(R.string.date_wise_reports);
                break;

            case R.id.repo_attend_datewise:
                trans.replace(R.id.framelayout, new AttendFragment()).commit();
                title = getString(R.string.attendance_report);
                break;

            case R.id.repo_attend_specific:
                trans.replace(R.id.framelayout, new DateWiseAttendFragment()).commit();
                title = getString(R.string.datewise_attendance);
                break;

            default:
                return false;
        }

        titleSet(this, title);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //region Action Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.screenshot, menu);
        return true;
    }

    File file = null;
    boolean isPermission = false;

    //region Request Permissions
    private void requestStore() {
        isPermission = false;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermission = true;
                } else {
                    view = findViewById(R.id.graphLayout);
                    isPermission = false;
                }
                break;
        }
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        requestStore();
        view = findViewById(R.id.graphLayout);

        if (!isPermission) {
            Snackbar.make(view, "Give Permission From Settings", Snackbar.LENGTH_LONG).show();
            return false;
        }

        try {
            Fragment cur = getSupportFragmentManager().findFragmentById(R.id.framelayout);
            String ss = cur.toString();
            ss = ss.substring(0, cur.toString().indexOf("{"));
            ss = ss.replace("Fragment", "").toLowerCase();

            switch (ss) {
                case "custsale":
                    screenName = CustSaleFragment.spnCal.getSelectedItem().toString();
                    break;

                case "custavg":
                    screenName = "Avg Cust Sale-" + CustAvgFragment.spnCal.getSelectedItem().toString();
                    if (screenName.equals(getResources().getString(R.string.month)))
                        screenName += "-" + CustAvgFragment.spnYear.getSelectedItem().toString();
                    break;

                case "grossrepo":
                    screenName = GrossRepoFragment.spnCal.getSelectedItem().toString();
                    break;

                case "maxsale":
                    screenName = "Max In-" + MaxSaleFragment.spnCal.getSelectedItem().toString();
                    screenName += "-" + MaxSaleFragment.spnCust.getSelectedItem().toString();
                    break;

                case "salebycust":
                    screenName = "Sale By-" + MaxSaleFragment.spnCal.getSelectedItem().toString();
                    screenName += "Of-" + MaxSaleFragment.spnCust.getSelectedItem().toString();
                    break;

                case "customrepo":
                    screenName = "Custom-" + CustomRepoFragment.spnCal.getSelectedItem().toString();
                    screenName += "Of-" + CustomRepoFragment.spnYear.getSelectedItem().toString();
                    break;

                case "specific":
                    screenName = "Custom-" + SpecificFragment.spnCal.getSelectedItem().toString();
                    screenName += "Of-" + SpecificFragment.spnYear.getSelectedItem().toString();
                    break;


                case "datewise":
                    screenName = "Datewise-" + DateWiseFragment.spnCal.getSelectedItem().toString();
                    break;

                case "attend":
                    screenName = "Attendance-" + AttendFragment.spnCal.getSelectedItem().toString();
                    screenName += "Of-" + SpecificFragment.spnYear.getSelectedItem().toString();
                    break;

                case "datewiseattend":
                    screenName = "Datewise Attendance:";
                    screenName += " " + DateWiseAttendFragment.btnDtStart.getText().toString();
                    screenName += " to " + DateWiseAttendFragment.btnDtEnd.getText().toString();
                    break;

                default:
                    screenName = "temp";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            screenName = "temp";
        }

        switch (item.getItemId()) {
            case R.id.action_save:
                file = take(activity, view, screenName);

                if (file != null)
                    toast(activity, "Save");
                break;

            case R.id.action_share:
                if (file == null)
                    file = take(activity, view, screenName);

                share(activity, file);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            isFirst = true;
            finish();
        }
    }
}
