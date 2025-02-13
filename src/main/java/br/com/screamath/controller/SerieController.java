package br.com.screamath.controller;

import br.com.screamath.dto.EpisodioDTO;
import br.com.screamath.dto.SerieDTO;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import br.com.screamath.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obeterSeries(){
        return service.obeterTodasAsSeries();
    }

    @GetMapping("/top5")
    public  List<SerieDTO> obterTop5Serie(){
        return service.obterTop5();
    }

    @GetMapping("/lancamentos")
    public  List<SerieDTO> obterLancamentos(){
        return service.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obeterPorId(@PathVariable Long id){
        return service.obeterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return service.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id,@PathVariable Long numero){
        return service.obterTemporadasPorNumero(id,numero);
    }
}