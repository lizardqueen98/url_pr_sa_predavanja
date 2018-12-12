package ba.unsa.etf.rpr.predavanje18;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.json.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void ispisiElement(Element element) {
        System.out.print("Element "+element.getTagName()+", ");

        int brAtributa = element.getAttributes().getLength();
        System.out.print(brAtributa+" atributa");

        NodeList djeca = element.getChildNodes();

        // Ako nema djece ispisujemo sadržaj
        if (djeca.getLength() == 1) {
            String sadrzaj = element.getTextContent();
            System.out.println(", sadrzaj: '" + sadrzaj + "'");
        } else {
            System.out.println("");
        }

        for(int i = 0; i < djeca.getLength(); i++) {
            Node dijete = djeca.item(i);
            if (dijete instanceof Element) {
                ispisiElement((Element)dijete);
            }
        }
    }

    public static void urlDemo() {
        Scanner scanner = new Scanner(System.in);
        String adresa = scanner.nextLine();
        try {
            URL url = new URL(adresa);
            System.out.println("URL je ok, protokol " + url.getProtocol() + ", autoritet " + url.getAuthority());
        } catch(MalformedURLException e) {
            System.out.println("String "+adresa+" ne predstavlja validan URL");
        }
    }

    public static void xmlApiDemo() {
        String adresa = "http://www.thomas-bayer.com/sqlrest/CUSTOMER/0/";
        try {
            URL url = new URL(adresa);
            InputStream ulaz = url.openStream();

            DocumentBuilder docReader
                    = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmldoc = docReader.parse(ulaz);

            Element korijen = xmldoc.getDocumentElement();
            ispisiElement(korijen);

            ulaz.close();
        } catch(MalformedURLException e) {
            System.out.println("String "+adresa+" ne predstavlja validan URL");
        } catch(IOException e) {
            System.out.println("Greška pri kreiranju ulaznog toka");
        } catch(Exception e) {
            System.out.println("Poteškoće sa obradom XML podataka");
        }
    }

    public static void jsonApiDemo() {
        String adresa = "https://aws.random.cat/meow";
        try {
            URL url = new URL(adresa);
            BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String json = "", line = null;
            while ((line = ulaz.readLine()) != null)
                json = json + line;

            JSONObject obj = new JSONObject(json);
            System.out.println("cat: " + obj.getString("file"));

            ulaz.close();
        } catch(MalformedURLException e) {
            System.out.println("String "+adresa+" ne predstavlja validan URL");
        } catch(IOException e) {
            System.out.println("Greška pri kreiranju ulaznog toka");
            System.out.println(e.getMessage());
        } catch(Exception e) {
            System.out.println("Poteškoće sa obradom JSON podataka");
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        urlDemo();
        xmlApiDemo();
        jsonApiDemo();
    }
}
