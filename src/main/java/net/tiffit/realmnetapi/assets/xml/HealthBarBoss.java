package net.tiffit.realmnetapi.assets.xml;

import org.w3c.dom.Element;

import java.io.Serializable;

public record HealthBarBoss(float radius, float xOffset, float yOffset) implements Serializable {

    public static HealthBarBoss NONE = new HealthBarBoss(0, 0, 0);

    public static HealthBarBoss load(Element element){
        float radius = element.hasAttribute("radius") ? Float.parseFloat(element.getAttribute("radius")) : 30;
        float xOffset = element.hasAttribute("xOffset") ? Float.parseFloat(element.getAttribute("xOffset")) : 0;
        float yOffset = element.hasAttribute("yOffset") ? Float.parseFloat(element.getAttribute("yOffset")) : 0;
        return new HealthBarBoss(radius, xOffset, yOffset);
    }
}
