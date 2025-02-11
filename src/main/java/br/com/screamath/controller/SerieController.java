package br.com.screamath.controller;

import br.com.screamath.dto.SerieDTO;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import br.com.screamath.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}