package com.example.da.BLL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da.R;

import java.util.List;

public class OnboaringAdapter extends RecyclerView.Adapter<OnboaringAdapter.OnboaringViewHolder> {

    private List<OnboaringItem> onboaringItems;

    public OnboaringAdapter(List<OnboaringItem> onboaringItems) {
        this.onboaringItems = onboaringItems;
    }

    @NonNull
    @Override
    public OnboaringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboaringViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboaring, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboaringViewHolder holder, int position) {
        holder.setOnboaringData(onboaringItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboaringItems.size();
    }

    class OnboaringViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnboaring;

        OnboaringViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageOnboaring = itemView.findViewById(R.id.imageOnboaring);
        }

        void setOnboaringData(OnboaringItem onboaringItem){
            textTitle.setText(onboaringItem.getTilte());
            textDescription.setText(onboaringItem.getDescription());
            imageOnboaring.setImageResource(onboaringItem.getImage());
        }
    }
}
