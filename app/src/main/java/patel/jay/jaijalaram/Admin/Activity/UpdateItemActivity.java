package patel.jay.jaijalaram.Admin.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.ConstClass.ImageUpload;
import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.ModelClass.Categorys;
import patel.jay.jaijalaram.R;

import static android.content.Intent.createChooser;
import static patel.jay.jaijalaram.Adapter.CatAdAdapter.CATEGORY_ADMIN;
import static patel.jay.jaijalaram.Adapter.ItemAdAdapter.ITEM_ADMIN;
import static patel.jay.jaijalaram.ConstClass.MyConst.etBlank;
import static patel.jay.jaijalaram.ConstClass.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.ConstClass.MyConst.getPath;
import static patel.jay.jaijalaram.ModelClass.Categorys.allCatStr;
import static patel.jay.jaijalaram.ModelClass.Categorys.getCatName;

public class UpdateItemActivity extends AppCompatActivity implements View.OnClickListener,
        ServerCall.OnAsyncResponse, ImageUpload.OnComplete {

    private static final int IT = 1001, CAT = 1002,
            STORAGE_PERMISSION = 123, IMAGE_REQUEST = 1;
    SimpleDraweeView sdvImage;
    EditText etName, etPrice;
    Button btnImgAdd, btnAdd, btnClear;
    LinearLayout layout;
    Spinner spnCat;
    String folder = "";
    File file;
    private Activity activity = UpdateItemActivity.this;
    private HashMap<String, String> hm;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_add_item);
        try {
            sdvImage = findViewById(R.id.sdvImage);

            layout = findViewById(R.id.layout);

            etName = findViewById(R.id.etName);
            etPrice = findViewById(R.id.etPrice);

            spnCat = findViewById(R.id.spnCat);
            findViewById(R.id.spnAction).setVisibility(View.GONE);

            btnImgAdd = findViewById(R.id.btnImgAdd);
            btnAdd = findViewById(R.id.btnAdd);
            btnClear = findViewById(R.id.btnClear);

            btnAdd.setOnClickListener(this);
            btnImgAdd.setOnClickListener(this);
            btnClear.setOnClickListener(this);

            btnAdd.setText(getResources().getString(R.string.update));
            btnClear.setText(getResources().getString(R.string.delete));

            discardChange();
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    private void discardChange() {
        if (CATEGORY_ADMIN != null) {
            folder = PrefConst.CATEGORY_S;
            etName.setHint(MyConst.CATEGORY + " Name");
            spnCat.setVisibility(View.GONE);
            etPrice.setVisibility(View.GONE);

            etName.setText(CATEGORY_ADMIN.getName());

            sdvImage.setImageURI(Uri.parse(CATEGORY_ADMIN.getImgSrc()));

        } else if (ITEM_ADMIN != null) {

            folder = PrefConst.ITEM_S;
            etName.setHint(MyConst.ITEM + " Name");
            ArrayList<String> allCat = Categorys.allCatStr(activity);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.tv_spn, R.id.tvRow, allCat);
            spnCat.setAdapter(adapter);

            etName.setText(ITEM_ADMIN.getName());
            etPrice.setText(ITEM_ADMIN.getPrice() + "");

            sdvImage.setImageURI(Uri.parse(ITEM_ADMIN.getImgSrc()));

            spnCat.setSelection(allCat.indexOf(getCatName(activity, ITEM_ADMIN.getcId())));
        }
    }

    @Override
    public void onClick(View view) {
        try {
            btnImgAdd.setBackground(getResources().getDrawable(R.drawable.btnback));

            switch (view.getId()) {
                case R.id.btnClear:
                    openDialog();
                    break;

                case R.id.btnImgAdd:
                    //region Image Add
                    String url = ServerCall.BASE_URL + ServerCall.ITEMS;
                    if (CATEGORY_ADMIN != null) {
                        url += "allCat";
                        new ServerCall(activity, url, new HashMap<String, String>(), CAT)
                                .execute();
                    } else if (ITEM_ADMIN != null) {
                        url += "allItem";
                        new ServerCall(activity, url, new HashMap<String, String>(), IT)
                                .execute();
                    }
                    requestPermission();
                    //endregion
                    break;

                case R.id.btnAdd:
                    //region Image Upload
                    if (CATEGORY_ADMIN == null) {
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
                        if (CATEGORY_ADMIN != null) {
                            saveCat(CATEGORY_ADMIN.getImgSrc());
                        } else if (ITEM_ADMIN != null) {
                            saveItem(ITEM_ADMIN.getImgSrc());
                        }
                    } else {
                        String name = etName.getText().toString().trim();
                        new ImageUpload(activity, file, folder + "/" + name).upload();
                    }
                    //endregion
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    private void saveItem(String path) {
        url = ServerCall.BASE_URL + ServerCall.ITEMS;
        hm = new HashMap<>();
        url += "updateItem";
        hm.put(MYSQL.Items.IID, ITEM_ADMIN.getiId() + "");
        hm.put(MYSQL.Items.NAME, etName.getText().toString().trim());
        hm.put(MYSQL.Category.NAME, spnCat.getSelectedItem().toString().trim());
        hm.put(MYSQL.Items.PRICE, etPrice.getText().toString().trim());
        hm.put(MYSQL.IMG, path);
        new ServerCall(activity, url, hm, MyConst.INSERT).execute();
    }

    private void saveCat(String path) {
        url = ServerCall.BASE_URL + ServerCall.ITEMS;
        hm = new HashMap<>();
        url += "updateCat";
        hm.put(MYSQL.Category.CID, CATEGORY_ADMIN.getcId() + "");
        hm.put(MYSQL.Category.NAME, etName.getText().toString().trim());
        hm.put(MYSQL.IMG, path);
        new ServerCall(activity, url, hm, MyConst.INSERT).execute();
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

    public void openDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Delete Data..!!");
        dialog.setMessage("Are You Sure..!!");
        dialog.setCancelable(true);
        dialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                delete();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void delete() {
        if (CATEGORY_ADMIN != null) {

            url = ServerCall.BASE_URL + ServerCall.ITEMS;
            hm = new HashMap<>();
            url += "deleteCat";
            hm.put(MYSQL.Category.CID, CATEGORY_ADMIN.getcId() + "");
            new ServerCall(activity, url, hm, MyConst.DELETE).execute();

        } else if (ITEM_ADMIN != null) {

            url = ServerCall.BASE_URL + ServerCall.ITEMS;
            hm = new HashMap<>();
            url += "deleteItem";
            hm.put(MYSQL.Items.IID, ITEM_ADMIN.getiId() + "");
            new ServerCall(activity, url, hm, MyConst.DELETE).execute();
        }
    }

    private void deleteImage() {
        String path = "";
        if (CATEGORY_ADMIN != null) {
            path = PrefConst.CATEGORY_S + "/" + CATEGORY_ADMIN.getName() + ".jpg";
        } else if (ITEM_ADMIN != null) {
            path = PrefConst.ITEM_S + "/" + ITEM_ADMIN.getName() + ".jpg";
        }

        ImageUpload.delete(activity, path);
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case MyConst.INSERT:
                    if (response.trim().equals("1")) {
                        MyConst.toast(activity, "Updated");
                        finish();
                    } else
                        MyConst.toast(activity, "Name Already Define" + response);
                    break;

                case MyConst.DELETE:
                    if (response.trim().equals("1")) {
                        deleteImage();
                        finish();
                    } else
                        MyConst.toast(activity, "Error:" + response);
                    break;

                case CAT:
                    if (response.length() > 0) {
                        MyConst.putIntoPref(activity, PrefConst.CATEGORY_S, response);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.tv_spn, R.id.tvRow, allCatStr(activity));
                        spnCat.setAdapter(adapter);
                    } else
                        MyConst.toast(activity, "Error:" + response);
                    break;

                case IT:
                    if (response.length() > 0)
                        MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);
                    else
                        MyConst.toast(activity, "Error:" + response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    public void getUploadPath(String response, boolean isUpload) {
        if (isUpload) {
            if (CATEGORY_ADMIN != null) {
                saveCat(response);
            } else if (ITEM_ADMIN != null) {
                saveItem(response);
            }
        }
    }
}