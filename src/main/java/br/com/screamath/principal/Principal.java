package br.com.screamath.principal;

import br.com.screamath.model.DadosSerie;
import br.com.screamath.model.DadosTemporada;
import br.com.screamath.model.Episodios;
import br.com.screamath.model.Serie;
import br.com.screamath.repository.SerieRepository;
import br.com.screamath.service.ConsumoAPI;
import br.com.screamath.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

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
                1-Inserir novas Series
                2-Buscas episodio
                3-Listar Serie Buscada
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
        SalvarNalistaSerie();
        System.out.println("Digite nome de uma Serie");
       var serieBuscada = leitura.nextLine();

       seriebuscadaOptinal = serie.stream()
               .filter(s -> s.getTitulo().toLowerCase().contains(serieBuscada.toLowerCase()))
               .findFirst();
      if(seriebuscadaOptinal.isPresent()) {
          var serieEncontrada = seriebuscadaOptinal.get();
          List<DadosTemporada> temporadas = new ArrayList<>();
          for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
              var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+")+"&season="+ i + API_KEY);
              DadosTemporada dadosTemporada = conversor.obetemDados(json,DadosTemporada.class);
              temporadas.add(dadosTemporada);

          }
          temporadas.forEach(System.out::println);

          List<Episodios> episodios = temporadas.stream()
                  .flatMap(d -> d.episodios().stream()
                          .map(e -> new Episodios(d.numero(),e)))
                  .collect(Collectors.toList());

          serieEncontrada.setEpisodios(episodios);
          repositorio.save(serieEncontrada);
      }else {
          System.out.println("Serie não encontrada");
      }


    }

    private void SalvarNalistaSerie() { 
        serie = repositorio.findAll();

        serie.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
