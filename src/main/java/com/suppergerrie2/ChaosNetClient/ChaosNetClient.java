package com.suppergerrie2.ChaosNetClient;

import com.google.gson.*;
import com.suppergerrie2.ChaosNetClient.components.*;
import com.suppergerrie2.ChaosNetClient.components.nnet.NeuralNetwork;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.AbstractNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.HiddenNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.InputNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.OutputNeuron;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * The ChaosNetClient which will hide all of the magic so you can for example call {@link #createTrainingRoom(TrainingRoom)} to create room.
 *
 * @author suppergerrie2
 */
public class ChaosNetClient {

    private Gson gson = new GsonBuilder().create();
    private Authentication auth;

    private HashMap<String, TrainingRoom> trainingRoomByNamespace = new HashMap<>();

    private HashMap<String, AbstractNeuron> typeToClassMap = new HashMap<>();

    private Organism organismToUse = new Organism();

    private boolean saveRefreshToken;
    private boolean saveSessions = true;

    FileHelper fileHelper;

    {
        try {
            fileHelper = new FileHelper("chaosnet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authorizes with the chaosnet server so this client can make authorized requests.
     *
     * @param username The username to authorize with
     * @param password The password to authorize with
     * @param useCode  If true an attempt will be made to load a refresh code, the refresh code will also be saved if no refresh code is found/
     * @throws IOException Thrown when the URL is not valid (Eg host {@link Constants#HOST} is invalid). Or when the post request fails.
     * @author suppergerrie2
     */
    public void authenticate(String username, String password, boolean useCode) throws IOException {
        this.saveRefreshToken = useCode;
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("Authorizing already authorized client!");
        }

        if (useCode) {
            try {
                JsonObject fileData = fileHelper.loadAsJson("data-" + username + ".json").getAsJsonObject();

                if (fileData != null && fileData.has("refreshToken")) {
                    auth = new Authentication().setClient(this).setUserName(username);
                    auth.requestAuthToken(username, fileData.get("refreshToken").getAsString());
                    return;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        URL url = new URL(Constants.HOST + "/v0/auth/login");

        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        object.addProperty("password", password);

        JsonObject response = doPostRequest(url, object).getAsJsonObject();
        this.auth = gson.fromJson(response, Authentication.class).setClient(this).setUserName(username);

        saveRefreshCode();
    }

    public void saveRefreshCode() {
        if (!saveRefreshToken) return;

        JsonObject object = new JsonObject();
        object.addProperty("username", auth.getUserName());
        object.addProperty("refreshToken", auth.getRefreshToken());
        try {
            fileHelper.saveJson("data-" + auth.getUserName() + ".json", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a trainingRoom based on the trainingroom passed in. Needs the client to be authorized using {@link #authenticate} first.
     *
     * @param trainingRoom The trainingroom to create
     * @return If successful true will be returned. (No check is made to guarantee it succeeded)
     * @author suppergerrie2
     */
    @SuppressWarnings("WeakerAccess")
    public boolean createTrainingRoom(TrainingRoom trainingRoom) {
        try {
            URL url = new URL(Constants.HOST + "/v0/trainingrooms");

            doPostRequest(url, gson.toJsonTree(trainingRoom).getAsJsonObject(), true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all of the trainingrooms from the chaosnet server.
     *
     * @return An array of {@link TrainingRoom} with all of the trainingrooms found.
     * @author suppergerrie2
     */
    @SuppressWarnings("WeakerAccess")
    public TrainingRoom[] getTrainingRooms() {
        try {
            JsonArray result = doGetRequest(new URL(Constants.HOST + "/v0/trainingrooms"), false).getAsJsonArray();

            TrainingRoom[] trainingRooms = new TrainingRoom[result.size()];

            for (int i = 0; i < result.size(); i++) {
                String nameSpace = result.get(i).getAsJsonObject().get("namespace").getAsString();

                TrainingRoom receivedRoom = gson.fromJson(result.get(i).getAsJsonObject(), TrainingRoom.class);

                if (result.get(i).getAsJsonObject().has("fitnessRules")) {
                    if (result.get(i).getAsJsonObject().get("fitnessRules").isJsonArray()) {
                        receivedRoom.parseFitnessRules(result.get(i).getAsJsonObject().getAsJsonArray("fitnessRules"));
                    }
                }

                if (trainingRoomByNamespace.containsKey(receivedRoom.ownerName + "-" + nameSpace)) {
                    TrainingRoom room = trainingRoomByNamespace.get(receivedRoom.ownerName + "-" + nameSpace);
                    receivedRoom.update(receivedRoom);
                    trainingRooms[i] = room;
                } else {
                    trainingRooms[i] = receivedRoom;
                    trainingRoomByNamespace.put(receivedRoom.ownerName + "-" + receivedRoom.namespace, receivedRoom);
                }
            }

            return trainingRooms;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a specific {@link TrainingRoom} from a specific user.
     *
     * @param username  The owner's username
     * @param namespace The {@link TrainingRoom}'s namespace
     * @return The {@link TrainingRoom} with returned values set.
     */
    @SuppressWarnings("WeakerAccess")
    public TrainingRoom getTrainingRoom(String username, String namespace) {
        try {
            URL url = new URL(Constants.HOST + "/v0/" + username + "/trainingrooms/" + namespace);

            JsonElement element = doGetRequest(url, true);

            TrainingRoom room = gson.fromJson(element, TrainingRoom.class);

            if (element.getAsJsonObject().has("fitnessRules")) {
                room.parseFitnessRules(element.getAsJsonObject().getAsJsonArray("fitnessRules"));
            }

            if (trainingRoomByNamespace.containsKey(room.ownerName + "-" + room.namespace)) {
                return trainingRoomByNamespace.get(room.ownerName + "-" + room.namespace).update(room);
            }

            trainingRoomByNamespace.put(room.ownerName + "-" + room.namespace, room);
            return room;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Start session for the passed {@link TrainingRoom}
     *
     * @param room The trainingRoom to start the session on
     * @return The {@link Session} object.
     */
    @SuppressWarnings("WeakerAccess")
    public Session startSession(TrainingRoom room) {

        System.out.println("Trying to load session...");
        Session session = Session.loadFromFile(room, auth.getUserName(), fileHelper);

        if (session != null) {
            System.out.println("Loaded session from file!");
            System.out.println("Session is " + (session.isValid() ? "valid" : "invalid"));
            if (session.isValid()) {
                session.setTrainingRoom(room);
                return session;
            }
        }

        System.out.println("No session loaded, creating session...");
        try {
            URL url = new URL(Constants.HOST + "/v0/" + room.ownerName + "/trainingrooms/" + room.namespace + "/sessions/start");

            JsonElement element = doPostRequest(url, null, true);

            session = gson.fromJson(element, Session.class);
            session.setTrainingRoom(room);
            session.saveToFile(fileHelper);
            return session;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public Organism[] getOrganisms(Session session) {
        return getOrganisms(session, new Organism[0]);
    }

    public Organism[] getOrganisms(Session session, Organism[] testedOrganisms) {
        try {
            URL url = new URL(Constants.HOST + "/v0/" + session.getTrainingRoom().ownerName + "/trainingrooms/" + session.getTrainingRoom().namespace + "/sessions/" + session.getNamespace() + "/next");

            JsonObject body = null;

            if (testedOrganisms.length > 0) {
                body = new JsonObject();
                JsonArray toReport = new JsonArray();
                for (Organism organism : testedOrganisms) {
                    JsonObject organismObject = new JsonObject();
                    organismObject.addProperty("namespace", organism.getNamespace());
                    organismObject.addProperty("score", organism.getScore());
                    toReport.add(organismObject);
                }
                body.add("report", toReport);
            }


            JsonElement element = doPostRequest(url, body, true);

            JsonArray array = element.getAsJsonObject().getAsJsonArray("organisms");

            Organism[] organisms = new Organism[array.size()];

            for (int i = 0; i < array.size(); i++) {
                organisms[i] = gson.fromJson(array.get(i), organismToUse.getClass());
                parseNeuralNetwork(organisms[i], array.get(i).getAsJsonObject().getAsJsonObject("nNet"));
                organisms[i].parseBiologyFromJson(array.get(i).getAsJsonObject().getAsJsonObject("nNet").getAsJsonObject("biology"));
            }

            TrainingRoomStats stats = gson.fromJson(element.getAsJsonObject().getAsJsonObject("stats"), TrainingRoomStats.class);
            session.getTrainingRoom().setStats(stats);

            return organisms;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseNeuralNetwork(Organism organism, JsonObject neuralNetwork) {
        JsonArray neurons = neuralNetwork.getAsJsonArray("neurons");

        NeuralNetwork network = new NeuralNetwork();

        for (int i = 0; i < neurons.size(); i++) {
            JsonObject neuronJson = neurons.get(i).getAsJsonObject();
            String type = neuronJson.get("$TYPE").getAsString();

            AbstractNeuron neuronType;
            if (!typeToClassMap.containsKey(type)) {
                System.err.println("Trying to find neuron type " + type + " but this type is not registered!");

                switch (neuronJson.get("_base_type").getAsString()) {
                    case "input":
                        neuronType = new InputNeuron();
                        break;
                    case "output":
                        neuronType = new OutputNeuron();
                        break;
                    case "hidden":
                    default:
                        neuronType = new HiddenNeuron();
                }
                System.err.println("Falling back to " + neuronType.getClass() + "!");
            } else {
                neuronType = typeToClassMap.get(type);
            }

            AbstractNeuron neuron = neuronType.parseFromJson(neuronJson, organism);
            network.addNeuron(neuron);
        }

        network.buildStructure();

        organism.setNetwork(network);
    }

    public void registerNeuronType(String type, AbstractNeuron neuron) {
        if (this.typeToClassMap.containsKey(type)) {
            System.out.println("Registering already registered neuron! Type being registered " + type);
        }

        this.typeToClassMap.put(type, neuron);
    }

    public void registerCustomOrganismType(Organism organism) {
        this.organismToUse = organism;
    }

    /**
     * Check if this client is authenticated
     *
     * @return true if this client is authenticated
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isAuthenticated() {
        return auth != null && auth.isAuthenticated();
    }

    /**
     * Writes the json object to the given {@link HttpURLConnection} body
     *
     * @param object The object to be written to the body.
     * @param con    The connection to be written to
     * @throws IOException
     * @author suppergerrie2
     */
    private void writeToConnectionBody(JsonObject object, HttpURLConnection con) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(gson.toJson(object));
        writer.close();
    }

    /**
     * Do a get request to the given {@link URL} and return the result as an {@link JsonObject}
     *
     * @param url        The url to do the get request to.
     * @param authorized Whether this request should use the accesstoken.
     * @return The result is wrapped in a jsonelement.
     * @throws IOException
     * @author suppergerrie2
     */
    @SuppressWarnings("SameParameterValue")
    private JsonElement doGetRequest(URL url, boolean authorized) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if (authorized) {
            auth.authenticateConnection(con);
        }

        if (con.getResponseCode() == 200) {
            return new JsonParser().parse(new InputStreamReader(con.getInputStream()));
        } else {
            System.err.println("----------------------------------------------------------------------------------");
            System.err.print("Failed GET Request: ");
            System.err.println(con.getResponseCode());
            String result = new BufferedReader(new InputStreamReader(con.getErrorStream()))
                    .lines().collect(Collectors.joining("\n"));
            System.err.println(result);
        }

        return new JsonObject();
    }

    /**
     * Do a post request to the given {@link URL} with the given {@link JsonObject} as body.
     * If this client is authorized it will do an authorized post request. Use {@link #doPostRequest(URL, JsonObject, boolean)} for more control.
     *
     * @param url  The URL to do the post request to
     * @param body The body to send with the post request.
     * @return The result of the post request as a {@link JsonElement}
     * @throws IOException
     * @author suppergerrie2
     */
    private JsonElement doPostRequest(URL url, JsonObject body) throws IOException {
        return doPostRequest(url, body, auth != null);
    }

    /**
     * Do a post request to the given {@link URL} with the given {@link JsonObject} as body
     *
     * @param url        The URL to do the post request to
     * @param body       The body to send with the post request.
     * @param authorized If true this request will be authorized using {@link Authentication#authenticateConnection(HttpURLConnection)}
     * @return The result of the post request as a {@link JsonElement}
     * @throws IOException
     * @author suppergerrie2
     */
    public JsonElement doPostRequest(URL url, JsonObject body, boolean authorized) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        if (authorized) {
            auth.authenticateConnection(con);
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        if (body != null) {
            writeToConnectionBody(body, con);
        }

        if (con.getResponseCode() == 200) {
            return new JsonParser().parse(new InputStreamReader(con.getInputStream()));
        } else {
            System.err.println("----------------------------------------------------------------------------------");
            System.err.print("Failed POST Request: ");
            System.err.println(con.getResponseCode());
            String result = new BufferedReader(new InputStreamReader(con.getErrorStream()))
                    .lines().collect(Collectors.joining("\n"));
            System.err.println(result);
        }

        return new JsonObject();
    }
}
