package com.jsrspring.screenmatch;

import com.jsrspring.screenmatch.main.MenuMain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    public void run(String... args) {

        MenuMain menu = new MenuMain();
        menu.showMenu();
    }

}
