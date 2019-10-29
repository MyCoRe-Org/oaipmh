package org.mycore.oai.pmh.dataprovider;

/**
 * A <code>OAIProvider</code> handles oai requests to generate valid oai-pmh response.
 * 
 * @author Matthias Eichner
 */
public interface OAIProvider {

    /**
     * Handles the request and returns an OAI-PMH response.
     *
     * @param request the request to handle
     * @return the response
     */
    OAIResponse handleRequest(OAIRequest request);

    /**
     * Returns the adapter associated with this provider.
     *
     * @return the adapter
     */
    OAIAdapter getAdapter();

    /**
     * Returns the adapter associated with this provider.
     * @throws IllegalStateException if adapter is already set
     */
    void setAdapter(OAIAdapter adapter) throws IllegalStateException;

}
