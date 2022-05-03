package br.cefsa.compiladores.parserxml_json;

import java.util.List;
import java.util.ArrayList;

public class Tag {
    public String name;
    public List<Tag> child = new ArrayList<>();
    public String value;
}