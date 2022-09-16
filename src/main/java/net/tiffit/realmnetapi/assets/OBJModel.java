package net.tiffit.realmnetapi.assets;
import lombok.SneakyThrows;
import net.tiffit.realmnetapi.util.math.Vec2f;
import net.tiffit.realmnetapi.util.math.Vec3f;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OBJModel implements  Serializable{

    public static HashMap<String, OBJModel> MODELS = new HashMap<>();

    public List<Vec3f> vertices = new ArrayList<>();
    public List<Vec2f> vertexTextures = new ArrayList<>();
    public List<Vec3f> vertexNormals = new ArrayList<>();
    public List<OBJVertexDefinition[]> faces = new ArrayList<>();
    public List<String> faceMaterals = new ArrayList<>();

    @SneakyThrows
    public static void LoadModels(File directory){
        for (File file : Objects.requireNonNull(directory.listFiles((dir, name) -> name.endsWith(".obj")))) {
            String text = Files.readString(file.toPath());
            MODELS.put(file.getName().replaceAll(".obj", ""), parse(text));
            System.out.println("Loaded model " + file.getName());
        }
    }

    public static class OBJVertexDefinition implements Serializable {
        public int vertex;
        public int texture;
        public int normal;
        public boolean hasNormal;
    }

    public static OBJModel parse(String str){
        String[] lines = str.split("\n");
        OBJModel model = new OBJModel();
        String material = "Unknown";
        for(String line : lines){
            line = line.trim().replaceAll(" +", " ");
            if(line.isEmpty())continue;

            String[] sections = line.split(" ");
            String type = sections[0];
            String[] arguments = new String[sections.length - 1];
            System.arraycopy(sections, 1, arguments, 0, arguments.length);

            if(type.equals("v")){
                model.vertices.add(new Vec3f(Float.parseFloat(arguments[0]), Float.parseFloat(arguments[1]), Float.parseFloat(arguments[2])));
            }else if(type.equals("vt")){
                model.vertexTextures.add(new Vec2f(Float.parseFloat(arguments[0]), Float.parseFloat(arguments[1])));
            }else if(type.equals("vn")){
                model.vertexNormals.add(new Vec3f(Float.parseFloat(arguments[0]), Float.parseFloat(arguments[1]), Float.parseFloat(arguments[2])));
            }else if(type.equals("usemtl")){
                material = arguments[0];
            }else if(type.equals("f")){
                OBJVertexDefinition[] definitions = new OBJVertexDefinition[arguments.length];
                for(int i = 0; i < definitions.length; i++){
                    String[] separate = arguments[i].split("/");
                    OBJVertexDefinition def = new OBJVertexDefinition();
                    def.vertex = Integer.parseInt(separate[0]);
                    if(separate.length > 1 && !separate[1].isEmpty()){
                        def.texture = Integer.parseInt(separate[1]);
                    }else{
                        def.texture = 0;
                    }
                    if(separate.length > 2) {
                        def.normal = Integer.parseInt(separate[2]);
                        def.hasNormal = true;
                    }else{
                        def.hasNormal = false;
                    }
                    definitions[i] = def;
                }
                model.faces.add(definitions);
                model.faceMaterals.add(material);
            }
        }
        return  model;
    }

    public static OBJModel getModel(String name){
        return MODELS.get(name);
    }

}
