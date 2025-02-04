package br.com.screamath.repository;

import br.com.screamath.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>{
//    @Query("SELECT s from Serie s WHERE LOWER(s.titulo) LIKE LOWER(CONCAT('%', :buscaSerie,'%'))")
//    Optional<Serie> buscaPorNome(@Param("buscaSerie") String buscaSerie);

    Optional<Serie> findByTituloContainsIgnoreCase(String buscaSerie);

    List<Serie> findByAtoresContainsIgnoreCase(String ator);

    @Query("SELECT s FROM Serie s ORDER BY s.avaliacao DESC LIMIT 5")
    List<Serie> buscaporTop();
    @Query("SELECT s FROM Serie s WHERE LOWER(s.genero) LIKE LOWER(CONCAT('%',:genero,'%'))")
    List<Serie> buscaPorGenero(String genero);
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao ORDER BY s.avaliacao DESC")
    List<Serie> bucaPorNumTemporaENota(int totalTemporadas, double avaliacao);
}