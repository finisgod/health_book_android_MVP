package ru.mott.presenters;

import java.util.ArrayList;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class ScanBarcodeActivityPresenter implements ArchitrectureInterface.Presenter {
    private ArchitrectureInterface.Repository repository;

    public ScanBarcodeActivityPresenter(ArchitrectureInterface.Repository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserLog() {
        return repository.getUser() != null;
    }

    public void setBarcode(String Barcode) {
        repository.setBarcode(Barcode);
    }

    public ArrayList<String> getBarcodeId() {
        return repository.getBarcodeId();
    }
}

