package fr.cnes.sitools.login;

import org.restlet.Request;
import org.restlet.data.ChallengeRequest;
import org.restlet.data.Method;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import fr.cnes.sitools.common.SitoolsResource;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.util.RIAPUtils;

/**
 * Resource just for return a standard response when authentication succeed This resource can be attached in each
 * application to return user roles...
 * 
 * @author AKKA Technologies
 * 
 */
public final class LoginResource extends SitoolsResource {

    @Override
    public void sitoolsDescribe() {
        setName("LoginResource");
        setDescription("Resource for authentication");
    }

    /**
   * Login tentative
   * 
   * @param variant
   *          client preference for response media type
   * @return Representation if success
   */
    @Get
    public Representation login(Variant variant) {
        Response response = null;
        if (this.getRequest().getClientInfo().isAuthenticated()) {
            response = new Response(true, "User authenticated.");
            return getRepresentation(response, variant.getMediaType());
        } else {
            response = new Response(false, "User not authenticated.");
        }
        String reference = this.getReference().toString();
        response.setUrl(reference);
        if (this.getReference().getBaseRef().toString().endsWith("login")) {
            Request request = new Request(Method.GET, this.getReference().getBaseRef().toString() + "-mandatory");
            request.setChallengeResponse(this.getRequest().getChallengeResponse());
            org.restlet.Response responseMandatory = null;
            try {
                responseMandatory = this.getContext().getClientDispatcher().handle(request);
                if ((responseMandatory.getChallengeRequests() != null) && (responseMandatory.getChallengeRequests().size() > 0)) {
                    ChallengeRequest challengeRequest = responseMandatory.getChallengeRequests().get(0);
                    SitoolsAuthenticationInfo sai = new SitoolsAuthenticationInfo();
                    sai.setALGORITHM(challengeRequest.getDigestAlgorithm());
                    sai.setNONCE(challengeRequest.getServerNonce());
                    sai.setREALM(challengeRequest.getRealm());
                    sai.setSCHEME(challengeRequest.getScheme().toString());
                    response.setItem(sai);
                    response.setItemClass(SitoolsAuthenticationInfo.class);
                }
            } finally {
                RIAPUtils.exhaust(responseMandatory);
            }
        }
        return getRepresentation(response, variant.getMediaType());
    }

    /**
   * Describe the GET method
   * 
   * @param info
   *          WADL method info
   */
    public void describeGet(MethodInfo info) {
        info.setDocumentation("Method to reach the Login process.");
        this.addStandardGetRequestInfo(info);
        this.addStandardResponseInfo(info);
    }
}
