package me.codeminions.common.widget.loaddialog;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

class LoaderCreator {
    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView create(String type, Context context) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (!LOADING_MAP.containsKey(type)) {
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        if (!name.contains(".")) {
            final String packageName = AVLoadingIndicatorView.class.getPackage().getName();
            sb.append(packageName)
                    .append(".indicators")
                    .append(".");
        }
        sb.append(name);
        try {
            Class<?> avLoadingIndicatorViewClass = Class.forName(sb.toString());
            return (Indicator) avLoadingIndicatorViewClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

}