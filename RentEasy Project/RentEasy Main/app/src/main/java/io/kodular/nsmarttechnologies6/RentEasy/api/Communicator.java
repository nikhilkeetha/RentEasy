package io.RentEasy.api;

import android.content.Context;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by arthonsystechnologiesllp on 03/03/17.
 */

public class Communicator {

    public void post(final int reqCode, final Context context, String url, RequestParams params, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);


    }

    public void postWithoutHeader(final int reqCode, final Context context, String url, RequestParams params, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);


    }


    public void postMultipart(final int reqCode, final Context context, String url, RequestParams params, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "multipart/form-data");
        client.setTimeout(5 * 60000);
        client.setResponseTimeout(5 * 60000);
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseListener.onFailure(reqCode, error);
            }

        });

    }

    public void get(final int reqCode, final Context context, String url, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(1, 60000);
        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.hideProgressBar();
                Utils.printLog("Error", error.toString());
                responseListener.onFailure(reqCode, error);
            }

        });
        Utils.printLog("URL", url);

    }

    public void get(final int reqCode, final Context context, String url, RequestParams params, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 5 * 60000);
        client.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }
        });
        Utils.printLog("URL", url);

    }

    public void post(final int reqCode, final Context context, String url, StringEntity entity, final io.RentEasy.api.CustomResponseListener responseListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5 * 60000);
        client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Utils.printLog("Response", response);
                responseListener.onResponse(reqCode, response.trim());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.printLog("Error", error.toString());
                Utils.hideProgressBar();
                responseListener.onFailure(reqCode, error);
            }

        });

        Utils.printLog("URL", url);

    }


}