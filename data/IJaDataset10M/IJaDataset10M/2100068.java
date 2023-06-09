package net.sf.istcontract.wsimport.api.pipe;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import net.sf.istcontract.wsimport.api.EndpointAddress;
import net.sf.istcontract.wsimport.api.WSBinding;
import net.sf.istcontract.wsimport.api.WSService;
import net.sf.istcontract.wsimport.api.model.SEIModel;
import net.sf.istcontract.wsimport.api.model.wsdl.WSDLPort;
import net.sf.istcontract.wsimport.api.server.WSEndpoint;

/**
 * Creates a pipeline.
 *
 * <p>
 * This pluggability layer enables the upper layer to
 * control exactly how the pipeline is composed.
 *
 * <p>
 * JAX-WS is going to have its own default implementation
 * when used all by itself, but it can be substituted by
 * other implementations.
 *
 * <p>
 * See {@link PipelineAssemblerFactory} for how {@link PipelineAssembler}s
 * are located.
 *
 * <p>
 * TODO: the JAX-WS team felt that no {@link Pipe} should be relying
 * on the {@link SEIModel}, so it is no longer given to the assembler.
 * Talk to us if you need it.
 *
 * @see ClientPipeAssemblerContext
 *
 * @author Kohsuke Kawaguchi
 */
public interface PipelineAssembler {

    /**
     * Creates a new pipeline for clients.
     *
     * <p>
     * When a JAX-WS client creates a proxy or a {@link Dispatch} from
     * a {@link Service}, JAX-WS runtime internally uses this method
     * to create a new pipeline as a part of the initilization.
     *
     * @param context
     *      Object that captures various contextual information
     *      that can be used to determine the pipeline to be assembled.
     *
     * @return
     *      non-null freshly created pipeline.
     *
     * @throws WebServiceException
     *      if there's any configuration error that prevents the
     *      pipeline from being constructed. This exception will be
     *      propagated into the application, so it must have
     *      a descriptive error.
     */
    @NotNull
    Pipe createClient(@NotNull ClientPipeAssemblerContext context);

    /**
     * Creates a new pipeline for servers.
     *
     * <p>
     * When a JAX-WS server deploys a new endpoint, it internally
     * uses this method to create a new pipeline as a part of the
     * initialization.
     *
     * <p>
     * Note that this method is called only once to set up a
     * 'master pipeline', and it gets {@link Pipe#copy(PipeCloner) copied}
     * from it.
     *
     * @param context
     *      Object that captures various contextual information
     *      that can be used to determine the pipeline to be assembled.
     *
     * @return
     *      non-null freshly created pipeline.
     *
     * @throws WebServiceException
     *      if there's any configuration error that prevents the
     *      pipeline from being constructed. This exception will be
     *      propagated into the container, so it must have
     *      a descriptive error.
     *
     */
    @NotNull
    Pipe createServer(@NotNull ServerPipeAssemblerContext context);
}
