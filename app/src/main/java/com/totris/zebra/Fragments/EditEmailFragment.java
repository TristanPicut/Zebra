package com.totris.zebra.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.totris.zebra.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditEmailFragment extends Fragment implements EditProfileItemFragment {

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    public EditEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    private boolean validate() {
        boolean isValid = emailEditText.getText().toString().matches(getString(R.string.pattern_email));

        if (!isValid) {
            emailEditText.setError(getString(R.string.error_invalid_email));
        }

        return isValid;
    }

    @Override
    public String getValue() {
        if (!validate()) return null;

        return emailEditText.getText().toString();
    }

    @Override
    public void setError(String error) {

    }
}
