module org.mycore.oai.pmh.harvester.impl {
    requires jakarta.xml.bind;
    requires org.mycore.oai.pmh;
    requires org.mycore.oai.pmh.harvester;
    requires org.mycore.oai.pmh.jaxb;
    requires org.apache.logging.log4j;
    requires org.jdom2;
    requires static jdk.httpserver; //for junit
    exports org.mycore.oai.pmh.harvester.impl;
    provides org.mycore.oai.pmh.harvester.HarvesterFactory with org.mycore.oai.pmh.harvester.impl.JAXBHarvesterFactory;
}
