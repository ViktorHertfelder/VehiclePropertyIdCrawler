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
        List<String> vehiclePropId = getDivs("https://developer.android.com/reference/android/car/VehiclePropertyIds#constants_1");
        HashMap<String, String> propMap = appendValuesToMap(vehiclePropId);
        writeToFile(propMap, "VehiclePropertyList.csv");

        List<String> vehicleIgnState = getDivs("https://developer.android.com/reference/android/car/VehicleIgnitionState#constants_1");
        HashMap<String, String> ignStateMap = appendValuesToMap(vehicleIgnState);
        writeToFile(ignStateMap, "VehicleIgnitionStateProperties.csv");

        List<String> vehicleUnits = getDivs("https://developer.android.com/reference/android/car/VehicleUnit#constants_1");
        HashMap<String, String> unitMap = appendValuesToMap(vehicleUnits);
        writeToFile(unitMap, "VehicleUnitList.csv");

        List<String> vehicleGear = getDivs("https://developer.android.com/reference/android/car/VehicleGear#constants_1");
        HashMap<String, String> gearMap = appendValuesToMap(vehicleGear);
        writeToFile(gearMap, "VehicleGearList.csv");

        List<String> vehicleAreaWheel = getDivs("https://developer.android.com/reference/android/car/VehicleAreaWheel#constants_1");
        HashMap<String, String> wheelMap = appendValuesToMap(vehicleAreaWheel);
        writeToFile(wheelMap, "VehicleAreaWheelMap.csv");

        List<String> vehicleAreaType = getDivs("https://developer.android.com/reference/android/car/VehicleAreaType#constants_1");
        HashMap<String, String> typeMap = appendValuesToMap(vehicleAreaType);
        writeToFile(typeMap, "VehicleAreaTypeList.csv");

        List<String> vehicleAreaSeat = getDivs("https://developer.android.com/reference/android/car/VehicleAreaSeat#constants_1");
        HashMap<String, String> seatMap = appendValuesToMap(vehicleAreaSeat);
        writeToFile(seatMap, "VehicleAreaSeatList.csv");

        List<String> portLocationType = getDivs("https://developer.android.com/reference/android/car/PortLocationType#constants_1");
        HashMap<String, String> portLocationMap = appendValuesToMap(portLocationType);
        writeToFile(portLocationMap, "PortLocationTypeList.csv");

        List<String> fuelType = getDivs("https://developer.android.com/reference/android/car/FuelType#constants_1");
        HashMap<String, String> fuelTypeMap = appendValuesToMap(fuelType);
        writeToFile(fuelTypeMap, "FuelTypeList.csv");

        List<String> evConnectorType = getDivs("https://developer.android.com/reference/android/car/EvConnectorType#constants_1");
        HashMap<String, String> connectorTypeMap = appendValuesToMap(evConnectorType);
        writeToFile(connectorTypeMap, "EvConnectorTypeList.csv");

        List<String> carOccupantZoneMngr = getDivs("https://developer.android.com/reference/android/car/CarOccupantZoneManager#constants_1");
        HashMap<String, String> zoneManagerMap = appendValuesToMap(carOccupantZoneMngr);
        writeToFile(zoneManagerMap, "CarOccupantZoneManagerList.csv");
    }

    private static HashMap<String, String> appendValuesToMap(List<String> propDivList) {
        Pattern pattern = Pattern.compile("Constant Value:\\s*([0-9]+)");

        HashMap<String, String> propMap = new HashMap<>();

        for (String div : propDivList) {
            Document document = Jsoup.parse(div);
            Elements propName = document.select("div[data-version-added] h3.api-name");
            for (Element name : propName) {
                if (name.text().matches("^[A-Z0-9_]+$")) {
                    Element parentDiv = name.parent();

                    String divText = null;
                    if (parentDiv != null) {
                        divText = parentDiv.text();
                    }

                    Matcher matcher = null;
                    if (divText != null) {
                        matcher = pattern.matcher(divText);
                    }

                    String matchedVal = "";
                    if (matcher != null && matcher.find()) {
                        matchedVal = matcher.group(1);
                    }

                    propMap.put(name.text(), matchedVal);
                }
            }
        }
        return propMap;
    }

    private static void writeToFile(HashMap<String, String> propMap, String fileName) throws IOException {

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

    private static List<String> getDivs(String url) throws IOException {
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