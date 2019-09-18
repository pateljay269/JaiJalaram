package patel.jay.jaijalaram.Constants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Screenshot {

    private static File setPath() {
        TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

        File sd = Environment.getExternalStorageDirectory();
        File directory = new File(sd.getAbsolutePath() + tc.getFile_Path());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static File take(Activity activity, View view, String name) {
        Bitmap b = getScreenShot(view);

        if (b != null)
            return store(b, name + ".jpg");
        else
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();

        return null;
    }

    public static void share(Activity activity, File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Share");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(intent, "Share..."));
    }

    private static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private static File store(Bitmap bm, String fileName) {
        File file = new File(setPath(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
