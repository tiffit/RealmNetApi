package net.tiffit.realmnetapi.assets.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.HashMap;

public class XMLElementHelper {

    public final Element elem;
    private static XPath xPath;
    private static final HashMap<String, XPathExpression> COMPILED = new HashMap<>();

    public XMLElementHelper(Element elem){
        this.elem = elem;
        if(xPath == null){
            xPath = XPathFactory.newInstance().newXPath();
        }
    }

    public String getAttribute(String name, String defaultValue) {
        return elem.hasAttribute(name) ? elem.getAttribute(name) : defaultValue;
    }

    public float getAttributeFloat(String name, float defaultValue) {
        return elem.hasAttribute(name) ? Float.parseFloat(elem.getAttribute(name)) : defaultValue;
    }

    public boolean getAttributeBoolean(String name, boolean defaultValue) {
        return elem.hasAttribute(name) ? Boolean.parseBoolean(elem.getAttribute(name)) : defaultValue;
    }

    public Element getChild(String child) throws XPathExpressionException {
        return (Element)compile("./" + child).evaluate(elem, XPathConstants.NODE);
    }

    public NodeList getChilds(String child) throws XPathExpressionException {
        return (NodeList)compile("./" + child).evaluate(elem, XPathConstants.NODESET);
    }

    public String getChildAttribute(String child, String name) throws XPathExpressionException {
        return getChild(child).getAttribute(name);
    }

    public int getChildAttributeInteger(String child, String name) throws XPathExpressionException {
        return Integer.parseInt(getChildAttribute(child, name));
    }

    public boolean hasChild(String child) throws XPathExpressionException {
        return getChild(child) != null;
    }

    public String getChildElementText(String child) throws XPathExpressionException {
        return getChildElementText(child, "");
    }

    public String getChildElementText(String child, String fallback) throws XPathExpressionException {
        Element childElem = getChild(child);
        if(childElem == null)return fallback;
        return childElem.getTextContent();
    }

    public int getChildElementInteger(String child) throws XPathExpressionException {
        return getChildElementInteger(child, 0);
    }

    public int getChildElementInteger(String child, int fallback) throws XPathExpressionException {
        Element childElem = getChild(child);
        if(childElem == null)return fallback;
        return parseInt(childElem.getTextContent().trim());
    }

    public float getChildElementFloat(String child) throws XPathExpressionException {
        return getChildElementFloat(child, 0);
    }

    public float getChildElementFloat(String child, float fallback) throws XPathExpressionException {
        Element childElem = getChild(child);
        if(childElem == null)return fallback;
        return Float.parseFloat(childElem.getTextContent().trim());
    }

    public int parseInt(String value){
        if(value.startsWith("0x")){
            return Integer.valueOf(value.substring(2), 16);
        }else{
            return Integer.parseInt(value);
        }
    }

    private XPathExpression compile(String exp) throws XPathExpressionException {
        if(COMPILED.containsKey(exp))return COMPILED.get(exp);
        XPathExpression xexp = xPath.compile(exp);
        COMPILED.put(exp, xexp);
        return xexp;
    }

}
