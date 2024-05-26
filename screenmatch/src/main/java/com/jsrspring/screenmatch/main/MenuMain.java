package com.jsrspring.screenmatch.main;

import com.jsrspring.screenmatch.model.Episode;
import com.jsrspring.screenmatch.model.SeasonAndEpisode;
import com.jsrspring.screenmatch.model.Season;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.service.APIConsumption;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuMain {

    private final Scanner scanner = new Scanner(System.in);
    private final APIConsumption apiConsumption = new APIConsumption();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    public void showMenu() {

        System.out.println("Ingresa el nombre de la serie a consultar: ");
        var seriesName = scanner.nextLine();
        var resultSeriesName = encodeAndFormatSeriesName(seriesName);

        String url = BASE_URL + resultSeriesName + "&apikey=" + apiKey;
        var json = apiConsumption.getData(url);
        var seriesData = convertData.getData(json, Series.class);
        System.out.println();
        System.out.println(seriesData);

        List<Season> seasons = new ArrayList<>();
        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            url = BASE_URL + resultSeriesName + "&Season=" + i + "&apikey=" + apiKey;
            json = apiConsumption.getData(url);
            Season seasonData = convertData.getData(json, Season.class);
            seasons.add(seasonData);
        }
        System.out.println();
        seasons.forEach(System.out::println);

        // Mostrar titulo de los episodios de las temporadas
        System.out.println();
        seasons.forEach(s -> s.episodes().forEach(e ->
                System.out.println("Title: " + e.title() + " => Temporada: " + s.season())
        ));

        // Convertir toda la info a una lista de Episode
        List<Episode> episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()) // convierte cada teporada a episodios
                //.toList(); // hacemos una lista inmutable
                .collect(Collectors.toList()); // con collect hacemos una lista mutable

        // Top 5 episodes
        System.out.println();
        System.out.println("Top 5 episodes of: " + seriesName);
        episodes.stream()
                .filter(e -> !e.evaluation().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(Episode::evaluation).reversed())
                .limit(5)
                .forEach(System.out::println);

        // convert data to type SeasonAndEpisode
        List<SeasonAndEpisode> seasonAndEpisodeList = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new SeasonAndEpisode(s.season(), e))
                )
                .collect(Collectors.toList());

        seasonAndEpisodeList.forEach(System.out::println);

        // Busqueda de episodios por x año
        System.out.println();
        System.out.println("Ingresa el año del cual deseas ver el episodio");
        var year = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        seasonAndEpisodeList.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada=" + e.getSeason() +
                                ", Titulo=" + e.getTitle() +
                                ", Episodio=" + e.getEpisodeNumber() +
                                ", Evaluacion=" + e.getEvaluation() +
                                ", Fecha de Lanzamiento = " + e.getReleaseDate().format(dtf)
                ));
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }
}
