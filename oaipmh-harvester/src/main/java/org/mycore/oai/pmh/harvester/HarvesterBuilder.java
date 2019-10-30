package org.mycore.oai.pmh.harvester;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

import org.mycore.oai.pmh.FriendsDescription;
import org.mycore.oai.pmh.OAIIdentifierDescription;

/**
 * Use this builder to construct a {@link Harvester} instance.
 * To use default config call {@link #createNewInstance(String)}, you can also create your own
 * config and call {@link #createNewInstance(String, HarvesterConfig)}.
 * 
 * @author Matthias Eichner
 */
public abstract class HarvesterBuilder {

    private static HarvesterFactory harvesterFactory;

    private static HarvesterConfig defaultConfig;

    static {
        harvesterFactory = ServiceLoader.load(HarvesterFactory.class).findFirst().orElse(null);
        defaultConfig = new HarvesterConfig();
        defaultConfig.registerDescription("oai-identifier", OAIIdentifierDescription.class);
        defaultConfig.registerDescription("friends", FriendsDescription.class);
    }

    /**
     * Creates a new {@link Harvester} instance. The default config is used.
     * 
     * @param baseURL
     *            base URL of OAI-PMH data provider (e.g. http://archive.thulb.uni-jena.de/hisbest/oai2). Sample OAI-PMH data providers can be found <a
     *            href="http://www.openarchives.org/Register/BrowseSites">here</a>.
     * @return new instance of <code>Harvester</code>
     */
    public static Harvester createNewInstance(String baseURL) {
        return createNewInstance(baseURL, defaultConfig);
    }

    /**
     * Creates a new {@link Harvester} instance.
     * 
     * @param baseURL
     *            base URL of OAI-PMH data provider (e.g. http://archive.thulb.uni-jena.de/hisbest/oai2). Sample OAI-PMH data providers can be found <a
     *            href="http://www.openarchives.org/Register/BrowseSites">here</a>.
     * @param config
     *            use special configuration
     * @return new instance of <code>Harvester</code>
     */
    public static Harvester createNewInstance(String baseURL, HarvesterConfig config) {
        URL oaiURL;
        try {
            oaiURL = new URL(baseURL);
        } catch (MalformedURLException e) {
            throw new HarvestException(e);
        }
        return getHarvesterFactory()
            .map(f -> f.getHarvester(oaiURL, config))
            .orElseThrow(
                () -> new HarvestException("Could not find implementation of " + HarvesterFactory.class.getName()));
    }

    /**
     * Set your own harvester implementation.
     * 
     * @param harvesterFactory the HarvesterFactoryto set
     */
    public static void setHarvesterFactory(HarvesterFactory harvesterFactory) {
        HarvesterBuilder.harvesterFactory = Objects.requireNonNull(harvesterFactory);
    }

    public static Optional<? extends HarvesterFactory> getHarvesterFactory() {
        return Optional.ofNullable(harvesterFactory);
    }

    /**
     * Returns the default config.
     * 
     * @return instance of <code>HarvesterConfig</code>
     */
    public static HarvesterConfig getDefaultConfig() {
        return defaultConfig;
    }

}
