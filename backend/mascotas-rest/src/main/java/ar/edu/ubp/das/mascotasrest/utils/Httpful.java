package ar.edu.ubp.das.mascotasrest.utils;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Httpful {

    private final Client client;
    private WebTarget target;
    private String method = HttpMethod.GET;
    private Entity<?> entity;
    private final Map<String, String> headers = new HashMap<>();
    private final Gson gson = new Gson();

    public Httpful(String baseUrl) {
        this.client = ClientBuilder.newClient();
        this.target = this.client.target(baseUrl);
    }

    //region Configuración de la petición
    public Httpful path(String path) {
        this.target = this.target.path(path);
        return this;
    }

    public Httpful addQueryParam(String key, String value) {
        this.target = this.target.queryParam(key, value);
        return this;
    }

    public Httpful method(String method) {
        this.method = method;
        return this;
    }

    public Httpful apiKey(String headerName, String value) {
        if (headerName == null || headerName.isBlank())
            throw new IllegalArgumentException("El nombre del header de API Key no puede ser nulo/vacío");

        if (value == null || value.isBlank())
            throw new IllegalArgumentException("El valor de la API Key no puede ser nulo/vacío");

        this.headers.put(headerName, value);
        return this;
    }

    public Httpful header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }
    //endregion

    //region Métodos HTTP
    public Httpful get() {
        this.method = HttpMethod.GET;
        this.entity = null;
        return this;
    }

    public Httpful get(Object body) {
        this.method = HttpMethod.GET;
        this.entity = jsonEntity(body);
        return this;
    }

    public Httpful post(Object body) {
        this.method = HttpMethod.POST;
        this.entity = jsonEntity(body);
        return this;
    }

    public Httpful put(Object body) {
        this.method = HttpMethod.PUT;
        this.entity = jsonEntity(body);
        return this;
    }

    public Httpful patch(Object body) {
        this.method = HttpMethod.PATCH;
        this.entity = jsonEntity(body);
        return this;
    }

    public Httpful delete() {
        this.method = HttpMethod.DELETE;
        this.entity = null;
        return this;
    }

    public Httpful delete(Object body) {
        this.method = HttpMethod.DELETE;
        this.entity = jsonEntity(body);
        return this;
    }
    //endregion

    //region Ejecución
    public <T> T execute(Class<T> responseType) {
        return executeRequest(responseType);
    }

    public <T> T execute(Type responseType) {
        return executeRequest(responseType);
    }

    @SuppressWarnings("unchecked")
    private <T> T executeRequest(Object responseType) {
        Response response = sendRequest();
        try {
            int status = response.getStatus();
            // Éxito si 2xx
            if (status >= 200 && status < 300) {
                // 204 No Content
                if (status == 204 || response.getLength() == 0) {
                    return null;
                }

                String json = response.readEntity(String.class);
                if (json == null || json.isBlank()) {
                    return null;
                }

                if (responseType instanceof Class<?>) {
                    return gson.fromJson(json, (Class<T>) responseType);
                }
                return gson.fromJson(json, (Type) responseType);
            }

            // Error
            String errorBody = safeReadBody(response);
            String message = "Error HTTP " + status;
            String detail = extractMessageFromJson(errorBody);
            if (!detail.isEmpty()) {
                message += " - " + detail;
            }
            throw new RuntimeException(message + (errorBody.isEmpty() ? "" : ": " + errorBody));
        }
        finally {
            response.close();
        }
    }

    private Response sendRequest() {
        Invocation.Builder builder = this.target
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        headers.forEach(builder::header);
        return switch (this.method) {
            case HttpMethod.GET ->
                    entity == null ? builder.get() : builder.method(HttpMethod.GET, entity);

            case HttpMethod.POST ->
                    builder.post(this.entity);

            case HttpMethod.PUT ->
                    builder.put(this.entity);

            case HttpMethod.PATCH ->
                    builder.method(HttpMethod.PATCH, this.entity);

            case HttpMethod.DELETE ->
                    entity == null ? builder.delete() : builder.method(HttpMethod.DELETE, entity);

            default ->
                    throw new RuntimeException("Método HTTP no soportado: " + this.method);
        };
    }
    //endregion

    //region Procesamiento de la respuesta
    private static String safeReadBody(Response response) {
        try {
            String body = response.readEntity(String.class);
            return body == null ? "" : body;
        } catch (Exception e) {
            return "";
        }
    }

    private String extractMessageFromJson(String body) {
        try {
            if (body == null || body.isBlank()) {
                return "";
            }

            JsonObject obj =
                    JsonParser.parseString(body).getAsJsonObject();

            if (obj.has("message")) {
                return obj.get("message").getAsString();
            }

            if (obj.has("error_description")) {
                return obj.get("error_description").getAsString();
            }

            if (obj.has("error")) {
                return obj.get("error").getAsString();
            }

        } catch (Exception ignored) {
        }

        return "";
    }
    //endregion

    //region Utilidades
    private Entity<?> jsonEntity(Object body) {
        return Entity.entity(
                gson.toJson(body),
                MediaType.APPLICATION_JSON
        );
    }
    //endregion
}