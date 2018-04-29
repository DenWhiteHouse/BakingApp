package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailsActivity;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static com.example.android.bakingapp.widget.WidgetIntent.INGRIDIENTLIST;

/**
 * Created by casab on 29/04/2018.
 */

public class WidgetProvider extends AppWidgetProvider {

    static ArrayList<String> ingredientsList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Use the XML file to build the remoteView for the Widger
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_view);

        //Getting the details of the Recipe to make the intent for the Widget
        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        //Those FLAGs help the Widget to open also the main activity without causing error on fragments stack
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_gridView, appPendingIntent);
        // Set the Widget adatper to show the view
        Intent intent = new Intent(context, WidgetAdapter.class);
        views.setRemoteAdapter(R.id.widget_gridView, intent);
        // UPDATE THE WIDGET
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgtes(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            ingredientsList = intent.getExtras().getStringArrayList(INGRIDIENTLIST);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridView);
            //In case of multiple widgtes
            WidgetProvider.updateWidgtes(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }
}
