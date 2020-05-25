package ru.mott.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import ru.mott.data.DataRecycler;
import ru.mott.presenters.ChooseFragmentPresenter;

public class ChooseFragment extends Fragment implements ArchitrectureInterface.View {

    private ChooseFragmentPresenter presenter;
    private MyDataAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose, container, false);

        presenter = new ChooseFragmentPresenter(this, (AccountSingleton) Objects.requireNonNull(this.getActivity()).getApplication());

        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MyDataAdapter(DataRecycler
                .getInstance(presenter.getComponents(), presenter.getBannedComponents()).getData());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }


    class MyDataAdapter extends RecyclerView.Adapter<viewHolder> {

        final List<DataRecycler.MyData> data;

        private MyDataAdapter(List<DataRecycler.MyData> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
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

    class viewHolder extends RecyclerView.ViewHolder {
        private final TextView count;

        private viewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.counter);

            count.setOnClickListener(v -> {
                if(count.getTextColors() == ColorStateList.valueOf(Color.WHITE)){
                    count.setTextColor(Color.RED);
                } else {
                    count.setTextColor(Color.WHITE);
                }
                count.setEnabled(false);
                change("" + count.getText());
                count.setEnabled(true);
            });
        }

    }

    private void change(String text) {
        presenter.setBannedComponent(text);
        adapter.notifyItemChanged(adapter.getItemCount());
    }
}