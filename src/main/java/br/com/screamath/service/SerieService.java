package br.com.screamath.service;

import br.com.screamath.dto.SerieDTO;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obeterTodasAsSeries(){
        return obterDados(repository.findAll());

    }
    public List<SerieDTO> obterTop5() {
        return obterDados(repository.buscaporTop());
    }


    private List<SerieDTO> obterDados(List<Serie> serie){
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                        s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse()))
                .collect(Collectors.toList());


    }


}
