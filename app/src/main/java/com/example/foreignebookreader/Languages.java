package com.example.foreignebookreader;

import java.util.LinkedHashMap;
import java.util.Locale;

public class Languages {

    private static final LinkedHashMap<String, String> CODE_FROM_LANGUAGE = new LinkedHashMap<>();;
    private static final LinkedHashMap<String, String> LANGUAGE_FROM_CODE = new LinkedHashMap<>();;
    static {
        CODE_FROM_LANGUAGE.put("Afrikaans", "af");
        CODE_FROM_LANGUAGE.put("Albanian", "sq");
        CODE_FROM_LANGUAGE.put("Amharic", "am");
        CODE_FROM_LANGUAGE.put("Arabic", "ar");
        CODE_FROM_LANGUAGE.put("Armenian", "hy");
        CODE_FROM_LANGUAGE.put("Azerbaijani", "az");
        CODE_FROM_LANGUAGE.put("Basque", "eu");
        CODE_FROM_LANGUAGE.put("Belarusian", "be");
        CODE_FROM_LANGUAGE.put("Bengali", "bn");
        CODE_FROM_LANGUAGE.put("Bosnian", "bs");
        CODE_FROM_LANGUAGE.put("Bulgarian", "bg");
        CODE_FROM_LANGUAGE.put("Catalan", "ca");
        CODE_FROM_LANGUAGE.put("Cebuano", "ceb");
        CODE_FROM_LANGUAGE.put("Chinese (Simplified)", "zh-CN");
        CODE_FROM_LANGUAGE.put("Chinese (Traditional)", "zh-TW");
        CODE_FROM_LANGUAGE.put("Corsican", "co");
        CODE_FROM_LANGUAGE.put("Croatian", "hr");
        CODE_FROM_LANGUAGE.put("Czech", "cs");
        CODE_FROM_LANGUAGE.put("Danish", "da");
        CODE_FROM_LANGUAGE.put("Dutch", "nl");
        CODE_FROM_LANGUAGE.put("English", "en");
        CODE_FROM_LANGUAGE.put("Esperanto", "eo");
        CODE_FROM_LANGUAGE.put("Estonian", "et");
        CODE_FROM_LANGUAGE.put("Finnish", "fi");
        CODE_FROM_LANGUAGE.put("French", "fr");
        CODE_FROM_LANGUAGE.put("Frisian", "fy");
        CODE_FROM_LANGUAGE.put("Galician", "gl");
        CODE_FROM_LANGUAGE.put("Georgian", "ka");
        CODE_FROM_LANGUAGE.put("German", "de");
        CODE_FROM_LANGUAGE.put("Greek", "el");
        CODE_FROM_LANGUAGE.put("Gujarati", "gu");
        CODE_FROM_LANGUAGE.put("Haitian Creole", "ht");
        CODE_FROM_LANGUAGE.put("Hausa", "ha");
        CODE_FROM_LANGUAGE.put("Hawaiian", "haw");
        CODE_FROM_LANGUAGE.put("Hebrew", "he");
        CODE_FROM_LANGUAGE.put("Hindi", "hi");
        CODE_FROM_LANGUAGE.put("Hmong", "hmn");
        CODE_FROM_LANGUAGE.put("Hungarian", "hu");
        CODE_FROM_LANGUAGE.put("Icelandic", "is");
        CODE_FROM_LANGUAGE.put("Igbo", "ig");
        CODE_FROM_LANGUAGE.put("Indonesian", "id");
        CODE_FROM_LANGUAGE.put("Irish", "ga");
        CODE_FROM_LANGUAGE.put("Italian", "it");
        CODE_FROM_LANGUAGE.put("Japanese", "ja");
        CODE_FROM_LANGUAGE.put("Javanese", "jv");
        CODE_FROM_LANGUAGE.put("Kannada", "kn");
        CODE_FROM_LANGUAGE.put("Kazakh", "kk");
        CODE_FROM_LANGUAGE.put("Khmer", "km");
        CODE_FROM_LANGUAGE.put("Kinyarwanda", "rw");
        CODE_FROM_LANGUAGE.put("Korean", "ko");
        CODE_FROM_LANGUAGE.put("Kurdish", "ku");
        CODE_FROM_LANGUAGE.put("Kyrgyz", "ky");
        CODE_FROM_LANGUAGE.put("Lao", "lo");
        CODE_FROM_LANGUAGE.put("Latvian", "lv");
        CODE_FROM_LANGUAGE.put("Lithuanian", "lt");
        CODE_FROM_LANGUAGE.put("Luxembourgish", "lb");
        CODE_FROM_LANGUAGE.put("Macedonian", "mk");
        CODE_FROM_LANGUAGE.put("Malagasy", "mg");
        CODE_FROM_LANGUAGE.put("Malay", "ms");
        CODE_FROM_LANGUAGE.put("Malayalam", "ml");
        CODE_FROM_LANGUAGE.put("Maltese", "mt");
        CODE_FROM_LANGUAGE.put("Maori", "mi");
        CODE_FROM_LANGUAGE.put("Marathi", "mr");
        CODE_FROM_LANGUAGE.put("Mongolian", "mn");
        CODE_FROM_LANGUAGE.put("Myanmar (Burmese)", "my");
        CODE_FROM_LANGUAGE.put("Nepali", "ne");
        CODE_FROM_LANGUAGE.put("Norwegian", "no");
        CODE_FROM_LANGUAGE.put("Nyanja (Chichewa)", "ny");
        CODE_FROM_LANGUAGE.put("Odia (Oriya)", "or");
        CODE_FROM_LANGUAGE.put("Pashto", "ps");
        CODE_FROM_LANGUAGE.put("Persian", "fa");
        CODE_FROM_LANGUAGE.put("Polish", "pl");
        CODE_FROM_LANGUAGE.put("Portuguese", "pt");
        CODE_FROM_LANGUAGE.put("Punjabi", "pa");
        CODE_FROM_LANGUAGE.put("Romanian", "ro");
        CODE_FROM_LANGUAGE.put("Russian", "ru");
        CODE_FROM_LANGUAGE.put("Samoan", "sm");
        CODE_FROM_LANGUAGE.put("Scots Gaelic", "gd");
        CODE_FROM_LANGUAGE.put("Serbian", "sr");
        CODE_FROM_LANGUAGE.put("Sesotho", "st");
        CODE_FROM_LANGUAGE.put("Shona", "sn");
        CODE_FROM_LANGUAGE.put("Sindhi", "sd");
        CODE_FROM_LANGUAGE.put("Sinhala (Sinhalese)", "si");
        CODE_FROM_LANGUAGE.put("Slovak", "sk");
        CODE_FROM_LANGUAGE.put("Slovenian", "sl");
        CODE_FROM_LANGUAGE.put("Somali", "so");
        CODE_FROM_LANGUAGE.put("Spanish", "es");
        CODE_FROM_LANGUAGE.put("Sundanese", "su");
        CODE_FROM_LANGUAGE.put("Swahili", "sw");
        CODE_FROM_LANGUAGE.put("Swedish", "sv");
        CODE_FROM_LANGUAGE.put("Tagalog (Filipino)", "tl");
        CODE_FROM_LANGUAGE.put("Tajik", "tg");
        CODE_FROM_LANGUAGE.put("Tamil", "ta");
        CODE_FROM_LANGUAGE.put("Tatar", "tt");
        CODE_FROM_LANGUAGE.put("Telugu", "te");
        CODE_FROM_LANGUAGE.put("Thai", "th");
        CODE_FROM_LANGUAGE.put("Turkish", "tr");
        CODE_FROM_LANGUAGE.put("Turkmen", "tk");
        CODE_FROM_LANGUAGE.put("Ukrainian", "uk");
        CODE_FROM_LANGUAGE.put("Urdu", "ur");
        CODE_FROM_LANGUAGE.put("Uyghur", "ug");
        CODE_FROM_LANGUAGE.put("Uzbek", "uz");
        CODE_FROM_LANGUAGE.put("Vietnamese", "vi");
        CODE_FROM_LANGUAGE.put("Welsh", "cy");
        CODE_FROM_LANGUAGE.put("Xhosa", "xh");
        CODE_FROM_LANGUAGE.put("Yiddish", "yi");
        CODE_FROM_LANGUAGE.put("Yoruba", "yo");
        CODE_FROM_LANGUAGE.put("Zulu", "zu");
        // CODE_FROM_LANGUAGE.put("unknown", null);
        // TODO add unknown to this class instead of using constant in EntityBook class
        for (String language : CODE_FROM_LANGUAGE.keySet()) {
            LANGUAGE_FROM_CODE.put(CODE_FROM_LANGUAGE.get(language), language);
        }
    }

    private Languages() { }

    public static LinkedHashMap<String, String> getLanguages() {
        return CODE_FROM_LANGUAGE; // TODO replace this method with getCodeFromLanguage and getLanguages which returns a list of the language names
    }

    public static String getLanguageFromCode(String langCode) {
        return LANGUAGE_FROM_CODE.get(langCode);
    }

}
