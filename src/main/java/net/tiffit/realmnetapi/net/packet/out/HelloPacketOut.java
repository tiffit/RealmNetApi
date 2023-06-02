package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.RealmNetApi;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class HelloPacketOut extends RotMGPacketOut {

    private final String buildVersion;
    private final int gameId;
    private final String accessToken;
    private final int keyTime;
    private final byte[] key;
    private final String userPlatform;
    private final String playPlatform;
    private final String platformToken;
    private final String clientToken;
    private final String userToken;

    public HelloPacketOut(String accessToken, String clientHash, int gameId, byte[] key, int keyTime) {
        super((byte)74);
        this.buildVersion = RealmNetApi.ENV.latestVersion;
        this.gameId = gameId;
        this.accessToken = accessToken;
        this.keyTime = keyTime;
        this.key = key;
        this.userPlatform = "rotmg";
        this.playPlatform = "rotmg";
        this.platformToken = "";
        this.clientToken = clientHash;
        this.userToken = "2Jzc9JbAT7f82MOXVTJBUqKzwcEPZ3ZO";
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(buildVersion);
        out.writeInt(gameId);
        out.writeUTF(accessToken);
        out.writeInt(keyTime);
        out.writeShort(key.length);
        out.write(key);
        out.writeUTF(userPlatform);
        out.writeUTF(playPlatform);
        out.writeUTF(platformToken);
        out.writeUTF(clientToken);
        out.writeUTF(userToken);
    }
}
