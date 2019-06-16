package com.snubbull.app.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.snubbull.app.R;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link OnPlaceUpdatedListener} interface to handle interaction events. Use the {@link
 * PlacesFragment#newInstance} factory method to create an instance of this fragment.
 */
public class PlacesFragment extends Fragment {

  private static final String PLACES_API_KEY = "AIzaSyC_qDihopcxKeboUHYHwnaf2LFuvn_hEYA";
  private OnPlaceUpdatedListener mListener;

  /**
   * Required empty public constructor
   */
  public PlacesFragment() {
  }

  /**
   * Use this factory method to create a new instance of this fragment, in order to be able to
   * attach it programmatically rather than through the xml layout files.
   *
   * @return A new instance of fragment PlacesFragment.
   */
  public static PlacesFragment newInstance() {
    PlacesFragment fragment = new PlacesFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_places, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Context context = view.getContext();
    Places.initialize(context, PLACES_API_KEY);
    PlacesClient placesClient = Places.createClient(context);
    AutocompleteSupportFragment fromInput = (AutocompleteSupportFragment)
        (getChildFragmentManager()).findFragmentById(R.id.placesFrom);

    AutocompleteSupportFragment toInput = (AutocompleteSupportFragment)
        getChildFragmentManager().findFragmentById(R.id.placesTo);

    fromInput.setPlaceFields(Arrays.asList(Field.NAME, Field.ADDRESS));
    toInput.setPlaceFields(Arrays.asList(Field.NAME, Field.ADDRESS));

    fromInput.setHint(getString(R.string.from));
    fromInput.a.setSingleLine(false);
    fromInput.a.setTextSize(14);
    fromInput.setOnPlaceSelectedListener(new PlaceSelectionListener() {

      @Override
      public void onPlaceSelected(@NonNull Place place) {

        Log.e("place", place.getName() + ", " + place.getAddress());
        onFromUpdated(place.getName() + ", " + place.getAddress());
      }

      @Override
      public void onError(@NonNull Status status) {

      }

    });

    toInput.setHint(getString(R.string.to));
    toInput.a.setTextSize(14);
    toInput.a.setSingleLine(false);
    toInput.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(@NonNull Place place) {
        Log.e("place", place.getName() + ", " + place.getAddress());
        onToUpdated(place.getName() + ", " + place.getAddress());
      }

      @Override
      public void onError(@NonNull Status status) {

      }
    });

  }

  public void onFromUpdated(String newFrom) {
    if (mListener != null) {
      mListener.onFromUpdated(newFrom);
    }
  }

  public void onToUpdated(String newTo) {
    if (mListener != null) {
      mListener.onToUpdated(newTo);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // instanceof just for error checking. Causes an explicit well defined error at program start
    // instead of a vague error in mid-program execution.
    if (context instanceof OnPlaceUpdatedListener) {
      mListener = (OnPlaceUpdatedListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement onPlacesUpdatedListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


}
