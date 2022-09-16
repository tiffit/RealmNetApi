package net.tiffit.realmnetapi.assets.unity;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.tiffit.realmnetapi.util.math.Vec2f;
import net.tiffit.realmnetapi.util.math.Vec3f;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

public class UMeshParser {

    public static boolean meshHasNormals(UAsset asset){
        List<Map<String, Object>> channels = asset.getValue("Mesh/m_VertexData/m_Channels");
        boolean hasSeen3 = false;
        for (Map<String, Object> channel : channels) {
            int dimValue = (int) channel.get("dimension");
            if(dimValue == 3){
                if(hasSeen3)return true;
                hasSeen3 = true;
            }
            if(dimValue == 2)return false;
        }
        return false;
    }

    @SneakyThrows @SuppressWarnings("UnstableApiUsage")
    public static String parse(String name, boolean hasNormal, String mesh, String index) {
        mesh = normalizeStr(mesh);
        index = normalizeStr(index);

        byte[] meshBytes = new byte[mesh.length()/2];
        List<Vec3f> vertices = new ArrayList<>();
        List<Vec3f> normals = new ArrayList<>();
        List<Vec2f> uvs = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < mesh.length(); i+=2) {
            meshBytes[i/2] = (byte)Integer.parseInt(mesh.substring(i, i + 2), 16);
        }
        @Cleanup
        LittleEndianDataInputStream stream = new LittleEndianDataInputStream(new ByteArrayInputStream(meshBytes));
        while(stream.available() > 0){
            vertices.add(new Vec3f(-stream.readFloat(), stream.readFloat(), stream.readFloat()));
            if(hasNormal)
                normals.add(new Vec3f(stream.readFloat(), stream.readFloat(), stream.readFloat()));
            uvs.add(new Vec2f(stream.readFloat(), stream.readFloat()));
        }
        for (int ii = 0; ii < index.length() / 4; ++ii) {
            indices.add(swap16(Integer.parseInt(index.substring(ii * 4, ii * 4 + 4), 16)) + 1);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("g ").append(name).append("\n");
        for (Vec3f vertex : vertices) {
            builder.append(String.format("v %f %f %f\n", vertex.x(), vertex.y(), vertex.z()));
        }
        for (Vec2f uv : uvs) {
            builder.append(String.format("vt %f %f\n", uv.x(), uv.y()));
        }
        for (Vec3f normal : normals) {
            builder.append(String.format("vn %f %f %f\n", normal.x(), normal.y(), normal.z()));
        }

        builder.append("g ").append(name).append("_0\n");
        PrimitiveIterator.OfInt indexStream = indices.stream().mapToInt(value -> value).iterator();
        for (int i = 0; i < indices.size()/3; i++) {
            String formatStr = hasNormal ? "f %3$d/%3$d/%3$d %2$d/%2$d/%2$d %1$d/%1$d/%1$d\n" : "f %3$d/%3$d %2$d/%2$d %1$d/%1$d\n";
            builder.append(String.format(formatStr, indexStream.next(), indexStream.next(), indexStream.next()));
        }
        return builder.toString();
    }

    private static String normalizeStr(String str) {
        return str.replaceAll("\\\\", "").replaceAll("\n", "").replaceAll(" ", "");
    }

    private static int swap16(int val){
        return ((val & 0xFF) << 8) | ((val >> 8) & 0xFF);
    }

}
