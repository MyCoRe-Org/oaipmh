/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mycore.oai.pmh.dataprovider.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIConstants;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIImplementationException;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;
import org.mycore.oai.pmh.jaxb.ListMetadataFormatsType;
import org.mycore.oai.pmh.jaxb.MetadataFormatType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

public class ListMetadataFormatsHandler extends JAXBVerbHandler {

    protected final static Logger LOGGER = LogManager.getLogger(ListMetadataFormatsHandler.class);

    private static Map<Argument, ArgumentType> ARGUMENT_MAP;

    static {
        ARGUMENT_MAP = new HashMap<>();
        ARGUMENT_MAP.put(Argument.identifier, ArgumentType.optional);
    }

    @Override
    public Map<Argument, ArgumentType> getArgumentMap() {
        return ARGUMENT_MAP;
    }

    public ListMetadataFormatsHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) throws OAIException {
        String id = request.getIdentifier();
        List<? extends MetadataFormat> formatList;
        // get format list
        if (id == null) {
            // get all metadata formats
            formatList = this.oaiAdapter.getMetadataFormats();
            if (formatList == null || !checkOAIDublinCore(formatList)) {
                throw new OAIImplementationException(
                    "OAI adapter has to support oai_dc (http://www.openarchives.org/OAI/openarchivesprotocol.html#MetadataNamespaces). You can use DCMetadataFormat class.");
            }
        } else {
            // get metadata formats of single object
            formatList = this.oaiAdapter.getMetadataFormats(id);
        }
        // to jaxb element
        ListMetadataFormatsType listMetadataFormatsType = new ListMetadataFormatsType();
        for (MetadataFormat format : formatList) {
            MetadataFormatType mft = new MetadataFormatType();
            mft.setMetadataPrefix(format.getPrefix());
            mft.setSchema(format.getSchema());
            mft.setMetadataNamespace(format.getNamespace());
            listMetadataFormatsType.getMetadataFormat().add(mft);
        }
        // return oaipmh
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setListMetadataFormats(listMetadataFormatsType);
        return oaipmh;
    }

    /**
     * Checks if the list contains the OAI_DC metadata format. This is required by specification see <a
     * href="http://www.openarchives.org/OAI/openarchivesprotocol.html#MetadataNamespaces">here</a>
     * 
     * @param formatList
     *            list of metadata formats
     * @return true if list contains dublic core format
     */
    private boolean checkOAIDublinCore(List<? extends MetadataFormat> formatList) {
        for (MetadataFormat f : formatList) {
            if (f.getNamespace().equals(OAIConstants.NS_OAI_DC.getURI()))
                return true;
        }
        return false;
    }

}
