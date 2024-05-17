package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.controller.ColaboradorController;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class WebApp {
    public static void main(String[] args) {
        var env = System.getenv();
        ObjectMapper objectMapper = createObjectMapper();
        var fachada = new Fachada();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

     //   var app = Javalin.create().start(port);
        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                configureObjectMapper(mapper);
            }));
        }).start(port);

        var colaboradorController = new ColaboradorController(fachada);

        app.post("/colaboradores", colaboradorController::agregar);
        app.get("/colaboradores/{id}", colaboradorController::obtenerColaborador);
        app.get("/listaColaboradores", colaboradorController::mostrarColaboradores);
        app.patch("/colaboradores/{id}", colaboradorController::modificarColaboracion);
        app.get("/colaboradores/{id}/puntos", colaboradorController::obtenerPuntos);
        app.put("/formula", colaboradorController::actualizarPesosPuntos);
    }


    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}