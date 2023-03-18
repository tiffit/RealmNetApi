package net.tiffit.realmnetapi.auth.data;

public record ServerInfo(String name, String dns, float usage){

    public enum ServerUsage {
        Normal, Crowded, Full
    }

    public ServerUsage getUsageEnum(){
        if(usage == 0) return ServerUsage.Normal;
        if(usage == 1) return ServerUsage.Full;
        return ServerUsage.Crowded;
    }

}
