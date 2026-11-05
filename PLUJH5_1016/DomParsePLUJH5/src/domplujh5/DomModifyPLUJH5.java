package domplujh5;

import java.io.File;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomModifyPLUJH5 {

    public static void main(String[] args) {
        try {
            // XML fájl beolvasása a projekt gyökeréből
            File xmlFile = new File("PLUJH5XML.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(xmlFile);
            dom.getDocumentElement().normalize();

            // 1.) Vendég módosítása (Vkod="V1" -> Nev és Eletkor átírása)
            NodeList vendegList = dom.getElementsByTagName("Vendeg");
            for (int i = 0; i < vendegList.getLength(); i++) {
                Node node = vendegList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element vendeg = (Element) node;
                    if (vendeg.getAttribute("Vkod").equals("V1")) {
                        // Név módosítása
                        NodeList nevList = vendeg.getElementsByTagName("Nev");
                        if (nevList.getLength() > 0) {
                            nevList.item(0).setTextContent("Kovács Péter");
                        }
                        // Eletkor módosítása
                        NodeList korList = vendeg.getElementsByTagName("Eletkor");
                        if (korList.getLength() > 0) {
                            korList.item(0).setTextContent("30");
                        }
                    }
                }
            }

            // 2.) Gyakornok módosítása (minden Gyakornok elemnél e_gy attribútum átírása "e3"-ra)
            NodeList gyakornokList = dom.getElementsByTagName("Gyakornok");
            for (int i = 0; i < gyakornokList.getLength(); i++) {
                Node node = gyakornokList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element gyakornok = (Element) node;
                    gyakornok.setAttribute("e_gy", "e3");
                }
            }

            // 3.) Összes Rendeles elem törlése az XML-ből
            NodeList rendelesList = dom.getElementsByTagName("Rendeles");
            // Hátulról előrefelé haladva törlünk az indexek dinamikus változása miatt
            for (int i = rendelesList.getLength() - 1; i >= 0; i--) {
                Node rendelesNode = rendelesList.item(i);
                rendelesNode.getParentNode().removeChild(rendelesNode);
            }

            // 4.) A módosított XML dokumentum kiírása a konzolra
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // Konzolformázás beállítása (behúzások és XML deklaráció)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://apache.org}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(dom);
            //StreamResult result = new StreamResult(xmlFile); // Itt adtuk meg a fájlt a StringWriter helyett
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            
            transformer.transform(source, result);
            
         // --- ATOMBIZTOS ÜRES SOR ELTÁVOLÍTÁS ---
            String nyersXml = writer.toString();
            StringBuilder tisztaXml = new StringBuilder();
            
            // Soronként daraboljuk fel a kapott XML-t
            String[] sorok = nyersXml.split("\\r?\\n");
            for (String sor : sorok) {
                // Ha a sor NEM üres és nem csak szóközökből áll, akkor megtartjuk
                if (!sor.trim().isEmpty()) {
                    tisztaXml.append(sor).append(System.lineSeparator());
                }
            }
            
            // A tökéletesen letisztított XML kiírása
            System.out.println(tisztaXml.toString());
            
            //System.out.println(writer.toString());
            //System.out.println(result.toString());
            
            // Mentés a file-ba
            String veglegesXmlSzoveg = tisztaXml.toString();
            java.io.FileOutputStream fos = new java.io.FileOutputStream(xmlFile);
            java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8);
           
            osw.write(veglegesXmlSzoveg);
            osw.flush(); // Biztosítjuk, hogy minden adat kiíródjon

        } catch (Exception e) {
            System.err.println("Hiba történt a módosítás során: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
