package kr.ypshop.softend.file.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import kr.ypshop.softend.exception.FileException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

@AllArgsConstructor
public class FileRepo {

    private final Plugin plugin;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 데이터를 JSON 방식으로 저장합니다.
     * @param data (Savable 의 구현체)
     * @param fileName 무조건 .json 확장자를 사용해야합니다. ( .json 확장자 미사용시 강제로 추가함 )
     */
    public boolean saveData(Savable data, String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName = fileName + ".json";
        }

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), fileName);
        try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("data", data.toJson());
            jsonObject.addProperty("class", data.getClass().getName());

            gson.toJson(jsonObject, writer);
            plugin.getLogger().info(String.format("%s을 성공적으로 저장했습니다."));
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe(String.format("%s을 저장하는 중 오류가 발생했습니다.", fileName));
            return false;
        }

        return true;
    }

    /**
     * JSON 방식으로 저장된 데이터를 불러옵니다.
     * @param clazz 불러올 데이터의 원래 클래스
     * @param fileName 무조건 .json 확장자를 사용해야합니다. <br>( .json 확장자 미사용시 강제로 추가함 )
     * @param ignoreClassDifferent 파일에 저장된 클래스명과 clazz 값이 달라도<br>무시하는지 여부 ( true 는 무시 )
     */
    public<T> T loadData(Class<T> clazz, String fileName, boolean ignoreClassDifferent) {
        if (!fileName.endsWith(".json")) {
            fileName = fileName + ".json";
        }

        if (!Arrays.stream(clazz.getInterfaces()).anyMatch(aClass -> aClass.equals(Savable.class))) {
            new FileException("불러오기 하는 데이터는 무조건 Savable 을 interface 로 사용해야합니다").printStackTrace();
            return null;
        }

        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            return null;
        }

        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            if (!jsonObject.has("class") || !jsonObject.has("data")) {
                throw new IOException("파일 형식이 일치하지 않습니다.");
            }

            if (!clazz.getName().equals(jsonObject.get("class").getAsString()) && !ignoreClassDifferent) {
                throw new IOException("파일에 저장된 클래스명과 clazz 값이 다릅니다.");
            }

            T data = gson.fromJson(jsonObject.getAsJsonObject("data"), clazz);
            plugin.getLogger().severe(String.format("%s을 성공적으로 불러왔습니다.", fileName));

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe(String.format("%s을 불러오는 중 오류가 발생했습니다.", fileName));
            return null;
        }
    }

}
