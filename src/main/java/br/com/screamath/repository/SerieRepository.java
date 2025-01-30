package br.com.screamath.repository;

import br.com.screamath.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SerieRepository extends JpaRepository<Serie, Long>{
}
