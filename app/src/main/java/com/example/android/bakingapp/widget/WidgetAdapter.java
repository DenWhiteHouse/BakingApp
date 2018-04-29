package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.List;

// Intent Statics declared for Widget
import static com.example.android.bakingapp.widget.WidgetProvider.ingredientsList;
import static com.example.android.bakingapp.widget.WidgetProvider.REMOTEVIEW_BUNDLE;
import static com.example.android.bakingapp.widget.WidgetProvider.REMOTEVIEW_INGREDIENT_LIST;


/**
 * Created by casab on 29/04/2018.
 */

public class WidgetAdapter extends RemoteViewsService {
    List<String> remoteViewingredientsList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(),intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        //For the Remote view built as GridView
        Context mContext = null;
        public GridRemoteViewsFactory(Context context,Intent intent) {
            mContext = context;

        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            remoteViewingredientsList = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewingredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            //Filling the remote view
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            views.setTextViewText(R.id.widget_textView, remoteViewingredientsList.get(position));
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_textView, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
