package com.jsrspring.screenmatch.main;

import com.jsrspring.screenmatch.model.*;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import com.jsrspring.screenmatch.service.ApiService;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class MenuMain {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiConsumption = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    private List<Series> series;
    private Optional<Series> searchedSeries;

    private final SeriesRepository repository;

    public MenuMain(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        //24, Game of Thrones, Vikings, Spartacus

        var option = -1;
        while (option != 0) {

            var menu = """
                     Elija una de las siguientes opciones que desee realizar:
                     1.- Buscar una Serie
                     2.- Buscar Episodios
                     3.- Mostrar Series buscadas
                     4.- Buscar Series por Titulo
                     5.- Top 5 mejores Series en la BD
                     6.- Buscar Serie por Categoria
                     7.- Buscar Series por cierto Numero de Temporadas y su Evaluación
                     8.- Buscar Episodios por Nombre
                     9.- Top 5 Episodios por Serie
                    \s
                     0. Salir;
                    \s""";
            System.out.println();
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            System.out.println();
            switch (option) {
                case 1:
                    // Buscar cualquier serie y guardar en la BD
                    searchWebSeries();
                    break;
                case 2:
                    // Buscar episodio y guardar en la BD
                    searchEpisodeBySeries();
                    break;
                case 3:
                    // Mostrar todas las seriesData buscadas
                    showSearchedSeries();
                    break;
                case 4:
                    //Buscar series por titulo
                    searchSeriesByTitle();
                    break;
                case 5:
                    //Buscar Top 5 de las mejores series en la BD
                    top5BestSeries();
                    break;
                case 6:
                    //Buscar Serie por categoria
                    searchSeriesByCategory();
                    break;
                case 7:
                    //Buscar serie por num de temporadas y su evaluacion
                    filterSeriesByNumberOfSeasonsAndEvaluation();
                    break;
                case 8:
                    //Buscar episodios por nombre
                    searchEpisodesByTitle();
                    break;
                case 9:
                    //Top 5 episodios por serie
                    top5EpisodesBySeries();
                    break;
                case 0:
                    System.out.println("Cerrando Aplicación....");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private SeriesData fetchSeriesData() {
        System.out.println();
        System.out.println("Ingresa el nombre de la serie a consultar: ");
        String seriesName = scanner.nextLine();
        String resultSeriesName = encodeAndFormatSeriesName(seriesName);

        String url = BASE_URL + resultSeriesName + "&apikey=" + apiKey;

        String json = apiConsumption.getData(url);
        System.out.println(json);
        return convertData.getData(json, SeriesData.class);
    }

    private void searchWebSeries() {
        SeriesData data = fetchSeriesData();
        //seriesData.add(seriesData);

        //Save seriesData
        Series series = new Series(data);
        repository.save(series);
    }

    private void showSearchedSeries() {
        series = repository.findAll();

        if (!series.isEmpty()) {
            series.stream()
                    .sorted(Comparator.comparing(Series::getGenre))
                    .forEach(System.out::println);
        } else System.out.println("Aún no haz buscado ninguna serie");
    }

    private void searchEpisodeBySeries() {
        System.out.println();
        showSearchedSeries();

        System.out.println();
        System.out.println("Escribe el nombre de la serie para ver sus episodios");
        var seriesName = scanner.nextLine();

        Optional<Series> filteredSeries = series.stream()
                .filter(s -> s.getTitle().toUpperCase().contains(seriesName.toUpperCase()))
                .findFirst();

        if (filteredSeries.isPresent()) {
            var foundSeries = filteredSeries.get();

            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= foundSeries.getTotalSeasons(); i++) {
                String url = BASE_URL + encodeAndFormatSeriesName(foundSeries.getTitle()) + "&season=" + i + "&apikey=" + apiKey;
                String json = apiConsumption.getData(url);
                SeasonData seasonData = convertData.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            System.out.println();
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodeData().stream()
                            .map(e -> new Episode(s.season(), e))
                    ).collect(Collectors.toList());

            foundSeries.setEpisodes(episodes);

            //Save series episodes in db
            repository.save(foundSeries);

        } else System.out.println("Serie no encontrada");
    }

    private void searchSeriesByTitle() {
        System.out.println();
        System.out.println("Escribe el nombre de la serie a buscar");
        var seriesName = scanner.nextLine();

        searchedSeries = repository.findByTitleContainsIgnoreCase(seriesName);

        if (searchedSeries.isPresent()) {
            System.out.println();
            System.out.println("Serie encontrada => " + searchedSeries.get());
        } else System.out.println("No encontre la serie en la BD");
    }

    private void top5BestSeries() {
        List<Series> topSeries = repository.findTop5ByOrderByEvaluationDesc();
        if (!topSeries.isEmpty()) {
            System.out.println();
            System.out.println("Top 5 series de la BD");
            topSeries.forEach(s ->
                    System.out.println("Serie: " + s.getTitle() + ", Evaluación: " + s.getEvaluation())
            );
        } else System.out.println("No encontre series en la BD");
    }

    private void searchSeriesByCategory() {
        System.out.println();
        System.out.println("Escribe la Genero/Categoria de la serie a buscar");
        var genre = scanner.nextLine();

        var category = Category.fromEsp(genre);

        List<Series> seriesByCategory = repository.findByGenre(category);

        if (!seriesByCategory.isEmpty()) {
            seriesByCategory.forEach(s -> {
                System.out.println("Series por categorias " + genre);
                System.out.println("Title: " + s.getTitle() + ", Categoria: " + s.getGenre());
            });
        } else System.out.println("no encontre series con la categoria: " + genre);

    }

    private void filterSeriesByNumberOfSeasonsAndEvaluation() {
        System.out.println();
        System.out.println("¿Filtrar séries con cuántas temporadas?");
        var totalSeason = scanner.nextInt();
        scanner.nextLine();

        System.out.println("¿Como evaluación apartir de cuál valor? ");
        var evaluation = scanner.nextDouble();
        scanner.nextLine();

        List<Series> filterSeries = repository
                .seriesBySeasonsAndEvaluation(totalSeason, evaluation);

        if (!filterSeries.isEmpty()) {
            System.out.println();
            System.out.println("**** SERIES FILTRADAS ****");
            filterSeries.forEach(s ->
                    System.out.println("Serie: " + s.getTitle() + " - Evaluation: " + s.getEvaluation() + " - Total Seasons: " + s.getTotalSeasons())
            );
        } else System.out.println("No encontre series por el numero de temporadas: "+ totalSeason + "  y evaluación: " + evaluation);
    }

    private void searchEpisodesByTitle() {
        System.out.println();
        System.out.println("Escribe el episodio a consultar");
        var episodeName = scanner.nextLine();

        List<Episode> episodes = repository.getEpisodeByName(episodeName);

        if (!episodes.isEmpty()) {
            System.out.println();
            System.out.println("+++***** Episodes Encontrados *****+++");
            episodes.forEach(e ->
                    System.out.printf("Serie: %s - Temporada: %s - Episodio: %s - Evaluacion: %s\n",
                            e.getSeries().getTitle(), e.getSeason(), e.getEpisodeNumber(), e.getEvaluation())
            );
        } else System.out.println("No encontre episodios con este titulo: "+ episodeName);
    }

    private void top5EpisodesBySeries() {
        searchSeriesByTitle();

        if (searchedSeries.isPresent()) {
            Series seriesName = searchedSeries.get();
            List<Episode> topEpisodes = repository.getTop5Episodes(seriesName);

            if (!topEpisodes.isEmpty()) {
                System.out.println();
                System.out.println("***** Top 5 episodios de la serie: " + seriesName.getTitle());
                topEpisodes.forEach(e ->
                        System.out.printf("Titulo: %s - Temporada: %s - Episodio: %s - Evaluacion: %s\n",
                                e.getTitle(), e.getSeason(), e.getEpisodeNumber(), e.getEvaluation())
                );
            } else System.out.println("No encontre Episodios para esta serie: " + seriesName.getTitle());
        }
    }
}