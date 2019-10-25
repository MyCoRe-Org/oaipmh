package org.mycore.oai.pmh.dataprovider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadVerbException;
import org.mycore.oai.pmh.DateUtils;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Verb;

/**
 * Base implementation of an {@link OAIProvider}. 
 *
 * @author Matthias Eichner
 */
public abstract class OAIBaseProvider<V extends OAIVerbHandler<?>> implements OAIProvider {

    private static Logger LOGGER = LogManager.getLogger();

    protected OAIAdapter oaiAdapter;

    public OAIBaseProvider(OAIAdapter oaiAdapter) {
        this();
        setAdapter(oaiAdapter);
    }

    public OAIBaseProvider() {
    }

    @Override
    public OAIResponse handleRequest(OAIRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // set global granularity
            DateUtils.setGranularity(this.getAdapter().getIdentify().getGranularity());
            // get verb handler
            V verbHandler;
            try {
                Verb verb = Verb.valueOf(request.getVerb());
                verbHandler = getVerbHandler(verb);
            } catch (BadVerbException exc) {
                return getErrorResponse(exc, request);
            } catch (Exception exc) {
                return getErrorResponse(new BadVerbException(request.getVerb()), request);
            }
            // check arguments
            try {
                request.checkBadArgument(verbHandler.getArgumentMap(), this.getAdapter());
            } catch (BadArgumentException exc) {
                return getErrorResponse(exc, request);
            }
            // handle request
            try {
                return handle(request, verbHandler);
            } catch (OAIException oaiExc) {
                return getErrorResponse(oaiExc, request);
            }
        } finally {
            LOGGER.info("OAI-PMH request {} took {}ms to process.", request, System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public OAIAdapter getAdapter() {
        return this.oaiAdapter;
    }

    @Override
    public void setAdapter(OAIAdapter adapter) {
        this.oaiAdapter = adapter;
    }

    protected abstract V getVerbHandler(Verb verb) throws BadVerbException;

    protected abstract OAIResponse handle(OAIRequest request, V verbHandler) throws OAIException;

    protected abstract OAIResponse getErrorResponse(OAIException oaiExc, OAIRequest request);

}
