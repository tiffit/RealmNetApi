package net.tiffit.realmnetapi.assets.xml;

import org.w3c.dom.Element;

import java.io.Serializable;

public class TileAnimate implements Serializable {

    public float dx, dy;
    public String type;

    public void load(Element element){
        dx = element.hasAttribute("dx") ? Float.parseFloat(element.getAttribute("dx")) : 0;
        dy = element.hasAttribute("dy") ? Float.parseFloat(element.getAttribute("dy")) : 0;
        type = element.getTextContent();
    }

}
