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

import java.util.Map;

import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;
import org.mycore.oai.pmh.dataprovider.OAIVerbHandler;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

/**
 * JAXB implementation of a verb handler. Uses a {@link OAIPMHtype} as result.
 */
public abstract class JAXBVerbHandler implements OAIVerbHandler<OAIPMHtype> {

    protected OAIAdapter oaiAdapter;

    public JAXBVerbHandler(OAIAdapter oaiAdapter) {
        this.oaiAdapter = oaiAdapter;
    }

    /**
     * Handles the request.
     *
     * @param request the request to handle
     */
    public abstract OAIPMHtype handle(OAIRequest request) throws OAIException;

    public abstract Map<Argument, ArgumentType> getArgumentMap();

}
