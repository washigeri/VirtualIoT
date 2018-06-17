import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class ClientConnexion implements Runnable {

    //Notre liste de commandes. Le serveur nous répondra différemment selon la commande utilisée.
    private static int count = 0;
    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String commamd = "";
    private String restUrl = null;
    private String username = null;
    private String password = null;
    private String jsonData = null;
    private String name = "Client-";

    public ClientConnexion() {
        name += ++count;
        restUrl = "http://localhost:8080/";
        username = "myusername";
        password = "mypassword";

    }

    public static int randInt(int min, int max) {


        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

        return randomNum;
    }

    public void run() {

        JSONObject user = new JSONObject();
        JSONObject data = new JSONObject();

        int ville = randInt(1, 500);

        // set Json object
        data.put("datatype", "temperature");
        data.put("value", String.valueOf(randInt(-30, 45)));
        user.put("location", "ville" + String.valueOf(ville));
        user.put("deviceID", String.valueOf(ville));
        user.put("data", data);
        jsonData = user.toString();


        //envoie de la requete en post
        HttpPostReq httpPostReq = new HttpPostReq();
        HttpPost httpPost = httpPostReq.createConnectivity(restUrl, username, password);
        httpPostReq.executeReq(jsonData, httpPost);
    }
}
