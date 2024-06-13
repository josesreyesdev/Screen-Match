package com.jsrspring.screenmatch.main;

import com.jsrspring.screenmatch.model.EpisodeData;
import com.jsrspring.screenmatch.model.SeasonData;
import com.jsrspring.screenmatch.model.SeriesData;
import com.jsrspring.screenmatch.service.ApiService;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FirstMenu {

    private final ApiService apiConsumption = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private final Scanner scanner = new Scanner(System.in);

    public void showFirstMenu() {

        try {
            System.out.println();
            System.out.println("Ingresa el nombre de la serie a consultar: ");
            var seriesName = scanner.nextLine();
            String encodeResultName = encodeAndFormatSeriesName(seriesName);

            // Consumo de una Serie
            SeriesData seriesData = getSeriesData(encodeResultName);
            System.out.println("SeriesData => " + seriesData);

            // Consumo de un Episodio
            EpisodeData episodeData = getEpisodeData(encodeResultName, "1", "1");
            System.out.println("EpisodeData of " + seriesName + " => " + episodeData);

            // Consumo de Temporadas
            List<SeasonData> seasonData = getSeasonsData(encodeResultName, seriesData.totalSeasons());
            seasonData.forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private SeriesData getSeriesData(String seriesName) {
        String url = buildURL(seriesName, null, null);
        String json = apiConsumption.getData(url);
        return convertData.getData(json, SeriesData.class);
    }

    private EpisodeData getEpisodeData(String seriesName, String seasonNumber, String episodeNumber) {
        String url = buildURL(seriesName, seasonNumber, episodeNumber);
        String json = apiConsumption.getData(url);
        return convertData.getData(json, EpisodeData.class);
    }

    private List<SeasonData> getSeasonsData(String seriesName, int totalSeasons) {
        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= totalSeasons; i++) {
            String url = buildURL(seriesName, String.valueOf(i), null);
            String json = apiConsumption.getData(url);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        return seasons;
    }

    // construccion de las Urls
    private String buildURL(String seriesName, String seasonNumber, String episodeNumber) {

        String apiKey = Configuration.API_KEY;
        StringBuilder urlBuilder = new StringBuilder("https://www.omdbapi.com/?t=");

        urlBuilder.append(seriesName).append("&apikey=").append(apiKey);
        if (seasonNumber != null && episodeNumber != null) {
            urlBuilder.append("&season=").append(seasonNumber).append("&episode=").append(episodeNumber);
        } else if (seasonNumber != null) {
            urlBuilder.append("&season=").append(seasonNumber);
        }
        return urlBuilder.toString();
    }
}
