package com.vbet;

import com.vbet.example.Bird;
import com.vbet.example.Snegir;
import com.vbet.example.Vorobey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class VbetApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(VbetApplication.class, args);
        Bird varaobey = new Vorobey();
        Bird snegir = new Snegir();
        int i = 0;

        List<Bird> birds = new ArrayList<>();
        birds.add(varaobey);
        birds.add(snegir);
        birds.add(snegir);

        for (Bird bird : birds) {
            bird.say();
        }
        for (int i =0; i < birds.size(); i++) {
            birds.get(i).say();
        }

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder
                                                         application) {
        return application.sources(VbetApplication.class);
    }

}