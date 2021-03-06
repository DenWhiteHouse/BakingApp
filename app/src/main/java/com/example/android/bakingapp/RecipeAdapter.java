package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by casab on 27/04/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    final private ListItemClickListener lOnClickListener;
    ArrayList<Recipe> recipesList;
    Context mContext;

    public RecipeAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void setRecipe(ArrayList<Recipe> recipes, Context context) {
        recipesList = recipes;
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //making the viewHolder for MainActivity  with Recipe's item of the Fragment
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //setting the name got from the JSON by Retrofit
        holder.textRecyclerView.setText(recipesList.get(position).getName());
        //managin the imageView
        String imageUrl = recipesList.get(position).getImage();
        if(!imageUrl.isEmpty()){
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.recipeImage);
        } else
        {
            holder.recipeImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return recipesList != null ? recipesList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItemIndex);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;
        ImageView recipeImage;

        public RecyclerViewHolder(View itemView) {
            //Setting the TextView
            super(itemView);
            textRecyclerView = (TextView) itemView.findViewById(R.id.title);
            recipeImage=(ImageView) itemView.findViewById(R.id.recipeImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(recipesList.get(clickedPosition));
        }
    }
}
