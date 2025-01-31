package br.com.screamath.traducao;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDeResposta(@JsonAlias(value ="translatedText") String textoTraduzido) {
}
