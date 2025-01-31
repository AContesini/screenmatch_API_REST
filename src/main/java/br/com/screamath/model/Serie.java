package br.com.screamath.model;

import br.com.screamath.traducao.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;

    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;

    private String sinopse;



    private String poster;

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.atores = dadosSerie.atores();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.poster = dadosSerie.poster();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        String sinopseTraduzida = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse().trim());
        this.sinopse = sinopseTraduzida.length() > 255 ? sinopseTraduzida.substring(0,255):sinopseTraduzida;


    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                ", titulo='" + titulo +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", genero=" + genero +
                ", atores='" + atores  +
                ", sinopse='" + sinopse +
                ", poster='" + poster ;
    }
}
