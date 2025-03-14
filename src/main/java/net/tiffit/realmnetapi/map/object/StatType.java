package net.tiffit.realmnetapi.map.object;

import java.util.HashMap;

public enum StatType {
    MAX_HP(0),
    HP(1),
    SIZE(2),
    MAX_MP(3),
    MP(4),
    NEXT_LEVEL_EXP(5),
    EXP(6, true),
    LEVEL(7),
    INVENTORY_0(8),
    INVENTORY_1(9),
    INVENTORY_2(10),
    INVENTORY_3(11),
    INVENTORY_4(12),
    INVENTORY_5(13),
    INVENTORY_6(14),
    INVENTORY_7(15),
    INVENTORY_8(16),
    INVENTORY_9(17),
    INVENTORY_10(18),
    INVENTORY_11(19),
    ATTACK(20),
    DEFENSE(21),
    SPEED(22),
    UNKNOWN_23(23),
    SEASONAL_CHARACTER(24),
    TEXTURE(25),
    VITALITY(26),
    WISDOM(27),
    DEXTERITY(28),
    CONDITION(29),
    NUM_STARS(30),
    NAME(31, true),
    TEX1(32),
    TEX2(33),
    MERCHANDISE_TYPE(34),
    CREDITS(35),
    MERCHANDISE_PRICE(36),
    ACTIVE(37),
    ACCOUNT_ID(38, true),
    FAME(39),
    MERCHANDISE_CURRENCY(40),
    CONNECT(41),
    MERCHANDISE_COUNT(42),
    MERCHANDISE_MINS_LEFT(43),
    MERCHANDISE_DISCOUNT(44),
    MERCHANDISE_RANK_REQ(45),
    MAX_HP_BOOST(46),
    MAX_MP_BOOST(47),
    ATTACK_BOOST(48),
    DEFENSE_BOOST(49),
    SPEED_BOOST(50),
    VITALITY_BOOST(51),
    WISDOM_BOOST(52),
    DEXTERITY_BOOST(53),
    OWNER_ACCOUNT_ID(54, true),
    RANK_REQUIRED(55),
    NAME_CHOSEN(56),
    CURR_FAME(57),
    NEXT_CLASS_QUEST_FAME(58),
    LEGENDARY_RANK(59),
    SINK_LEVEL(60),
    ALT_TEXTURE(61),
    GUILD_NAME(62, true),
    GUILD_RANK(63),
    BREATH(64),
    XP_BOOSTED(65),
    XP_TIMER(66),
    LD_TIMER(67),
    LT_TIMER(68),
    HEALTH_POTION_STACK(69),
    MAGIC_POTION_STACK(70),
    BACKPACK_0(71),
    BACKPACK_1(72),
    BACKPACK_2(73),
    BACKPACK_3(74),
    BACKPACK_4(75),
    BACKPACK_5(76),
    BACKPACK_6(77),
    BACKPACK_7(78),
    HASBACKPACK(79),
    TEXTURE_OLD(80, true),
    PET_INSTANCEID(81),
    PET_NAME(82, true),
    PET_TYPE(83),
    PET_RARITY(84),
    PET_MAXABILITYPOWER(85),
    PET_FAMILY(86),
    PET_FIRSTABILITY_POINT(87),
    PET_SECONDABILITY_POINT(88),
    PET_THIRDABILITY_POINT(89),
    PET_FIRSTABILITY_POWER(90),
    PET_SECONDABILITY_POWER(91),
    PET_THIRDABILITY_POWER(92),
    PET_FIRSTABILITY_TYPE(93),
    PET_SECONDABILITY_TYPE(94),
    PET_THIRDABILITY_TYPE(95),
    NEW_CON(96),
    FORTUNE_TOKEN(97),
    SUPPORTER_POINTS(98),
    SUPPORTER(99),
    CHALLENGER_STARBG(100),
    PLAYER_ID(101),
    PROJECTILE_SPEED_MULT(102),
    PROJECTILE_LIFE_MULT(103),
    OPENED_AT_TIMESTAMP(104),
    EXALTED_ATT(105),
    EXALTED_DEF(106),
    EXALTED_SPEED(107),
    EXALTED_VIT(108),
    EXALTED_WIS(109),
    EXALTED_DEX(110),
    EXALTED_HP(111),
    EXALTED_MP(112),
    EXALTATION_BONUS_DAMAGE(113),
    EXALTATION_IC_REDUCTION(114),
    GRAVE_ACCOUNT_ID(115, true),
    POTION_ONE_TYPE(116),
    POTION_TWO_TYPE(117),
    POTION_THREE_TYPE(118),
    POTION_BELT(119),
    FORGEFIRE(120),
    UNKNOWN121(121, true),
    UNKNOWN122(122),
    UNKNOWN123(123),
    UNKNOWN124(124),
    STYLE_ID_HASH(125),
    UNKNOWN126(126);

    public final int id;
    public final boolean stringType;

    private final static HashMap<Integer, StatType> map = new HashMap<>();

    StatType(int id) {
        this(id, false);
    }

    StatType(int id, boolean stringType) {
        this.id = (byte) id;
        this.stringType = stringType;
    }

    static {
        for (StatType type : StatType.values()) {
            map.put(type.id, type);
        }
    }

    public static StatType byID(int id) {
        return map.getOrDefault(id, null);
    }
}
