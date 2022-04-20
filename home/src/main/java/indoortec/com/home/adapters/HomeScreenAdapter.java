package indoortec.com.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import indoortec.com.home.databinding.HomeScreenItemListBinding;
import indoortec.com.home.model.HomeScreenItem;
import indoortec.com.home.viewholder.HomeScreenViewHolder;


public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenViewHolder> {

    private final List<HomeScreenItem> data = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setHomeScreenitems(List<HomeScreenItem> homeScreenItems){
        data.clear();
        data.addAll(homeScreenItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HomeScreenItemListBinding binding = HomeScreenItemListBinding.inflate(inflater,parent,false);
        return new HomeScreenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
