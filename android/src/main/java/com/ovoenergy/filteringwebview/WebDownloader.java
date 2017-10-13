package com.ovoenergy.filteringwebview;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.react.common.ReactConstants;

import java.io.File;

public class WebDownloader {
    private static final String FOLDER = Environment.DIRECTORY_DOWNLOADS;

    private final Context context;

    public WebDownloader(final Context context) {
        this.context = context.getApplicationContext();
    }

    public void download(final String url,
                         final String userAgent,
                         final String contentDisposition,
                         final String mimeType) {
        final Uri uri = Uri.parse(url);
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final String cookies = CookieManager.getInstance().getCookie(url);
        final String fileName = String.format("OVO_%s.%s", uri.getLastPathSegment(), MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType));

        FLog.d(ReactConstants.TAG, "Downloading File SRC URI: " + uri);
        FLog.d(ReactConstants.TAG, "Downloading Filename    : " + fileName);
        checkAndDeleteExistingFile(fileName);

        final DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(mimeType);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", userAgent);

        // UI STUFF
        request.setTitle(fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();

        Toast successToast = Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT);
        Toast errorToast = Toast.makeText(context, "Error downloading file", Toast.LENGTH_SHORT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            request.setDestinationInExternalFilesDir(context, FOLDER, fileName);
            dm.enqueue(request);
            successToast.show();

        } else {
            // this is mostly here as fallback for showing the error toast
            final File destinationFile = getDestinationFile(fileName);
            if (destinationFile != null) {
                FLog.d(ReactConstants.TAG, "Using file " + destinationFile.getAbsolutePath());

                // LOCAL LOCATION
                request.setDestinationUri(Uri.parse(destinationFile.toURI().toString()));
                dm.enqueue(request);
                successToast.show();
            } else {
                errorToast.show();
                FLog.w(ReactConstants.TAG, "No destination found for download");
            }
        }


    }


    @Nullable
    private File getDestinationFile(final String fileName) {

        final File[] downloadDirs = ContextCompat.getExternalFilesDirs(context, FOLDER);
        for (final File dir : downloadDirs) {

            if (dir != null && dir.exists()) {
                return new File(dir, fileName);
            }
        }
        // fallback
        return null;
    }

    private void checkAndDeleteExistingFile(final String fileName) {
        final File[] downloadDirs = ContextCompat.getExternalFilesDirs(context, FOLDER);
        if (downloadDirs != null) {
            for (final File dir : downloadDirs) {
                if (dir != null) {
                    if (dir.exists()) {
                        final File location = new File(dir, fileName);
                        if (location.exists()) {
                            FLog.d(ReactConstants.TAG, "Deleting " + location + ": " + location.delete());
                        }
                    } else {
                        dir.mkdirs();
                    }
                }
            }
        }
    }
}
