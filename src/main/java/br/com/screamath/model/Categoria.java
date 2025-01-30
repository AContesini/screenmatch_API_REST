package br.com.screamath.model;

import jdk.jfr.Category;

public enum Categoria {
    ACAO("Action", "Ação"),
    AVENTURA("Adventure", "Aventura"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DRAMA("Drama", "Drama"),
    FANTASIA("Fantasy", "Fantasia"),
    ROMANCE("Romance", "Romance"),
    TERROR("Horror", "Horror"),
    SHORT("Short","Short");

    private String CategoriaOmdB;
    private String CategoriaEmPortugues;

    Categoria(String categoriaOmdB, String categoriaEmPortugues) {
        CategoriaOmdB = categoriaOmdB;
        CategoriaEmPortugues = categoriaEmPortugues;
    }
    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
        if(categoria.CategoriaOmdB.equalsIgnoreCase(text)){
            return categoria;
         }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
    public static Categoria fromPortugues(String text){
        for (Categoria categoria : Categoria.values()){
            if(categoria.CategoriaEmPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
