package com.pointstudio.kgshmealdataapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

/**
 * Created by junsu on 2016-10-02.
 */

public class VersionProcess extends Thread {
    public Handler VersionHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    final AlertDialog.Builder updateDialog;
                    updateDialog = new AlertDialog.Builder(MealMainActivity.instance.getContext());
                    updateDialog.setTitle("업데이트");
                    updateDialog.setMessage("급식앱의 새로운 버전이 있습니다.\n보다 나은 사용을 위해 업데이트 해 주세요.");
                    updateDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);

                            marketLaunch.setData(Uri.parse("market://details?id=" + MealMainActivity.instance.getPackageName()));
                            MealMainActivity.instance.startActivity(marketLaunch);
                        }
                    });
                    updateDialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    updateDialog.show();
                }
                break;
            }
        }
    };

    @Override
    public void run() {
        // 패키지 네임 전달
        String storeVersion = MarketVersionChecker.getMarketVersion(MealMainActivity.instance.getPackageName());

        // 디바이스 버전 가져옴
        try {
            MealMainActivity.instance.deviceVersion = MealMainActivity.instance.getPackageManager().getPackageInfo(MealMainActivity.instance.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (storeVersion.compareTo(MealMainActivity.instance.deviceVersion) > 0) {
            VersionHandler.sendMessage(Message.obtain(VersionHandler, 0));
        }
    }
}