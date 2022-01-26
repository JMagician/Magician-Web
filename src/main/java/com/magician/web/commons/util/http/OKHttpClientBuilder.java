package com.magician.web.commons.util.http;

import com.magician.web.cloud.config.CloudConfig;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * okHttp客户端构建类
 */
public class OKHttpClientBuilder {

    private static Logger logger = LoggerFactory.getLogger(OKHttpClientBuilder.class);

    /**
     * 构建一个 忽略ssl验证的 okHttp客户端
     *
     * @return
     */
    public static OkHttpClient.Builder buildOKHttpClient() {
        try {
            TrustManager[] trustAllCerts = buildTrustManagers();
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            if(CloudConfig.getProxy() != null){
                builder.proxy(new Proxy(Proxy.Type.HTTP, CloudConfig.getProxy()));
            }

            builder.hostnameVerifier((hostname, session) -> true);
            builder.callTimeout(CloudConfig.getTimeout(), TimeUnit.MILLISECONDS);
            builder.connectTimeout(1000, TimeUnit.MILLISECONDS);
            return builder;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("构建okHttpClient异常", e);
            return new OkHttpClient.Builder();
        }
    }

    /**
     * 忽略ssl
     * @return
     */
    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }
}
