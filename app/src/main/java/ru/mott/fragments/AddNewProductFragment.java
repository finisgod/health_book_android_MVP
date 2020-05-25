package ru.mott.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.DataRecyclerNewProduct;
import ru.mott.presenters.AddNewProductPresenter;

public class AddNewProductFragment extends Fragment implements ArchitrectureInterface.View  {
    private AddNewProductPresenter presenter;
    private MyDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose, container, false);

        presenter = new AddNewProductPresenter(this,(AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new MyDataAdapter(DataRecyclerNewProduct
                .getInstance(presenter.getComponents()).getData());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }


    class MyDataAdapter extends RecyclerView.Adapter<AddNewProductFragment.viewHolder> {

        final List<DataRecyclerNewProduct.MyData> data;
        private MyDataAdapter(List<DataRecyclerNewProduct.MyData> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public AddNewProductFragment.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AddNewProductFragment.viewHolder holder, int position) {
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
            count = itemView.findViewById(R.id.counter);
            count.setOnClickListener(v -> change("" + count.getText()));
        }
    }

    private void change(String text){
        presenter.setNewComponent(text);
        adapter.notifyDataSetChanged();
    }

}
