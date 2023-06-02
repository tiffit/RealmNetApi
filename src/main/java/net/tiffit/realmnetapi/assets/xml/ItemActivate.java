package net.tiffit.realmnetapi.assets.xml;

public enum ItemActivate {

    BulletNova,
    Shoot;

    public static ItemActivate parse(String input){
        try {
            return ItemActivate.valueOf(input);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

}
