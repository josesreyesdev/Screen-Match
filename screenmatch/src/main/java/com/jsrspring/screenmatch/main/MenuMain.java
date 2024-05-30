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
import java.util.*;
import java.util.stream.Collectors;


public class MenuMain {

    private final Scanner scanner = new Scanner(System.in);
    private final APIConsumption apiConsumption = new APIConsumption();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    public void showMenu() {
        String seriesName = getUserInput("Ingresa el nombre de la serie a consultar: ");
        String resultSeriesName = encodeAndFormatSeriesName(seriesName);

        String url = BASE_URL + resultSeriesName + "&apikey=" + apiKey;
        Series seriesData = fetchSeriesData(url);
        System.out.println("\n" + seriesData);

        List<Season> seasons = fetchSeasonsData(resultSeriesName, seriesData.totalSeasons());
        System.out.println();
        seasons.forEach(System.out::println);

        /*displayEpisodeTitles(seasons);

        List<Episode> episodes = convertToEpisodeList(seasons);

        displayTop5Episodes(episodes, seriesName); */

        List<SeasonAndEpisode> seasonAndEpisodeList = convertToSeasonAndEpisodeList(seasons);

        /*searchEpisodesByYear(seasonAndEpisodeList);

        searchEpisodeByTitle(seasonAndEpisodeList); */

        evaluationPerSeason(seasonAndEpisodeList);
    }

    private String getUserInput(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private Series fetchSeriesData(String url) {
        String json = apiConsumption.getData(url);
        return convertData.getData(json, Series.class);
    }

    private List<Season> fetchSeasonsData(String seriesName, int totalSeasons) {
        List<Season> seasons = new ArrayList<>();
        for (int i = 1; i <= totalSeasons; i++) {
            String url = BASE_URL + seriesName + "&Season=" + i + "&apikey=" + apiKey;
            String json = apiConsumption.getData(url);
            Season seasonData = convertData.getData(json, Season.class);
            seasons.add(seasonData);
        }
        return seasons;
    }

    private void displayEpisodeTitles(List<Season> seasons) {
        System.out.println();
        seasons.forEach(s -> s.episodes().forEach(e ->
                System.out.println("Title: " + e.title() + " => Temporada: " + s.season())
        ));
    }

    private List<Episode> convertToEpisodeList(List<Season> seasons) {
        return seasons.stream()
                .flatMap(season -> season.episodes().stream())
                .collect(Collectors.toList());
    }

    private void displayTop5Episodes(List<Episode> episodes, String seriesName) {
        System.out.println();
        System.out.println("Top 5 episodes of: " + seriesName);
        episodes.stream()
                .filter(e -> !e.evaluation().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primero: filtro (N/A) " + e))
                .sorted(Comparator.comparing(Episode::evaluation).reversed())
                .peek(e -> System.out.println("Segundo: ordenando de mayor a menor " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Tercero: filtro del titulo a mayusculas " + e))
                .limit(5)
                .forEach(System.out::println);
    }

    private List<SeasonAndEpisode> convertToSeasonAndEpisodeList(List<Season> seasons) {
        return seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new SeasonAndEpisode(s.season(), e))
                )
                .collect(Collectors.toList());
    }

    private void searchEpisodesByYear(List<SeasonAndEpisode> seasonAndEpisodeList) {
        System.out.println();
        System.out.println("Ingresa el año del cual deseas ver el episodio");
        int year = scanner.nextInt();
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

    private void searchEpisodeByTitle(List<SeasonAndEpisode> seasonAndEpisodeList) {
        System.out.println();
        System.out.println("Ingresa el titulo del episodio que deseas ver:");
        String searchByPartOfTitle = scanner.nextLine();

        Optional<SeasonAndEpisode> searchByTitle = seasonAndEpisodeList.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(searchByPartOfTitle.toUpperCase()))
                .findFirst();

        if (searchByTitle.isPresent()) {
            System.out.println("Episodio encontrado:");
            System.out.println("Title: " + searchByTitle.get().getTitle());
            System.out.println("All data: " + searchByTitle.get());
        } else {
            System.out.println("Episodio no encontrado para: " + searchByPartOfTitle);
        }
    }

    private void evaluationPerSeason(List<SeasonAndEpisode> seasonAndEpisodes) {
        Map<Integer, Double> evaluationsSeason = seasonAndEpisodes.stream()
                .filter(e -> e.getEvaluation() > 0.0)
                .collect(Collectors.groupingBy(SeasonAndEpisode::getSeason,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(SeasonAndEpisode::getEvaluation),
                                average -> Math.round(average * 100.0) / 100.0)
                        )
                );

        System.out.println("Evaluaciones: " + evaluationsSeason);
    }
}