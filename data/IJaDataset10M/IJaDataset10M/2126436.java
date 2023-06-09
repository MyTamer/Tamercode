package br.com.sysmap.crux.gwt.client;

import br.com.sysmap.crux.core.client.declarative.DeclarativeFactory;
import br.com.sysmap.crux.core.client.declarative.TagAttribute;
import br.com.sysmap.crux.core.client.declarative.TagAttributes;
import br.com.sysmap.crux.core.client.declarative.TagEventDeclaration;
import br.com.sysmap.crux.core.client.declarative.TagEventsDeclaration;
import br.com.sysmap.crux.core.client.event.Event;
import br.com.sysmap.crux.core.client.event.Events;
import br.com.sysmap.crux.core.client.event.bind.EvtBind;
import br.com.sysmap.crux.core.client.screen.InterfaceConfigException;
import br.com.sysmap.crux.core.client.screen.factory.HasAllKeyHandlersFactory;
import br.com.sysmap.crux.core.client.screen.factory.HasAnimationFactory;
import br.com.sysmap.crux.core.client.screen.factory.HasSelectionHandlersFactory;
import br.com.sysmap.crux.core.client.screen.factory.HasTextFactory;
import br.com.sysmap.crux.core.client.screen.factory.HasValueChangeHandlersFactory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Factory for SuggestBox widget
 * @author Gesse S. F. Dafe
 */
@DeclarativeFactory(id = "suggestBox", library = "gwt")
public class SuggestBoxFactory extends CompositeFactory<SuggestBox> implements HasAnimationFactory<SuggestBox>, HasTextFactory<SuggestBox>, HasValueChangeHandlersFactory<SuggestBox>, HasSelectionHandlersFactory<SuggestBox>, HasAllKeyHandlersFactory<SuggestBox> {

    @Override
    @TagAttributes({ @TagAttribute(value = "accessKey", type = Character.class), @TagAttribute(value = "autoSelectEnabled", type = Boolean.class), @TagAttribute(value = "focus", type = Boolean.class), @TagAttribute(value = "limit", type = Integer.class), @TagAttribute("popupStyleName"), @TagAttribute(value = "tabIndex", type = Integer.class), @TagAttribute("value") })
    public void processAttributes(WidgetFactoryContext<SuggestBox> context) throws InterfaceConfigException {
        super.processAttributes(context);
    }

    @Override
    public SuggestBox instantiateWidget(Element element, String widgetId) throws InterfaceConfigException {
        Event eventLoadOracle = EvtBind.getWidgetEvent(element, "onLoadOracle");
        if (eventLoadOracle != null) {
            LoadOracleEvent<SuggestBox> loadOracleEvent = new LoadOracleEvent<SuggestBox>(widgetId);
            SuggestOracle oracle = (SuggestOracle) Events.callEvent(eventLoadOracle, loadOracleEvent);
            return new SuggestBox(oracle);
        }
        return new SuggestBox();
    }

    @Override
    @TagEventsDeclaration({ @TagEventDeclaration("onLoadOracle") })
    public void processEvents(WidgetFactoryContext<SuggestBox> context) throws InterfaceConfigException {
        super.processEvents(context);
    }
}
