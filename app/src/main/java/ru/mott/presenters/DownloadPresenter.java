package ru.mott.presenters;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class DownloadPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;
    private ArchitrectureInterface.View view;

    public DownloadPresenter(ArchitrectureInterface.View view, ArchitrectureInterface.Repository repository){
        this.repository = repository;
        this.view = view;
    }
    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }
    
    public String getBarcode() {
        return repository.getBarcode();
    }
}
