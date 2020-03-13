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

import org.mycore.oai.pmh.DateUtils;

/**
 * {@code XmlAdapter} mapping JSR-310 {@code Instant} to ISO-8601 string
 * <p>
 * String format details: {@link DateUtils#format(Instant)}
 *
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
 * @see java.time.Instant
 */
public class OAIUTCDateTimeAdapter extends DateTimeAdapter {

    public OAIUTCDateTimeAdapter() {
        super(DateUtils::parse, DateUtils::format);
    }
}
