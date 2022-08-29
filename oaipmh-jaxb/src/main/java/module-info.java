module org.mycore.oai.pmh.jaxb {
    requires jakarta.xml.bind;
    requires org.mycore.oai.pmh;
    exports org.mycore.oai.pmh.jaxb;
    opens org.mycore.oai.pmh.jaxb to jakarta.xml.bind;
}
