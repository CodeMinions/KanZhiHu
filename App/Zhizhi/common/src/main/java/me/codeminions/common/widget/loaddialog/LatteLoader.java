package me.codeminions.common.widget.loaddialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import me.codeminions.common.R;

public class LatteLoader {
    private static final int LOADER_SIZE_SCALE = 8;
    private static final int LOADER_OFFSET_SCALE = 10;

    // icon样式
    private static final String DEFAULT_LOADER = "BallClipRotatePulseIndicator";

    // 所有的loading放入一个list进行维护
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();

    /**
     * @param context 初始化Dialog
     * @param type Icon
     */
    private static void showLoading(Context context, String type) {
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.LoadDialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);

        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();

        int deviceWidth = dm.widthPixels;
        int deviceHeight = dm.heightPixels;

        // 对显示的dialog界面进行处理 控制高宽 和大小
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = deviceWidth / LOADER_SIZE_SCALE;
            layoutParams.height = deviceHeight / LOADER_SIZE_SCALE;
            layoutParams.height = layoutParams.height + deviceHeight / LOADER_OFFSET_SCALE;
            layoutParams.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    //显示默认图标
    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    //停止显示loading
    public static void stopLoader() {
        for (Dialog dialog : LOADERS) {
            if (dialog != null) {
                dialog.dismiss();
                dialog.cancel();
            }
        }
    }
}
