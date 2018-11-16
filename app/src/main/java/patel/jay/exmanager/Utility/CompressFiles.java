package patel.jay.exmanager.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static android.os.Environment.getExternalStorageDirectory;

public class CompressFiles extends AsyncTask<String, Integer, Boolean> {

    public static final String PATH = "Control_Money/MoneyManager/", FILE_PATH = PATH + "/tmp";
    private ProgressDialog pDialog = null;
    private zipComplete caller;
    private String fileName;
    private ArrayList<String> arFiles;

    public CompressFiles(Activity activity, String fileName, ArrayList<String> arFiles) {
        caller = (zipComplete) activity;
        this.fileName = fileName;
        this.arFiles = arFiles;

        pDialog = new ProgressDialog(activity);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgress(0);
    }

    protected Boolean doInBackground(String... urls) {
        File dir = new File(getExternalStorageDirectory().getAbsolutePath(), PATH);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return false;
            }
        }

        File file = new File(dir.getPath() + "/" + fileName);

        String zipFileName = file.getAbsolutePath();

        if (arFiles.size() > 0) {
            zip(zipFileName);
        }

        return true;
    }

    private void zip(String zipFilePath) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFilePath);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            final int BUFFER = 2048;
            byte data[] = new byte[BUFFER];

            for (int i = 0, j = 1; i < arFiles.size(); i++, j++) {
                publish(j);

                FileInputStream fi = new FileInputStream(arFiles.get(i));
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(arFiles.get(i).substring(arFiles.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.show();
    }

    private void publish(int completedProcess) {
        super.onPreExecute();

        publishProgress((100 * completedProcess) / arFiles.size());
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);

        pDialog.setProgress(progress[0]);
    }

    protected void onPostExecute(Boolean flag) {
        super.onPostExecute(flag);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.onZipComplete();
    }

    public interface zipComplete {
        void onZipComplete();
    }
}
