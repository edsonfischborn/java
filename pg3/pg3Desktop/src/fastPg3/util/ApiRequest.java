package fastPg3.util;

import fastPg3.types.DeliveryDetailDto;
import fastPg3.types.Message;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Edson Fischborn
 */
public class ApiRequest {
    private HttpURLConnection connection;
    private BufferedReader reader;
    private ArrayList<String[]> headers;
    private Gson gson = new Gson();
    private String rootUrl = "http://192.168.0.105:8080";

    public ApiRequest(){
        this.setDefaultHeaders();
    }

    public ApiRequest(String rootUrl){
        this.setRootUrl(rootUrl);
        this.setDefaultHeaders();
    }

    private void setDefaultHeaders(){
        String[] contentType = {"Content-Type", "application/json; utf-8"};
        String[] accept = {"Accept", "application/json"};
        this.headers = new ArrayList<String[]>(){{
            add(contentType);
            add(accept);
        }};
    }

    private void setRequestHeaders(){
        this.headers.forEach(headerArray -> {
            connection.addRequestProperty(headerArray[0], headerArray[1]);
        });
    }

    private void openConnection(String path) throws Exception {
        String fullUrl = rootUrl + path;

        URL url = new URL(fullUrl);
        connection = (HttpURLConnection) url.openConnection();
        setRequestHeaders();
    }

    private String getResponse() throws Exception{
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder json = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null ){
            json.append(line);
        }

        return json.toString();
    }

    public JsonElement get(String path) throws Exception{
        this.openConnection(path);
        connection.setRequestMethod("GET");

        String jsonResponse = getResponse();
        closeConnection();

        return gson.fromJson(jsonResponse, JsonElement.class);
    }

    public JsonElement post(String path, String requestBody) throws Exception {
        return submitData(path, requestBody, "POST");
    }

    public JsonElement put(String path, String requestBody) throws Exception {
        return submitData(path, requestBody, "PUT");
    }

    public JsonElement delete(String path) throws Exception{
        this.openConnection(path);
        connection.setRequestMethod("DELETE");

        String jsonResponse = getResponse();
        closeConnection();

        return gson.fromJson(jsonResponse, JsonElement.class);
    }

    public JsonElement submitData(String path, String requestBody, String method) throws Exception{
        // method = POST OR PUT
        this.openConnection(path);

        connection.setRequestMethod(method);
        connection.setDoOutput(true);

        OutputStream outStream = connection.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
        outStreamWriter.write(requestBody);
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();

        String jsonResponse = getResponse();

        closeConnection();
        return gson.fromJson(jsonResponse, JsonElement.class);
    }

    public void closeConnection(){
        if(connection != null){
            connection.disconnect();
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public ArrayList<String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<String[]> headers) {
        this.headers = headers;
    }

    public static void main(String[] args) {
        try {
            // Create a fictitious message
            Gson g = new Gson();
            Message mToPost = new Message("tiagãoo", "ed@difrente", "sei la, uma mensagem");
            String body = g.toJson(mToPost, Message.class);

            // Create api instance
            ApiRequest api = new ApiRequest("http://192.168.0.105:8080");
            String[] auth = {"Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1AcGczLmNvbSIsImV4cCI6MTYwNjI3OTAxNSwiaWF0IjoxNjA2MjYxMDE1LCJqdGkiOiIxIn0.mo02UheA11wiVF3keTj7tpPeBftXCgHjAJBIuaeUsG9Z6v-LwzXBK2JfXH_JEryEQPrbPEZ8ZHQvawkeqAt9sw"};
            api.getHeaders().add(auth);

            JsonObject json = api.get("/delivery").getAsJsonObject();
            JsonArray deliveryContent = json.getAsJsonArray("content");
            DeliveryDetailDto d = g.fromJson(deliveryContent.get(0), DeliveryDetailDto.class );


            // JsonObject json = api.get("/message/2");
            // JsonObject json = api.post("/message");
            // JsonObject json = api.put("/message/1");
            // JsonObject json = api.delete("/message/1");

            System.out.println(d.getDeliveryMan().getEmail());

            /*JsonObject jsonObject = g.fromJson(jsonSb.toString(), JsonObject.class);
            JsonArray messageContent = jsonObject.getAsJsonArray("content");
            Message m = g.fromJson(messageContent.get(0), Message.class );*/
            
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
