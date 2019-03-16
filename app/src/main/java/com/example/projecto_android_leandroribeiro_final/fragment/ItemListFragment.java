package com.example.projecto_android_leandroribeiro_final.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;

import com.example.projecto_android_leandroribeiro_final.R;
import com.example.projecto_android_leandroribeiro_final.activity.MainActivity;
import com.example.projecto_android_leandroribeiro_final.adapter.ItemAdapter;
import com.example.projecto_android_leandroribeiro_final.interfaceBook.AdapterCallback;
import com.example.projecto_android_leandroribeiro_final.interfaceBook.GetItemsService;
import com.example.projecto_android_leandroribeiro_final.model.Favorites;
import com.example.projecto_android_leandroribeiro_final.model.Result;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemListFragment extends Fragment implements AdapterCallback {
    private RecyclerView itemRecyclerview;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    GetItemCallback getItemCallback;
    private String Search;
    private EditText et_pesquisa;
    private Button btn_pesquisar;
    private Button btn_favorito;
    public static final String KEY_DIC = "1";
    public static final String KEY_VALUE = "key_value";

    public void connect(Activity activity){
        getItemCallback = (GetItemCallback) activity;
    }

    public ItemListFragment() {

    }

    public void setSearch(String search) {
        Search = search;
    }

    public String getSearch() {
        return Search;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        itemRecyclerview = view.findViewById(R.id.my_recycler);
        itemRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        itemRecyclerview.setLayoutManager(layoutManager);
        Realm.init(getActivity());
        if(getSearch()!=null){
            getItems();
        }
        et_pesquisa = view.findViewById(R.id.et_pesquisa);
        et_pesquisa.setGravity(Gravity.CENTER);
        btn_pesquisar = view.findViewById(R.id.btn_pesquisar);
        btn_favorito = view.findViewById(R.id.btn_favorito);
        btn_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_pesquisa.getText().toString()!=null){
                    setSearch(et_pesquisa.getText().toString());
                    getItems();

                    SharedPreferences msg           = getActivity().getSharedPreferences(KEY_DIC,0);
                    SharedPreferences.Editor editor = msg.edit();
                    editor.putString(KEY_VALUE,et_pesquisa.getText().toString());
                    editor.commit();
                }
            }
        });

        btn_favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FavoriteListFragment favoriteListFragment = new FavoriteListFragment();
                favoriteListFragment.connect(getActivity());
                fragmentTransaction.replace(R.id.container, favoriteListFragment,"list");
                fragmentTransaction.addToBackStack("list");
                fragmentTransaction.commit();
            }
        });
        SharedPreferences valorGuardado = getActivity().getSharedPreferences(KEY_DIC,0);
        String valor  = valorGuardado.getString(KEY_VALUE,"");
        et_pesquisa.setText(valor); //devolve a string pesquisada
        setSearch(et_pesquisa.getText().toString()); //devolve a pesquisa anterior
        getItems();
        return view;
    }

    private void getItems() {

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        GetItemsService service = retrofit.create(GetItemsService.class);

        Call<Result> itemCall = service.getItems(this.getSearch());

        itemCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.v("RETROFIT","OK");

                if(response.isSuccessful()){
                    itemAdapter = new ItemAdapter(response.body().getItems(),null,getActivity(), ItemListFragment.this);
                    itemRecyclerview.setAdapter(itemAdapter);
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.v("RETROFIT","NOK");
            }
        });
    }

    public interface GetItemCallback {
        void getItem(String id);
    }

    @Override
    public void selectItemOnclick(String posicao) {
        getItemCallback.getItem(posicao);
    }
}

