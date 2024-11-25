package org.faang.cv.bot.sbertools.gigachat.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.faang.cv.bot.generated.tokenprovider.model.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class AccessTokenGigaChatClient {

    private static final String CERT_PATH = "src/main/resources/russiantrustedca.pem";
    private static final String DEFAULT_SCOPE_GIGA_CHAT = "GIGACHAT_API_PERS";

    private final OkHttpClient client;
    private final String url;
    private final String clientSecret;
    private final String clientId;
    private final ObjectMapper mapper;

    public AccessTokenGigaChatClient(
            @Value("${access.token.url}") String url,
            @Value("${gigachat.api.token.clientSecret}") String clientSecret,
            @Value("${gigachat.api.token.clientId}") String clientId,
            ObjectMapper mapper
    ) {
        this.client = client();
        this.url = url;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
        this.mapper = mapper;
    }

    @SneakyThrows
    private static OkHttpClient client() {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream certInput = new FileInputStream(CERT_PATH);
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(certInput);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("caCert", caCert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @SneakyThrows
    public Token getToken(String rqUID) {
        String authKey = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret)
                .getBytes(StandardCharsets.UTF_8));

        RequestBody formBody = new FormBody.Builder()
                .add("scope", DEFAULT_SCOPE_GIGA_CHAT)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("RqUID", rqUID)
                .addHeader("Authorization", "Bearer " + authKey)
                .build();

        try (Response resp = client.newCall(request).execute()) {
            return mapper.readValue(resp.body().string(), Token.class);
        }
    }
}
