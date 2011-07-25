package org.mycore.oai.pmh;

import org.jdom.Element;

/**
 * An extensible mechanism for communities to describe their repositories. For example, the description container could be used to include collection-level
 * metadata in the response to the Identify request. <a href="http://www.openarchives.org/OAI/2.0/guidelines.htm">Implementation Guidelines</a> are available to
 * give directions with this respect. Each description container must be accompanied by the URL of an XML schema describing the structure of the description
 * container.
 * 
 * @author Matthias Eichner
 */
public interface Description {

    /**
     * Returns the description as {@link Element}.
     * 
     * @return description as jdom
     */
    public Element toXML();

}
