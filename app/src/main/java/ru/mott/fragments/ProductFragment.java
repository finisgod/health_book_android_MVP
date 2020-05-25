package ru.mott.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.mott.MainActivity;
import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.CommonData;
import ru.mott.data.DataRecyclerProduct;
import ru.mott.presenters.ProductFragmentPresenter;

public class ProductFragment extends Fragment implements ArchitrectureInterface.View {
    private ProductFragmentPresenter presenter;
    private MyDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product, container, false);

        presenter = new ProductFragmentPresenter(this,(AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());
        if (!presenter.isUserLog()) {
            Toast.makeText(getActivity(), "Please relogin", Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.analysis);
        adapter = new MyDataAdapter(DataRecyclerProduct
                .getInstance(presenter.getProductComponents(),presenter.getBannedComponents()).getData());
        Log.d("123123","" + presenter.getProductComponents().size());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    class MyDataAdapter extends RecyclerView.Adapter<viewHolder> {

        final List<DataRecyclerProduct.MyData> data;
        private MyDataAdapter(List<DataRecyclerProduct.MyData> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            holder.count.setText(data.get(position).count);
            holder.count.setTextColor(data.get(position).color);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }

    class viewHolder extends RecyclerView.ViewHolder{

        private final TextView count;

        private viewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.counter2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.setBarcodeToNull();
        ((MainActivity)getActivity()).onItemSelected(CommonData.mainMenu);
    }

}
