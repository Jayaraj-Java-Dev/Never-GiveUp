package com.jayaraj.CrazyChat7;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestNetworkController {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static final int REQUEST_PARAM = 0;
    public static final int REQUEST_BODY = 1;

    private static final int SOCKET_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 25000;

    protected OkHttpClient client;

    private static RequestNetworkController mInstance;

    public static synchronized RequestNetworkController getInstance() {
        if (RequestNetworkController.mInstance == null) {
            RequestNetworkController.mInstance = new RequestNetworkController();
        }
        return RequestNetworkController.mInstance;
    }

    private OkHttpClient getClient() {
        if (this.client == null) {
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();

            try {
                TrustManager[] trustAllCerts = {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(final java.security.cert.X509Certificate[] chain, final String authType)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(final java.security.cert.X509Certificate[] chain, final String authType)
                                    throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.connectTimeout(RequestNetworkController.SOCKET_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.readTimeout(RequestNetworkController.READ_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.writeTimeout(RequestNetworkController.READ_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(final String hostname, final SSLSession session) {
                        return true;
                    }
                });
            } catch (final Exception e) {
            }

            this.client = builder.build();
        }

        return this.client;
    }

    public void execute(RequestNetwork requestNetwork, final String method, final String url, String tag, RequestNetwork.RequestListener requestListener) {
        final Request.Builder reqBuilder = new Request.Builder();
        final Headers.Builder headerBuilder = new Headers.Builder();

        if (requestNetwork.getHeaders().size() > 0) {
            final HashMap<String, Object> headers = requestNetwork.getHeaders();

            for (final HashMap.Entry<String, Object> header : headers.entrySet()) {
                headerBuilder.add(header.getKey(), String.valueOf(header.getValue()));
            }
        }

        try {
            if (requestNetwork.getRequestType() == RequestNetworkController.REQUEST_PARAM) {
                if (method.equals(RequestNetworkController.GET)) {
                    final HttpUrl.Builder httpBuilder;

                    try {
                        httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
                    } catch (final NullPointerException ne) {
                        throw new NullPointerException("unexpected url: " + url);
                    }

                    if (requestNetwork.getParams().size() > 0) {
                        final HashMap<String, Object> params = requestNetwork.getParams();

                        for (final HashMap.Entry<String, Object> param : params.entrySet()) {
                            httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }

                    reqBuilder.url(httpBuilder.build()).headers(headerBuilder.build()).get();
                } else {
                    final FormBody.Builder formBuilder = new FormBody.Builder();
                    if (requestNetwork.getParams().size() > 0) {
                        final HashMap<String, Object> params = requestNetwork.getParams();

                        for (final HashMap.Entry<String, Object> param : params.entrySet()) {
                            formBuilder.add(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }

                    final RequestBody reqBody = formBuilder.build();

                    reqBuilder.url(url).headers(headerBuilder.build()).method(method, reqBody);
                }
            } else {
                final RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(requestNetwork.getParams()));

                if (method.equals(RequestNetworkController.GET)) {
                    reqBuilder.url(url).headers(headerBuilder.build()).get();
                } else {
                    reqBuilder.url(url).headers(headerBuilder.build()).method(method, reqBody);
                }
            }

            final Request req = reqBuilder.build();

            this.getClient().newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, IOException e) {
                    requestNetwork.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestListener.onErrorResponse(tag, e.getMessage());
                        }
                    });
                }

                @Override
                public void onResponse(final Call call, Response response) throws IOException {
                    assert response.body() != null;
                    String responseBody = response.body().string().trim();
                    requestNetwork.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Headers b = response.headers();
                            final HashMap<String, Object> map = new HashMap<>();
                            for (final String s : b.names()) {
                                map.put(s, b.get(s) != null ? b.get(s) : "null");
                            }
                            requestListener.onResponse(tag, responseBody, map);
                        }
                    });
                }
            });
        } catch (final Exception e) {
            requestListener.onErrorResponse(tag, e.getMessage());
        }
    }
}