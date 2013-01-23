package org.mycore.oai.pmh;

import org.jdom2.Namespace;

/**
 * Contains some usefull constants.
 * 
 * @author Matthias Eichner
 */
public abstract class OAIConstants {

    public final static Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    public static final Namespace NS_OAI = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/");

    public final static Namespace NS_OAI_ID = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/oai-identifier");
    
    public final static Namespace NS_OAI_FRIENDS = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/friends/");

    public final static Namespace NS_OAI_DC = Namespace.getNamespace("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");

    public final static Namespace NS_DC = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
   
    public static final String SCHEMA_OAI = "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";

    public static final String SCHEMA_OAI_ID = "http://www.openarchives.org/OAI/2.0/oai-identifier.xsd";
    
    public static final String SCHEMA_OAI_FRIENDS = "http://www.openarchives.org/OAI/2.0/friends.xsd";

    public final static String SCHEMA_DC = "http://www.openarchives.org/OAI/2.0/oai_dc.xsd";

    public static final String SCHEMA_LOC_OAI = NS_OAI.getURI() + " " + SCHEMA_OAI;
    
    public static final String SCHEMA_LOC_OAI_ID = NS_OAI_ID.getURI() + " " + SCHEMA_OAI_ID;
    
    public static final String SCHEMA_LOC_OAI_FRIENDS = NS_OAI_FRIENDS.getURI() + " " + SCHEMA_OAI_FRIENDS;

    public final static String SCHEMA_LOC_OAI_DC = NS_OAI_DC.getURI() + " " + SCHEMA_DC;

    public static final String XML_OAI_ROOT = "OAI-PMH";

}
