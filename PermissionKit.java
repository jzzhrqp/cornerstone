package com.cornerstone;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by xianjie.zhang on 2016/9/22.
 */

public class PermissionKit {
    public static boolean permissionCheck(Context context, String[] perms) {
        boolean b=true;
        if (perms!=null){
            for (String perm : perms) {
                boolean c=false;
                if (Build.VERSION.SDK_INT>=23){
                    c= context.checkSelfPermission(perm)==PERMISSION_GRANTED;
                }else {
                    c= true;
                }
                if (!c){
                    b=false;
                }
            }
        }

        return b;

    }

    public interface  ApplyPermissionListener{
        /**
         * 拒绝授权
         */
        void onNegative();
    }

    /**
     * 申请权限
     * @param activity 所属activity
     * @param prems 权限列表
     * @param reason 申请理由
     * @param requestCode 申请码
     * @param listener 监听拒绝时 {@link ApplyPermissionListener}
     */
    public static void applyPermission(@NonNull final Activity activity, @NonNull final String[] prems, @NonNull String reason, final @IntRange(from = 0) int requestCode, @Nullable final ApplyPermissionListener listener) {

        AlertDialog applyPermDialog = new AlertDialog.Builder(activity)
                .setTitle("权限申请")
                .setMessage(reason)
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener!=null){
                            listener.onNegative();
                        }
                    }
                }).setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(activity,
                                prems,requestCode);
                    }
                })
                .setCancelable(false).show();

    }


}
