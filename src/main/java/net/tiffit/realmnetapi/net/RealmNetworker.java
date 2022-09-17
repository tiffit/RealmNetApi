package net.tiffit.realmnetapi.net;

import lombok.SneakyThrows;
import net.tiffit.realmnetapi.auth.AccessToken;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.net.ack.AckHandler;
import net.tiffit.realmnetapi.net.crypto.RC4;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.net.packet.out.HelloPacketOut;
import net.tiffit.realmnetapi.util.MoveRecords;
import net.tiffit.realmnetapi.util.Tuple;
import net.tiffit.realmnetapi.util.Updater;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class RealmNetworker {

    private final RC4 rc4In = new RC4("c91d9eec420160730d825604e0");
    private final RC4 rc4Out = new RC4("5a4d2016bc16dc64883194ffd9");

    public RMap map;
    public final MoveRecords records = new MoveRecords();
    public final AckHandler ackHandler = new AckHandler(this);
    public int lastUpdate = 0;

    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;
    private int numSent;
    private int numGot;

    public boolean connected;
    public boolean showAllyShots = false;

    public final ConnectionAddress address;

    private static final long startTime = System.currentTimeMillis();
    private static long lastTick = startTime;
    public NetworkLogger logger;
    public Updater updater;

    private final LinkedBlockingQueue<Tuple<RotMGPacketOut, byte[]>> packets = new LinkedBlockingQueue<>();

    @SneakyThrows
    public RealmNetworker(ConnectionAddress address){
        this.address = address;
        RotMGPacketIn.init();
        this.logger = new NetworkLogger(this);
        this.socket = new Socket();
        socket.setTcpNoDelay(true);
    }

    public void connect(AccessToken token) {
        try {
            socket.connect(new InetSocketAddress(address.address(), address.port()));
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.connected = true;
            new Thread(this::listen, "Net Listener").start();
            new Thread(this::sendPackets, "Packet Sender").start();
            HelloPacketOut packet = new HelloPacketOut(token.getToken(), address.gameId(), address.key(), address.keyTime());
            while (true) {
                if (this.socket.isConnected()) {
                    logger.write("Connected to server!");
                    this.send(packet);
                    updater = new Updater(this);
                    Thread updaterThread = new Thread(updater, "Update");
                    updaterThread.start();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void listen() {
        while (this.connected) {
            try {
                if (this.socket.isClosed()) throw new IOException("Server closed the socket!");
                int packetSize = in.readInt() - 5;
                byte packetId = in.readByte();
                byte[] buf = new byte[packetSize];
                in.readFully(buf);
                buf = rc4In.cypher(buf);
                numGot++;
                if (RotMGPacketIn.PACKET_MAP.containsKey((int) packetId)) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                    DataInputStream in = new DataInputStream(bis);
                    RotMGPacketIn packet = RotMGPacketIn.PACKET_MAP.get((int) packetId).get();
                    packet.read(in);
                    logger.write("packet", "IN("+numGot+":"+getTime()+"): " + packet.getClass().getSimpleName() + packet.getExtraInfo());
                    try {
                        packet.handle(this);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    logger.write("packet", "IN("+numGot+":"+getTime()+"): " + " Unknown " + packetId);
                }
            } catch (IOException e) {
                if (connected) {
                    e.printStackTrace();
                    disconnect();
                }
            }
        }
    }

    private void sendPackets(){
        while (this.connected) {
            try {
                if (this.socket.isClosed()){
                    packets.clear();
                    throw new IOException("Server closed the socket!");
                }
                Tuple<RotMGPacketOut, byte[]> tuple = packets.take();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dataout = new DataOutputStream(baos);
                dataout.writeInt(tuple.b().length + 5);
                dataout.writeByte(tuple.a().id);
                dataout.write(tuple.b());
                baos.writeTo(out);
                out.flush();
                numSent++;
                logger.write("packet", "OU("+numSent+":"+getTime()+"): " + tuple.a().getClass().getSimpleName() + tuple.a().getExtraInfo());
            } catch (IOException | InterruptedException e) {
                if (connected) {
                    e.printStackTrace();
                    disconnect();
                }
            }
        }
    }

    public void send(RotMGPacketOut packet){
        if (!this.connected) return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dataout = new DataOutputStream(baos);
        try {
            packet.write(dataout);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                baos.close();
                dataout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] buf = rc4Out.cypher(baos.toByteArray());
        packets.add(new Tuple<>(packet, buf));
    }

    public void disconnect() {
        if (!connected) return;
        logger.write("Disconnected from RotMG Servers: " + (System.currentTimeMillis() - startTime));
        logger.save();
        connected = false;
        try {
            in.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getTime(){
        return (int)(lastTick - startTime);
    }

//    public static int getTime(){
//        return (int)(System.currentTimeMillis() - startTime);
//    }

    public static int getTimeReal(){
        return (int)(System.currentTimeMillis() - startTime);
    }

    public static void updateTime() {
        lastTick = System.currentTimeMillis();
    }
}
