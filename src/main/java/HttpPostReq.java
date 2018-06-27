import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Classe gérant la connexion HTTP à proprement parler
 */
public class HttpPostReq {
    /**
     * Méthode main
     *
     * @param args Paramètres de la ligne de commande. Inutilisés.
     */
    public static void main(String args[]) {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new ClientConnexion());
            t.start();
        }
    }

    HttpPost createConnectivity(String restUrl, String username, String password) {
        HttpPost post = new HttpPost(restUrl);
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        post.setHeader("AUTHORIZATION", authHeader);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream", "true");
        return post;
    }

    void executeReq(String jsonData, HttpPost httpPost) {
        try {
            executeHttpRequest(jsonData, httpPost);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error while encoding api url : " + e);
        } catch (IOException e) {
            System.out.println("ioException occured while sending http request : " + e);
        } catch (Exception e) {
            System.out.println("exception occured while sending http request : " + e);
        } finally {
            httpPost.releaseConnection();
        }
    }

    private void executeHttpRequest(String jsonData, HttpPost httpPost) throws IOException {
        HttpResponse response;
        String line;
        StringBuilder result = new StringBuilder();
        httpPost.setEntity(new StringEntity(jsonData));
        HttpClient client = HttpClientBuilder.create().build();
        response = client.execute(httpPost);
        System.out.println("Post parameters : " + jsonData);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }
}