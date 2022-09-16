package net.tiffit.realmnetapi.net;

public record ConnectionAddress(String address, int port, byte[] key, int keyTime, int gameId) {

    public static ConnectionAddress getNexusAddress(String address) {
        return new ConnectionAddress(address, 2050, new byte[0], -1, -2);
    }

}
