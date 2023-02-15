package ru.fazziclay.openwidgets.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class InternetUtils {
    public static String parseTextPage(String pageURL) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(pageURL);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

        String lineText;
        while((lineText = bufferedReader.readLine()) != null)
            result.append(lineText);

        bufferedReader.close();
        return result.toString();
    }
}
