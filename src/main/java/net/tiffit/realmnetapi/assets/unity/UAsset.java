package net.tiffit.realmnetapi.assets.unity;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UAsset {

    private static Yaml yaml = new Yaml();

    private final Map<String, Object> map;

    private UAsset(Map<String, Object> map){
        this.map = map;
    }

    @SneakyThrows
    public static UAsset load(Path path){
        List<String> lines = Files.readAllLines(path);
        lines = lines.stream().filter(s -> !s.startsWith("%")).map(s -> s.startsWith("---") ? "---" : s).collect(Collectors.toList());
        List<StringBuilder> documents = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        documents.add(builder);
        for (String line : lines) {
            if(line.startsWith("---")){
                builder = new StringBuilder("%YAML 1.1\n");
                documents.add(builder);
            }
            builder.append(line).append("\n");
        }
        Map<String, Object> fullMap = new HashMap<>();
        for (StringBuilder document : documents) {
            if(!document.isEmpty()){
                Map<String, Object> map = yaml.load(document.toString());
                fullMap.putAll(map);
            }
        }
        return new UAsset(fullMap);
    }

    public <T> T getValue(String pathComb){
        String[] path = pathComb.split("/");
        Map<String, Object> curMap = map;
        for (int i = 0; i < path.length - 1; i++) {
            curMap = (Map<String, Object>) curMap.get(path[i]);
        }
        return (T)curMap.get(path[path.length-1]);
    }

}
