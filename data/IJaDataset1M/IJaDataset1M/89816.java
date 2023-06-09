package piramide.multimodal.downloader.client;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ApplicationDownloader {

    private static final String APK_FILENAME = "application.apk";

    private final Context context;

    private final String url;

    ApplicationDownloader(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    void download() throws DownloaderException {
        final HttpClient client = new DefaultHttpClient();
        try {
            final FileOutputStream fos = this.context.openFileOutput(APK_FILENAME, Context.MODE_WORLD_READABLE);
            final HttpResponse response = client.execute(new HttpGet(this.url));
            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 400) {
                throw new DownloaderException("Invalid status code downloading application: " + response.getStatusLine().getReasonPhrase());
            }
            downloadFile(response, fos);
            fos.close();
        } catch (ClientProtocolException e) {
            throw new DownloaderException(e);
        } catch (IOException e) {
            throw new DownloaderException(e);
        }
    }

    void install() {
        final Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + this.context.getFilesDir().getAbsolutePath() + "/" + APK_FILENAME), "application/vnd.android.package-archive");
        this.context.startActivity(intent);
    }

    private void downloadFile(HttpResponse response, OutputStream os) throws IOException {
        final InputStream is = response.getEntity().getContent();
        long size = response.getEntity().getContentLength();
        BufferedInputStream bis = new BufferedInputStream(is);
        final byte[] buffer = new byte[1024 * 1024];
        long position = 0;
        while (position < size) {
            final int read = bis.read(buffer, 0, buffer.length);
            if (read <= 0) break;
            os.write(buffer, 0, read);
            os.flush();
            position += read;
        }
        is.close();
    }
}
