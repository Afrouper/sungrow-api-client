package de.afrouper.server.sungrow.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.afrouper.server.sungrow.api.dto.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SungrowClient {

    private final HttpClient httpClient;
    private final String appKey;
    private final String secretKey;
    private final Duration requestTimeout;
    private final URI uri;
    private final Gson gson;
    private LoginResponse loginResponse;
    private LocalDateTime lastAPICall;

    SungrowClient(URI uri, String appKey, String secretKey, Duration connectTimeout, Duration requestTimeout) {
        Objects.requireNonNull(uri, "URI cannot be null");
        Objects.requireNonNull(appKey, "App key cannot be null");
        Objects.requireNonNull(secretKey, "Secret key cannot be null");
        Objects.requireNonNull(connectTimeout, "Connect timeout cannot be null");
        Objects.requireNonNull(requestTimeout, "Request timeout cannot be null");
        this.uri = uri;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.requestTimeout = requestTimeout;
        this.httpClient = HttpClient
                .newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        gson = new GsonBuilder()
                .create();
    }

    public void login() throws IOException {
        login(EnvironmentConfiguration.getAccountEmail(), EnvironmentConfiguration.getAccountPassword());
    }

    public void login(String username, String password) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri.resolve("/openapi/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(new Login(username, password, appKey))))
                    .timeout(requestTimeout)
                    .headers(getDefaultHeaders())
                    .build();

            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (send.statusCode() == 200) {
                LoginResponse loginResponse = gson.fromJson(send.body(), LoginResponse.class);
                if(loginResponse.isSuccess()) {
                    LoginResponse.LoginResult loginResult = loginResponse.getData();
                    if(loginResult.getLoginState().equals(LoginState.SUCCESS)) {
                        this.loginResponse = loginResponse;
                        apiCallSuccess();
                    }
                    else {
                        throw new IOException("Login error " + loginResult.getLoginState() + ", Message:" + loginResult.getMessage());
                    }
                }
                else {
                    throw new IOException("Login error: '" + send.body() + "'");
                }
            }
            else {
                throw new IOException("Login failed. ResponseCode " + send.statusCode() + ": '" + send.body() + "'");
            }
        }
        catch (InterruptedException e) {
            throw new IOException("Interrupted while initializing Client", e);
        }
    }

    private String[] getDefaultHeaders() {
        return new String[] {
                "Content-Type", "application/json",
                "x-access-key", secretKey,
                "sys_code", "901"
        };
    }

    private void apiCallSuccess() {
        lastAPICall = LocalDateTime.now();
    }

    public void execute(APIOperation operation) throws IOException {
        if(operation.getMethod() != APIOperation.Method.POST) {
            throw new IOException("Method not supported: " + operation.getMethod());
        }
        String json = null;
        try {
            BaseRequest baseRequest = operation.getRequest();
            baseRequest.setAppKey(appKey);
            baseRequest.setToken(loginResponse.getData().getToken());
            HttpRequest request = HttpRequest.newBuilder(uri.resolve(operation.getPath()))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(baseRequest)))
                    .timeout(requestTimeout)
                    .headers(getDefaultHeaders())
                    .build();

            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            json = send.body();

            if(send.statusCode() == 200) {
                //System.out.println(json);
                Type baseResponseType = getResponseType(operation);
                BaseResponse<?> baseResponse = gson.fromJson(json, baseResponseType);

                if("1".equals(baseResponse.getErrorCode())) {
                    apiCallSuccess();
                    operation.setResponse(baseResponse.getData());
                }
                else {
                    throw new IOException("Operation error: '" + json + "'");
                }
            }
            else {
                throw new IOException("Operation failed. ResponseCode " + send.statusCode() + ": '" + json + "'");
            }
        } catch (InterruptedException | NumberFormatException e) {
            throw new RuntimeException("Unable to execute Operation. Json from server: '" + json + "'.", e);
        }
    }

    private Type getResponseType(Object operation) {
        Type[] genericTypes;

        if (operation.getClass().getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
            genericTypes = parameterizedType.getActualTypeArguments();
        } else if (operation.getClass().getGenericInterfaces().length > 0 && operation.getClass().getGenericInterfaces()[0] instanceof ParameterizedType parameterizedType) {
            genericTypes = parameterizedType.getActualTypeArguments();
        } else {
            throw new IllegalArgumentException("Class not implementing an generic interface or extends a generic base class.");
        }
        Type resType = genericTypes[1];
        return TypeToken.getParameterized(BaseResponse.class, resType).getType();
    }
}
