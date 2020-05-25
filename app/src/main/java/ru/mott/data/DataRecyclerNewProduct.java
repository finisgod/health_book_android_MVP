package ru.mott.data;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class DataRecyclerNewProduct {

    private static List<MyData> data = null;
    private static DataRecyclerNewProduct sInstance;

    private static final int[] mColors = new int[]{
            Color.RED,
            Color.WHITE
    };

    private DataRecyclerNewProduct(ArrayList<String> allComponents) {
        data = new ArrayList<>();
        for (int i = 0; i < allComponents.size(); i++) {
            data.add(new MyData(allComponents.get(i), mColors[1]));
        }
    }

    public synchronized static DataRecyclerNewProduct getInstance(ArrayList<String> allComponents) {
        if (sInstance == null) {
            sInstance = new DataRecyclerNewProduct(allComponents);
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
