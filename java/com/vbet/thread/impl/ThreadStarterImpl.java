package com.vbet.thread.impl;

import com.vbet.reader.FileReader;
import com.vbet.thread.AutomaticDataUpdater;
import com.vbet.thread.ThreadStarter;
import com.vbet.writer.FileWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@RequiredArgsConstructor
public class ThreadStarterImpl implements ThreadStarter {

    private static final String SETTINGS_TEXT = "auto_update_data=on\n" + "partial_renewal=week\n" + "\n" +
            "//Настройки:\n" + "//auto_update_data\n" + "//on | off\n" + "//partial_renewal:\n" +
            "//Месяц=month\n" + "//Неделя=week\n" + "//День=day\n" + "//Час=hour\n" + "//Минута=minute\n" +
            "//Если время конкретное, пишите числом в милисекундах(1 секунда = 1000 мсек)";
    private static final String ENCODING = "utf-8";

    private static AutomaticDataUpdater updater;
    private final FileWriter fileWriter;
    private final FileReader fileReader;




//    @PostConstruct
    public void startUpdateThread() {

        updater = new AutomaticDataUpdater();
        updater.start();
    }

    @Override
    public void restartThread() {
        if(updater.isAlive()){
            updater.interrupt();
        }
        updater = new AutomaticDataUpdater();
        updater.start();
    }
}
