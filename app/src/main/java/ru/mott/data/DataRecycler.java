package ru.mott.data;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class DataRecycler {

    private static List<MyData> data = null;
    private static DataRecycler sInstance;

    private static final int[] mColors = new int[]{
            Color.RED,
            Color.WHITE
    };

    private DataRecycler(ArrayList<String> allComponents, ArrayList<String> bannedComponents) {
        data = new ArrayList<>();
        int check;
        for (int i = 0; i < allComponents.size(); i++) {
            check = 0;
            for (int j = 0; j < bannedComponents.size(); j++) {
                if (allComponents.get(i).equals(bannedComponents.get(j))) {
                    data.add(new MyData(allComponents.get(i), mColors[0]));
                    check++;
                    break;
                }
            }
            if(check == 0){
                data.add(new MyData(allComponents.get(i), mColors[1]));
            }
        }
    }

    public synchronized static DataRecycler getInstance(ArrayList<String> allComponents, ArrayList<String> bannedComponents) {
        if (sInstance == null) {
            sInstance = new DataRecycler(allComponents,bannedComponents);
        }
        return sInstance;
    }

    public List<MyData> getData() {
        return data;
    }

    public static class MyData {
        private MyData(String count, int color) {
            this.count = count;
            this.color = color;
        }

        public final String count;
        public final int color;
    }


}
