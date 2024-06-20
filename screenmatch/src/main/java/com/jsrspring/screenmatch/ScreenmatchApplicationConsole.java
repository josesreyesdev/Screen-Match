/*package com.jsrspring.screenmatch;

import com.jsrspring.screenmatch.main.MenuMain;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplicationConsole implements CommandLineRunner {

    @Autowired
    private SeriesRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplicationConsole.class, args);
    }

    /*
     * Metodo que se ejecuta justo despues de que la app se inicie
     * Note:  para saber consultar: interfaz CommandLineRunner
     * */
/*
    @Override
    public void run(String... args) {

        MenuMain menu = new MenuMain(repository);
        menu.showMenu();
    }

}
*/
