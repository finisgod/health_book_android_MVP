package ru.mott.account;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

class AccountManager {
    FirebaseAuth mAuth;
    FirebaseUser user;
    String barcode = "";
    private FirebaseFirestore firestore;
    int c;
    ArrayList<String> BarcodeId = new ArrayList<>();
    ArrayList<String> list;
    static ArrayList<String> bannedComponents;
    ArrayList<String> productComponents;

    AccountManager() {
        firstThreadToInit thread = new firstThreadToInit();
        thread.start();
    }

    private void threadProcessingInit() {
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        backgroundThreadGetCollInit();
        if (user != null) {
            backgroundThreadBannedComponents();
        }
        getBarcodeId();
    }

    void updateBannedComponents() {
        threadIfLogging thread = new threadIfLogging();
        thread.start();
    }

    void setNewBannedComponent(String text) {
        threadIfChanging thread = new threadIfChanging(text);
        thread.start();
    }

    void setProductComponents() {
        if (!barcode.isEmpty()) {
            threadIfScan thread = new threadIfScan(barcode);
            thread.start();
        } else {
            Log.d(TAG, "barcode is empty");
        }
    }

    private void getBarcodeId() {
        CollectionReference docref = firestore.collection("Products");
        docref.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                BarcodeId.add(documentSnapshot.getId());
            }
        });
    }

    void setnewComponent(String text) {
        Map<String, Object> field = new HashMap<>();
        DocumentReference docRef = firestore.collection("Products")
                .document(barcode);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int numOfAllComponents = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("numOfProductComponents")));
                for (int i = 1; i <= numOfAllComponents; i++) {
                    if (documentSnapshot.getString("" + i).equals(text)) {
                        c = 1;
                        field.put("" + i, documentSnapshot.getString("" + i));
                    } else {
                        field.put("" + i, documentSnapshot.getString("" + i));
                    }
                }
                if (c == 0) {
                    field.put("" + ++numOfAllComponents, text);
                }
                c = 0;
                field.put("numOfAllComponents", "" + numOfAllComponents);
                firestore.collection("Products")
                        .document(barcode).set(field);
            } else {
                field.put("1", text);
                field.put("numOfAllComponents", "1");
                firestore.collection("Products")
                        .document(barcode).set(field);
            }

        }).addOnFailureListener(documentSnapshot -> {
            Log.d(TAG, documentSnapshot.toString());
        });
    }


    // лист всех компонентов продукта
    private void backgroundThreadGetProductComponents(String barcode) {
        DocumentReference docRef = firestore.collection("Products").document(barcode);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int numOfProductComponents = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("numOfProductComponents")));
                productComponents = new ArrayList<>(numOfProductComponents);
                for (int i = 1; i <= numOfProductComponents; i++) {
                    productComponents.add(documentSnapshot.getString("" + i));
                }
            } else {
                Log.d(TAG, "document doesn't exist");
            }
        }).addOnFailureListener(documentSnapshot -> Log.d(TAG, documentSnapshot.toString()));
    }

    // Создание листа всех компонентов
    private void backgroundThreadGetCollInit() {
        DocumentReference docRef = firestore.collection("Components").document("AllComponents");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int numOfAllComponents = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("NumOfAllComponents")));
                list = new ArrayList<>(numOfAllComponents);
                for (int i = 1; i <= numOfAllComponents; i++) {
                    list.add(documentSnapshot.getString("" + i));
                    Log.d(TAG, list.get(i - 1));
                }
            } else {
                Log.d(TAG, "document doesn't exist");
            }
        }).addOnFailureListener(documentSnapshot ->
                Log.d(TAG, documentSnapshot.toString())
        );
    }

    // Доставание листа запрещённых компонентов
    private void backgroundThreadBannedComponents() {
        DocumentReference docRef = firestore.collection("Users")
                .document(user.getUid()).collection("bannedComponents").document("ban");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int numOfAllComponents = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("numOfBannedComponents")));
                bannedComponents = new ArrayList<>(numOfAllComponents);
                for (int i = 1; i <= numOfAllComponents; i++) {
                    bannedComponents.add(documentSnapshot.getString("" + i));
                }
            } else {
                Map<String, Object> field = new HashMap<>();
                field.put("numOfBannedComponents", "0");
                firestore.collection("Users")
                        .document(user.getUid()).collection("bannedComponents").document("ban").set(field);
            }
        }).addOnFailureListener(documentSnapshot ->
                Log.d(TAG, documentSnapshot.toString())
        );
    }

    // Изменение листа запрещённых компонентов в фаербейз
    synchronized private void backgroundThreadSetBannedComponent(String text) {
        DocumentReference docRef = firestore.collection("Users")
                .document(user.getUid()).collection("bannedComponents").document("ban");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            int check = 0;
            if (documentSnapshot.exists()) {
                int numOfAllComponents = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("numOfBannedComponents")));
                for (int i = 1; i <= numOfAllComponents; i++) {
                    if (text.equals(documentSnapshot.getString("" + i))) {
                        setAndSwapWithDelete(i, numOfAllComponents);
                        check++;
                        break;
                    }
                }
                if (check == 0) {
                    addAndExpand(text, numOfAllComponents);
                }
            }
        });
    }

    // Вспомогательная функция 1
    synchronized private void setAndSwapWithDelete(int i, int numOfAllComponents) {
        Map<String, Object> newBannedList = new HashMap<>();
        for (int j = 1; j <= numOfAllComponents; j++) {
            if (j == i) {
                if (j != numOfAllComponents) {
                    for (int k = j; k < bannedComponents.size(); k++) {
                        newBannedList.put(k + "", bannedComponents.get(k));
                    }
                    break;
                } else {
                    break;
                }
            }
            newBannedList.put(j + "", bannedComponents.get(j - 1));
        }
        newBannedList.put("numOfBannedComponents", numOfAllComponents - 1 + "");
        DocumentReference docRef = firestore.collection("Users")
                .document(user.getUid()).collection("bannedComponents").document("ban");
        docRef.set(newBannedList)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "successful"))
                .addOnFailureListener(e -> Log.d(TAG, "failure" + e));
    }

    // Вспомогательная функция 2
    private void addAndExpand(String text, int numOfAllComponents) {
        Map<String, Object> newBannedList = new HashMap<>();
        int i = 1;
        for (String s : bannedComponents) {
            newBannedList.put(i + "", s);
            i++;
        }
        bannedComponents.add(text);
        newBannedList.put("" + ++numOfAllComponents, text);
        newBannedList.put("numOfBannedComponents", "" + numOfAllComponents);

        DocumentReference docRef = firestore.collection("Users")
                .document(user.getUid()).collection("bannedComponents").document("ban");
        docRef.set(newBannedList)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "successful"))
                .addOnFailureListener(e -> Log.d(TAG, "failure" + e));

    }

    class firstThreadToInit extends Thread {
        @Override
        public void start() {
            super.start();
            threadProcessingInit();
        }
    }

    class threadIfLogging extends Thread {
        @Override
        public void start() {
            super.start();
            backgroundThreadBannedComponents();
        }

    }

    class threadIfChanging extends Thread {
        private String textToChange;

        threadIfChanging(String text) {
            textToChange = text;
        }

        @Override
        public void start() {
            super.start();
            backgroundThreadSetBannedComponent(textToChange);
        }
    }

    class threadIfScan extends Thread {
        private String Barcode;

        threadIfScan(String Barcode) {
            this.Barcode = Barcode;
        }

        @Override
        public void start() {
            super.start();
            backgroundThreadGetProductComponents(Barcode);
        }
    }

    void setBarcodeToNull() {
        barcode = "";
    }
}
