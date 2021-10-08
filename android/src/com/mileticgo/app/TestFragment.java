package com.mileticgo.app;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        TextView textView = view.findViewById(R.id.message);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AndroidApplication)TestFragment.this.getActivity().getApplication()).callGame();
                User user = Repository.get().getUser();
                Log.d("LOG", user.toString());
            }
        });
        return view;
    }

}