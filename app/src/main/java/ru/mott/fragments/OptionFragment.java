package ru.mott.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mott.MainActivity;
import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.CommonData;
import ru.mott.presenters.OptionFragmentPresenter;

public class OptionFragment extends Fragment implements ArchitrectureInterface.View {
    private OptionFragmentPresenter presenter;
    private Button button1;
    private Button button2;

    @SuppressLint("ShowToast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.options, container, false);
        presenter = new OptionFragmentPresenter((AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());

        if (!presenter.isUserLog()) {
            Toast.makeText(getActivity(), "Please relogin", Toast.LENGTH_SHORT);
        }
        button1 = view.findViewById(R.id.sign_out);
        button2 = view.findViewById(R.id.verify_email);

        button1.setOnClickListener(v -> signOut());
        button2.setOnClickListener(v -> verifyEmail());

        return view;
    }

    private void signOut() {
        button1.setEnabled(false);
        button2.setEnabled(false);
        presenter.signOut();
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(CommonData.emailAndPass);
    }

    private void verifyEmail() {
        presenter.sendEmailVerification()
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),
                                "Verification email sent to " + presenter.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "Failed to send verification email, please restart HealthBook",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
