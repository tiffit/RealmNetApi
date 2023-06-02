package net.tiffit.realmnetapi.assets.xml;

import net.tiffit.realmnetapi.util.math.Vec2f;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;

public class SubAttack implements Serializable {

    public int index;
    public int projectileId = 0;
    public int numProjectiles = 1;
    public float rateOfFire = 1;
    public Vec2f posOffset = Vec2f.ZERO;
    public float defaultAngleDeg = 0;
    public int burstCount = 0;
    public float burstDelay = 0;
    public float burstMinDelay = 0;
    public float arcGap = -1;

    public void load(Element element){
        projectileId = element.hasAttribute("projectileId") ? Integer.parseInt(element.getAttribute("projectileId")) : 0;
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if(node instanceof Element elem){
                switch (elem.getTagName()) {
                    case "NumProjectiles" -> numProjectiles = Integer.parseInt(elem.getTextContent());
                    case "RateOfFire" -> rateOfFire = Float.parseFloat(elem.getTextContent());
                    case "PosOffset" -> {
                        String[] offsetStr = elem.getTextContent().split(",");
                        posOffset = new Vec2f(Float.parseFloat(offsetStr[0]), Float.parseFloat(offsetStr[1]));
                    }
                    case "DefaultAngle" -> defaultAngleDeg = Float.parseFloat(elem.getTextContent());
                    case "BurstCount" -> burstCount = Integer.parseInt(elem.getTextContent());
                    case "BurstDelay" -> burstDelay = Float.parseFloat(elem.getTextContent());
                    case "BurstMinDelay" -> burstMinDelay = Float.parseFloat(elem.getTextContent());
                    case "ArcGap" -> arcGap = Float.parseFloat(elem.getTextContent());
                }
            }
        }
    }

}
