package net.tiffit.realmnetapi.assets;

public enum ItemType {
    UNKNOWN(-99),
    NO_ITEM(-1),
    ALL_TYPE(0),
    SWORD_TYPE(1),
    DAGGER_TYPE(2),
    BOW_TYPE(3),
    TOME_TYPE(4),
    SHIELD_TYPE(5),
    LEATHER_TYPE(6),
    PLATE_TYPE(7),
    WAND_TYPE(8),
    RING_TYPE(9),
    POTION_TYPE(10),
    SPELL_TYPE(11),
    SEAL_TYPE(12),
    CLOAK_TYPE(13),
    ROBE_TYPE(14),
    QUIVER_TYPE(15),
    HELM_TYPE(16),
    STAFF_TYPE(17),
    POISON_TYPE(18),
    SKULL_TYPE(19),
    TRAP_TYPE(20),
    ORB_TYPE(21),
    PRISM_TYPE(22),
    SCEPTER_TYPE(23),
    KATANA_TYPE(24),
    SHURIKEN_TYPE(25),
    EGG_TYPE(26),
    WAKI_TYPE(27),
    LUTE_TYPE(28);

    private int id;

    ItemType(int id){
        this.id = id;
    }

    public static ItemType byID(int id){
        for(ItemType type : ItemType.values()){
            if(type.id == id)return type;
        }
        return ItemType.UNKNOWN;
    }

    public boolean canBeEquipped(){
        return this != POTION_TYPE && this != EGG_TYPE && this != ALL_TYPE && this != NO_ITEM && this != UNKNOWN;
    }

}
