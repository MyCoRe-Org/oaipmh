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

package org.mycore.oai.pmh.harvester.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class SimpleOAIProvider implements AutoCloseable {

    HttpServer server;

    public SimpleOAIProvider() throws IOException {
        server = HttpServer.create();
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 0); //any free port
        server.bind(socketAddress, -1);
        server.createContext("/", exchange -> {
            try {
                switch (exchange.getRequestURI().toString()) {
                    case "/oai?verb=Identify":
                        sendResource("oai/identify.xml", exchange);
                        break;
                    case "/oai?verb=ListSets":
                        sendResource("oai/listsets.xml", exchange);
                        break;
                    case "/oai?verb=ListRecords&metadataPrefix=oai_dc&set=patent":
                        sendResource("oai/listpatentrecords.xml", exchange);
                        break;
                    case "/oai?verb=GetRecord&identifier=oai%3Apub.uni-bielefeld.de%3A1893305&metadataPrefix=oai_dc":
                        sendResource("oai/getdcrecord.xml", exchange);
                        break;
                    case "/oai?verb=ListIdentifiers&metadataPrefix=oai_dc&set=doc-type%3AdoctoralThesis":
                        sendResource("oai/listDoctoralThesis.xml", exchange);
                        break;
                    case "/oai?verb=ListIdentifiers&resumptionToken=doc-type%3AdoctoralThesis%21%21%21oai_dc%21200":
                        sendResource("oai/listDoctoralThesis2.xml", exchange);
                        break;
                    case "/oai?verb=ListRecords&metadataPrefix=oai_dc&set=doc-type%3AdoctoralThesis":
                        sendResource("oai/listRecordsDoctoralThesis.xml", exchange);
                        break;
                    case "/oai?verb=ListRecords&resumptionToken=doc-type%3AdoctoralThesis%21%21%21oai_dc%21200":
                        sendResource("oai/listRecordsDoctoralThesis2.xml", exchange);
                        break;
                    case "/oai?verb=ListMetadataFormats":
                        sendResource("oai/metadataFormats.xml", exchange);
                        break;
                    default:
                        System.err
                            .println("Request: https://pub.uni-bielefeld.de" + exchange.getRequestURI().toString());
                        exchange.sendResponseHeaders(404, -1);
                }
            } finally {
                exchange.close();
            }
        });
        server.start();
    }

    private void sendResource(String resource, HttpExchange exchange) throws IOException {
        final URL resourceURL = getClass().getClassLoader().getResource(resource);
        if (resourceURL == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }
        System.err.println(exchange.getRequestMethod() + " " + resourceURL);
        final URLConnection urlConnection = resourceURL.openConnection();
        urlConnection.connect();
        try (final InputStream resourceStream = urlConnection.getInputStream()) {
            exchange.getResponseHeaders()
                .set("Content-Type", urlConnection.getContentType());
            final String lastModified = DateTimeFormatter.RFC_1123_DATE_TIME
                .format(new Date(urlConnection.getLastModified()).toInstant().atZone(
                    ZoneId.of("UTC")));
            exchange.getResponseHeaders()
                .set("Last-Modified", lastModified);
            exchange.sendResponseHeaders(200, urlConnection.getContentLengthLong());
            if ("GET".equals(exchange.getRequestMethod())) {
                resourceStream.transferTo(exchange.getResponseBody());
            }
        }
    }

    public URI getBaseURI() {
        return URI.create("http:/" + server.getAddress().toString());
    }

    @Override
    public void close() {
        server.stop(0);
    }

}
