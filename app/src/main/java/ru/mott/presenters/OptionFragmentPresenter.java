package ru.mott.presenters;

import com.google.android.gms.tasks.Task;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class OptionFragmentPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;

    public OptionFragmentPresenter(ArchitrectureInterface.Repository repository) {
        this.repository = repository;
    }

    public void signOut(){
        repository.signOut();
    }

    public Task<Void> sendEmailVerification() {
        return repository.getUser().sendEmailVerification();
    }

    public String getEmail(){
        return repository.getUser().getEmail();
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }
}
