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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SungrowClient {

    private final HttpClient httpClient;
    private final String appKey;
    private final String secretKey;
    private final Duration requestTimeout;
    private final URI uri;
    private final Gson gson;
    private LoginResponse loginResponse;
    private EncryptionUtility encryptionUtility;
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

    void activateEncryption(String rsaPublicKey, String password) {
        encryptionUtility = new EncryptionUtility(rsaPublicKey, password);
    }

    public void login() throws IOException {
        login(EnvironmentConfiguration.getAccountEmail(), EnvironmentConfiguration.getAccountPassword());
    }

    public void login(String username, String password) throws IOException {
        try {
            Login login = new Login(username, password, appKey);

            String json;
            if(encryptionUtility != null) {
                login.setApiKey(encryptionUtility.createApiKeyParameter());
                json = encryptionUtility.encrypt(gson.toJson(login));
            }
            else {
                json = gson.toJson(login);
            }

            HttpRequest request = HttpRequest.newBuilder(uri.resolve("/openapi/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(requestTimeout)
                    .headers(getDefaultHeaders())
                    .build();

            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String body = send.body();
            if(send.statusCode() >= 200 && send.statusCode() < 500 && encryptionUtility != null) {
                body = encryptionUtility.decrypt(body);
            }
            if (send.statusCode() == 200) {
                LoginResponse loginResponse = gson.fromJson(body, LoginResponse.class);
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
                    throw new IOException("Login error: '" + body + "'");
                }
            }
            else {
                throw new IOException("Login failed. ResponseCode " + send.statusCode() + ": '" + body + "'");
            }
        }
        catch (InterruptedException e) {
            throw new IOException("Interrupted while initializing Client", e);
        }
    }

    private String[] getDefaultHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Content-Type");
        headers.add("application/json");
        headers.add("x-access-key");
        headers.add(secretKey);
        headers.add("sys_code");
        headers.add("901");
        if(encryptionUtility != null) {
            headers.add("x-random-secret-key");
            headers.add(encryptionUtility.createRandomPublicKey());
        }
        return headers.toArray(new String[0]);
    }

    private void apiCallSuccess() {
        lastAPICall = LocalDateTime.now();
    }

    public void execute(APIOperation operation) throws IOException {
        if(operation.getMethod() != APIOperation.Method.POST) {
            throw new IOException("Method not supported: " + operation.getMethod());
        }
        String jsonResponse = null;
        try {
            BaseRequest baseRequest = operation.getRequest();
            baseRequest.setAppKey(appKey);
            baseRequest.setToken(loginResponse.getData().getToken());

            String jsonRequest;
            if(encryptionUtility != null) {
                baseRequest.setApiKey(encryptionUtility.createApiKeyParameter());
                jsonRequest = encryptionUtility.encrypt(gson.toJson(baseRequest));
            }
            else {
                jsonRequest = gson.toJson(baseRequest);
            }

            HttpRequest request = HttpRequest.newBuilder(uri.resolve(operation.getPath()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .timeout(requestTimeout)
                    .headers(getDefaultHeaders())
                    .build();

            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            jsonResponse = send.body();
            if(send.statusCode() >= 200 && send.statusCode() < 500 && encryptionUtility != null) {
                jsonResponse = encryptionUtility.decrypt(jsonResponse);
            }

            if(send.statusCode() == 200) {
                //System.out.println(jsonResponse);
                Type baseResponseType = getResponseType(operation);
                BaseResponse<?> baseResponse = gson.fromJson(jsonResponse, baseResponseType);

                if("1".equals(baseResponse.getErrorCode())) {
                    apiCallSuccess();
                    operation.setResponse(baseResponse.getData());
                }
                else {
                    throw new IOException("Operation error: '" + jsonResponse + "'");
                }
            }
            else {
                throw new IOException("Operation failed. ResponseCode " + send.statusCode() + ": '" + jsonResponse + "'");
            }
        } catch (InterruptedException | NumberFormatException e) {
            throw new RuntimeException("Unable to execute Operation. Json from server: '" + jsonResponse + "'.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
