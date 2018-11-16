package patel.jay.jaijalaram.Admin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import patel.jay.jaijalaram.ConstClass.ImageUpload;
import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.ModelClass.Categorys;
import patel.jay.jaijalaram.ModelClass.Items;
import patel.jay.jaijalaram.R;

import static android.content.Intent.createChooser;
import static patel.jay.jaijalaram.ConstClass.MyConst.clearEdittext;
import static patel.jay.jaijalaram.ConstClass.MyConst.etBlank;
import static patel.jay.jaijalaram.ConstClass.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.ConstClass.MyConst.getPath;
import static patel.jay.jaijalaram.ModelClass.Categorys.allCatStr;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerCall.OnAsyncResponse, ImageUpload.OnComplete {

    private static final int ITEM = 1001, CATEGORY = 1002,
            STORAGE_PERMISSION = 123, IMAGE_REQUEST = 1;
    Button btnImgAdd, btnAdd, btnClear;
    Spinner spnAction, spnCat;
    EditText etName, etPrice;
    LinearLayout layout;
    SimpleDraweeView sdvImage;
    Activity activity = AddItemActivity.this;
    String folder = "";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_add_item);

        sdvImage = findViewById(R.id.sdvImage);

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
                        layout.setVisibility(View.GONE);
                        spnCat.setVisibility(View.GONE);
                        break;

                    case 1:
                        layout.setVisibility(View.VISIBLE);
                        spnCat.setVisibility(View.GONE);
                        etPrice.setVisibility(View.GONE);
                        folder = PrefConst.CATEGORY_S;
                        break;

                    case 2:
                        layout.setVisibility(View.VISIBLE);
                        spnCat.setVisibility(View.VISIBLE);
                        etPrice.setVisibility(View.VISIBLE);
                        folder = PrefConst.ITEM_S;

                        String url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
                        ServerCall asyncTask = new ServerCall(activity, url, new HashMap<String, String>(), CATEGORY);
                        asyncTask.execute();
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onClick(View view) {
        try {
            btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnback));
            String url = "";
            switch (view.getId()) {
                case R.id.btnClear:
                    clearData();
                    break;

                case R.id.btnImgAdd:
                    url = ServerCall.BASE_URL + ServerCall.ITEMS;
                    switch (spnAction.getSelectedItemPosition()) {
                        case 1:
                            url += "allCat";
                            new ServerCall(activity, url, new HashMap<String, String>(), CATEGORY)
                                    .execute();
                            break;

                        case 2:
                            url += "allItem";
                            new ServerCall(activity, url, new HashMap<String, String>(), ITEM)
                                    .execute();
                            break;
                    }
                    requestPermission();
                    break;

                case R.id.btnAdd:
                    saveData();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    private void saveData() {
        String selected = spnAction.getSelectedItem().toString();
        if (selected.equals(getResources().getString(R.string.select_items))) {
            int pos = spnCat.getSelectedItemPosition();
            if (pos == 0 || etBlank(etPrice)) {

                etBlankCheck(etPrice);
                etBlankCheck(etName);

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

            switch (spnAction.getSelectedItemPosition()) {
                case 1:
                    if (Categorys.isCategoryAvail(activity, etName.getText().toString())) {
                        etName.setError("Name Already Stored");
                        return;
                    }
                    break;

                case 2:
                    if (Items.isItemAvail(activity, etName.getText().toString())) {
                        etName.setError("Name Already Stored");
                        return;
                    }
                    break;
            }
            String name = etName.getText().toString().trim();
            new ImageUpload(activity, file, folder + "/" + name).upload();
        }
    }

    @Override
    public void getUploadPath(String response, boolean isUpload) {
        if (isUpload) {
            String selected = spnAction.getSelectedItem().toString();
            HashMap<String, String> hm = new HashMap<>();
            String url = ServerCall.BASE_URL + ServerCall.ITEMS;

            if (selected.equals(getResources().getString(R.string.select_cat))) {
                url += "insertCat";
                hm.put(MYSQL.Category.NAME, etName.getText().toString().trim());

            } else if (selected.equals(getResources().getString(R.string.select_items))) {
                url += "insertItem";
                hm.put(MYSQL.Items.NAME, etName.getText().toString().trim());
                hm.put(MYSQL.Category.NAME, spnCat.getSelectedItem().toString().trim());
                hm.put(MYSQL.Items.PRICE, etPrice.getText().toString().trim());
            }
            hm.put(MYSQL.IMG, response);

            new ServerCall(activity, url, hm, MyConst.INSERT).execute();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case MyConst.INSERT:
                    if (response.trim().equals("1")) {
                        MyConst.toast(activity, "Added");
                        clearData();
                    } else
                        MyConst.toast(activity, "Name Already Define" + response);
                    break;

                case CATEGORY:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.CATEGORY_S, response);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.tv_spn, R.id.tvRow, allCatStr(activity));
                        spnCat.setAdapter(adapter);
                    } else
                        MyConst.toast(activity, "Error:" + response);
                    break;

                case ITEM:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);
                    } else
                        MyConst.toast(activity, "Error:" + response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    private void clearData() {
        btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnback));
        clearEdittext(layout);
        folder = "";
        file = null;
        sdvImage.setImageURI(Uri.parse(""));
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
}