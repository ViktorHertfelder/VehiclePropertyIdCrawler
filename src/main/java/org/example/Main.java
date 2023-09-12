package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "https://developer.android.com/reference/android/car/VehiclePropertyIds#constants_1";
        Document doc = Jsoup.connect(url).get();

        Elements names = doc.select("div[data-version-added] h3");
        Elements ids = doc.select("div[data-version-added]");

        List<String> propNameList = new ArrayList<>();
        for (Element element : names) {
            String line = element.text();
            propNameList.add(line);
        }
        List<String> propIdList = new ArrayList<>();
        Pattern pattern = Pattern.compile("Constant Value:\\s*([0-9]+ \\(0x[0-9a-fA-F]+\\))");
        for (Element id : ids) {
            String line = id.text();
            propIdList.add(line);
            System.out.println(line);
        }


        System.out.println(propNameList.size());
        System.out.println(propIdList.size());
    }
}