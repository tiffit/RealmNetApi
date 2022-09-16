package net.tiffit.realmnetapi.auth;

public enum RotmgEnv {
    PRODUCTION("https://www.realmofthemadgod.com/", "54.86.47.176"),
    TESTING("https://test.realmofthemadgod.com/", "3.90.61.214");

    public final String baseUrl;
    public final String defaultIp;

    RotmgEnv(String baseUrl, String defaultIp){
        this.baseUrl = baseUrl;
        this.defaultIp = defaultIp;
    }

}
