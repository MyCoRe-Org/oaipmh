module org.mycore.oai.pmh.jaxb {
    requires java.xml.bind;
    requires org.mycore.oai.pmh;
    exports org.mycore.oai.pmh.jaxb;
    opens org.mycore.oai.pmh.jaxb to java.xml.bind;
}
