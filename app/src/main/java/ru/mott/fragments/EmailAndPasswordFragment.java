package ru.mott.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import ru.mott.presenters.EmailAndPasswordPresenter;

public class EmailAndPasswordFragment extends Fragment implements ArchitrectureInterface.View {
    private EmailAndPasswordPresenter presenter;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button button1;
    private Button button2;
    private Button button3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.email_and_password, container, false);
        presenter = new EmailAndPasswordPresenter((AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());

        mEmailField = view.findViewById(R.id.fieldEmail);
        mPasswordField = view.findViewById(R.id.fieldPassword);

        button1 = view.findViewById(R.id.emailSignInButton);
        button1.setOnClickListener(view1 -> signIn());

        button2 = view.findViewById(R.id.emailCreateAccountButton);
        button2.setOnClickListener(view1 -> createAccount());

        button3 = view.findViewById(R.id.emailForgotBottom);
        button3.setOnClickListener(view3 -> forgotPassword());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter.isUserLog()) {
            call_view();
        }
    }

    private boolean validateForm() {

        if (validateEmail() && validatePassword()) {
            changeButtonEnable(false);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required password more than 6 symbols");
            return false;
        } else {
            mPasswordField.setError(null);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            return false;
        } else {
            mEmailField.setError(null);
        }
        return true;
    }

    private void signIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (validateForm()) {
            return;
        }

        presenter.signInAccount(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        presenter.setUser(Objects.requireNonNull(task.getResult()).getUser());
                        if (presenter.isUserLog()) {
                            call_view();
                            Toast.makeText(getActivity(), "Login successful.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        changeButtonEnable(true);
                    }
                });
    }

    private void createAccount() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (validateForm()) {
            return;
        }

        presenter.createAccount(email, password).addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Registration completed.",
                        Toast.LENGTH_SHORT).show();
                presenter.setUser(Objects.requireNonNull(task.getResult()).getUser());
                if (presenter.isUserLog()) {
                    call_view();
                }
            } else {
                if (password.length() < 6) {
                    Toast.makeText(getActivity(), "Password must contain more than 6 symbols",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Registration failed",
                            Toast.LENGTH_SHORT).show();
                }
                changeButtonEnable(true);
            }
        });
    }

    private void forgotPassword() {
        if(!validateEmail()){
            return;
        }
        changeButtonEnable(false);
        String email = mEmailField.getText().toString();
        presenter.sendPaswordReset(email)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Letter was sent to your mail.",
                                Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Password reset was failed",
                                Toast.LENGTH_SHORT).show();
                    changeButtonEnable(true);
                });
    }

    private void changeButtonEnable(boolean bool){
        button1.setEnabled(bool);
        button2.setEnabled(bool);
        button3.setEnabled(bool);
    }

    private void call_view() {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemSelected(CommonData.mainMenu);
    }

}
