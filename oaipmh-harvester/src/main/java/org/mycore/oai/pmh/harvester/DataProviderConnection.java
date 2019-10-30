package org.mycore.oai.pmh.harvester;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.Verb;

/**
 * Provides methods to read from OAI-PMH data providers via HTTP. This class should only be used by a {@link Harvester}.
 * 
 * @author Matthias Eichner
 */
public class DataProviderConnection {

    private static Logger LOGGER = LogManager.getLogger(DataProviderConnection.class);

    public enum Encoding {
        compress, gzip, identify, deflate
    }

    protected String baseURL;

    public DataProviderConnection(String baseURL) {
        this.baseURL = baseURL;
    }

    public InputStream identify() {
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.Identify.name());
        return doRequest(requestURL);
    }

    public InputStream listSets(String resumptionToken) {
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListSets.name());
        requestURL = addParameter(requestURL, Argument.resumptionToken, resumptionToken);
        return doRequest(requestURL);
    }

    public InputStream listMetadataFormats(String identifier) {
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListMetadataFormats.name());
        requestURL = addParameter(requestURL, Argument.identifier, identifier);
        return doRequest(requestURL);
    }

    public InputStream listIdentifiers(String metadataPrefix, String from, String until, String setSpec) {
        if (metadataPrefix == null) {
            throw new IllegalArgumentException("metadataPrefix is null");
        }
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListIdentifiers.name());
        requestURL = addParameter(requestURL, Argument.metadataPrefix, metadataPrefix);
        requestURL = addParameter(requestURL, Argument.from, from);
        requestURL = addParameter(requestURL, Argument.until, until);
        requestURL = addParameter(requestURL, Argument.set, setSpec);
        return doRequest(requestURL);
    }

    public InputStream listIdentifiers(String resumptionToken) {
        if (resumptionToken == null) {
            throw new IllegalArgumentException("resumptionToken is null");
        }
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListIdentifiers.name());
        requestURL = addParameter(requestURL, Argument.resumptionToken, resumptionToken);
        return doRequest(requestURL);
    }

    public InputStream listRecords(String metadataPrefix, String from, String until, String setSpec) {
        if (metadataPrefix == null) {
            throw new IllegalArgumentException("metadataPrefix is null");
        }
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListRecords.name());
        requestURL = addParameter(requestURL, Argument.metadataPrefix, metadataPrefix);
        requestURL = addParameter(requestURL, Argument.from, from);
        requestURL = addParameter(requestURL, Argument.until, until);
        requestURL = addParameter(requestURL, Argument.set, setSpec);
        return doRequest(requestURL);
    }

    public InputStream listRecords(String resumptionToken) {
        if (resumptionToken == null) {
            throw new IllegalArgumentException("resumptionToken is null");
        }
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.ListRecords.name());
        requestURL = addParameter(requestURL, Argument.resumptionToken, resumptionToken);
        return doRequest(requestURL);
    }

    public InputStream getRecord(String identifier, String metadataPrefix) {
        if (identifier == null || metadataPrefix == null) {
            throw new IllegalArgumentException("identifier or metadataPrefix is null");
        }
        String requestURL = baseURL;
        requestURL = addParameter(requestURL, Argument.verb, Verb.GetRecord.name());
        requestURL = addParameter(requestURL, Argument.identifier, identifier);
        requestURL = addParameter(requestURL, Argument.metadataPrefix, metadataPrefix);
        return doRequest(requestURL);
    }

    protected InputStream doRequest(String requestString) throws HarvestException {
        URL requestURL = buildRequestURL(requestString);
        // open connection
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) requestURL.openConnection();
            setRequestProperties(con);
            // get response code
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new HarvestException("HTTP connection error " + con.getResponseCode() + " "
                    + con.getResponseMessage() + " for " + requestURL.toString());
            }
            // get content
            return getContent(con);
        } catch (IOException ioExc) {
            throw new HarvestException("Unable to handle connection " + requestURL.toString(), ioExc);
        }
    }

    private URL buildRequestURL(String requestString) {
        try {
            LOGGER.info("Request {}", requestString);
            return new URL(requestString);
        } catch (Exception exc) {
            throw new HarvestException("Unable to request " + requestString, exc);
        }
    }

    private InputStream getContent(HttpURLConnection con) {
        String contentEncoding = con.getHeaderField("Content-Encoding");
        try {
            InputStream in = con.getInputStream();
            if (Encoding.compress.name().equals(contentEncoding)) {
                ZipInputStream zis = new ZipInputStream(in);
                zis.getNextEntry();
                return zis;
            } else if (Encoding.gzip.name().equals(contentEncoding)) {
                return new GZIPInputStream(in);
            } else if (Encoding.deflate.name().equals(contentEncoding)) {
                return new InflaterInputStream(in);
            } else {
                return in;
            }
        } catch (IOException ioExc) {
            throw new HarvestException("Unable to decode content for " + con.getURL().toString(), ioExc);
        }
    }

    private void setRequestProperties(HttpURLConnection con) {
        // set request properties
        con.setRequestProperty("User-Agent", "OAIHarvester/2.0");
        StringBuilder encodingBuf = new StringBuilder();
        for (int i = 0; i < Encoding.values().length; i++) {
            encodingBuf.append(Encoding.values()[i]);
            if (i < Encoding.values().length) {
                encodingBuf.append(", ");
            }
        }
        con.setRequestProperty("Accept-Encoding", encodingBuf.toString());
    }

    protected String addParameter(String url, Argument arg, String value) {
        if (value == null || value.length() <= 0) {
            return url;
        }
        int qpos = url.indexOf('?');
        int hpos = url.indexOf('#');
        char sep = qpos == -1 ? '?' : '&';
        String seg = sep + encodeUrl(arg.name()) + '=' + encodeUrl(value);
        return hpos == -1 ? url + seg : url.substring(0, hpos) + seg + url.substring(hpos);
    }

    /**
     * The same behaviour as Web.escapeUrl, only without the "funky encoding" of the characters ? and ;
     * (uses JDK URLEncoder directly).
     * 
     * @param url
     *            The string to encode.
     * @return <code>toencode</code> fully escaped using URL rules.
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalArgumentException(uee);
        }
    }
}
