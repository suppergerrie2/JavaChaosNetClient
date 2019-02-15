package com.suppergerrie2.ChaosNetClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class FileHelper {

    public final Path rootFolder;

    public FileHelper(String rootFolder) throws IOException {
        File file = new File(rootFolder);
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdirs()) {
                throw new IOException("Failed to create root folder!");
            }
        }

        this.rootFolder = file.toPath();
    }

    public JsonElement loadAsJson(String fileName) throws FileNotFoundException {
        Path path = rootFolder.resolve(fileName);

        File file = path.toFile();

        JsonParser parser = new JsonParser();
        return parser.parse(new FileReader(file));
    }

    public void saveJson(String fileName, JsonElement json) throws IOException {
        Path path = rootFolder.resolve(fileName);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(json);

        File file = path.toFile();

        if (!file.exists() || !file.isFile()) {
            file.createNewFile();
        }

        Files.write(path, Collections.singleton(jsonString));
    }

    public void appendToFile(String filename, String text) throws IOException {
        File file = rootFolder.resolve(filename).toFile();
        if (!file.exists()) {
            file.createNewFile();
        }

        Files.write(file.toPath(), Collections.singletonList(text), StandardOpenOption.APPEND);
    }
}
