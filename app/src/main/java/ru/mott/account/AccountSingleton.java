package ru.mott.account;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import ru.mott.architectureInterface.ArchitrectureInterface;

public class AccountSingleton extends Application implements ArchitrectureInterface.Repository {
    private AccountManager acc;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        acc = new AccountManager();
    }

    @Override
    public ArrayList<String> getComponents() {
        return acc.list;
    }

    @Override
    public ArrayList<String> getBannedComponents() {
        return acc.bannedComponents;
    }

    @Override
    public ArrayList<String> getProductComponents() {
        return acc.productComponents;
    }

    @Override
    public void setProductComponents() {
        acc.setProductComponents();
    }

    @Override
    public void setNewBannedComponent(String text){
        acc.setNewBannedComponent(text);
    }

    @Override
    public void setUser(FirebaseUser mUser) {
        acc.user = mUser;
        acc.updateBannedComponents();
    }

    @Override
    public FirebaseUser getUser() {
        return acc.user;
    }

    @Override
    public String getBarcode() {
        return acc.barcode;
    }

    @Override
    public void setBarcode(String mBarcode) {
        acc.barcode = mBarcode;
        setProductComponents();
    }

    @Override
    public FirebaseAuth getAccount() {
        return acc.mAuth;
    }

    @Override
    public void signOut() {
        acc.mAuth.signOut();
        acc.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void setnewComponent(String text){
        acc.setnewComponent(text);
    }

    @Override
    public ArrayList<String> getBarcodeId() {
        return acc.BarcodeId;
    }

    @Override
    public void setBarcodeToNull() {
        acc.setBarcodeToNull();
    }
}
