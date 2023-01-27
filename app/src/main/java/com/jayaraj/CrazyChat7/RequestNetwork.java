package com.jayaraj.CrazyChat7;

import android.app.Activity;

import java.util.HashMap;

public class RequestNetwork {
private HashMap<String, Object> params = new HashMap<>();
private HashMap<String, Object> headers = new HashMap<>();

private final Activity activity;

private int requestType;

public RequestNetwork(final Activity activity) {
this.activity = activity;
}

public void setHeaders(final HashMap<String, Object> headers) {
this.headers = headers;
}

public void setParams(final HashMap<String, Object> params, final int requestType) {
this.params = params;
this.requestType = requestType;
}

public HashMap<String, Object> getParams() {
return this.params;
}

public HashMap<String, Object> getHeaders() {
return this.headers;
}

public Activity getActivity() {
return this.activity;
}

public int getRequestType() {
return this.requestType;
}

public void startRequestNetwork(final String method, final String url, final String tag, final RequestListener requestListener) {
RequestNetworkController.getInstance().execute(this, method, url, tag, requestListener);
}

public interface RequestListener {
void onResponse(String tag, String response, HashMap<String, Object> responseHeaders);
void onErrorResponse(String tag, String message);
}
}
