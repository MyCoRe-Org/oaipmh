package org.mycore.oai.pmh;

import org.jdom.Namespace;

public abstract class OAIConstants {

    public final static Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    public static final Namespace NS_OAI = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/");

    public final static Namespace NS_OAI_DC = Namespace.getNamespace("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");

    public final static Namespace NS_DC = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");

    public static final String SCHEMA_OAI = "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";

    public final static String SCHEMA_DC = "http://www.openarchives.org/OAI/2.0/oai_dc.xsd";

    public static final String SCHEMA_LOC_OAI = NS_OAI.getURI() + " " + SCHEMA_OAI;

    public final static String SCHEMA_LOC_OAI_DC = NS_OAI_DC.getURI() + " " + SCHEMA_DC;

    public static final String XML_OAI_ROOT = "OAI-PMH";

}
