package com.jsrspring.screenmatch.utils.rules;

import com.jsrspring.screenmatch.model.Episode;
import com.jsrspring.screenmatch.model.EpisodeData;
import com.jsrspring.screenmatch.model.SeasonData;
import com.jsrspring.screenmatch.model.SeriesData;
import com.jsrspring.screenmatch.service.ApiService;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BusinessRules {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiConsumption = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private static final String BASE_URL = "https://www.omdbapi.com/?t=";
    private static final String apiKey = Configuration.API_KEY;

    private String getUserInput(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private SeriesData fetchSeriesData() {
        String seriesName = getUserInput("Ingresa el nombre de la serie a consultar: ");
        String resultSeriesName = encodeAndFormatSeriesName(seriesName);

        String url = BASE_URL + resultSeriesName + "&apikey=" + apiKey;

        String json = apiConsumption.getData(url);
        System.out.println(json);
        return convertData.getData(json, SeriesData.class);
    }

    private List<SeasonData> fetchSeasonsData(String seriesName, int totalSeasons) {
        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= totalSeasons; i++) {
            String url = BASE_URL + seriesName + "&SeasonData=" + i + "&apikey=" + apiKey;
            String json = apiConsumption.getData(url);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        return seasons;
    }

    private void displayEpisodeTitles(List<SeasonData> seasonData) {
        System.out.println();
        seasonData.forEach(s -> s.episodeData().forEach(e ->
                System.out.println("Title: " + e.title() + " => Temporada: " + s.season())
        ));
    }

    private List<EpisodeData> convertToEpisodeList(List<SeasonData> seasonData) {
        return seasonData.stream()
                .flatMap(season -> season.episodeData().stream())
                .collect(Collectors.toList());
    }

    private void displayTop5Episodes(List<EpisodeData> episodeData, String seriesName) {
        System.out.println();
        System.out.println("Top 5 episodeData of: " + seriesName);
        episodeData.stream()
                .filter(e -> !e.evaluation().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primero: filtro (N/A) " + e))
                .sorted(Comparator.comparing(EpisodeData::evaluation).reversed())
                .peek(e -> System.out.println("Segundo: ordenando de mayor a menor " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Tercero: filtro del titulo a mayusculas " + e))
                .limit(5)
                .forEach(System.out::println);
    }

    private List<Episode> convertToSeasonAndEpisodeList(List<SeasonData> seasonData) {
        return seasonData.stream()
                .flatMap(s -> s.episodeData().stream()
                        .map(e -> new Episode(s.season(), e))
                )
                .collect(Collectors.toList());
    }

    private void searchEpisodesByYear(List<Episode> episodeList) {
        System.out.println();
        System.out.println("Ingresa el año del cual deseas ver el episodio");
        int year = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodeList.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada=" + e.getSeason() +
                                ", Titulo=" + e.getTitle() +
                                ", Episodio=" + e.getEpisodeNumber() +
                                ", Evaluacion=" + e.getEvaluation() +
                                ", Fecha de Lanzamiento = " + e.getReleaseDate().format(dtf)
                ));
    }

    private void searchEpisodeByTitle(List<Episode> episodeList) {
        System.out.println();
        System.out.println("Ingresa el titulo del episodio que deseas ver:");
        String searchByPartOfTitle = scanner.nextLine();

        Optional<Episode> searchByTitle = episodeList.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(searchByPartOfTitle.toUpperCase()))
                .findFirst();

        System.out.println();
        if (searchByTitle.isPresent()) {
            System.out.println("Episodio encontrado:");
            System.out.println("Title: " + searchByTitle.get().getTitle());
            System.out.println("All data: " + searchByTitle.get());
        } else {
            System.out.println("Episodio no encontrado para: " + searchByPartOfTitle);
        }
    }

    private void evaluationPerSeason(List<Episode> episodes) {
        Map<Integer, Double> evaluationsSeason = episodes.stream()
                .filter(e -> e.getEvaluation() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                                Collectors.collectingAndThen(
                                        Collectors.averagingDouble(Episode::getEvaluation),
                                        average -> Math.round(average * 100.0) / 100.0)
                        )
                );

        System.out.println();
        System.out.println("Evaluaciones: " + evaluationsSeason);
    }

    private void statistics(List<Episode> episodes) {
        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getEvaluation() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getEvaluation));

        System.out.println();
        System.out.println("Estadisticas => " + est);
        System.out.println("Media Evaluaciones => " + est.getAverage());
        System.out.println("Mejor evaluado => " + est.getMax());
        System.out.println("Peor evaluado => " + est.getMin());
    }
}
