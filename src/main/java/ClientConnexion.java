import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class ClientConnexion implements Runnable {

    private static final long runningTimeS = 300L;
    private static final long sendIntervalInfo = 15L;
    private String restUrl;
    private String username;
    private String password;

    ClientConnexion() {
        restUrl = "http://localhost:8090/data";
        username = "myusername";
        password = "mypassword";

    }

    private static int randInt(int min, int max) {


        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Long startTime = System.nanoTime();
        do {
            JSONObject user = new JSONObject();
            JSONObject data = new JSONObject();
            int ville = randInt(1, 500);
            data.put("datatype", "temperature");
            data.put("value", String.valueOf(randInt(-30, 45)));
            user.put("data", data); // set Json object
            user.put("location", "ville" + String.valueOf(ville));
            user.put("deviceID", ville);
            String jsonData = user.toJSONString();
            //envoi de la requete en post
            HttpPostReq httpPostReq = new HttpPostReq();
            HttpPost httpPost = httpPostReq.createConnectivity(restUrl, username, password);
            httpPostReq.executeReq(jsonData, httpPost);
            try {
                Thread.sleep(sendIntervalInfo * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (((System.nanoTime() - startTime) / 1000000000.0f) < runningTimeS);
    }
}
