package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public enum Language {

    @SerializedName("_zh_CN")
    CHINESE_SIMPLE,
    @SerializedName("_zh_TW")
    CHINESE_TRADITIONAL,
    @SerializedName("_en_US")
    ENGLISH,
    @SerializedName("_ja_JP")
    JAPANESE,
    @SerializedName("_es_ES")
    SPANISH,
    @SerializedName("_de_DE")
    GERMAN,
    @SerializedName("_pt_BR")
    BRAZILIAN,
    @SerializedName("_pt_BR")
    PORTUGUESE,
    @SerializedName("_fr_FR")
    FRENCH,
    @SerializedName("_it_IT")
    ITALIAN,
    @SerializedName("_ko_KR")
    KOREAN,
    @SerializedName("_nl_NL")
    DUTCH,
    @SerializedName("_pl_PL")
    POLISH,
    @SerializedName("_vi_VN")
    VIETNAMESE;

    public static Language fromString(Locale locale) {
        switch (locale.getLanguage()) {
            case "zh":
                return CHINESE_SIMPLE;
            case "en":
                return ENGLISH;
            case "ja":
                return JAPANESE;
            case "es":
                return SPANISH;
            case "de":
                return GERMAN;
            case "pt":
                return PORTUGUESE;
            case "fr":
                return FRENCH;
            case "it":
                return ITALIAN;
            case "kr":
                return KOREAN;
            case "nl":
                return DUTCH;
            case "pl":
                return POLISH;
            case "vi":
                return VIETNAMESE;
            default:
                return ENGLISH;
        }
    }
}
