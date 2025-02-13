package br.com.screamath.service;

import br.com.screamath.dto.EpisodioDTO;
import br.com.screamath.dto.SerieDTO;
import br.com.screamath.model.Categoria;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obeterTodasAsSeries() {
        return obterDados(repository.findAll());

    }

    public List<SerieDTO> obterTop5() {
        return obterDados(repository.buscaporTop());
    }

    public List<SerieDTO> obterLancamentos() {
        return obterDados(repository.lancamentosMaisRecentes());
    }

    public SerieDTO obeterPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                    s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());

        } else {
            return null;
        }
    }

    private List<SerieDTO> obterDados(List<Serie> serie) {
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                        s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());


    }


    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios()
                    .stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
       return repository.obterEpisodiosPorTemporada(id,numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterPorCatgoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return obterDados(repository.findByGenero(categoria));
    }
}