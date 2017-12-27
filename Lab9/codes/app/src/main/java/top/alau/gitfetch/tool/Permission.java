package top.alau.gitfetch.tool;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by alcanderian on 2017/12/21.
 */

public class Permission {
    public static boolean verify(Activity activity, String request, Integer code) {
        int permission = ActivityCompat.checkSelfPermission(activity, request);
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{request}, code);
        else return true;
        return false;
    }
}
