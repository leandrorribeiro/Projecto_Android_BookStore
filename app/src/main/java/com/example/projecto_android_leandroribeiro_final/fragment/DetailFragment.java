package com.example.projecto_android_leandroribeiro_final.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projecto_android_leandroribeiro_final.R;
import com.example.projecto_android_leandroribeiro_final.interfaceBook.GetItemsService;
import com.example.projecto_android_leandroribeiro_final.model.Favorites;
import com.example.projecto_android_leandroribeiro_final.model.Item;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends Fragment {
    private ImageView ivImage;
    private TextView tvNome;
    private TextView tvData;
    private TextView tvBuy;
    private TextView tvSub;
    private TextView tvAutor;
    private TextView tvDesc;
    private TextView tvPreco;
    private TextView tvRating;
    private TextView tvPags;
    private Button btnAdcfavorito;
    private Button btnComprar;


    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ivImage = view.findViewById(R.id.iv_image);
        tvNome = view.findViewById(R.id.tv_nome);
        tvSub = view.findViewById(R.id.tv_subtitulo);
        tvData = view.findViewById(R.id.tv_data);
        tvAutor = view.findViewById(R.id.tv_autor);
        tvDesc = view.findViewById(R.id.tv_descricao);
        tvPreco = view.findViewById(R.id.tv_preco);
        tvRating = view.findViewById(R.id.tv_rating);
        tvPags = view.findViewById(R.id.tv_paginas);
        btnAdcfavorito = view.findViewById(R.id.btn_adcfavorito);
        btnComprar = view.findViewById(R.id.btn_comprar);
        Realm.init(getActivity());
        Bundle args = this.getArguments();


        getItemDetail(args.getString("ID"));
        return view;
    }

    public void prepareView(final Item item) {
        tvNome.setText(item.getVolumeInfo().getTitle());
        if (item.getVolumeInfo().getSubtitle() != null){
            tvSub.setText(item.getVolumeInfo().getSubtitle());
        }else{
            tvSub.setVisibility(View.GONE);
        }
        if (item.getVolumeInfo().getPublishedDate() != null){
            tvData.setText(item.getVolumeInfo().getPublishedDate());
        }else{
            tvData.setVisibility(View.GONE);
        }
        tvAutor.setText(item.getVolumeInfo().getAuthors().toString());
        tvDesc.setText(item.getVolumeInfo().getDescription());
        if (item.getSaleInfo().getListPrice() == null){
            tvPreco.setText("");
            tvPreco.setVisibility(View.GONE);
        }else{
            tvPreco.setText("Preço: "+String.valueOf(item.getSaleInfo().getListPrice().getAmount())+item.getSaleInfo().getListPrice().getCurrencyCode());
        }
        if (item.getVolumeInfo().getAverageRating() != null){
           tvRating.setText("Rating: "+String.valueOf(item.getVolumeInfo().getAverageRating()));
        }else{
            tvRating.setVisibility(View.GONE);
        }
        tvPags.setText("Número de páginas: "+String.valueOf(item.getVolumeInfo().getPageCount()));
        if (item.getVolumeInfo().getImageLinks() != null) {
            Picasso.get().load(item.getVolumeInfo().getImageLinks().getThumbnail()).into(ivImage);
        }
        if (checkSize(item) == false){
            btnAdcfavorito.setText("Adicionar dos Favoritos!");
        }else{
            btnAdcfavorito.setText("Retirar dos Favoritos!");
        }
        btnAdcfavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                if (checkSize(item) == false) { //verificar não trouxe resultados, então add
                    realm.beginTransaction();
                    Favorites favorite = realm.createObject(Favorites.class);
                    favorite.setTvNome(item.getVolumeInfo().getTitle());
                    favorite.setTvData(item.getVolumeInfo().getPublishedDate());
                    favorite.setTvSub(item.getVolumeInfo().getSubtitle());
                    favorite.setId(item.getId());
                    if (item.getVolumeInfo().getImageLinks() != null) {
                        favorite.setIvImage(item.getVolumeInfo().getImageLinks().getSmallThumbnail());
                    } else {
                        favorite.setIvImage(null);
                    }
                    realm.commitTransaction();
                    btnAdcfavorito.setText("Retirar dos Favoritos!");
                } else { //retira da bd
                    final RealmResults<Favorites> results = realm.where(Favorites.class).equalTo("id", item.getId()).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm2) {
                            results.deleteAllFromRealm();
                        }
                    });
                    btnAdcfavorito.setText("Adicionar aos Favoritos!");
                }
            }
        });
        if (item.getSaleInfo().getBuyLink() != null){
            btnComprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getSaleInfo().getBuyLink() != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String buyLink = item.getSaleInfo().getBuyLink();
                        intent.setData(Uri.parse(
                                buyLink));
                        intent.setPackage("com.android.vending");
                        startActivity(intent);
                    }
                }
            });
        }else{
            btnComprar.setText("Não é possível adquirir este livro!");
        }

    }






    private boolean checkSize(Item item){
        Realm realm =  Realm.getDefaultInstance();
        RealmResults<Favorites> user = realm.where(Favorites.class).equalTo("id",item.getId()).findAll();
        //já existe
//não existe
        return user.size() > 0;
    }

    private void getItemDetail(String id) {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl("https://www.googleapis.com/books/v1/").
                addConverterFactory(GsonConverterFactory.create()).build();

        GetItemsService service = retrofit.create(GetItemsService.class);


        Call<Item> ItemDetail = service.getItemDetail(String.valueOf(id));

        ItemDetail.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.v("RETROFIT","OK");

                prepareView(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.v("RETROFIT","NOK");

            }
        });
    }

}

