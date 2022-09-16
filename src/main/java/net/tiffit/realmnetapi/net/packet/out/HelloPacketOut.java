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

    public HelloPacketOut(String accessToken, int gameId, byte[] key, int keyTime) {
        super((byte)1);
        this.buildVersion = "3.0.2.0.0";
        this.gameId = gameId;
        this.accessToken = accessToken;
        this.keyTime = keyTime;
        this.key = key;
        this.userPlatform = "rotmg";
        this.playPlatform = "rotmg";
        this.platformToken = "";
        this.clientToken = RealmNetApi.CLIENT_TOKEN;
        this.userToken = "8bV53M5ysJdVjU4M97fg3g7BnPXhefnc";
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
