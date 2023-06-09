package org.jumpmind.symmetric.web;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jumpmind.symmetric.common.InfoConstants;
import org.jumpmind.symmetric.common.ParameterConstants;
import org.jumpmind.symmetric.model.Node;
import org.jumpmind.symmetric.model.NodeGroup;
import org.jumpmind.symmetric.service.IConfigurationService;
import org.jumpmind.symmetric.service.INodeService;
import org.jumpmind.symmetric.service.IParameterService;

/**
 * Responsible for providing high level information about the node in property
 * format
 */
public class InfoUriHandler extends AbstractUriHandler {

    private static final long serialVersionUID = 1L;

    private INodeService nodeService;

    private IConfigurationService configurationService;

    public InfoUriHandler(IParameterService parameterService, INodeService nodeService, IConfigurationService configurationService) {
        super("/info/*", parameterService);
        this.nodeService = nodeService;
        this.configurationService = configurationService;
    }

    public void handle(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/plain");
        Node node = nodeService.findIdentity();
        List<NodeGroup> nodeGroups = configurationService.getNodeGroups();
        Properties properties = new Properties();
        properties.setProperty(ParameterConstants.EXTERNAL_ID, parameterService.getExternalId());
        properties.setProperty(ParameterConstants.NODE_GROUP_ID, parameterService.getNodeGroupId());
        properties.setProperty(ParameterConstants.EXTERNAL_ID, parameterService.getExternalId());
        if (nodeGroups != null) {
            StringBuilder b = new StringBuilder();
            for (NodeGroup nodeGroup : nodeGroups) {
                b.append(nodeGroup.getNodeGroupId());
                b.append(",");
            }
            properties.setProperty(InfoConstants.NODE_GROUP_IDS, b.substring(0, b.length() - 1));
        }
        if (node != null) {
            properties.setProperty(InfoConstants.NODE_ID, node.getNodeId());
            properties.setProperty(InfoConstants.DATABASE_TYPE, node.getDatabaseType());
            properties.setProperty(InfoConstants.DATABASE_VERSION, node.getDatabaseVersion());
            properties.setProperty(InfoConstants.DEPLOYMENT_TYPE, node.getDeploymentType());
            properties.setProperty(InfoConstants.TIMEZONE_OFFSET, node.getTimezoneOffset());
            properties.setProperty(InfoConstants.SYMMETRIC_VERSION, node.getSymmetricVersion());
        }
        properties.store(res.getOutputStream(), "SymmetricDS");
        res.flushBuffer();
    }
}
