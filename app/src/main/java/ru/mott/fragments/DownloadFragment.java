package ru.mott.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ru.mott.MainActivity;
import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.CommonData;
import ru.mott.presenters.DownloadPresenter;

public class DownloadFragment extends Fragment implements ArchitrectureInterface.View {
    private DownloadPresenter downloadPresenter;

    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download, container, false);

        downloadPresenter = new DownloadPresenter(this, (AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());
        view.findViewById(R.id.pbDownload);
        view.findViewById(R.id.textToDownload);

        new Handler() {
            public void handleMessage(android.os.Message msg) {

            }
        };

        go();
        return view;
    }

    private void go() {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (downloadPresenter.getBarcode().isEmpty()) {
                ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(CommonData.chooseFrag);
            } else {
                ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(CommonData.productFragment);
            }
        });
        t.start();
    }
}
