module org.mycore.oai.pmh.dataprovider.impl {
    requires jakarta.xml.bind;
    requires org.mycore.oai.pmh;
    requires org.mycore.oai.pmh.dataprovider;
    requires org.mycore.oai.pmh.jaxb;
    requires org.apache.logging.log4j;
    requires org.jdom2;
    exports org.mycore.oai.pmh.dataprovider.impl;
    provides org.mycore.oai.pmh.dataprovider.OAIProvider with org.mycore.oai.pmh.dataprovider.impl.JAXBOAIProvider;
}
