package com.jsrspring.screenmatch;

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
		String seriesName = "24";//"Game of Thrones";

		String encodedMovieName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
		String seriesResultName = encodedMovieName.replace("+", "%20");

		String apikey = Configuration.API_KEY;

		String url = "https://www.omdbapi.com/?t="+seriesResultName+"&apikey="+apikey;

		var apiConsumption = new APIConsumption();
		var json = apiConsumption.getData(url);
		System.out.println("Response => " + json);

		ConvertData convertData = new ConvertData();
		var data = convertData.getData(json, Series.class);

		System.out.println();
		System.out.println("Series => " + data);
	}
}
