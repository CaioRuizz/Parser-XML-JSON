package br.cefsa.compiladores.parserxml_json;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String json = Util.convertXmlToJson("sample.xml");
        System.out.println(json);
        Util.writeFile("sample.json", json);
    }
}
