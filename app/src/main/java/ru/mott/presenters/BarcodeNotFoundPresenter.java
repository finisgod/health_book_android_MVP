package ru.mott.presenters;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class BarcodeNotFoundPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;


    public BarcodeNotFoundPresenter(ArchitrectureInterface.Repository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }
}