package net.sourceforge.eclipsetrader.core.db.columns;

import java.text.SimpleDateFormat;
import net.sourceforge.eclipsetrader.core.CorePlugin;
import net.sourceforge.eclipsetrader.core.db.WatchlistItem;
import net.sourceforge.eclipsetrader.core.db.feed.Quote;
import net.sourceforge.eclipsetrader.core.db.internal.Messages;

public class Date extends Column {

    protected SimpleDateFormat formatter = CorePlugin.getDateFormat();

    public Date() {
        super(Messages.Date_Label, RIGHT);
    }

    public String getText(WatchlistItem item) {
        if (item.getSecurity() == null) return "";
        Quote quote = item.getSecurity().getQuote();
        if (quote != null && quote.getDate() != null) return formatter.format(quote.getDate());
        return "";
    }

    public int compare(Object arg0, Object arg1) {
        java.util.Date d0 = getValue((WatchlistItem) arg0);
        java.util.Date d1 = getValue((WatchlistItem) arg1);
        if (d0 == null || d1 == null) return 0;
        return d0.compareTo(d1);
    }

    private java.util.Date getValue(WatchlistItem item) {
        if (item.getSecurity() == null) return null;
        Quote quote = item.getSecurity().getQuote();
        if (quote != null && quote.getDate() != null) return quote.getDate();
        return null;
    }
}
