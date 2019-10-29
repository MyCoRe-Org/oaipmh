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
import java.util.Map;

import org.jdom2.JDOMException;
import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.Description;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.OAIUtils;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIImplementationException;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;
import org.mycore.oai.pmh.jaxb.DescriptionType;
import org.mycore.oai.pmh.jaxb.ListSetsType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;
import org.mycore.oai.pmh.jaxb.SetType;

public class ListSetsHandler extends ListRequestsHandler {

    private static Map<Argument, ArgumentType> ARGUMENT_MAP;

    static {
        ARGUMENT_MAP = new HashMap<>();
        ARGUMENT_MAP.put(Argument.resumptionToken, ArgumentType.exclusive);
    }

    @Override
    public Map<Argument, ArgumentType> getArgumentMap() {
        return ARGUMENT_MAP;
    }

    public ListSetsHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) throws OAIException {
        OAIDataList<? extends Set> setList;
        if (request.isResumptionToken()) {
            setList = this.oaiAdapter.getSets(request.getResumptionToken());
        } else {
            setList = this.oaiAdapter.getSets();
        }

        ListSetsType listSetsType = new ListSetsType();
        for (Set set : setList) {
            SetType setType = new SetType();
            setType.setSetName(set.getName());
            setType.setSetSpec(set.getSpec());

            for (Description description : set.getDescription()) {
                DescriptionType descriptionType = new DescriptionType();
                try {
                    descriptionType.setAny(OAIUtils.jdomToDOM(description.toXML()));
                    setType.getSetDescription().add(descriptionType);
                } catch (JDOMException exc) {
                    throw new OAIImplementationException(exc);
                }
            }
            listSetsType.getSet().add(setType);
        }
        // set resumption token
        if (setList.isResumptionTokenSet()) {
            listSetsType.setResumptionToken(toJAXBResumptionToken(setList.getResumptionToken()));
        }
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setListSets(listSetsType);
        return oaipmh;
    }

}
