package ru.mott.presenters;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class MainActivityPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;

    public MainActivityPresenter(ArchitrectureInterface.Repository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }

    public String getBarcode() { return repository.getBarcode(); }

}
