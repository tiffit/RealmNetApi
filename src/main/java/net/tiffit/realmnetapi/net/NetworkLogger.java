package net.tiffit.realmnetapi.net;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkLogger {

    private boolean saved = false;
    private final File logFolder;
    private boolean enabled;
    private RealmNetworker net;
    private HashMap<String, ArrayList<String>>values = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    public NetworkLogger(RealmNetworker net, @Nullable File log){
        this.net = net;
        enabled = log != null;
        logFolder = log;
        //File log = new File("./debug/");
        if (enabled) {
            if(!log.exists()){
                //noinspection ResultOfMethodCallIgnored
                log.mkdirs();
            }
            for (File file : Objects.requireNonNull(log.listFiles())) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    public void write(String line){
        write("log", line);
    }

    public void writeFormat(String line, Object... values){
        write(String.format(line, values));
    }

    public void write(String file, String line){
        if(!enabled)return;
        lock.lock();
        if(!values.containsKey(file))values.put(file, new ArrayList<>());
        values.get(file).add(line);
        lock.unlock();
        //if(saved){
            save();
        //}
    }

    public void writeFormat(String file, String line, Object... values){
        write(file, String.format(line, values));
    }

    public void save(){
        lock.lock();
        values.forEach((s, strings) -> {
            File log = new File(logFolder, s + ".txt");
            try {
                if(!log.exists())
                    log.createNewFile();
                Files.write(log.toPath(), strings, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        values.clear();
        saved = true;
        lock.unlock();
    }

}
