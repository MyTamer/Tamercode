package net.sourceforge.eclipsetrader.core.ui.wizards;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.sourceforge.eclipsetrader.core.CorePlugin;
import net.sourceforge.eclipsetrader.core.db.Security;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 */
public class QuoteFeedPage extends WizardPage {

    private Combo feed;

    private Text symbol;

    private Combo exchange;

    private Combo level2Feed;

    private Text level2Symbol;

    private Combo level2Exchange;

    private Security security;

    public QuoteFeedPage() {
        this(null);
    }

    public QuoteFeedPage(Security security) {
        super("");
        setTitle("Quote Feed");
        setDescription("Set the security feed for quotes");
        setPageComplete(true);
        this.security = security;
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        setControl(composite);
        Label label = new Label(composite, SWT.NONE);
        label.setText("Feed");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        feed = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        feed.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        feed.setVisibleItemCount(10);
        feed.add("");
        label = new Label(composite, SWT.NONE);
        label.setText("Exchange");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        exchange = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        exchange.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        exchange.setVisibleItemCount(10);
        exchange.add("");
        label = new Label(composite, SWT.NONE);
        label.setText("Symbol");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        symbol = new Text(composite, SWT.BORDER);
        symbol.setLayoutData(new GridData(100, SWT.DEFAULT));
        if (security != null && security.getQuoteFeed() != null) symbol.setText(security.getQuoteFeed().getSymbol());
        label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        label = new Label(composite, SWT.NONE);
        label.setText("Level II Feed");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        level2Feed = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        level2Feed.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        level2Feed.setVisibleItemCount(10);
        level2Feed.add("");
        label = new Label(composite, SWT.NONE);
        label.setText("Exchange");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        level2Exchange = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        level2Exchange.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        level2Exchange.setVisibleItemCount(10);
        level2Exchange.add("");
        label = new Label(composite, SWT.NONE);
        label.setText("Symbol");
        label.setLayoutData(new GridData(125, SWT.DEFAULT));
        level2Symbol = new Text(composite, SWT.BORDER);
        level2Symbol.setLayoutData(new GridData(100, SWT.DEFAULT));
        if (security != null && security.getLevel2Feed() != null) level2Symbol.setText(security.getLevel2Feed().getSymbol());
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(CorePlugin.FEED_EXTENSION_POINT);
        if (extensionPoint != null) {
            IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
            java.util.List plugins = Arrays.asList(elements);
            Collections.sort(plugins, new Comparator() {

                public int compare(Object arg0, Object arg1) {
                    if ((arg0 instanceof IConfigurationElement) && (arg1 instanceof IConfigurationElement)) {
                        String s0 = ((IConfigurationElement) arg0).getAttribute("name");
                        String s1 = ((IConfigurationElement) arg1).getAttribute("name");
                        return s0.compareTo(s1);
                    }
                    return 0;
                }
            });
            for (Iterator iter = plugins.iterator(); iter.hasNext(); ) {
                IConfigurationElement element = (IConfigurationElement) iter.next();
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                IConfigurationElement[] children = element.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].getName().equals("quote")) {
                        feed.setData(String.valueOf(feed.getItemCount()), id);
                        feed.add(name);
                        if (security != null && security.getQuoteFeed() != null) {
                            if (id.equals(security.getQuoteFeed().getId())) feed.select(feed.getItemCount() - 1);
                        }
                    } else if (children[i].getName().equals("level2")) {
                        level2Feed.setData(String.valueOf(level2Feed.getItemCount()), id);
                        level2Feed.add(name);
                        if (security != null && security.getLevel2Feed() != null) {
                            if (id.equals(security.getLevel2Feed().getId())) level2Feed.select(level2Feed.getItemCount() - 1);
                        }
                    }
                }
            }
            SecurityWizard.updateFeedExchanges("quote", exchange, security != null ? security.getQuoteFeed() : null);
            SecurityWizard.updateFeedExchanges("level2", level2Exchange, security != null ? security.getLevel2Feed() : null);
        }
        feed.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String id = (String) feed.getData(String.valueOf(feed.getSelectionIndex()));
                SecurityWizard.updateFeedExchanges("quote", exchange, id);
            }
        });
        level2Feed.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String id = (String) level2Feed.getData(String.valueOf(level2Feed.getSelectionIndex()));
                SecurityWizard.updateFeedExchanges("level2", level2Exchange, id);
            }
        });
    }

    public String getId() {
        return (String) feed.getData(String.valueOf(feed.getSelectionIndex()));
    }

    public String getSymbol() {
        return symbol.getText();
    }

    public String getLevel2Id() {
        return (String) level2Feed.getData(String.valueOf(level2Feed.getSelectionIndex()));
    }

    public String getLevel2Symbol() {
        return level2Symbol.getText();
    }

    public String getExchange() {
        return (String) exchange.getData(String.valueOf(exchange.getSelectionIndex()));
    }

    public String getLevel2Exchange() {
        return (String) level2Exchange.getData(String.valueOf(level2Exchange.getSelectionIndex()));
    }
}
