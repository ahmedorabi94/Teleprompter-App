package com.test.orabi.teleprompter.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.databinding.ListViewItemBinding;
import com.test.orabi.teleprompter.repository.data.Tele;

import java.util.List;

public class TeleAdapter extends ListAdapter<Tele,TeleAdapter.MyViewHolder> {

    private TeleCallBack mCallback;

    public TeleAdapter(TeleCallBack callBack) {
        super(DIFF_CALLBACK);
        this.mCallback =callBack;
    }

    private static final DiffUtil.ItemCallback<Tele> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tele>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tele oldItem, @NonNull Tele newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tele oldItem, @NonNull Tele newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getBody().equals(newItem.getBody());
        }
    };


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListViewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_view_item
                , parent, false);
        binding.setCallback(mCallback);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mBinding.setItem(getItem(position));
        holder.mBinding.executePendingBindings();
    }


    public Tele getTele(int position) {
        return getItem(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ListViewItemBinding mBinding;

        MyViewHolder(ListViewItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }


}
