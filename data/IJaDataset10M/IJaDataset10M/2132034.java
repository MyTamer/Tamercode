package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_nn_NO extends ListResourceBundle {

    static final String decimalSeparator = ",";

    static final String groupingSeparator = " ";

    static final String numberFormat = "#,##0.###";

    static final String percentFormat = "#,##0%";

    static final String[] weekdays = { null, "sundag ", "måndag ", "tysdag ", "onsdag ", "torsdag ", "fredag ", "laurdag " };

    static final String[] shortWeekdays = { null, "su ", "må ", "ty ", "on ", "to ", "fr ", "lau " };

    static final String[] shortMonths = { "jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", null };

    static final String[] months = { "januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", null };

    static final String[] ampms = { "", "" };

    static final String shortDateFormat = "dd. MMM yyyy";

    static final String defaultTimeFormat = "";

    static final String currencySymbol = "kr";

    static final String intlCurrencySymbol = "NOK";

    static final String currencyFormat = "$ #,##0.00;-$#,##0.00";

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
