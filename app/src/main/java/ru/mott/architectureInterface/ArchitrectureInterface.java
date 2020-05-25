package ru.mott.architectureInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public interface ArchitrectureInterface {
    interface View {

    }

    interface Presenter {
        boolean isUserLog();
    }

    interface Repository {
        ArrayList<String> getBannedComponents();

        ArrayList<String> getComponents();

        void setNewBannedComponent(String text);

        void setUser(FirebaseUser mUser);

        FirebaseUser getUser();

        FirebaseAuth getAccount();

        String getBarcode();

        void setBarcode(String mBarcode);

        void signOut();

        ArrayList<String> getBarcodeId();

        void setnewComponent(String text);

        ArrayList<String> getProductComponents();

        void setProductComponents();

        void setBarcodeToNull();
    }
}
