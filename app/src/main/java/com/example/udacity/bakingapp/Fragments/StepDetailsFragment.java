package com.example.udacity.bakingapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.Models.Step;
import com.example.udacity.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //StepDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment {

    public static final String STEP_KEY = "currentStep";
    private static final String PLAYER_POSITION_KEY = "playerPosition";
    private static final String PLAY_READY_KEY = "playerReady";
    private static final String START_WINDOW = "startWindow";

    private Step step;
    public static SimpleExoPlayer exoPlayer;
    TrackSelector trackSelector;

    boolean isPlayerReady;////////*****
    long currentPosition;
    int startWindow;

    SimpleExoPlayerView simpleExoPlayerView;
    ImageView stepImage;
    TextView stepDescription;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance() {
        StepDetailsFragment fragment = new StepDetailsFragment();
        return fragment;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(STEP_KEY)) {
            step = getArguments().getParcelable(STEP_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        stepImage = view.findViewById(R.id.step_image);
        stepDescription = view.findViewById(R.id.step_description);
        simpleExoPlayerView = view.findViewById(R.id.step_exoplayer);

        stepDescription.setText(""+step.description);

        // If the video URL is not empty, set up the video player
        // Else, display the step thumbnail.
        // If the step doesn't have a video or thumbnail, display a default image
        if (!step.getVideoURL().equals("")) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);

            if (exoPlayer!=null){
//                isPlayerReady = false;/////////*************
                exoPlayer.stop();////to stop in portrait
            }
            ////////////////////****************
            if(savedInstanceState != null || getArguments()!=null){// && savedInstanceState.containsKey(step.getShortDescription()+"Position")) {
                if (savedInstanceState==null){
                    savedInstanceState=getArguments();
                }
              if (savedInstanceState.containsKey(step.getShortDescription()+PLAYER_POSITION_KEY)) {//////*******
                    currentPosition = savedInstanceState.getLong(step.getShortDescription()+PLAYER_POSITION_KEY);//of the current step
                    isPlayerReady = savedInstanceState.getBoolean(step.getShortDescription()+PLAY_READY_KEY);
                    startWindow = savedInstanceState.getInt(START_WINDOW);
              }
//                else {
//                    // initialize members with default values for a new instance
//                    currentPosition=0;
//                    isPlayerReady=true;
//                }

            }//
//            if(!isPlayerReady) {////////////***********
//                initializeExoPlayer(step.getVideoURL());
//            }


        } else if (!step.getThumbnailURL().equals("")) {

            Picasso.get()
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.cooking)
                    .into(stepImage);

            stepImage.setVisibility(View.VISIBLE);
        } else {
            stepImage.setVisibility(View.VISIBLE);
        }

        return view;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {///////**************
        super.onSaveInstanceState(outState);
        if(exoPlayer != null) {
            startWindow = exoPlayer.getCurrentWindowIndex();
            currentPosition = Math.max(0, exoPlayer.getCurrentPosition());
            isPlayerReady = exoPlayer.getPlayWhenReady();
            outState.putLong(step.getShortDescription()+PLAYER_POSITION_KEY, currentPosition);//or:Key=step.shortDescription
            outState.putBoolean(step.getShortDescription()+PLAY_READY_KEY, isPlayerReady);
            outState.putInt(START_WINDOW, startWindow);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
//            if(isPlayerReady) {
                initializeExoPlayer(step.getVideoURL());
//            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || exoPlayer == null) {
//            if(isPlayerReady) {
                initializeExoPlayer(step.getVideoURL());
//            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseExoplayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseExoplayer();
        }
    }


    private void initializeExoPlayer(String videoURL) {

        if (!videoURL.equals("")) {
            Bundle bundle =getArguments();
            if(bundle != null && bundle.containsKey(step.getShortDescription()+PLAYER_POSITION_KEY)) {
//            if(exoPlayer != null) {
                currentPosition = bundle.getLong(step.getShortDescription()+PLAYER_POSITION_KEY);//of the current step
                isPlayerReady = bundle.getBoolean(step.getShortDescription()+PLAY_READY_KEY);
                startWindow = bundle.getInt(START_WINDOW);
//            }

            }

            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
             trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                    trackSelector);
            // Bind the player to the view.
            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();
            simpleExoPlayerView.setPlayer(exoPlayer);


            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
//            // This is the MediaSource representing the media to be played.
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoURL));
////
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

            // onRestore////////////***************
//            if (currentPosition != 0) {
            exoPlayer.seekTo(startWindow,currentPosition);
//            }
        }
    }

    private void releaseExoplayer() {
        if(exoPlayer!=null) {
            startWindow = exoPlayer.getCurrentWindowIndex();
            currentPosition = Math.max(0, exoPlayer.getCurrentPosition());
            isPlayerReady = exoPlayer.getPlayWhenReady();

            Bundle bundle = getArguments();
            bundle.putLong(step.getShortDescription()+PLAYER_POSITION_KEY, currentPosition);//or:Key=step.shortDescription
            bundle.putBoolean(step.getShortDescription()+PLAY_READY_KEY, isPlayerReady);
            bundle.putInt(START_WINDOW, startWindow);

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer=null;
            trackSelector=null;

        }
    }



}
