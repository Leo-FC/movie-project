package br.com.cfl.movieproject.principal;

import br.com.cfl.movieproject.model.*;
import br.com.cfl.movieproject.service.ConsumoApi;
import br.com.cfl.movieproject.service.ConverteDados;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import java.io.FileInputStream;
import java.util.Properties;

public class Principal {


    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private List<Serie> seriesAdicionadas = new ArrayList<>();

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){
        var opcao = -1;

        while(opcao != 0) {
            var menu = """
                    \n
                    (1) Buscar séries
                    (2) Buscar episódios
                    (3) Listar séries buscadas
                    
                    (0) Sair.
                    \n
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodiosSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Encerrado.");
                    break;
                default:
                    System.out.println("Insira uma opcao válida.");
            }
        }
//        System.out.println("\n== Top 5 Episodios ==");
//        episodios.stream()
//                .sorted(Comparator.comparingDouble(Episodio::getAvaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

//        System.out.println("\nA partir de que ano você deseja ver os episódios? ");
//        var ano = sc.nextInt();
//        sc.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "S" + e.getTemporada() + "E" + e.getNumeroEpisodio() + " - " + e.getTitulo()
//                        + " | Data de lancamento: " + e.getDataLancamento().format(fmt)
//                ));


//        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        //System.out.println(avaliacoesPorTemporada);

//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//
//        Optional<Episodio> piorEpisodio = episodios.stream().filter(e -> e.getAvaliacao() == est.getMin()).findFirst();
//        Optional<Episodio> melhorEpisodio = episodios.stream().filter(e -> e.getAvaliacao() == est.getMax()).findFirst();
//
//        System.out.println("\nAvaliação média da Série " + nomeSerie + " : " +  est.getAverage()
//        + "\nMelhor episódio: " + est.getMax() + " - S" + melhorEpisodio.get().getTemporada() + "E" + melhorEpisodio.get().getNumeroEpisodio() + " - " + melhorEpisodio.get().getTitulo()
//        + "\nPior episódio: " + est.getMin() + " - S" + piorEpisodio.get().getTemporada() + "E" + piorEpisodio.get().getNumeroEpisodio() + " - " + piorEpisodio.get().getTitulo()
//        + "\nQuantidade de episódios avaliados: " + est.getCount());

    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série que voce deseja buscar: ");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + getApiKey());
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarSerieWeb(){
        Serie serieInfo = new Serie(getDadosSerie());
        seriesAdicionadas.add(serieInfo);
        System.out.println(serieInfo);
    }

    private void listarSeriesBuscadas(){

        //seriesAdicionadas.forEach(System.out::println);
        seriesAdicionadas.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarEpisodiosSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i <= dadosSerie.totalTemporadas() ; i++){
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + getApiKey());
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(), d)))
//                .collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);

    }

    private String getApiKey(){
        Properties props = new Properties();
        String apiKey;
        try {
            props.load(new FileInputStream("config.properties"));
            apiKey = props.getProperty("API_KEY");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return apiKey;
    }

}
