package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.List;

/**
 * A set is an optional construct for grouping items for the purpose of selective harvesting.
 * Repositories may organize items into sets.
 * Set organization may be flat, i.e. a simple list, or hierarchical.
 * Multiple hierarchies with distinct, independent top-level nodes are allowed.
 * 
 * @author Matthias Eichner
 */
public class Set {

    private String spec;

    private String name;

    private List<Description> descriptionList;

    /**
     * Creates a new Set by spec.
     * 
     * @param spec
     *            a colon [:] separated list indicating the path from the root of the set hierarchy
     *            to the respective node (e.g. institution:nebraska:lincoln)
     */
    public Set(String spec) {
        this(spec, "");
    }

    /**
     * Creates a new Set by spec and name.
     * 
     * @param spec
     *            a colon [:] separated list indicating the path from the root of the set hierarchy
     *            to the respective node (e.g. institution:nebraska:lincoln)
     * @param name
     *            a short human-readable string naming the set
     */
    public Set(String spec, String name) {
        this.spec = spec;
        this.name = name;
        this.descriptionList = new ArrayList<>();
    }

    /**
     * A colon [:] separated list indicating the path from the root of the set hierarchy
     * to the respective node (e.g. institution:nebraska:lincoln).
     * 
     * @return spec of the set
     */
    public String getSpec() {
        return this.spec;
    }

    /**
     * A list container that may hold community-specific XML-encoded data about the set; the accompanying <a
     * href="http://www.openarchives.org/OAI/2.0/guidelines.htm">Implementation Guidelines</a> document
     * provides suggestions regarding the usage of this container.
     * 
     * @return list of descriptions
     */
    public List<Description> getDescription() {
        return this.descriptionList;
    }

    /**
     * Sets a short human-readable string naming the set.
     * 
     * @param name name of set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a short human-readable string naming the set.
     * 
     * @return name of the set
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Set && this.spec.equals(((Set) obj).getSpec());
    }

    @Override
    public String toString() {
        String name = this.name != null ? " (" + this.name + ")" : "";
        return this.spec + name;
    }
}
