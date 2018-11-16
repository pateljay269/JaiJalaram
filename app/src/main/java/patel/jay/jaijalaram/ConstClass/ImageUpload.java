package patel.jay.jaijalaram.ConstClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Jay on 23-Feb-18.
 */

public class ImageUpload {

    private ProgressDialog pDialog;
    private OnComplete caller;
    private Activity activity;
    private String fileName = "";
    private File imgFile = null;

    public ImageUpload(Activity activity, File imgFile, String fileName) {
        caller = (OnComplete) activity;
        this.activity = activity;
        this.imgFile = imgFile;
        this.fileName = fileName;
    }

    public ImageUpload(Fragment fragment, Activity activity, File imgFile, String fileName) {
        caller = (OnComplete) fragment;
        this.activity = activity;
        this.fileName = fileName;
        this.imgFile = imgFile;
    }

    public static void delete(Activity activity, String fileName) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Deleting Image...");
        pDialog.setCancelable(false);
        pDialog.show();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mStorageRef.child(fileName);

        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.cancel();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.cancel();
                }
            }
        });
    }

    public void upload() {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Uploading Image...");
        pDialog.setCancelable(false);
        pDialog.show();

        Uri uri = Uri.fromFile(imgFile);
        StorageReference mImageStore = mStorageRef.child(fileName + ".jpg");

        mImageStore.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String filePath = taskSnapshot.getDownloadUrl().toString();
                        caller.getUploadPath(filePath, true);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.cancel();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        MyConst.toast(activity, "Image Upload Fail..!!");
                        caller.getUploadPath("", false);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.cancel();
                        }
                    }
                });
    }

    public interface OnComplete {
        void getUploadPath(String response, boolean isUpload);
    }
}