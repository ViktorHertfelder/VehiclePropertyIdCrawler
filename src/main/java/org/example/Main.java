package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> propDivList = getDivs();

        Pattern pattern = Pattern.compile("Constant Value:\\s*([0-9]+)");

        HashMap<String, String> propMap = new HashMap<>();

        for (String div : propDivList) {
            Document document = Jsoup.parse(div);
            Elements propName = document.select("div[data-version-added] h3.api-name");

            for (Element name : propName) {
                Element parentDiv = name.parent();
                String divText = null;
                if (parentDiv != null) {
                    divText = parentDiv.text();
                }
                Matcher matcher = pattern.matcher(divText);
                String matchedVal = "";

                if (matcher.find()) {
                    matchedVal = matcher.group(1);
                }

                propMap.put(name.text(), matchedVal);
            }
        }

        writeToFile(propMap);
    }

    private static void writeToFile(HashMap<String, String> propMap) throws IOException {
        String fileName = "VehiclePropertyList.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            for (Map.Entry<String, String> entry : propMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                writer.append(key);
                writer.append(",");
                writer.append(value);
                writer.append("\n");
            }
        }
    }

    private static List<String> getDivs() throws IOException {
        String url = "https://developer.android.com/reference/android/car/VehiclePropertyIds#constants_1";
        Document doc = Jsoup.connect(url).get();

        Elements propDivs = doc.select("div[data-version-added]");

        List<String> propDivList = new ArrayList<>();
        for (Element div : propDivs) {
            String html = div.html();
            propDivList.add(html);
        }
        return propDivList;
    }
}