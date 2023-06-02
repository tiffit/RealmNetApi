package net.tiffit.realmnetapi.auth;

public enum RotmgEnv {
    PRODUCTION("https://www.realmofthemadgod.com/", "3.3.8.0.0"),
    TESTING("https://test.realmofthemadgod.com/", "3.3.8.0.0");

    public final String baseUrl;
    public final String latestVersion;

    RotmgEnv(String baseUrl, String latestVersion){
        this.baseUrl = baseUrl;
        this.latestVersion = latestVersion;
    }

}
