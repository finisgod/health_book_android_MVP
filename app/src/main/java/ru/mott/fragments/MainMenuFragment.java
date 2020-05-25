package ru.mott.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mott.data.CommonData;
import ru.mott.MainActivity;
import ru.mott.R;

public class MainMenuFragment extends Fragment {
    private Button button1;
    private Button button2;
    private Button button3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu, container, false);

        button1 = view.findViewById(R.id.scan_barcode);
        button1.setOnClickListener(view1 -> call_view(CommonData.barcodeScan));
        button2 = view.findViewById(R.id.massive_of_products);
        button2.setOnClickListener(view1 -> call_view(CommonData.download));
        button3 = view.findViewById(R.id.options);
        button3.setOnClickListener(view1 -> call_view(CommonData.options));

        return view;
    }

    private void call_view(int call) {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(call);
    }

    @Override
    public void onResume() {
        super.onResume();
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }
}
