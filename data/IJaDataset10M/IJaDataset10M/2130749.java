package net.webpasswordsafe.server.webservice;

import net.webpasswordsafe.client.remote.UserService;
import net.webpasswordsafe.common.model.User;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * AddUser web service
 * 
 * @author Josh Drummond
 *
 */
@Endpoint
public class AddUserJDomEndpoint extends BaseJDomEndpoint {

    private static Logger LOG = Logger.getLogger(AddUserJDomEndpoint.class);

    @Autowired
    private UserService userService;

    private XPath usernameXPath, passwordXPath, fullnameXPath, emailXPath, activeXPath;

    public AddUserJDomEndpoint() throws JDOMException {
        setXPath();
    }

    private void setXPath() throws JDOMException {
        setBaseXPath("AddUserRequest");
        usernameXPath = XPath.newInstance("/wps:AddUserRequest/wps:user/wps:username");
        usernameXPath.addNamespace(namespace);
        passwordXPath = XPath.newInstance("/wps:AddUserRequest/wps:user/wps:password");
        passwordXPath.addNamespace(namespace);
        fullnameXPath = XPath.newInstance("/wps:AddUserRequest/wps:user/wps:fullname");
        fullnameXPath.addNamespace(namespace);
        emailXPath = XPath.newInstance("/wps:AddUserRequest/wps:user/wps:email");
        emailXPath.addNamespace(namespace);
        activeXPath = XPath.newInstance("/wps:AddUserRequest/wps:user/wps:active");
        activeXPath.addNamespace(namespace);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddUserRequest")
    @ResponsePayload
    public Element handleGetCurrentPasswordRequest(@RequestPayload Element requestDoc) throws Exception {
        Element returnDoc = null;
        try {
            boolean isSuccess = false;
            String message = "";
            try {
                String authnUsername = extractAuthnUsernameFromRequest(requestDoc);
                String authnPassword = extractAuthnPasswordFromRequest(requestDoc);
                User user = extractUserFromRequest(requestDoc);
                setIPAddress();
                boolean isAuthnValid = loginService.login(authnUsername, authnPassword);
                if (isAuthnValid) {
                    boolean isUserTaken = userService.isUserTaken(user.getUsername());
                    if (!isUserTaken) {
                        userService.addUser(user);
                        isSuccess = true;
                    } else {
                        message = "Username already exists";
                    }
                } else {
                    message = "Invalid authentication";
                }
                loginService.logout();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                isSuccess = false;
                message = e.getMessage();
            }
            returnDoc = createResponse(isSuccess, message);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
        return returnDoc;
    }

    private Element createResponse(boolean isSuccess, String message) {
        return createBaseResponse("AddUserResponse", isSuccess, message);
    }

    private User extractUserFromRequest(Element element) throws JDOMException {
        User user = new User();
        user.setUsername(usernameXPath.valueOf(element));
        user.updateAuthnPasswordValue(passwordXPath.valueOf(element));
        user.setFullname(fullnameXPath.valueOf(element));
        user.setEmail(emailXPath.valueOf(element));
        String activeFlag = activeXPath.valueOf(element).trim().toLowerCase();
        user.setActiveFlag(activeFlag.equals("true") || activeFlag.equals("yes") || activeFlag.equals("y"));
        return user;
    }
}
