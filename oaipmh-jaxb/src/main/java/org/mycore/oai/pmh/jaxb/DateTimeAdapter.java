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

package org.mycore.oai.pmh.jaxb;

import java.time.Instant;
import java.util.function.Function;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class is used to marshall and unmarshall {@link Instant} as <a href="http://www.openarchives.org/OAI/2.0/openarchivesprotocol.htm#Dates">dateTime</a>.
 */
abstract class DateTimeAdapter extends XmlAdapter<String, Instant> {
    protected final Function<String, Instant> unmarschaller;

    protected final Function<Instant, String> marschaller;

    DateTimeAdapter(Function<String, Instant> unmarschaller, Function<Instant, String> marschaller) {
        this.unmarschaller = unmarschaller;
        this.marschaller = marschaller;
    }

    @Override
    public Instant unmarshal(String stringValue) {
        return unmarschaller.apply(stringValue);
    }

    @Override
    public String marshal(Instant value) {
        return marschaller.apply(value);
    }
}
