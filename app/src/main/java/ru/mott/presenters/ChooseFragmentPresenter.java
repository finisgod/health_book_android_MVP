package ru.mott.presenters;

import java.util.ArrayList;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class ChooseFragmentPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;
    private ArchitrectureInterface.View view;

    public ChooseFragmentPresenter(ArchitrectureInterface.View view, ArchitrectureInterface.Repository repository){
        this.repository = repository;
        this.view = view;
    }

    public ArrayList<String> getComponents(){
        return repository.getComponents();
    }

    public ArrayList<String> getBannedComponents(){return repository.getBannedComponents();}

    public void setBannedComponent(String text){
        repository.setNewBannedComponent(text);
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }
}
