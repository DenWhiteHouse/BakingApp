package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailsActivity;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import static com.example.android.bakingapp.MainActivity.INDEX;
import static com.example.android.bakingapp.MainActivity.RECIPE;
import static com.example.android.bakingapp.MainActivity.STEP;
import static java.lang.System.in;

/**
 * Created by casab on 27/04/2018.
 */

public class StepsFragment extends Fragment {

    String SELECTED_POSITION = "playBackPosition";
    String PLAYWHENREADY = "playWhenReady";
    private boolean playWhenReady;
    private int currentWindow;
    private long playBackPosition;

    ArrayList<Recipe> recipe;
    String recipeName;
    private Uri mediaUri;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private BandwidthMeter meter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int index;
    private android.os.Handler handler;
    private ListItemClickListener itemClickListener;

    public StepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView;
        handler = new Handler();
        meter = new DefaultBandwidthMeter();
        itemClickListener = (RecipeDetailsActivity) getActivity();
        recipe = new ArrayList<>();
        View view = inflater.inflate(R.layout.recipe_steps_fragment_body, container, false);

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP);
            index = savedInstanceState.getInt(INDEX);
            recipeName = savedInstanceState.getString("Title");
            playBackPosition = savedInstanceState.getLong(SELECTED_POSITION);
            playWhenReady=savedInstanceState.getBoolean(PLAYWHENREADY);
        } else {
            steps = getArguments().getParcelableArrayList(STEP);
            if (steps != null) {
                steps = getArguments().getParcelableArrayList(STEP);
                index = getArguments().getInt(INDEX);
                recipeName = getArguments().getString("Title");
            } else {
                recipe = getArguments().getParcelableArrayList(RECIPE);
                steps = (ArrayList<Step>) recipe.get(0).getSteps();
                index = 0;
            }
        }
        //Set the Fragment layout
        textView = (TextView) view.findViewById(R.id.recipeStepText);
        textView.setText(steps.get(index).getDescription());
        textView.setVisibility(View.VISIBLE);
        //Bind the Playerview
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        // taking the JSON's paths
        String videoURL = steps.get(index).getVideoURL();
        String imageUrl = steps.get(index).getThumbnailURL();
        ImageView thumbImage = (ImageView) view.findViewById(R.id.thumbImage);

        //Managin the Images
        if (!imageUrl.isEmpty()) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }
        else{
            thumbImage.setVisibility(View.GONE);
        }

        //Managin the videos

        if (view.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            recipeName = ((RecipeDetailsActivity) getActivity()).recipeName;
            ((RecipeDetailsActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }


        if (!videoURL.isEmpty()) {
            mediaUri = Uri.parse(steps.get(index).getVideoURL());
            initializePlayer(mediaUri);
            if (view.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                //getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                //make the video take the whoile card
                textView.setVisibility(View.GONE);
            }
        } else {
            player = null;
            // make invisbile the Player if there isn't any content to show
            simpleExoPlayerView.setVisibility(View.GONE);
        }

        //Binding the botton to move towards the steps
        Button mPrevStep = (Button) view.findViewById(R.id.previousStep);
        Button mNextstep = (Button) view.findViewById(R.id.nextStep);

        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (steps.get(index).getId() > 0) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, steps.get(index).getId() - 1, recipeName);
                } else {
                    Toast.makeText(getActivity(), "This is the first step", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int lastIndex = steps.size() - 1;
                if (steps.get(index).getId() < steps.get(lastIndex).getId()) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, steps.get(index).getId() + 1, recipeName);
                } else {
                    Toast.makeText(getContext(), "This was the last step of the recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //Rubric asks to use ExoPlayer, below the needed code to be using it
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(meter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(playWhenReady);
        }
        if(playBackPosition != 0){
            // In case of rotation continue from the stored playBackPosition
            player.seekTo(currentWindow, playBackPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(STEP, steps);
        currentState.putInt(INDEX, index);
        currentState.putString("Title", recipeName);
        currentState.putLong(SELECTED_POSITION, playBackPosition);
        currentState.putBoolean(PLAYWHENREADY, playWhenReady);;
    }

    public boolean isInLandscapeMode(Context context) {
        //metod to check if the device is in landscape as the rubric asks to manage the view differenlty
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(mediaUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(mediaUri);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        playBackPosition = player.getCurrentPosition();
        playWhenReady = player.getPlayWhenReady();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playBackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(List<Step> allSteps, int Index, String recipeName);
    }
}
