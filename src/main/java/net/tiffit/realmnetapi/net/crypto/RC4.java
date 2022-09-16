package net.tiffit.realmnetapi.net.crypto;

public class RC4 {

    private byte[] state = new byte[256];
    private int x = 0;
    private int y = 0;

    public RC4(String key){
        this(hexToBytes(key));
    }

    private RC4(byte[] key){
        for (int i = 0; i < 256; i++)
            state[i] = (byte)i;
        int j = 0;
        int k = 0;
        byte tmp;
        for (int i = 0; i < 256; i++) {
            k = ((key[j] & 0xff) + (state[i] & 0xff) + k) & 0xff;
            tmp = state[i];
            state[i] = state[k];
            state[k] = tmp;
            j = (j + 1) % key.length;
        }
    }

    public byte[] cypher(byte[] buf) {
        if (buf == null)return null;
        int xorIndex;
        byte tmp;
        byte[] result = new byte[buf.length];
        for (int i = 0; i < buf.length; i++) {
            x = (x + 1) & 0xff;
            y = ((state[x] & 0xff) + y) & 0xff;

            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;
            xorIndex = ((state[x] & 0xff) + (state[y] & 0xff)) & 0xff;
            result[i] = (byte) (buf[i] ^ state[xorIndex]);
        }
        return result;
    }

    private static byte[] hexToBytes(String key) {
        byte[] bytes = new byte[key.length()/2];
        for(int i = 0; i < key.length(); i+=2) {
            bytes[i/2] = (byte)Integer.parseInt(key.substring(i, i+2),16);
        }
        return bytes;
    }

}
