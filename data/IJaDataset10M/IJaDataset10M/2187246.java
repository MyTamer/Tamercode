package com.googlecode.psiprobe.controllers.connectors;

import com.googlecode.psiprobe.beans.ContainerListenerBean;
import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.Connector;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class GetConnectorController extends TomcatContainerController {

    private ContainerListenerBean containerListenerBean;

    public ContainerListenerBean getContainerListenerBean() {
        return containerListenerBean;
    }

    public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
        this.containerListenerBean = containerListenerBean;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);
        Connector connector = null;
        if (connectorName != null) {
            List connectors = containerListenerBean.getConnectors(false);
            for (int i = 0; i < connectors.size(); i++) {
                Connector p = (Connector) connectors.get(i);
                if (connectorName.equals(p.getName())) {
                    connector = p;
                    break;
                }
            }
        }
        return new ModelAndView(getViewName(), "connector", connector);
    }
}
