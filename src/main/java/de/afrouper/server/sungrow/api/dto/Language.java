package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

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
    VIETNAMESE
}
