package net.tiffit.realmnetapi.assets.spritesheet;

import net.tiffit.realmnetapi.util.math.Vec2i;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnimMap {

    public static final int ACTION_IDLE = 0, ACTION_WALK = 1, ACTION_ATTACK = 2;
    public static final int DIRECTION_SIDE = 0, DIRECTION_UP = 2, DIRECTION_DOWN = 3;


    // (Action, Direction) -> AnimSpriteDefinition
    private final HashMap<Vec2i, List<AnimSpriteDefinition>> definitions = new HashMap<>();

    public void addDefinition(AnimSpriteDefinition def){
        Vec2i vec = new Vec2i(def.action, def.direction);
        List<AnimSpriteDefinition> list = definitions.computeIfAbsent(vec, vec2i -> new LinkedList<>());
        list.add(def);
    }

    public List<AnimSpriteDefinition> getDefinition(int action, int direction){
        Vec2i vec = new Vec2i(action, direction);
        if(definitions.containsKey(vec)){
            return definitions.get(vec);
        }
        List<AnimSpriteDefinition> def = new LinkedList<>();
        search: {
            if(direction != DIRECTION_SIDE){
                def = getDefinition(action, DIRECTION_SIDE);
                break search;
            }
            if(action != ACTION_IDLE){
                def = getDefinition(action - 1, direction);
                break search;
            }
            def = definitions.values().stream().findFirst().orElse(def);
        }
        definitions.put(vec, def);
        return def;
    }

    public Map<Vec2i, List<AnimSpriteDefinition>> getMap(){
        return definitions;
    }

}
