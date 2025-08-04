package br.com.cfl.movieproject.principal;

import br.com.cfl.movieproject.model.*;
import br.com.cfl.movieproject.repository.SerieRepository;
import br.com.cfl.movieproject.service.ConsumoApi;
import br.com.cfl.movieproject.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

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

    private List<Serie> seriesArmazenadas = new ArrayList<>();
    private List<Episodio> episodiosArmazenados = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu(){
        var opcao = -1;

        while(opcao != 0) {
            var menu = """
                    \n
                    (1) Buscar séries
                    (2) Buscar episódios
                    (3) Listar séries buscadas
                    (4) Buscar série por titulo
                    (5) Buscar séries por ator
                    (6) Top 5 séries
                    (7) Buscar séries por genero
                    (8) Buscar série por um máximo de temporadas e avaliacação
                    (9) Buscar episódio de uma série por nome
                    (10) Buscar top episódios de uma série
                    
                    (0) Sair.
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorGenero();
                    break;
                case 8:
                    buscarSeriePorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
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

    private void topEpisodiosPorSerie() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = sc.nextLine().toUpperCase();

        System.out.println("Top quantos episódios? (ex: Top 3, Top 5, etc). Digite apenas o número: ");
        var limite = sc.nextInt();
        sc.nextLine();

        List<Episodio> topEpisodiosEncontrados = repositorio.buscarTopEpisodios(nomeSerie, limite);
        System.out.println("Top " + limite + " de " + nomeSerie + ":");
        topEpisodiosEncontrados.forEach(System.out::println);
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio:");
        var trechoEpisodio = sc.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.println("► Episódio da série: " + e.getSerie().getTitulo() + " | " + e));
    }

    private void buscarSeriePorTemporadaEAvaliacao() {
        System.out.println("Digite a quantidade máxima de temporadas que a série deve possuir:");
        var qtdTemporadas = sc.nextInt();
        sc.nextLine();

        System.out.println("Digite a avaliação mínima que a série deve ter: ");
        var avaliacao = sc.nextDouble();

        List<Serie> seriesPorAvaliacaoENota = repositorio.seriesPorTemporadaEAvaliacao(qtdTemporadas, avaliacao);
        seriesPorAvaliacaoENota.forEach(System.out::println);
    }

    private void buscarSeriesPorGenero() {
        System.out.println("Digite o nome do gênero: ");
        var nomeGenero = sc.nextLine();
        Categoria genero = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorGenero = repositorio.findByGenero(genero);

        System.out.println("Séries do gênero " + nomeGenero + ":");
        seriesPorGenero.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5Series.forEach(System.out::println);
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator/atriz: ");
        var nomeAtor = sc.nextLine();
        List<Serie> seriesEncontradas = repositorio.findByElencoContainingIgnoreCase(nomeAtor);

        seriesEncontradas.forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("\nEscolha uma série pelo nome:");
        var nomeSerie = sc.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println(serieBuscada.get());
        }else{
            System.out.println("Série não encontrada.");
        }
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

        repositorio.save(serieInfo);

        System.out.println(serieInfo);
    }

    private void listarSeriesBuscadas(){

        seriesArmazenadas = repositorio.findAll();
        seriesArmazenadas.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

//        //seriesAdicionadas.forEach(System.out::println);
//        seriesAdicionadas.stream()
//                .sorted(Comparator.comparing(Serie::getGenero))
//                .forEach(System.out::println);
    }

    private void buscarEpisodiosSerie(){

        //episodiosArmazenados.forEach(System.out::println);
        listarSeriesBuscadas();

        System.out.println("\nEscolha uma série pelo nome:");
        var nomeSerie = sc.nextLine();


        seriesArmazenadas = repositorio.findAll().stream().filter(s -> s.getTitulo().equalsIgnoreCase(nomeSerie)).toList();
        episodiosArmazenados = seriesArmazenadas.stream()
                .flatMap(s -> s.getEpisodios().stream())
                .collect(Collectors.toList());

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getQtdTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + getApiKey());
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            //temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                        .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);


            if(serieEncontrada.getEpisodios().equals(episodiosArmazenados)){
                System.out.println("Os episodios dessa série já foram adicionados, nenhuma mudança realizada.\nVeja os episódios dessa série:\n");
                episodiosArmazenados.forEach(System.out::println);
            }
            else {
                System.out.println("Episodios novos adicionados:");
                serieEncontrada.getEpisodios().forEach(System.out::println);

                repositorio.save(serieEncontrada);
            }
            //serieEncontrada.getEpisodios().forEach(System.out::println);
            //repositorio.save(serieEncontrada);

        }else{
            System.out.println("Série não encontrada.");
        }

//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(), d)))
//                .collect(Collectors.toList());
//
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
