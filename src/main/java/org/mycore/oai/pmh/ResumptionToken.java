package org.mycore.oai.pmh;

/**
 * Represents an OAI resumption token.
 * <p>
 * A number of OAI-PMH requests return a list of discrete entities: ListRecords returns a list of records,
 * ListIdentifiers returns a list of headers, and ListSets returns a list of sets. Collectively these requests are
 * called list requests. In some cases, these lists may be large and it may be practical to partition them among a
 * series of requests and responses. This partitioning is accomplished as follows:
 * </p>
 * <ol>
 * <li>A repository replies to a request with an incomplete list and a resumptionToken</li>
 * <li>In order to make the response a complete list, the harvester will need to issue one or more requests with
 * resumptionTokens as arguments. The complete list then consists of the concatenation of the incomplete lists from the
 * sequence of requests, known as a list request sequence.</li>
 * </ol>
 * 
 * @author Matthias Eichner
 */
public interface ResumptionToken {

    /**
     * Returns the token as string.
     * 
     * @return token as string
     */
    public String getToken();

}
