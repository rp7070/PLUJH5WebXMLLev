
package domplujh5;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class DomReadPLUJH5 {

    public static void main(String[] args) {
        try {
            // Az xml fájl beolvasása a projekt gyökeréből
            File xmlFile = new File("PLUJH5XML.xml");
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(xmlFile);
            
            // Normalizálás a felesleges szóközök és törések kiszűrésére
            dom.getDocumentElement().normalize();
            
            System.out.println("Gyökérelem: " + dom.getDocumentElement().getNodeName());
            System.out.println("=========================================\n");

            // --- Etterem feldolgozása ---
            NodeList etteremList = dom.getElementsByTagName("Etterem");
            for (int i = 0; i < etteremList.getLength(); i++) {
                Node node = etteremList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[ETTEREM]");
                    System.out.println("  Kód: " + element.getAttribute("Ekod"));
                    System.out.println("  Név: " + getChildValue("Nev", element));
                    System.out.println("  Cím: " + getChildValue("Varos", element) + ", " 
                                               + getChildValue("Utca", element) + " " 
                                               + getChildValue("Hazszam", element) + ".");
                    System.out.println("  Csillag: " + getChildValue("Csillag", element));
                    System.out.println("-----------------------------------------");
                }
            }

            // --- Foszakacs feldolgozása ---
            NodeList foszakacsList = dom.getElementsByTagName("Foszakacs");
            for (int i = 0; i < foszakacsList.getLength(); i++) {
                Node node = foszakacsList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[FOSZAKACS]");
                    System.out.println("  Kód: " + element.getAttribute("Fkod"));
                    System.out.println("  Étrem kód (e_f): " + element.getAttribute("e_f"));
                    System.out.println("  Név: " + getChildValue("Nev", element));
                    System.out.println("  Életkor: " + getChildValue("Eletkor", element));
                    System.out.println("  Végzettség: " + getChildValue("Vegzettseg", element));
                    System.out.println("-----------------------------------------");
                }
            }

            // --- Szakacs feldolgozása ---
            NodeList szakacsList = dom.getElementsByTagName("Szakacs");
            for (int i = 0; i < szakacsList.getLength(); i++) {
                Node node = szakacsList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[SZAKACS]");
                    System.out.println("  Kód: " + element.getAttribute("SZkod"));
                    System.out.println("  Étterem kód (e_sz): " + element.getAttribute("e_sz"));
                    System.out.println("  Név: " + getChildValue("Nev", element));
                    System.out.println("  Részleg: " + getChildValue("Reszleg", element));
                    System.out.println("  Végzettség: " + getChildValue("Vegzettseg", element));
                    System.out.println("-----------------------------------------");
                }
            }

            // --- Gyakornok feldolgozása ---
            NodeList gyakornokList = dom.getElementsByTagName("Gyakornok");
            for (int i = 0; i < gyakornokList.getLength(); i++) {
                Node node = gyakornokList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[GYAKORNOK]");
                    System.out.println("  Kód: " + element.getAttribute("GYkod"));
                    System.out.println("  Étterem kód (e_gy): " + element.getAttribute("e_gy"));
                    System.out.println("  Név: " + getChildValue("Nev", element));
                    System.out.println("  Gyakorlat kezdete: " + getChildValue("Kezdete", element));
                    System.out.println("  Időtartama: " + getChildValue("Idotartama", element));
                    System.out.println("  Műszak: " + getChildValue("Muszak", element));
                    System.out.println("-----------------------------------------");
                }
            }

            // --- Vendeg feldolgozása ---
            NodeList vendegList = dom.getElementsByTagName("Vendeg");
            for (int i = 0; i < vendegList.getLength(); i++) {
                Node node = vendegList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[VENDEG]");
                    System.out.println("  Kód: " + element.getAttribute("Vkod"));
                    System.out.println("  Név: " + getChildValue("Nev", element));
                    System.out.println("  Életkor: " + getChildValue("Eletkor", element));
                    System.out.println("  Cím: " + getChildValue("Varos", element) + ", " 
                                               + getChildValue("Utca", element) + " " 
                                               + getChildValue("Hazszam", element) + ".");
                    System.out.println("-----------------------------------------");
                }
            }

            // --- Rendeles feldolgozása ---
            NodeList rendelesList = dom.getElementsByTagName("Rendeles");
            for (int i = 0; i < rendelesList.getLength(); i++) {
                Node node = rendelesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("[RENDELES]");
                    System.out.println("  Vendég kód (Vkod): " + element.getAttribute("Vkod"));
                    System.out.println("  Étterem kód (Ekod): " + element.getAttribute("Ekod"));
                    System.out.println("  Étel: " + getChildValue("Etel", element));
                    System.out.println("  Összeg: " + getChildValue("Osszeg", element) + " Ft");
                    System.out.println("-----------------------------------------");
                }
            }

        } catch (Exception e) {
            System.err.println("Hiba történt a feldolgozás során: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Univerzális segédfüggvény a gyermekcsomópontok szöveges értékének lekérésére
    private static String getChildValue(String tagName, Element parentElement) {
        NodeList list = parentElement.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            Node node = list.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "Nincs megadva";
    }
}

