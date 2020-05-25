package ru.mott.presenters;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class EmailAndPasswordPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;

    public EmailAndPasswordPresenter(ArchitrectureInterface.Repository repository) {
        this.repository = repository;
    }

    public Task<AuthResult> createAccount(String email, String password) {
        return repository.getAccount().createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInAccount(String email, String password) {
        return repository.getAccount().signInWithEmailAndPassword(email, password);
    }

    public Task<Void> sendPaswordReset(String email) {
        return repository.getAccount().sendPasswordResetEmail(email);
    }

    public void setUser(FirebaseUser user) {
        repository.setUser(user);
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }
}

