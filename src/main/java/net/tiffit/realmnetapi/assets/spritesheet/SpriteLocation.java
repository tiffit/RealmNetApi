package net.tiffit.realmnetapi.assets.spritesheet;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class SpriteLocation implements Serializable {

    public final String spritesheet;
    public final int index;
}
