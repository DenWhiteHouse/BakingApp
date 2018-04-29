package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by casab on 29/04/2018.
 */

public class WidgetIntent extends IntentService {

    public static String INGRIDIENTLIST ="INGRIDIENTLIST";

    public WidgetIntent() {
        super("WidgetIntent");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, WidgetIntent.class);
        intent.putExtra(INGRIDIENTLIST,fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientsList = intent.getExtras().getStringArrayList(INGRIDIENTLIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientsList);

        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(INGRIDIENTLIST,fromActivityIngredientsList);
        sendBroadcast(intent);
    }
}
