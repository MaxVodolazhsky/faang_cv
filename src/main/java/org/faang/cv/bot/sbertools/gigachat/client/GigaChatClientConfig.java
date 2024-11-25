package org.faang.cv.bot.sbertools.gigachat.client;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.okhttp.OkHttpClient;
import lombok.SneakyThrows;
import org.faang.cv.bot.sbertools.provider.AccessTokenProvider;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
public class GigaChatClientConfig {

    private static final String CERT_PATH = "src/main/resources/russiantrustedca.pem";

    @Bean
    public RequestInterceptor interceptor(AccessTokenProvider tokenProvider) {
        return new GigaChatRequestInterceptor(tokenProvider);
    }

    @Bean
    public Decoder decoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverters));

    }

    @Bean
    @SneakyThrows
    public OkHttpClient client() {
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

        return new OkHttpClient(new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build());
    }
}
