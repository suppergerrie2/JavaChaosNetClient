package com.suppergerrie2.ChaosNetClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.suppergerrie2.ChaosNetClient.components.TrainingRoom;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@FixMethodOrder(NAME_ASCENDING)
public class ChaosNetClientTest {

    @Test
    public void authorize() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        client.Authorize(getUsername(), getPassword(), false);

        assertNotNull("AccessToken should not be null!", client.accessToken);
        assertNotNull("RefreshToken should not be null!", client.refreshToken);
    }

    String getUsername() {
        String username = System.getProperty("username");

        if(username!=null) {
            return username;
        }

        return System.getenv("username");
    }

    String getPassword() {
        String password = System.getProperty("password");

        if(password!=null) {
            return password;
        }

        return System.getenv("password");
    }

    @Test
    public void trainingRooms() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        System.out.println("Logging in!");
        client.Authorize(getUsername(), getPassword(), false);

        String randomName = getRandomName(10);

        System.out.println("Creating room with name: Debug-"+randomName);
        assertTrue("Room creation failed!", client.createTrainingRoom(new TrainingRoom("Debug-"+randomName, "client-test")));

        System.out.println("Checking if room was created!");
        TrainingRoom[] result = client.getTrainingRooms();
        assertNotNull(result);

        boolean found = false;
        for(TrainingRoom trainingRoom : result) {
            if(trainingRoom.ownerName.equals(System.getProperty("username"))
                && trainingRoom.roomName.equals("Debug-"+randomName)
                && trainingRoom.namespace.equals("client-test")) {
                found = true;
            }
        }

        assertTrue("Created room not found! Result was: \n" + getArrayAsString(result), found);
    }

    /**
     * Convert the trainingroom array to a nice looking string
     * @param result The trainingroom array to convert
     * @return A nice looking string with the trainingrooms data.
     */
    private String getArrayAsString(TrainingRoom[] result) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        for(TrainingRoom room : result) {
            stringBuilder.append("    {\n");
            stringBuilder.append("        RoomName: ").append(room.roomName).append("\n");
            stringBuilder.append("        OwnerName: ").append(room.ownerName).append("\n");
            stringBuilder.append("        Namespace: ").append(room.namespace).append("\n");
            stringBuilder.append("        PartitionNamespace: ").append(room.partitionNamespace).append("\n");
            stringBuilder.append("    }\n");
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    /**
     * Get a random generated name with the passed length consisting of [A-Z]
     * @param length The length of the random string
     * @return A randomly generated name
     */
    String getRandomName(int length) {
        Random random = new Random();

        StringBuilder name = new StringBuilder();
        for(int i = 0; i < length; i++) {
            name.append((char) (random.nextInt(26) + 65));
        }

        return name.toString();
    }
}