package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LogisticaRetrofitClient {

    @GET("logistica/{qr}")
    Call<TrasladoDTO> get(@Path("qr") String qr);
}