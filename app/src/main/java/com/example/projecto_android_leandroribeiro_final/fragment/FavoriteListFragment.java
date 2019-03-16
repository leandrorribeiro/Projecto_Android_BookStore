package com.example.projecto_android_leandroribeiro_final.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projecto_android_leandroribeiro_final.R;
import com.example.projecto_android_leandroribeiro_final.adapter.ItemAdapter;
import com.example.projecto_android_leandroribeiro_final.interfaceBook.AdapterCallback;
import com.example.projecto_android_leandroribeiro_final.model.Favorites;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class FavoriteListFragment extends Fragment implements AdapterCallback {
    private RecyclerView favoriteRecyclerview;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    GetFavoriteCallback getFavoriteCallback;

    public void connect(Activity activity){
        getFavoriteCallback = (GetFavoriteCallback) activity;
    }

    public FavoriteListFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        favoriteRecyclerview = view.findViewById(R.id.my_recycler2);
        favoriteRecyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        favoriteRecyclerview.setLayoutManager(layoutManager);
        Realm.init(getActivity());
        getFavorites();
        return view;
    }

    private void getFavorites(){
        Realm realm =  Realm.getDefaultInstance();
        RealmQuery<Favorites> query = realm.where(Favorites.class);
        RealmResults<Favorites> favorites = query.findAll();
        itemAdapter = new ItemAdapter(null, favorites, getActivity(),FavoriteListFragment.this);
        favoriteRecyclerview.setAdapter(itemAdapter);

    }

    public interface GetFavoriteCallback {
        void getItem(String id);
    }

    @Override
    public void selectItemOnclick(String posicao) {
        getFavoriteCallback.getItem(posicao);
    }
}


