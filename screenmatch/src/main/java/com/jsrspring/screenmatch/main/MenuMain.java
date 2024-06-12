package com.jsrspring.screenmatch.main;

import com.jsrspring.screenmatch.model.SeasonData;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.model.SeriesData;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import com.jsrspring.screenmatch.service.ApiService;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class MenuMain {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiConsumption = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    //private final List<SeriesData> seriesData = new ArrayList<>();

    private final SeriesRepository repository;

    public MenuMain(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        //24, Game of Thrones, Vikings, Spartacus

        var option = -1;
        while (option != 0) {

            /*4.- Buscar Serie por Titulo
                     5.- Top 5 mejores SeriesData
                     6.- Buscar SeriesData por Categoria
                     7.- Filtrar SeriesData por el numero de Temporadas y su Evaluación
                     8.- Buscar Episodios por Nombre
                     9-. Top 5 Episodios por Serie */
            var menu = """
                     Elija una de las siguientes opciones que desee realizar:
                     1.- Buscar una Serie
                     2.- Buscar Episodios
                     3.- Mostrar Series buscadas
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
                /*case 4:
                    // Buscar serie por title
                    //showSeriesByTitle();
                    break;
                case 5:
                    // Top 5 mejores seriesData
                    //showTopSeries();
                    break;
                case 6:
                    // buscar seriesData por categoria
                    //searchSeriesByCategories();
                    break;
                case 7:
                    // Filtrar seriesData por el num de temporaas y su evaluacion
                    //filterSeriesByNumSeasonAndEvaluation();
                    break;
                case 8:
                    // search episodeData by name
                    //searchEpisodesByName();
                    break;
                case 9:
                    // top 5 episodeData by seriesData
                    //topEpisodesBySeason();
                    break; */
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
        List<Series> seriesList = repository.findAll();

        if (!seriesList.isEmpty()) {
            seriesList.stream()
                    .sorted(Comparator.comparing(Series::getGenre))
                    .forEach(System.out::println);
        } else System.out.println("Aún no haz buscado ninguna serie");
    }

    private void searchEpisodeBySeries() {
        SeriesData seriesData = fetchSeriesData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            String url = BASE_URL + encodeAndFormatSeriesName(seriesData.title()) + "&SeasonData=" + i + "&apikey=" + apiKey;
            String json = apiConsumption.getData(url);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

}