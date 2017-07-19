package com.tms.govt.champcash.home.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tms.govt.champcash.R;
import com.tms.govt.champcash.home.report.Report;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by govt on 19-04-2017.
 */

public class Utility {

    String protocol;
    String host;
    String port;
    Context ctx;

    public Utility(Context ctx) {
        this.ctx = ctx;
        protocol = ctx.getResources().getString(R.string.protocol);
//        host = ctx.getResources().getString(R.string.host);
//        port = ctx.getResources().getString(R.string.port);


//        c_port = ctx.getResources().getString(R.string.central_port);
//        os_port = ctx.getResources().getString(R.string.os_port);
//        tem_port = ctx.getResources().getString(R.string.tem_port);
    }

    public Report serviceResponse(JSONObject jsonObject,String URLString) {
        HttpURLConnection client = null;
        JSONObject response = null;
        StringBuilder result;
        OutputStreamWriter writer;
        InputStream input;
        String output, line;
        BufferedReader reader;
        Report report = new Report();

        try {

//            URL url = new URL("http://10.10.10.43:8082/Authentication/PilgrimLogIn");

            URL url = new URL(URLString);

            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(10000);
            client.setReadTimeout(10000);
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("Content-Type", "application/json");
//            client.setRequestProperty("etkn", encryptedString);
            client.setRequestMethod("POST");
            client.connect();
            writer = new OutputStreamWriter(client.getOutputStream());
            output = jsonObject.toString();
            writer.write(output);
            writer.flush();
            writer.close();
            input = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            response = new JSONObject(result.toString());
            report.setStatus("true");
            report.setJsonObject(response);
        } catch (ConnectException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (SocketTimeoutException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (Exception e) {
            report.setStatus("false");
            report.setErr_code(500);
            report.setMessage("Unknown error occurred, please try again");
        } finally {
            try {
                client.disconnect();
            } catch (NullPointerException ex) {
                report.setStatus("false");
                report.setErr_code(500);
                report.setMessage("Unknown error occurred, please try again");
            }
        }
        return report;
    }

    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


/*
    public Report serviceResponse(JSONObject jsonObject, String encryptedString, String URLString) {
        HttpURLConnection client = null;
        JSONObject response = null;
        StringBuilder result;
        OutputStreamWriter writer;
        InputStream input;
        String output, line;
        BufferedReader reader;
        Report report = new Report();

        try {

//            URL url = new URL("http://10.10.10.43:8082/Authentication/PilgrimLogIn");

            URL url = new URL(URLString);

            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(10000);
            client.setReadTimeout(10000);
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("etkn", encryptedString);
            client.setRequestMethod("POST");
            client.connect();
            writer = new OutputStreamWriter(client.getOutputStream());
            output = jsonObject.toString();
            writer.write(output);
            writer.flush();
            writer.close();
            input = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            response = new JSONObject(result.toString());
            report.setStatus("true");
            report.setJsonObject(response);
        } catch (ConnectException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (SocketTimeoutException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (Exception e) {
            report.setStatus("false");
            report.setErr_code(500);
            report.setMessage("Unknown error occurred, please try again");
        } finally {
            try {
                client.disconnect();
            } catch (NullPointerException ex) {
                report.setStatus("false");
                report.setErr_code(500);
                report.setMessage("Unknown error occurred, please try again");
            }
        }
        return report;
    }
*/

    public Report serviceResponse(JSONArray jsonArray, String encryptedString, String URLString) {
        HttpURLConnection client = null;
        JSONObject response = null;
        StringBuilder result;
        OutputStreamWriter writer;
        InputStream input;
        String output, line;
        BufferedReader reader;
        Report report = new Report();

        try {
            URL url = new URL(URLString);

            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(10000);
            client.setReadTimeout(10000);
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("etkn", encryptedString);
            client.setRequestMethod("POST");
            client.connect();
            writer = new OutputStreamWriter(client.getOutputStream());
            output = jsonArray.toString();
            writer.write(output);
            writer.flush();
            writer.close();
            input = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            response = new JSONObject(result.toString());
            report.setStatus("true");
            report.setJsonObject(response);
        } catch (ConnectException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (SocketTimeoutException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (Exception e) {
            report.setStatus("false");
            report.setErr_code(500);
            report.setMessage("Unknown error occurred, please try again");
        } finally {
            try {
                client.disconnect();
            } catch (NullPointerException ex) {
                report.setStatus("false");
                report.setErr_code(500);
                report.setMessage("Unknown error occurred, please try again");
            }
        }
        return report;
    }

    public Report getComplaintTemplesInfoResponse(String cURL) {

        Report report = new Report();
        HttpURLConnection client = null;
        JSONObject response = null;
        StringBuilder result;
        OutputStreamWriter writer;
        InputStream input;
        String output, line;
        BufferedReader reader;

        try {
            URL url = new URL(cURL);

            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.connect();
            // Read the input stream into temples_list_row String
            InputStream inputStream = client.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            output = buffer.toString();
            response = new JSONObject(output);
            report.setStatus("true");
            report.setJsonObject(response);
        } catch (ConnectException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (SocketTimeoutException e) {
            report.setStatus("false");
            report.setErr_code(401);
            report.setMessage("Connection timed out, please try again");
        } catch (Exception e) {
            report.setStatus("false");
            report.setErr_code(500);
            report.setMessage("Unknown error occurred, please try again");
        } finally {
            try {
                client.disconnect();
            } catch (NullPointerException ex) {
                report.setStatus("false");
                report.setErr_code(500);
                report.setMessage("Unknown error occurred, please try again");
            }
        }
        return report;
    }

}
