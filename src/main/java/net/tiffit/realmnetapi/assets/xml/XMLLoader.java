package net.tiffit.realmnetapi.assets.xml;

import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLLoader {

    public static HashMap<Integer, GameObject> OBJECTS = new HashMap<>();
    public static HashMap<Integer, GameObject> PET_ABILITIES = new HashMap<>();
    public static HashMap<Integer, GameObject> PET_BEHAVIOR = new HashMap<>();
    public static HashMap<Integer, Ground> GROUNDS = new HashMap<>();

    public static HashMap<String, GameObject> ID_TO_OBJECT = new HashMap<>();

    private static DocumentBuilder docbuilder;
    private static XPath xPath;

    @SneakyThrows
    public static void loadAllXml(){
        File f = new File("./assets/xml/");
        if(f.exists()){
            for (File file : Objects.requireNonNull(f.listFiles((dir, name) -> name.endsWith(".xml")))) {
                try(Stream<String> lines = Files.lines(file.toPath())){
                    String output = lines.collect(Collectors.joining("\n"));
                    parse(output, file.getName());
                }
            }
        }else{
            System.out.println("File does not exist: " + f.getAbsolutePath());
        }
        System.out.println("Loaded " + GROUNDS.size() + " grounds, " + OBJECTS.size() + " objects");
    }

    public static void parse(String xml, String fileName) {
        try {
            if(!xml.startsWith("<?xml")){
                System.out.println("Detected invalid XML " + fileName);
                return;
            }
            if (docbuilder == null) {
                docbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                xPath = XPathFactory.newInstance().newXPath();
            }
            InputSource is = new InputSource(new StringReader(xml));
            is.setEncoding("UTF-8");
            Document doc = docbuilder.parse(is);
            doc.normalize();
            NodeList objects = doc.getElementsByTagName("Object");
            for(int i = 0; i < objects.getLength(); i++){
                Element elem = (Element) objects.item(i);
                try {
                    loadObject((Element)elem.cloneNode(true));
                }catch(Exception ex){
                    System.out.println("Unable to load object: " + elem.getAttribute("id"));
                    ex.printStackTrace();
                }
            }

            NodeList grounds = doc.getElementsByTagName("Ground");
            for(int i = 0; i < grounds.getLength(); i++){
                try {
                    Element elem = (Element) grounds.item(i);
                    loadGround((Element)elem.cloneNode(true));
                }catch(Exception ex){
                    System.out.println("Unable to load ground!");
                    ex.printStackTrace();
                }
            }
        }catch (SAXParseException ex){
            System.out.println("Unable to parse " + fileName);
            return;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void loadObject(Element elem) throws XPathExpressionException {
        XMLElementHelper helper = new XMLElementHelper(elem);
        GameObject go = new GameObject();
        go.type = Integer.parseInt(elem.getAttribute("type").substring(2), 16);
        go.id = elem.getAttribute("id");
        go.setName = elem.hasAttribute("setName") ? elem.getAttribute("setName") : "";
        go.goClass = helper.getChildElementText("Class", "Unknown");
        if(hasChild(elem,"Texture")) go.texture.add(loadTexture(getChild(elem, "Texture")));
        else if(hasChild(elem,"RandomTexture")){
            NodeList textures = (NodeList)compile("RandomTexture/Texture").evaluate(elem, XPathConstants.NODESET);
            for(int i = 0; i < textures.getLength(); i++){
                Element textureElem = (Element) textures.item(i);
                go.texture.add(loadTexture(textureElem));
            }
        }
        else if(hasChild(elem,"AnimatedTexture")){
            Texture texture = loadTexture((Element)elem.getElementsByTagName("AnimatedTexture").item(0));
            texture.animated = true;
            go.texture.add(texture);
        }
        if(hasChild(elem,"Top/Texture")) go.textureTop = loadTexture(getChild(elem, "Top/Texture"));
        else if(hasChild(elem,"Top/RandomTexture")) go.textureTop = loadTexture(getChild(elem, "Top/RandomTexture/Texture"));

        if(hasChild(elem,"HealthBarBoss")) go.healthBar = HealthBarBoss.load(getChild(elem, "HealthBarBoss"));

        NodeList altTextureList = helper.getChilds("AltTexture");
        for (int i = 0; i < altTextureList.getLength(); i++) {
            Element altTexElem = (Element) altTextureList.item(i);
            int id = Integer.parseInt(altTexElem.getAttribute("id"));
            boolean animated = false;
            NodeList texList = altTexElem.getElementsByTagName("Texture");
            if(texList.getLength() == 0){
                texList = altTexElem.getElementsByTagName("AnimatedTexture");
                animated = true;
            }
            Texture texture = loadTexture((Element)texList.item(0));
            texture.animated = animated;
            go.altTextures.put(id, texture);
        }

        NodeList animations = helper.getChilds("Animation");
        for (int i = 0; i < animations.getLength(); i++) {
            Element animationElem = (Element) animations.item(i);
            XMLElementHelper animHelper = new XMLElementHelper(animationElem);
            Animation animation = new Animation();
            animation.index = i;
            animation.id = animHelper.getAttribute("id", "");
            animation.period = animHelper.getAttributeFloat("period", 0);
            animation.prob = animHelper.getAttributeFloat("prob", 1);
            animation.sync = animHelper.getAttributeBoolean("sync", false);

            NodeList frames = animHelper.getChilds("Frame");
            for (int j = 0; j < frames.getLength(); j++) {
                Element frameElem = (Element) frames.item(j);
                XMLElementHelper frameHelper = new XMLElementHelper(frameElem);
                Animation.AnimationFrame frame = new Animation.AnimationFrame();
                frame.time = frameHelper.getAttributeFloat("time", 1);
                frame.texture = loadTexture(frameHelper.getChild("Texture"));
                animation.frames.add(frame);
            }
            if(animation.frames.size() > 0){
                go.animations.put(animation.id, animation);
            }
        }

        go.displayid = helper.getChildElementText("DisplayId");
        go.description = helper.getChildElementText("Description");
        go.model = helper.getChildElementText("Model");
        go.color = helper.getChildElementInteger("Color");
        go.rotation = helper.getChildElementInteger("Rotation");
        go.angleCorrection = helper.getChildElementFloat("AngleCorrection");
        go.radius = helper.getChildElementFloat("Radius", 0.5f);
        go.size = helper.getChildElementInteger("Size", 100);
        go.dungeonName = helper.getChildElementText("DungeonName");
        go.tier = helper.getChildElementInteger("Tier", -1);
        go.quantity = helper.getChildElementInteger("Quantity", 1);
        go.slotType = helper.getChildElementInteger("SlotType");
        go.rateOfFire = helper.getChildElementFloat("RateOfFire");
        go.bagType = helper.getChildElementInteger("BagType");
        go.numProjectiles = helper.getChildElementInteger("NumProjectiles", 1);
        go.maxHitPoints = helper.getChildElementInteger("MaxHitPoints", 1);
        go.arcGap = helper.getChildElementFloat("ArcGap");
        go.defaultSkin = helper.getChildElementText("DefaultSkin");
        go.noTexture = helper.hasChild("NoTexture");
        go.enemy = helper.hasChild("Enemy");
        go.player = helper.hasChild("Player");
        go.drawOnGround = helper.hasChild("DrawOnGround");
        go.staticObject = helper.hasChild("Static");
        go.occupySquare = helper.hasChild("OccupySquare");
        go.fullOccupy = helper.hasChild("FullOccupy");
        go.enemyOccupySquare = helper.hasChild("EnemyOccupySquare");
        go.blocksSight = helper.hasChild("BlocksSight");
        go.invincible = helper.hasChild("Invincible");
        go.intergamePortal = helper.hasChild("IntergamePortal");
        go.item = helper.hasChild("Item");
        go.soulbound = helper.hasChild("Soulbound");
        go.potion = helper.hasChild("Potion");
        go.consumable = helper.hasChild("Consumable");
        go.mpCost = helper.getChildElementInteger("MpCost", 0);
        go.usable = helper.hasChild("Usable");
        go.cooldown = helper.getChildElementFloat("Cooldown", 0.5f);

        go.hitSound = helper.getChildElementText("HitSound");
        go.deathSound = helper.getChildElementText("DeathSound");
        go.sound = helper.getChildElementText("Sound");

        NodeList projectiles = (NodeList)compile("./Projectile").evaluate(elem.cloneNode(true), XPathConstants.NODESET);
        for(int i = 0; i < projectiles.getLength(); i++){
            Element projectileElem = (Element)projectiles.item(i).cloneNode(true);
            XMLElementHelper phelper = new XMLElementHelper(projectileElem);
            Projectile proj = new Projectile();
            if(projectileElem.hasAttribute("id"))proj.id = Integer.parseInt(projectileElem.getAttribute("id"));
            proj.objectId = phelper.getChildElementText("ObjectId");
            proj.damage = phelper.getChildElementInteger("Damage");
            proj.minDamage = phelper.getChildElementInteger("MinDamage");
            proj.maxDamage = phelper.getChildElementInteger("MaxDamage");
            proj.speed = phelper.getChildElementFloat("Speed");
            proj.lifetimeMS = phelper.getChildElementFloat("LifetimeMS");
            proj.size = phelper.getChildElementInteger("Size", 100);
            proj.parametric = phelper.hasChild("Parametric");
            proj.faceDir = phelper.hasChild("FaceDir");
            proj.passesCover = phelper.hasChild("PassesCover");
            proj.multiHit = phelper.hasChild("MultiHit");
            proj.armorPierce = phelper.hasChild("ArmorPierce");
            proj.boomerang = phelper.hasChild("Boomerang");
            proj.wavy = phelper.hasChild("Wavy");
            proj.magnitude = phelper.getChildElementFloat("Magnitude", 3);
            proj.amplitude = phelper.getChildElementFloat("Amplitude", 0);
            proj.frequency = phelper.getChildElementFloat("Frequency", 1);
            proj.acceleration = phelper.getChildElementFloat("Acceleration", 0);
            proj.accelerationDelay = phelper.getChildElementFloat("AccelerationDelay", 0);
            proj.speedClamp = phelper.getChildElementFloat("SpeedClamp", 100_000);
            proj.circleTurnDelay = phelper.getChildElementFloat("CircleTurnDelay", 0);
            proj.circleTurnAngle = phelper.getChildElementFloat("CircleTurnAngle", 0);
            go.projectiles.add(proj);
        }

        if(helper.hasChild("Presentation")){
            Element presentation = helper.getChild("Presentation");
            NodeList styles = presentation.getChildNodes();
            for (int i = 0; i < styles.getLength(); i++) {
                if(styles.item(i) instanceof Element styleElem){
                    Style style = new Style();
                    style.load(styleElem);
                    go.styles.put(style.getIdHash(), style);
                }
            }
        }

        if(go.goClass.equals("PetBehavior")){
            PET_BEHAVIOR.put(go.type, go);
        }else if(go.goClass.equals("PetAbility")){
            PET_ABILITIES.put(go.type, go);
        }else{
            OBJECTS.put(go.type, go);
        }
    }

    private static void loadGround(Element elem) throws XPathExpressionException {
        Ground ground = new Ground();
        ground.type = Integer.parseInt(elem.getAttribute("type").substring(2), 16);
        ground.id = elem.getAttribute("id");
        if(hasChild(elem,"Texture")){
            ground.textures.add(loadTexture(getChild(elem, "Texture")));
        }
        else if(hasChild(elem,"RandomTexture")){
            NodeList textures = (NodeList)compile("RandomTexture/Texture").evaluate(elem, XPathConstants.NODESET);
            for(int i = 0; i < textures.getLength(); i++){
                Element textureElem = (Element) textures.item(i);
                ground.textures.add(loadTexture(textureElem));
            }
        }
        else if(hasChild(elem,"AnimatedTexture")){
            Texture texture = loadTexture((Element)elem.getElementsByTagName("AnimatedTexture").item(0));
            texture.animated = true;
            ground.textures.add(texture);
        }
        if(hasChild(elem,"Speed")){
            ground.speed = Float.parseFloat(getChildElementText(elem, "Speed"));
        }
        if(hasChild(elem, "NoWalk"))ground.nowalk = true;
        if(hasChild(elem, "Sink"))ground.sink = true;
        GROUNDS.put(ground.type, ground);
    }

    private static Texture loadTexture(Element elem) throws XPathExpressionException {
        Texture texture = new Texture();
        if(elem != null && hasChild(elem, "File")){
            texture.file = getChildElementText(elem, "File");
            String indexStr = getChildElementText(elem, "Index");
            if (indexStr.startsWith("0x")) texture.index = Integer.parseInt(indexStr.substring(2), 16);
            else texture.index = Integer.parseInt(indexStr);
        }
        return texture;
    }

    private static String getChildElementText(Element elem, String child) throws XPathExpressionException {
        return getChild(elem, child).getTextContent();
    }

    private static Element getChild(Element elem, String child) throws XPathExpressionException {
        return (Element)compile("./" + child).evaluate(elem, XPathConstants.NODE);
    }

    private static boolean hasChild(Element elem, String child) throws XPathExpressionException {
        return (Boolean)compile("./" + child).evaluate(elem, XPathConstants.BOOLEAN);
    }

    public static List<GameObject> getGameObjectsOfClass(String classType){
        List<GameObject> objects = new ArrayList<>();
        OBJECTS.forEach((integer, gameObject) -> {
            if(gameObject.goClass.equals(classType))objects.add(gameObject);
        });
        return objects;
    }

    public static List<GameObject> getGameObjectsOfClass(String... classType){
        List<GameObject> objects = new ArrayList<>();
        OBJECTS.forEach((integer, gameObject) -> {
            if(Arrays.stream(classType).anyMatch(s -> gameObject.goClass.equals(s)))objects.add(gameObject);
        });
        return objects;
    }

    private static HashMap<String, XPathExpression> COMPILED = new HashMap<>();

    private static XPathExpression compile(String exp) throws XPathExpressionException {
        if(COMPILED.containsKey(exp))return COMPILED.get(exp);
        XPathExpression xexp = xPath.compile(exp);
        COMPILED.put(exp, xexp);
        return xexp;
    }

    public static void buildIdMap(){
        ID_TO_OBJECT.clear();
        for (GameObject value : OBJECTS.values()) {
            ID_TO_OBJECT.put(value.id, value);
        }
    }


}
