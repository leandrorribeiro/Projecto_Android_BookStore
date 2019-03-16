package com.example.projecto_android_leandroribeiro_final.interfaceBook;


import com.example.projecto_android_leandroribeiro_final.model.Item;
import com.example.projecto_android_leandroribeiro_final.model.Result;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetItemsService {
    @GET("volumes")
    Call<Result> getItems (@Query("q") String item);


    @GET("volumes/{id}") // Parametro que passamos para o serviço, {valor} e colocamos o "valor" no metodo
    Call<Item> getItemDetail (@Path("id") String id);// valor que passamos
}
