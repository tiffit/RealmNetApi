package net.tiffit.realmnetapi.assets.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Style implements Serializable {
    public String id;
    public List<StyleStep> steps = new ArrayList<>();

    public static final int EMPTY_PRESENTATION = hashString("");

    public void load(Element element){
        id = element.getAttribute("id");
        NodeList childs = element.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            if(childs.item(i) instanceof Element child) {
                String type = child.getTextContent();
                StyleStep step = switch (type) {
                    case "SetAltTexture" -> new SetAltTextureStep();
                    case "SetAnimation" -> new SetAnimationStep();
                    default -> null;
                };
                if (step != null) {
                    step.load(child);
                    steps.add(step);
                }
            }
        }
    }

    @Override
    public String toString() {
        return id;
    }

    public int getIdHash(){
        return hashString(id);
    }

    private static int hashString(String str){
        int val = 0x811c9dc5;
        for (int i = 0; i < str.length(); i++) {
            short c = (short) str.charAt(i);
            val = (c ^ val) * 0x1000193;
        }
        return val;
    }

    public static abstract class StyleStep implements Serializable {
        abstract void load(Element element);

        protected String getAttribOrElse(Element element, String attrib, String defaultVal){
            if(element.hasAttribute(attrib))return element.getAttribute(attrib);
            return defaultVal;
        }
    }

    public static class SetAnimationStep extends StyleStep {

        public String animationId;

        @Override
        void load(Element element) {
            animationId = getAttribOrElse(element, "id", "");
        }

    }

    public static class SetAltTextureStep extends StyleStep {

        public int altTextureId;

        @Override
        void load(Element element) {
            altTextureId = Integer.parseInt(getAttribOrElse(element, "altTextureId", "0"));
        }
    }
}
