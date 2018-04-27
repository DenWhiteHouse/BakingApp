package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import java.util.List;

/**
 * Created by casab on 27/04/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.RecyclerViewHolder> {

    final private ListItemClickListener lOnClickListener;
    List<Step> stepList;
    private String recipeName;

    public DetailsAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void setRecipe(List<Recipe> recipes, Context context) {
        stepList = recipes.get(0).getSteps();
        recipeName = recipes.get(0).getName();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.details_card;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(stepList.get(position).getId() + ". " + stepList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {

        return stepList != null ? stepList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = (TextView) itemView.findViewById(R.id.shortDescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(stepList, clickedPosition, recipeName);
        }

    }
}
