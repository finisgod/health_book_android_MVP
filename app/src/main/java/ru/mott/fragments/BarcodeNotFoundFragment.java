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

import ru.mott.MainActivity;
import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.CommonData;
import ru.mott.presenters.BarcodeNotFoundPresenter;

public class BarcodeNotFoundFragment extends Fragment implements ArchitrectureInterface.View {
    private BarcodeNotFoundPresenter presenter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.not_found, container, false);
        presenter = new BarcodeNotFoundPresenter((AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());
        Button button1 = view.findViewById(R.id.add_new_product);
        button1.setOnClickListener(view1 -> call_view());
        return view;
    }

    private void call_view() {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(CommonData.addNew);
    }
}