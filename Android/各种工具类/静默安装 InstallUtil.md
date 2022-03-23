## InstallUtil
```java
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.kitking.upgrade.receiver.InstallResultReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;

public class InstallUtil {

    private static final String TAG = "InstallUtil";

    private static int mSessionId = -1;

    private static PackageInstaller.SessionCallback mSessionCallback;

    public static void initSession(Context context) {
        mSessionCallback = new InstallSessionCallback();
        context.getPackageManager().getPackageInstaller().registerSessionCallback(mSessionCallback);
    }

    /**
     * 适配android9的安装方法。
     * 全部替换安装
     */
    public static void installApp(Context context, String apkFilePath) {
        Log.d(TAG, "installApp()------->" + apkFilePath);
//        apkFilePath = apkFilePath.replace("storage/emulated/0","sdcard");
        File apkFile = new File(apkFilePath);
        if (!apkFile.exists()) {
            Log.d(TAG, "文件不存在");
        }

        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo != null) {
            String packageName = packageInfo.packageName;
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            Log.d("ApkActivity", "packageName=" + packageName + ", versionCode=" + versionCode + ", versionName=" + versionName);
        }

        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        Log.d(TAG, "apkFile length" + apkFile.length());
        sessionParams.setSize(apkFile.length());

        try {
            mSessionId = packageInstaller.createSession(sessionParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "sessionId---->" + mSessionId);
        if (mSessionId != -1) {
            boolean copySuccess = onTransfesApkFile(context,apkFilePath);
            Log.d(TAG, "copySuccess---->" + copySuccess);
            if (copySuccess) {
                execInstallAPP(context);
            }
        }
    }

    /**
     * 通过文件流传输apk
     *
     * @param apkFilePath
     * @return
     */
    private static boolean onTransfesApkFile(Context context, String apkFilePath) {
        Log.d(TAG, "---------->onTransfesApkFile()<---------------------");
        InputStream in = null;
        OutputStream out = null;
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            File apkFile = new File(apkFilePath);
            session = context.getPackageManager().getPackageInstaller().openSession(mSessionId);
            out = session.openWrite("base.apk", 0, apkFile.length());
            in = new FileInputStream(apkFile);
            int total = 0, c;
            byte[] buffer = new byte[1024 * 1024];
            while ((c = in.read(buffer)) != -1) {
                total += c;
                out.write(buffer, 0, c);
            }
            session.fsync(out);
            Log.d(TAG, "streamed " + total + " bytes");
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
        /**
     * 执行安装并通知安装结果
     *
     */
    private static void execInstallAPP(Context context) {
        Log.d(TAG, "--------------------->execInstallAPP()<------------------");
        PackageInstaller.Session session = null;
        try {
            session = context.getPackageManager().getPackageInstaller().openSession(mSessionId);
            Intent intent = new Intent(context, InstallResultReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            session.commit(pendingIntent.getIntentSender());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static class InstallSessionCallback extends PackageInstaller.SessionCallback {
        String TAG="InstallSessionCallback";
        @Override
        public void onCreated(int sessionId) {
            // empty
            Log.d(TAG, "onCreated()" + sessionId);
        }

        @Override
        public void onBadgingChanged(int sessionId) {
            // empty
            Log.d(TAG, "onBadgingChanged()" + sessionId + "active");
        }

        @Override
        public void onActiveChanged(int sessionId, boolean active) {
            // empty
            Log.d(TAG, "onActiveChanged()" + sessionId + "active" + active);
        }

        @Override
        public void onProgressChanged(int sessionId, float progress) {
            Log.d(TAG, "onProgressChanged()" + sessionId);
            if (sessionId == -1) {
                int progres = (int) (Integer.MAX_VALUE * progress);
                Log.d(TAG, "onProgressChanged" + progres);
            }
        }

        @Override
        public void onFinished(int sessionId, boolean success) {
            // empty, finish is handled by InstallResultReceiver
            Log.d(TAG, "onFinished()" + sessionId + "success" + success);
            if (-1 == sessionId) {
                if (success) {
                    Log.d(TAG, "onFinished() 安装成功");
                } else {
                    Log.d(TAG, "onFinished() 安装失败");
                }

            }
        }
    }

}

```