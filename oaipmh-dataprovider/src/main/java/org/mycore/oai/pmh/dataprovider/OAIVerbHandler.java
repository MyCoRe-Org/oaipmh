package org.mycore.oai.pmh.dataprovider;

import java.util.Map;

import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;

/**
 * Base interface to process a OAI-PMH request for a specific verb.
 *
 * @param <R> the result of the handler
 * @author Matthias Eichner
 */
public interface OAIVerbHandler<R> {

    /**
     * Handles the given request and returns a result object.
     *
     * @param request the oai request to handle
     * @return the response
     * @throws OAIException something went wrong while handling the request
     */
    R handle(OAIRequest request) throws OAIException;

    /**
     * Returns a map of arguments associated with a type of "required", "optional" or "exclusive". Those arguments
     * are used to check if a request is valid.
     *
     * @return map of arguments associated with a type
     */
    Map<Argument, ArgumentType> getArgumentMap();

}
