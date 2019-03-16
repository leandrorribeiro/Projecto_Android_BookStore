package com.example.projecto_android_leandroribeiro_final.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projecto_android_leandroribeiro_final.R;
import com.example.projecto_android_leandroribeiro_final.interfaceBook.AdapterCallback;
import com.example.projecto_android_leandroribeiro_final.model.Favorites;
import com.example.projecto_android_leandroribeiro_final.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {
    private List<Item> items;
    private List<Favorites> favorites;
    private Context context;
    private AdapterCallback adapterCallback;

    public ItemAdapter(List<Item> items, List<Favorites> favorites, Context context, AdapterCallback adapterCallback) {
        this.items = items;
        this.favorites = favorites;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);
        ItemAdapterViewHolder itemAdapterViewHolder = new ItemAdapterViewHolder(view);
        return itemAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder( ItemAdapterViewHolder itemAdapterViewHolder, final int i) {
        if(items != null){
            Item item = items.get(i);
            itemAdapterViewHolder.tvNome.setText(item.getVolumeInfo().getTitle());
            itemAdapterViewHolder.tvNome.setTypeface(null, Typeface.BOLD);
            itemAdapterViewHolder.tvData.setText(item.getVolumeInfo().getPublishedDate());
            if (item.getVolumeInfo().getSubtitle() == null) {
                itemAdapterViewHolder.tvSub.setVisibility(View.GONE);
            }else{
                itemAdapterViewHolder.tvSub.setText(item.getVolumeInfo().getSubtitle());
            }
            if(item.getVolumeInfo().getImageLinks()!=null){
                Picasso.get()
                        .load(item.getVolumeInfo().getImageLinks().getSmallThumbnail())
                        .into(itemAdapterViewHolder.ivItem);
            }
            itemAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.selectItemOnclick(items.get(i).getId());
                }
            });
        }else {
            final Favorites favorite = favorites.get(i);
            itemAdapterViewHolder.tvNome.setText(favorite.getTvNome());
            itemAdapterViewHolder.tvNome.setTypeface(null, Typeface.BOLD);
            itemAdapterViewHolder.tvSub.setText(favorite.getTvSub());
            itemAdapterViewHolder.tvData.setText(favorite.getTvData());
            if(favorite.getIvImage()!=null){
                Picasso.get()
                        .load(favorite.getIvImage())
                        .into(itemAdapterViewHolder.ivItem);
            }
            itemAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.selectItemOnclick(favorite.getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(items==null){
            return favorites.size();
        }else{
            return items.size();
        }

    }


    public static class ItemAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNome;
        private TextView tvData;
        private ImageView ivItem;
        private TextView tvSub;


        public ItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome  = itemView.findViewById(R.id.tv_nome);
            ivItem = itemView.findViewById(R.id.iv_item);
            tvData = itemView.findViewById(R.id.tv_data);
            tvSub = itemView.findViewById(R.id.tv_subtitulo);
        }
    }
}

