package ru.mott.presenters;

import java.util.ArrayList;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class ProductFragmentPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;
    private ArchitrectureInterface.View view;

    public ProductFragmentPresenter(ArchitrectureInterface.View view, ArchitrectureInterface.Repository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }

    public ArrayList<String> getBannedComponents() {
        return repository.getBannedComponents();
    }

    public ArrayList<String> getProductComponents() {
        return repository.getProductComponents();
    }

    public void setBarcodeToNull(){
        repository.setBarcodeToNull();
    }
}
