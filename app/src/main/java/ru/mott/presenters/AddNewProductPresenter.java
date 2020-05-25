package ru.mott.presenters;

import java.util.ArrayList;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class AddNewProductPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;
    private ArchitrectureInterface.View view;

    public AddNewProductPresenter(ArchitrectureInterface.View view, ArchitrectureInterface.Repository repository){
        this.repository = repository;
        this.view = view;
    }

    public ArrayList<String> getComponents(){
        return repository.getComponents();
    }

    public void setNewComponent(String text){
        repository.setnewComponent(text);
    }

    @Override
    public boolean isUserLog() {
        return false;
    }
}