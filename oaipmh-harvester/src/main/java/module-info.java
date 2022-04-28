module org.mycore.oai.pmh.harvester {
    requires org.mycore.oai.pmh;
    requires org.apache.logging.log4j;
    requires org.jdom2;
    exports org.mycore.oai.pmh.harvester;
    uses org.mycore.oai.pmh.harvester.HarvesterFactory;
}
