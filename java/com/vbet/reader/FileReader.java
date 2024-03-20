package com.vbet.reader;

import org.springframework.stereotype.Component;

@Component
public interface FileReader {

    String readFile(String filePath, String encoding);
}
