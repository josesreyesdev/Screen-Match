package com.jsrspring.screenmatch;

import com.jsrspring.screenmatch.model.Episode;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.service.APIConsumption;
import com.jsrspring.screenmatch.service.ConvertData;
import com.jsrspring.screenmatch.utils.config.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	/*
	* Metodo que se ejecuta justo despues de que la app se inicie
	* Note:  para saber consultar: interfaz CommandLineRunner
	 * */
	@Override
	public void run(String... args) throws Exception {

        String seriesName = "24";//"Game of Thrones, 24";
        String encodeResultName = encodeAndFormatSeriesName(seriesName);
		String apikey = Configuration.API_KEY;

        /* Consumo de una Serie */
		String url = "https://www.omdbapi.com/?t="+encodeResultName+"&apikey="+apikey;

		var apiConsumption = new APIConsumption();

		var json = apiConsumption.getData(url);

		ConvertData convertData = new ConvertData();

		var seriesData = convertData.getData(json, Series.class);

		System.out.println();
		System.out.println("Series => " + seriesData);

        /* Consumo de una Temporada y Episodio */
        var episode = 1;
        url = "https://www.omdbapi.com/?t="+encodeResultName+"&season=1&episode=1&apikey="+apikey;
        json = apiConsumption.getData(url);

        var episodeData = convertData.getData(json, Episode.class);
        System.out.println("Episode of 24 => " + episodeData);
	}

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedMovieName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedMovieName.replace("+", "%20");
    }
}
