package org.mipt.service;


import java.util.Arrays;
import java.util.List;

public final class Parser {
    private Parser() {
    }

    public static String parseJson(String data) {
        String[] modelParsed = data.split("(\":\\s\")|(\",\\s\")");
        StringBuilder sb = new StringBuilder("{");
        for (int pairNum = 0; pairNum < modelParsed.length - 2; pairNum += 2) {
            sb.append(modelParsed[pairNum]);
            sb.append(":");
            sb.append(modelParsed[pairNum + 1]);
            sb.append(",");
        }
        sb.append(modelParsed[modelParsed.length - 2]);
        sb.append(":");
        sb.append(modelParsed[modelParsed.length - 1]);
        sb.append("}");
        return sb.toString();
    }

    public static List<Double> stringToPair(String str) {
        String[] strParsed = str.substring(2, str.length() - 2).split(",");
        return Arrays.asList(Double.parseDouble(strParsed[0]), Double.parseDouble(strParsed[1]));
    }
}
