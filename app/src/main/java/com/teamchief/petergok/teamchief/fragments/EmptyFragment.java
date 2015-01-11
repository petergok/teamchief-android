package com.teamchief.petergok.teamchief.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamchief.petergok.teamchief.R;

/**
 * Created by Peter on 2015-01-10.
 */
public class EmptyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_conversation, container, false);

        return rootView;
    }
}
