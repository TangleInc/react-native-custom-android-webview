package com.ovoenergy.filteringwebview;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

class CustomTabsHelper {

    private static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";

    private static final String STABLE_PACKAGE = "com.android.chrome";
    private static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";

    public void setToolbarColour(String toolbarColour) {
        this.toolbarColour = Color.parseColor(toolbarColour);
    }

    private Integer toolbarColour;


    public CustomTabsHelper() {
    }

    public CustomTabsHelper(@Nullable String toolbarColour) {

        if (toolbarColour != null) {
            this.toolbarColour = Color.parseColor(toolbarColour);
        }
    }

    private static String getPackageNameToUse(Context context) {
        String packageNameToUse = null;

        PackageManager pm = context.getPackageManager();

        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));

        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);

        String defaultViewHandlerPackageName = null;
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);

        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        if (packagesSupportingCustomTabs.isEmpty()) {
            packageNameToUse = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            packageNameToUse = packagesSupportingCustomTabs.get(0);
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            packageNameToUse = defaultViewHandlerPackageName;
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            packageNameToUse = STABLE_PACKAGE;
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            packageNameToUse = LOCAL_PACKAGE;
        }

        return packageNameToUse;
    }

    private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        try {
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);

            if (handlers == null || handlers.size() == 0) {
                return false;
            }

            for (ResolveInfo resolveInfo : handlers) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) continue;
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
                if (resolveInfo.activityInfo == null) continue;
                return true;
            }

        } catch (RuntimeException e) {
            Log.e("LOG", "Runtime exception while getting specialized handlers");
        }

        return false;
    }

    public void openTab(Context context, String url, Intent fallbackIntent) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // Chrome tab customisations
        if (toolbarColour != null) {
            builder.setToolbarColor(toolbarColour);
        }

        CustomTabsIntent customTabsIntent = builder.build();

        String packageName = getPackageNameToUse(context);

        if (packageName == null) {
            context.startActivity(fallbackIntent);
        } else {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.intent.setData(Uri.parse(url));
            context.startActivity(customTabsIntent.intent);
        }
    }
}