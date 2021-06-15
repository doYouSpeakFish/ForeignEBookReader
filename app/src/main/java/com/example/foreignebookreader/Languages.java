package com.example.foreignebookreader;

import java.util.LinkedHashMap;

public class Languages {

    private final LinkedHashMap<String, String> LANGUAGES;

    private Languages() {
        LANGUAGES = new LinkedHashMap<>();
        LANGUAGES.put("Afrikaans", "af");
        LANGUAGES.put("Albanian", "sq");
        LANGUAGES.put("Amharic", "am");
        LANGUAGES.put("Arabic", "ar");
        LANGUAGES.put("Armenian", "hy");
        LANGUAGES.put("Azerbaijani", "az");
        LANGUAGES.put("Basque", "eu");
        LANGUAGES.put("Belarusian", "be");
        LANGUAGES.put("Bengali", "bn");
        LANGUAGES.put("Bosnian", "bs");
        LANGUAGES.put("Bulgarian", "bg");
        LANGUAGES.put("Catalan", "ca");
        LANGUAGES.put("Cebuano", "ceb");
        LANGUAGES.put("Chinese (Simplified)", "zh-CN");
        LANGUAGES.put("Chinese (Traditional)", "zh-TW");
        LANGUAGES.put("Corsican", "co");
        LANGUAGES.put("Croatian", "hr");
        LANGUAGES.put("Czech", "cs");
        LANGUAGES.put("Danish", "da");
        LANGUAGES.put("Dutch", "nl");
        LANGUAGES.put("English", "en");
        LANGUAGES.put("Esperanto", "eo");
        LANGUAGES.put("Estonian", "et");
        LANGUAGES.put("Finnish", "fi");
        LANGUAGES.put("French", "fr");
        LANGUAGES.put("Frisian", "fy");
        LANGUAGES.put("Galician", "gl");
        LANGUAGES.put("Georgian", "ka");
        LANGUAGES.put("German", "de");
        LANGUAGES.put("Greek", "el");
        LANGUAGES.put("Gujarati", "gu");
        LANGUAGES.put("Haitian Creole", "ht");
        LANGUAGES.put("Hausa", "ha");
        LANGUAGES.put("Hawaiian", "haw");
        LANGUAGES.put("Hebrew", "he");
        LANGUAGES.put("Hindi", "hi");
        LANGUAGES.put("Hmong", "hmn");
        LANGUAGES.put("Hungarian", "hu");
        LANGUAGES.put("Icelandic", "is");
        LANGUAGES.put("Igbo", "ig");
        LANGUAGES.put("Indonesian", "id");
        LANGUAGES.put("Irish", "ga");
        LANGUAGES.put("Italian", "it");
        LANGUAGES.put("Japanese", "ja");
        LANGUAGES.put("Javanese", "jv");
        LANGUAGES.put("Kannada", "kn");
        LANGUAGES.put("Kazakh", "kk");
        LANGUAGES.put("Khmer", "km");
        LANGUAGES.put("Kinyarwanda", "rw");
        LANGUAGES.put("Korean", "ko");
        LANGUAGES.put("Kurdish", "ku");
        LANGUAGES.put("Kyrgyz", "ky");
        LANGUAGES.put("Lao", "lo");
        LANGUAGES.put("Latvian", "lv");
        LANGUAGES.put("Lithuanian", "lt");
        LANGUAGES.put("Luxembourgish", "lb");
        LANGUAGES.put("Macedonian", "mk");
        LANGUAGES.put("Malagasy", "mg");
        LANGUAGES.put("Malay", "ms");
        LANGUAGES.put("Malayalam", "ml");
        LANGUAGES.put("Maltese", "mt");
        LANGUAGES.put("Maori", "mi");
        LANGUAGES.put("Marathi", "mr");
        LANGUAGES.put("Mongolian", "mn");
        LANGUAGES.put("Myanmar (Burmese)", "my");
        LANGUAGES.put("Nepali", "ne");
        LANGUAGES.put("Norwegian", "no");
        LANGUAGES.put("Nyanja (Chichewa)", "ny");
        LANGUAGES.put("Odia (Oriya)", "or");
        LANGUAGES.put("Pashto", "ps");
        LANGUAGES.put("Persian", "fa");
        LANGUAGES.put("Polish", "pl");
        LANGUAGES.put("Portuguese", "pt");
        LANGUAGES.put("Punjabi", "pa");
        LANGUAGES.put("Romanian", "ro");
        LANGUAGES.put("Russian", "ru");
        LANGUAGES.put("Samoan", "sm");
        LANGUAGES.put("Scots Gaelic", "gd");
        LANGUAGES.put("Serbian", "sr");
        LANGUAGES.put("Sesotho", "st");
        LANGUAGES.put("Shona", "sn");
        LANGUAGES.put("Sindhi", "sd");
        LANGUAGES.put("Sinhala (Sinhalese)", "si");
        LANGUAGES.put("Slovak", "sk");
        LANGUAGES.put("Slovenian", "sl");
        LANGUAGES.put("Somali", "so");
        LANGUAGES.put("Spanish", "es");
        LANGUAGES.put("Sundanese", "su");
        LANGUAGES.put("Swahili", "sw");
        LANGUAGES.put("Swedish", "sv");
        LANGUAGES.put("Tagalog (Filipino)", "tl");
        LANGUAGES.put("Tajik", "tg");
        LANGUAGES.put("Tamil", "ta");
        LANGUAGES.put("Tatar", "tt");
        LANGUAGES.put("Telugu", "te");
        LANGUAGES.put("Thai", "th");
        LANGUAGES.put("Turkish", "tr");
        LANGUAGES.put("Turkmen", "tk");
        LANGUAGES.put("Ukrainian", "uk");
        LANGUAGES.put("Urdu", "ur");
        LANGUAGES.put("Uyghur", "ug");
        LANGUAGES.put("Uzbek", "uz");
        LANGUAGES.put("Vietnamese", "vi");
        LANGUAGES.put("Welsh", "cy");
        LANGUAGES.put("Xhosa", "xh");
        LANGUAGES.put("Yiddish", "yi");
        LANGUAGES.put("Yoruba", "yo");
        LANGUAGES.put("Zulu", "zu");
    }

    public static LinkedHashMap<String, String> getLanguages() {
        return (new Languages()).LANGUAGES;
    }

}
