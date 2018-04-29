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

import static com.example.android.bakingapp.widget.WidgetIntent.INGRIDIENTLIST;

/**
 * Created by casab on 29/04/2018.
 */

public class WidgetProvider extends AppWidgetProvider {

    //Intenr Filter statics are faster if defined into the class
    public static String REMOTEVIEW_INGREDIENT_LIST="REMOTEVIEW_INGREDIENT_LIST";
    public static String REMOTEVIEW_BUNDLE="REMOTEVIEW_BUNDLE";

    static ArrayList<String> ingredientsList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Use the XML file to build the remoteView for the Widger
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_view);

        //Getting the details of the Recipe to make the intent for the Widget
        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_gridView, appPendingIntent);

        // Set the Widget adatper to show the view
        Intent intent = new Intent(context, WidgetAdapter.class);
        views.setRemoteAdapter(R.id.widget_gridView, intent);

        // UPDATE THE WIDGET
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        // for (int appWidgetId : appWidgetIds) {
        //updateAppWidget(context, appWidgetManager, ingredientsList, appWidgetId);
        // }
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            ingredientsList = intent.getExtras().getStringArrayList(INGRIDIENTLIST);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridView);
            //Now update all widgets
            WidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }


}
