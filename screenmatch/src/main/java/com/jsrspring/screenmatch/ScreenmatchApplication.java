package com.jsrspring.screenmatch;

import com.jsrspring.screenmatch.model.Episode;
import com.jsrspring.screenmatch.model.Season;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.service.APIConsumption;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    private final APIConsumption apiConsumption = new APIConsumption();
    private final ConvertData convertData = new ConvertData();

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	/*
	* Metodo que se ejecuta justo despues de que la app se inicie
	* Note:  para saber consultar: interfaz CommandLineRunner
	 * */
	@Override
	public void run(String... args) {

        try {
            String seriesName = "24";//"Game of Thrones, 24";
            String encodeResultName = encodeAndFormatSeriesName(seriesName);
            String apiKey = Configuration.API_KEY;

            // Consumo de una Serie
            Series seriesData = getSeriesData(encodeResultName, apiKey);
            System.out.println("Series => " + seriesData);

            // Consumo de un Episodio
            Episode episodeData = getEpisodeData(encodeResultName, "1", "1", apiKey);
            System.out.println("Episode of 24 => " + episodeData);

            // Consumo de Temporadas
            List<Season> seasons = getSeasonsData(encodeResultName, seriesData.totalSeasons(), apiKey);
            seasons.forEach(System.out::println);


        }catch (Exception e) {
            e.printStackTrace();
        }
	}

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private Series getSeriesData(String seriesName, String apiKey) {
        String url = buildURL(seriesName, apiKey, null, null);
        String json = apiConsumption.getData(url);
        return convertData.getData(json, Series.class);
    }

    private Episode getEpisodeData(String seriesName, String seasonNumber, String episodeNumber, String apiKey) {
        String url = buildURL(seriesName, apiKey, seasonNumber, episodeNumber);
        String json = apiConsumption.getData(url);
        return convertData.getData(json, Episode.class);
    }

    private List<Season> getSeasonsData(String seriesName, int totalSeasons, String apiKey) {
        List<Season> seasons = new ArrayList<>();
        for (int i = 1; i <= totalSeasons; i++) {
            String url = buildURL(seriesName, apiKey, String.valueOf(i), null);
            String json = apiConsumption.getData(url);
            Season seasonData = convertData.getData(json, Season.class);
            seasons.add(seasonData);
        }
        return seasons;
    }

    // construccion de las Urls
    private String buildURL(String seriesName, String apiKey, String seasonNumber, String episodeNumber) {
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
