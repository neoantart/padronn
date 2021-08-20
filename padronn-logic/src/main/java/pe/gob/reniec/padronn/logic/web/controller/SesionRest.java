package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SesionRest {

    public static String post(String url, String secret, String respuesta){

        StringBuilder retorno = new StringBuilder();
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            HttpPost postRequest = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("secret", secret));
            params.add(new BasicNameValuePair("response", respuesta));
            postRequest.setEntity(new UrlEncodedFormEntity(params));

            RequestConfig.Builder requestConfig = RequestConfig.custom();
            requestConfig.setConnectTimeout(3 * 1000);
            requestConfig.setConnectionRequestTimeout(3 * 1000);
            requestConfig.setSocketTimeout(3 * 1000);

            postRequest.setConfig(requestConfig.build());

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent()),"UTF-8"));

            String output;
            while ((output = br.readLine()) != null) {
                retorno.append(output);
            }

            httpClient.close();
            httpClient = null;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return retorno.toString();
    }

    public static String post(String url, String json){

        StringBuilder retorno = new StringBuilder();
        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(url);

            RequestConfig.Builder requestConfig = RequestConfig.custom();
            requestConfig.setConnectTimeout(3 * 1000);
            requestConfig.setConnectionRequestTimeout(3 * 1000);
            requestConfig.setSocketTimeout(3 * 1000);

            postRequest.setConfig(requestConfig.build());

            postRequest.addHeader("Content-Type","application/json;charset=utf-8");

            StringEntity input = new StringEntity(json,"UTF-8");
            input.setContentType("application/json;charset=utf-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent()),"UTF-8"));

            String output;
            while ((output = br.readLine()) != null) {
                retorno.append(output);
            }

            httpClient.close();
            httpClient = null;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return retorno.toString();

    }

}
