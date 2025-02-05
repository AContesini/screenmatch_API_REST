package br.com.screamath.repository;

import br.com.screamath.model.Episodios;
import br.com.screamath.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>{
    //Busca por nome serie JPQL 1#
//    @Query("SELECT s from Serie s WHERE LOWER(s.titulo) LIKE LOWER(CONCAT('%', :buscaSerie,'%'))")
//    Optional<Serie> buscaPorNome(@Param("buscaSerie") String buscaSerie);
    //Busca por nome serie JPA 1.2#
    Optional<Serie> findByTituloContainsIgnoreCase(String buscaSerie);
    //Busaca por ator
    List<Serie> findByAtoresContainsIgnoreCase(String ator);
    //Busca top 5 por maior avaliação ordem decrescente
    @Query("SELECT s FROM Serie s ORDER BY s.avaliacao DESC LIMIT 5")
    List<Serie> buscaporTop();
    //Busca todas as Series com genero pesquisado
    @Query("SELECT s FROM Serie s WHERE LOWER(s.genero) LIKE LOWER(CONCAT('%',:genero,'%'))")
    List<Serie> buscaPorGenero(String genero);
    //Busca por quantidade de temporada mais a avalição
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao ORDER BY s.avaliacao DESC")
    List<Serie> bucaPorNumTemporaENota(int totalTemporadas, double avaliacao);

    @Query("SELECT DISTINCT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodios> buscarPorTrecho(String trechoEpisodio);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE  s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodios> buscaPorTopEpisodio(Serie serie);
}