package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_it_IT extends ListResourceBundle {

    static final String decimalSeparator = ",";

    static final String groupingSeparator = "";

    static final String numberFormat = "#.###";

    static final String percentFormat = "#%";

    static final String[] weekdays = { null, "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato" };

    static final String[] shortWeekdays = { null, "dom", "lun", "mar", "mer", "gio", "ven", "sab" };

    static final String[] shortMonths = { "gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", null };

    static final String[] months = { "gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", null };

    static final String[] ampms = { "", "" };

    static final String shortDateFormat = "dd/MM/yyyy";

    static final String defaultTimeFormat = "";

    static final String currencySymbol = "L.";

    static final String intlCurrencySymbol = "ITL";

    static final String currencyFormat = "$ #,###,##0.;-$ #,###,##0.";

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
