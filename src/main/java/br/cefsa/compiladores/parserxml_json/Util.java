package br.cefsa.compiladores.parserxml_json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util 
{
	public static void writeFile(String endereco, String conteudo) throws IOException
	{
		 FileWriter file = new FileWriter(endereco);
		 PrintWriter printWriter = new PrintWriter(file);
		 
		 printWriter.printf(conteudo);
		 
		 file.close();
		 
	}

    public static StringBuilder mountObject(List<Tag> objects){
        StringBuilder json = new StringBuilder();
        
        for(int i = 0, size = objects.size(); i < size; i++){
            if((i + 1 < size && objects.get(i).name.equals(objects.get(i + 1).name)) || (i != 0 && i + 1 == size && objects.get(i).name.equals(objects.get(i - 1).name))){
                if(i == 0 || (i - 1 >= 0 && !objects.get(i - 1).name.equals(objects.get(i).name))){
                    json.append("\"" + objects.get(i).name + "\": [\n");
                }
            }
            else{
                json.append("\"" + objects.get(i).name + "\": ");
            }
            
            if(objects.get(i).value != null){
                json.append("\"" + objects.get(i).value + "\"");
            }
            else{
                json.append("{\n");
                json.append(mountObject(objects.get(i).child));
                json.append("\n}");
                if((i - 1 >= 0 && objects.get(i - 1).name.equals(objects.get(i).name)) && (i + 1 == size || (i + 1 < size && !objects.get(i).name.equals(objects.get(i + 1).name)))){
                    json.append("\n]");
                }
            }
            if(objects.size() > 1 && objects.get(objects.size() - 1) != objects.get(i))
                    json.append(",\n");
        }
        return json;
    }

    public static List<Tag> ConteudoDoObjeto(StringBuilder text){

        Pattern tagPattern = Pattern.compile("<(.+?)>(.+)</?(.+?)>", Pattern.DOTALL);
        Pattern property = Pattern.compile("[^<>]*");

        List<Tag> tags = new ArrayList<Tag>();

        StringBuilder xmlString = new StringBuilder();

        for(int i = 0; i < text.length(); i++){
            xmlString.append(text.toString().charAt(i));
            Matcher m = tagPattern.matcher(xmlString);
            if(m.find() && m.group(1).equals(m.group(3))){

                Tag tag = new Tag();
                tag.name = m.group(1);

                Matcher n = property.matcher(m.group(2).toString());

                if(n.matches()){
                    tag.value = m.group(2);
                }
                else{
                    StringBuilder novoTexto = new StringBuilder(m.group(2));
                    tag.child = ConteudoDoObjeto(novoTexto);
                }
                tags.add(tag);
                xmlString.setLength(0);
            }
        }
        
        return tags;
    }
        
    public static String convertXmlToJson (String fileName) throws IOException
    {
        Path path = Paths.get(fileName);

        StringBuilder file = new StringBuilder();
        file.append(Files.readAllLines(path));
        
        List<Tag> root = ConteudoDoObjeto(file);
        
        StringBuilder build = new StringBuilder();

        build = mountObject(root);
        build.insert(0, "{\n");
        build.append("\n}");

        return build.toString();
    }
}
