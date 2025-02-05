package br.com.screamath.principal;

import br.com.screamath.model.DadosSerie;
import br.com.screamath.model.DadosTemporada;
import br.com.screamath.model.Episodios;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import br.com.screamath.service.ConsumoAPI;
import br.com.screamath.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO =  "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("API_KEY");
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<Serie> serie = new ArrayList<>();
    private Optional<Serie> seriebuscadaOptinal;
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    @Autowired
    private SerieRepository repositorio;
    public Principal(SerieRepository repositorio) {
     this.repositorio =repositorio;
    }

    public void exibirMenu(){
        var menu =
                """
                1- Inserir novas Series
                2- Buscas episodio
                3- Listar Serie Buscada
                4- Buscar uma Serie por nome
                5- Buscar por Nome de Ator
                6- Busca Top 5
                7- Busca por Genero
                8- Busca por Quantidade de Temporadas e Avalição
                9- Buscar por trecho
                10-Busca por Top episodios
                0-Sair 
                """;
        var opcao = -1;

        while (opcao !=0){
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao){
                case 1:
                    buscaSerieNaWeb();
                    break;
                case 2:
                    buscaPorEpisodios();
                    break;
                case 3:
                    SalvarNalistaSerie();
                    break;
                case  4:
                    buscaPorNomeSerie();
                    break;
                case 5:
                    buscaPorNomeAtor();
                    break;
                case 6:
                    buscarPorTopSeries();
                    break;
                case 7:
                    buscaPorGenero();
                    break;
                case 8:
                    buscaPorQuantidadeDeTemporadas();
                    break;
                case 9:
                    buscaPorTrechoDeEpisodio();
                    break;
                case 10:
                    buscarPorTopEpisodios();
                    break;
                case 0:
                    System.out.println("saindo...");
                default:
                    System.out.println("Opção invalida");
                    break;
            }
        }
    }



    private void buscaSerieNaWeb() {
     DadosSerie dados = obterDadosdaSerie();
     Serie serie = new Serie(dados);
//     dadosSeries.add(dados);
     repositorio.save(serie);

        System.out.println(dados);


    }

    private DadosSerie obterDadosdaSerie(){

        System.out.println("Digite nome de uma Serie");
        var serieBuscada = leitura.nextLine();
        leitura.nextLine();
        var json = consumo.obterDados(ENDERECO+serieBuscada.toLowerCase().replace(" ","+")+API_KEY);
        DadosSerie dados = conversor.obetemDados(json, DadosSerie.class);

        return dados;
    }

    private void buscaPorEpisodios() {
        try {
            SalvarNalistaSerie();
            System.out.println("Digite nome de uma Serie");
            var serieBuscada = leitura.nextLine();

            seriebuscadaOptinal = serie.stream()
                    .filter(s -> s.getTitulo().toLowerCase().contains(serieBuscada.toLowerCase()))
                    .findFirst();
            if (seriebuscadaOptinal.isPresent()) {
                var serieEncontrada = seriebuscadaOptinal.get();
                List<DadosTemporada> temporadas = new ArrayList<>();
                for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                    var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obetemDados(json, DadosTemporada.class);
                    temporadas.add(dadosTemporada);

                }
                temporadas.forEach(System.out::println);

                List<Episodios> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                                .map(e -> new Episodios(d.numero(), e)))
                        .collect(Collectors.toList());
                if(!episodios.isEmpty()){
                serieEncontrada.getEpisodios().addAll(episodios);
                repositorio.save(serieEncontrada);
                } else {
                    System.out.println("Nenhum novo episódio para adicionar.");
                }


            } else {
                System.out.println("Serie não encontrada");
            }
        }catch (DataIntegrityViolationException e){
            System.out.println("Episodios que tentou inserir podem estar duplicados");
        }

    }

    private void SalvarNalistaSerie() { 
        serie = repositorio.findAll();

        serie.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscaPorNomeSerie() {
        System.out.println("Digite nome da Serie Buscada");
        var buscaSerie = leitura.nextLine();

//        seriebuscadaOptinal = repositorio.buscaPorNome(buscaSerie);

        seriebuscadaOptinal =repositorio.findByTituloContainsIgnoreCase(buscaSerie);

        if(seriebuscadaOptinal.isPresent()){
            System.out.println(seriebuscadaOptinal.get().getTitulo());
        }
        else{
            System.out.println("Serie não Encontrada");
        }
    }

    private void buscaPorNomeAtor() {
        System.out.println("Digite parte do nome do Ator");
        var ator = leitura.nextLine();
       List<Serie> buscaporAtor = repositorio.findByAtoresContainsIgnoreCase(ator);
        System.out.println("Series em que " + ator + " trabalhou\n");
       buscaporAtor.forEach( s -> System.out.println("Serie: "+s.getTitulo()));

    }
    private void buscarPorTopSeries() {

        List<Serie> buscaporTop5Series = repositorio.buscaporTop();
        buscaporTop5Series.forEach(System.out::println);
    }

    private void buscaPorGenero() {
        System.out.println("Digite o Genero que você busca");
        var genero = leitura.nextLine();
        List<Serie> buscaGenero= repositorio.buscaPorGenero(genero);
        buscaGenero.forEach(s -> System.out.println("Serie: "+ s.getTitulo() + ", "+s.getGenero()));
    }
    private void buscaPorQuantidadeDeTemporadas() {
        System.out.println("Digite o Maximo de temporadas que busca ");
        var numTemporada = leitura.nextInt();
        System.out.println("Qual a nota da avaliação buscada?");
        var notaSerie = leitura.nextDouble();

        List<Serie> maximoDeTemporda = repositorio.bucaPorNumTemporaENota(numTemporada,notaSerie);
        maximoDeTemporda.forEach(s-> System.out.println("Serie " + s.getTitulo() +
                ", Temporadas:" + s.getTotalTemporadas() +
                ", Avaliação:" + s.getAvaliacao()));

    }
    private void buscaPorTrechoDeEpisodio() {
        System.out.println("Digite um trecho do Episodio");
        var trechoEpisodio = leitura.nextLine();
        List<Episodios> trecho = repositorio.buscarPorTrecho(trechoEpisodio);

        trecho.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
        e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumeroEpisodio(), e.getTitulo()));
    }
    private void buscarPorTopEpisodios() {

        System.out.println("Digite nome da Serie");
        var nomeSerie = leitura.nextLine();
        leitura.nextLine();
        seriebuscadaOptinal = repositorio.findByTituloContainsIgnoreCase(nomeSerie);

        if (seriebuscadaOptinal.isPresent()){
            Serie serie = seriebuscadaOptinal.get();
            List<Episodios> topEpsidos =  repositorio.buscaPorTopEpisodio(serie);
            topEpsidos.forEach(System.out::println);
        }

    }






}
