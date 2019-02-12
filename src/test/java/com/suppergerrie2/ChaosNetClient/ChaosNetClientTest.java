package com.suppergerrie2.ChaosNetClient;

import com.suppergerrie2.ChaosNetClient.components.Organism;
import com.suppergerrie2.ChaosNetClient.components.Session;
import com.suppergerrie2.ChaosNetClient.components.TrainingRoom;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@FixMethodOrder(NAME_ASCENDING)
public class ChaosNetClientTest {

    @Test
    public void authorize() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        client.authenticate(getUsername(), getPassword(), false);

        assertTrue("Client not authenticated!", client.isAuthenticated());
    }

    private String getUsername() {
        String username = System.getProperty("chaosnet_username");

        if (username != null) {
            return username;
        }

        return System.getenv("chaosnet_username");
    }

    private String getPassword() {
        String password = System.getProperty("chaosnet_password");

        if (password != null) {
            return password;
        }

        return System.getenv("chaosnet_password");
    }

    @Test
    public void trainingRooms() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        System.out.println("Logging in!");
        client.authenticate(getUsername(), getPassword(), true);

        String name = "Debug-" + getRandomName(10);

        System.out.println("Creating room with name:" + name);
        assertTrue("Room creation failed!", client.createTrainingRoom(new TrainingRoom(name, "client-test", "chaoscraft")));

//        System.out.println("Checking if room was created!");
//        TrainingRoom[] result = client.getTrainingRooms();
//        assertNotNull(result);
//
//        boolean found = false;
//        for (TrainingRoom trainingRoom : result) {
//            if (trainingRoom.ownerName.equals(getUsername())
//                    && trainingRoom.roomName.equals(name)
//                    && trainingRoom.namespace.equals("client-test")
//                    && trainingRoom.simulationModelNamespace.equals("chaoscraft")) {
//                found = true;
//            }
//        }
//
//        assertTrue("Created room not found! Result was: \n" + getArrayAsString(result), found);

        System.out.println("Checking if single room can be found!");
        TrainingRoom room = client.getTrainingRoom(getUsername(), "client-test");
        assertNotNull("No room was found!", room);
        assertEquals("Name did not match!", room.roomName, name);

        client.getTrainingRoom("suppergerrie2", "supper-pong");
    }

    /**
     * Convert the trainingroom array to a nice looking string
     *
     * @param result The trainingroom array to convert
     * @return A nice looking string with the trainingrooms data.
     */
    private String getArrayAsString(TrainingRoom[] result) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        for (TrainingRoom room : result) {
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
     *
     * @param length The length of the random string
     * @return A randomly generated name
     */
    private String getRandomName(int length) {
        Random random = new Random();

        StringBuilder name = new StringBuilder();
        for (int i = 0; i < length; i++) {
            name.append((char) (random.nextInt(26) + 65));
        }

        return name.toString();
    }

    @Test
    public void startSession() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        client.authenticate(getUsername(), getPassword(), true);
        String name = "Debug-" + getRandomName(10);

        System.out.println("Creating room with name:" + name);
        client.createTrainingRoom(new TrainingRoom(name, "client-test", "chaoscraft"));

        TrainingRoom room = client.getTrainingRoom(getUsername(), "client-test");
        Session session = client.startSession(room);

        assertNotNull("Session is null!", session);

        assertNotNull("Namespace was null!", session.getNamespace());
        assertTrue("Session end time was < 0!", session.getSessionEndTime() > 0);
        assertNotNull("Session username was null!", session.getUsername());
    }

    @Test
    public void getOrganisms() throws IOException {
        ChaosNetClient client = new ChaosNetClient();

        client.authenticate(getUsername(), getPassword(), true);
        String name = "Debug-" + getRandomName(10);

        System.out.println("Creating room with name:" + name);
        client.createTrainingRoom(new TrainingRoom(name, "client-test-2", "suppercraft"));

        TrainingRoom room = client.getTrainingRoom(getUsername(), "client-test-2");
        Session session = client.startSession(room);

        Organism[] organisms = client.getOrganisms(session);

        System.out.println(session.getTrainingRoom().getStats().toString());

        assertNotNull("Organisms were null!", organisms);
        assertTrue("No organisms returned?",organisms.length>0);

        System.out.println("Current time: " + System.currentTimeMillis());
        for(Organism organism : organisms) {
            assertNotNull("Organism was null", organism);
            System.out.println("Organisms TTL is: " + organism.getTimeToLive() + ", it has " + organism.getLiveLeft() + " seconds left to live");
        }
    }
}