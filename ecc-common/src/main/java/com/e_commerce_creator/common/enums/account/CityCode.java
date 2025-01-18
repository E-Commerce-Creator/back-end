package com.e_commerce_creator.common.enums.account;

import java.util.Arrays;

public enum CityCode {
    CAIRO("01", "Cairo"),
    ALEXANDRIA("02", "Alexandria"),
    PORT_SAID("03", "Port Said"),
    SUEZ("04", "Suez"),
    DAMIETTA("11", "Damietta"),
    DOKKI("12", "Dokki"),
    SHARKIA("13", "Sharkia"),
    QALYUBIA("14", "Qalyubia"),
    KAFR_EL_SHEIKH("15", "Kafr El Sheikh"),
    GHARBIYA("16", "Gharbiya"),
    MONOFIA("17", "Monofia"),
    BEHIRA("18", "Behira"),
    ISMAILIA("19", "Ismailia"),
    GIZA("21", "Giza"),
    BENI_SUEF("22", "Beni Suef"),
    FAYOUM("23", "Fayoum"),
    MINYA("24", "Minya"),
    ASSIUT("25", "Assiut"),
    SOHAG("26", "Sohag"),
    QENA("27", "Qena"),
    ASWAN("28", "Aswan"),
    LUXOR("29", "Luxor"),
    RED_SEA("31", "Red Sea"),
    NEW_VALLEY("32", "New Valley"),
    MATROUH("33", "Matrouh"),
    NORTH_SINAI("34", "North Sinai"),
    SOUTH_SINAI("35", "South Sinai"),
    OUTSIDE_EGYPT("88", "Outside Egypt");

    private final String code;
    private final String name;

    CityCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CityCode fromCode(String inputCode) {
        return Arrays.stream(CityCode.values())
                .filter(city -> city.code.equals(inputCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid City code: " + inputCode));
    }

    public static String getCityNameByCode(String inputCode) {
        return fromCode(inputCode).getName();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
