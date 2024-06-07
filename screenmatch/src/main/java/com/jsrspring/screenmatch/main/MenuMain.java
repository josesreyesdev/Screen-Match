package com.jsrspring.screenmatch.main;

import com.jsrspring.screenmatch.model.Season;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.model.SeriesDB;
import com.jsrspring.screenmatch.service.ApiService;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class MenuMain {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiConsumption = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    private final List<Series> seriesData = new ArrayList<>();

    public void showMenu() {
        //24, Game of Thrones, Vikings, Spartacus

        var option = -1;
        while (option != 0) {

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
                    searchWebSeries();
                    break;
                case 2:
                    searchEpisodeBySeries();
                    break;
                case 3:
                    showSearchedSeries();
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

    private Series fetchSeriesData() {
        System.out.println();
        System.out.println("Ingresa el nombre de la serie a consultar: ");
        String seriesName = scanner.nextLine();
        String resultSeriesName = encodeAndFormatSeriesName(seriesName);

        String url = BASE_URL + resultSeriesName + "&apikey=" + apiKey;

        String json = apiConsumption.getData(url);
        System.out.println(json);
        return convertData.getData(json, Series.class);
    }

    private void searchWebSeries() {
        Series series = fetchSeriesData();
        seriesData.add(series);
    }

    private void showSearchedSeries() {
        List<SeriesDB> seriesDBList;

        seriesDBList = seriesData.stream()
                .map(s -> new SeriesDB(s)) //.map(s -> new SeriesDB(s))
                .collect(Collectors.toList());

        if (!seriesDBList.isEmpty()) {
            seriesDBList.stream()
                    .sorted(Comparator.comparing(SeriesDB::getGenre))
                    .forEach(System.out::println);
        } else System.out.println("Aún no haz buscado ninguna serie");
    }

    private void searchEpisodeBySeries() {
        Series seriesData = fetchSeriesData();
        List<Season> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            String url = BASE_URL + encodeAndFormatSeriesName(seriesData.title()) + "&Season=" + i + "&apikey=" + apiKey;
            String json = apiConsumption.getData(url);
            Season seasonData = convertData.getData(json, Season.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

}