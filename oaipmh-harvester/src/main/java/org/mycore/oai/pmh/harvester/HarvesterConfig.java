package org.mycore.oai.pmh.harvester;

import java.util.HashMap;
import java.util.Map;

import org.mycore.oai.pmh.Description;

/**
 * Class to configure a {@link Harvester}. 
 * 
 * @author Matthias Eichner
 */
public class HarvesterConfig {

    private Map<String, Class<? extends Description>> descriptionMap;

    public HarvesterConfig() {
        this.descriptionMap = new HashMap<String, Class<? extends Description>>();
    }

    /**
     * Register a new description.
     * 
     * @param descriptionName
     *            should be equal to the xml element name (e.g. oai-identifier)
     * @param descriptionClass
     *            Java class which represents the description
     * @return the previous value associated with descriptionName, or null if there was no mapping for descriptionName.
     */
    public Class<? extends Description> registerDescription(String descriptionName,
        Class<? extends Description> descriptionClass) {
        return this.descriptionMap.put(descriptionName, descriptionClass);
    }

    /**
     * Unregisters a description.
     * 
     * @param descriptionName
     *            should be equal to the xml element name (e.g. oai-identifier)
     * @return the previous value associated with descriptionName, or null if there was no mapping for descriptionName.
     */
    public Class<? extends Description> unregisterDescription(String descriptionName) {
        return this.descriptionMap.remove(descriptionName);
    }

    /**
     * Creates a new instance of <code>Description</code>. If the given name is not registered, null will be returned.
     * 
     * @param name
     *            name of description
     * @return a new <code>Description</code>
     */
    public Description createNewDescriptionInstance(String name) {
        Class<? extends Description> descClass = this.descriptionMap.get(name);
        if (descClass == null) {
            return null;
        }
        try {
            return descClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exc) {
            throw new HarvestException("while creating description " + name, exc);
        }
    }
}
