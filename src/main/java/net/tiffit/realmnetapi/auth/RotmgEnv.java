package net.tiffit.realmnetapi.auth;

public enum RotmgEnv {
    PRODUCTION("https://www.realmofthemadgod.com/"),
    TESTING("https://test.realmofthemadgod.com/");

    public final String baseUrl;

    RotmgEnv(String baseUrl){
        this.baseUrl = baseUrl;
    }

}
