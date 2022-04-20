package indoortec.com.home.viewholder;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import indoortec.com.home.databinding.HomeScreenItemListBinding;
import indoortec.com.home.model.HomeScreenItem;

public class HomeScreenViewHolder extends RecyclerView.ViewHolder {
    private final HomeScreenItemListBinding binding;

    public HomeScreenViewHolder(@NonNull HomeScreenItemListBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(HomeScreenItem homeScreenItem) {

    }
}
