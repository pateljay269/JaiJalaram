package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.ImageUpload;
import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.R;

import static android.content.Intent.createChooser;
import static patel.jay.jaijalaram.Constants.MyConst.clearEdittext;
import static patel.jay.jaijalaram.Constants.MyConst.etBlank;
import static patel.jay.jaijalaram.Constants.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Constants.MyConst.getPath;
import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;
import static patel.jay.jaijalaram.Models.Categorys.allCatStr;
import static patel.jay.jaijalaram.Models.Categorys.isCategoryAvail;
import static patel.jay.jaijalaram.Models.Items.isItemAvail;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerCall.OnAsyncResponse, ImageUpload.OnComplete {

    private static final int ITEM = 1001, CATEGORY = 1002,
            STORAGE_PERMISSION = 123, IMAGE_REQUEST = 1;

    Button btnImgAdd, btnAdd, btnClear;
    SimpleDraweeView sdvImage;
    Spinner spnAction, spnCat;
    EditText etName, etPrice;
    LinearLayout layout;
    View view;
    String folder = "", selected = "", name = "";
    File file;

    Activity activity = AddItemActivity.this;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        titleSet(this, "Add Category / Item");

        setContentView(R.layout.activity_add_item);

        sdvImage = findViewById(R.id.sdvImage);
        view = findViewById(R.id.scroll);

        layout = findViewById(R.id.layout);

        btnImgAdd = findViewById(R.id.btnImgAdd);
        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(this);
        btnImgAdd.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);

        spnAction = findViewById(R.id.spnAction);
        spnCat = findViewById(R.id.spnCat);

        spnAction.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        clearData();

        switch (adapterView.getId()) {
            case R.id.spnAction:
                switch (position) {
                    case 0:
                        titleSet(this, "Add Category / Item");
                        layout.setVisibility(View.GONE);
                        spnCat.setVisibility(View.GONE);
                        break;

                    case 1:
                        titleSet(this, "Add Category");
                        layout.setVisibility(View.VISIBLE);
                        spnCat.setVisibility(View.GONE);
                        etPrice.setVisibility(View.GONE);
                        folder = PrefConst.CATEGORY_S;
                        break;

                    case 2:
                        titleSet(this, "Add Item");
                        layout.setVisibility(View.VISIBLE);
                        spnCat.setVisibility(View.VISIBLE);
                        etPrice.setVisibility(View.VISIBLE);
                        folder = PrefConst.ITEM_S;

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.tv_spn, R.id.tvRow, allCatStr(activity));
                        spnCat.setAdapter(adapter);
                        break;
                }
                break;
        }

        refreshData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onClick(View view) {

        try {
            btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnback));

            switch (view.getId()) {
                case R.id.btnClear:
                    clearData();
                    break;

                case R.id.btnImgAdd:
                    refreshData();
                    requestPermission();
                    break;

                case R.id.btnAdd:
                    saveData();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
    }

    private void saveData() {
        if (!isNetAvail(activity, view)) {
            return;
        }

        selected = spnAction.getSelectedItem().toString();
        name = etName.getText().toString().trim();

        int pos = spnCat.getSelectedItemPosition();
        if (selected.equals(getResources().getString(R.string.select_items))) {
            if (pos == 0 || etBlank(etPrice)) {
                etBlankCheck(etPrice);

                if (pos == 0) {
                    ((TextView) findViewById(R.id.tvRow)).setError("Required");
                }
                return;
            }
        }

        if (etBlank(etName)) {
            etBlankCheck(etName);
            return;
        }

        if (file == null) {
            btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnerror));
        } else {

            switch (pos) {
                case 1:
                    if (isCategoryAvail(activity, name)) {
                        etName.setError("Name Already Stored");
                        return;
                    }
                    break;

                case 2:
                    if (isItemAvail(activity, name)) {
                        etName.setError("Name Already Stored");
                        return;
                    }
                    break;
            }

            new ImageUpload(activity, file, folder + "/" + name).upload();
        }
    }

    @Override
    public void getUploadPath(String imgPath, boolean isUpload) {
        if (isUpload) {

            HashMap<String, String> hm = new HashMap<>();
            url = ServerCall.BASE_URL + ServerCall.ITEMS;

            if (selected.equals(getResources().getString(R.string.select_cat))) {
                url += "insertCat";
                hm.put(MYSQL.Category.NAME, name);

            } else if (selected.equals(getResources().getString(R.string.select_items))) {
                url += "insertItem";
                hm.put(MYSQL.Items.NAME, name);
                hm.put(MYSQL.Category.NAME, spnCat.getSelectedItem().toString().trim());
                hm.put(MYSQL.Items.PRICE, etPrice.getText().toString().trim());
            }
            hm.put(MYSQL.IMG, imgPath);

            new ServerCall(activity, url, hm, MyConst.INSERT).execute();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case MyConst.INSERT:
                    if (response.trim().equals("1")) {
                        toast(activity, "Added");

                        refreshData();
                        clearData();
                    } else
                        toast(activity, "Name Already Define" + response);
                    break;

                case CATEGORY:
                    if (response.length() > 0) {
                        putIntoPref(activity, PrefConst.CATEGORY_S, response);
                    } else
                        toast(activity, "Error:" + response);
                    break;

                case ITEM:
                    if (response.length() > 0) {
                        putIntoPref(activity, PrefConst.ITEM_S, response);
                    } else
                        toast(activity, "Error:" + response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
    }

    private void clearData() {
        btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnback));
        clearEdittext(layout);
        folder = "";
        file = null;
        sdvImage.setImageURI("");
    }

    //region Request Permissions
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
            return;
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                finish();
                //Displaying another toast if permission is not granted
                Toast.makeText(activity, "Give Permission For Perform Task", Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion

    //region Image Storage
    private void selectImage() {
        Intent intent = new Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(createChooser(intent, getResources().getString(R.string.select_image)), IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data.getData() != null && data != null) {
            try {
                Uri strPath = data.getData();
                file = new File(getPath(activity, strPath));
                sdvImage.setImageURI(strPath);
            } catch (Exception e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    //endregion

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.getItem(0).setIcon(R.drawable.refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
//                logOut();
                refreshData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void refreshData() {
        if (isNetAvail(activity, view)) {
            url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
            new ServerCall(activity, url, null, CATEGORY).execute();

            url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
            new ServerCall(activity, url, null, ITEM).execute();

        }
    }
}