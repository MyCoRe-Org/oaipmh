package org.mycore.oai.pmh.harvester;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;

/**
 * Utility class to simplify harvesting.
 *
 * @author Matthias Eichner
 */
public abstract class HarvesterUtil {

    /**
     * Gets all records.
     *
     * @param metadataPrefix
     *            (required) Specifies that records should be returned only if the metadata format matching
     *            the supplied metadataPrefix is available or, depending on the repository's support for deletions,
     *            has been deleted.
     *            The metadata formats supported by a repository and for a particular item can be retrieved
     *            using the {@link Harvester#listMetadataFormats()} method.
     *            Optional parameters should be set to <code>null</code> if you don't want to use them.
     * @param from
     *            (optional) A UTCdatetime value,
     *            which specifies a lower bound for datestamp-based selective harvesting.
     * @param until
     *            (optional) A UTCdatetime value,
     *            which specifies a upper bound for datestamp-based selective harvesting.
     * @param setSpec
     *            (optional) Specifies set criteria for selective harvesting.
     * @return Stream of records.

     * @throws HarvestException
     *             General exception that something went wrong. Most likely an HTTP error occurred.
     */
    public static Stream<Record> streamRecords(Harvester harvester, String metadataPrefix, String from, String until,
        String setSpec) throws HarvestException {
        RecordSupplier supplier = new RecordSupplier(harvester, metadataPrefix, from, until, setSpec);
        return StreamSupport.stream(new OAISpliterator<>(supplier), false);
    }

    /**
     * Get all headers.
     *
     * @param metadataPrefix
     *            (required) Specifies that headers should be returned only if the metadata format matching
     *            the supplied metadataPrefix is available or, depending on the repository's support for deletions,
     *            has been deleted.
     *            The metadata formats supported by a repository and for a particular item can be retrieved using
     *            the {@link Harvester#listMetadataFormats()} method.
     *            Optional parameters should be set to <code>null</code> if you don't want to use them.
     * @param from
     *            (optional) A UTCdatetime value,
     *            which specifies a lower bound for datestamp-based selective harvesting.
     * @param until
     *            (optional) A UTCdatetime value,
     *            which specifies a upper bound for datestamp-based selective harvesting.
     * @param setSpec
     *            (optional) Specifies set criteria for selective harvesting.
     * @return Stream of headers.

     * @throws HarvestException
     *             General exception that something went wrong. Most likely an HTTP error occurred.
     */
    public static Stream<Header> streamHeaders(Harvester harvester, String metadataPrefix, String from, String until,
        String setSpec) throws HarvestException {
        IdentifiersSupplier supplier = new IdentifiersSupplier(harvester, metadataPrefix, from, until, setSpec);
        return StreamSupport.stream(new OAISpliterator<>(supplier), false);
    }

    /**
     * Get all sets
     *
     * @return Stream of sets.
     *
     * @throws HarvestException
     *             General exception that something went wrong. Most likely an HTTP error occurred.
     */
    public static Stream<Set> streamSets(Harvester harvester) {
        SetsSupplier supplier = new SetsSupplier(harvester);
        return StreamSupport.stream(new OAISpliterator<>(supplier), false);
    }

    /**
     * Spliterator for oai.
     */
    private static class OAISpliterator<T> implements Spliterator<T> {

        private OAIDataSupplier<T> supplier;

        private OAIDataList<T> dataList;

        OAISpliterator(OAIDataSupplier<T> supplier) {
            this.supplier = supplier;
            try {
                dataList = supplier.list();
            } catch (OAIException e) {
                throw new HarvestException(e);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (dataList != null) {
                if (dataList.isEmpty()) {
                    try {
                        getNextChunk();
                    } catch (OAIException e) {
                        throw new HarvestException(e);
                    }
                }
                if (dataList != null && !dataList.isEmpty()) {
                    action.accept(dataList.remove(0));
                    return true;
                }
            }
            return false;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            if (action == null) {
                throw new IllegalArgumentException("action should not be null");
            }
            try {
                // use an iterative and not recursive approach
                while (dataList != null) {
                    dataList.forEach(action::accept);
                    getNextChunk();
                }
            } catch (OAIException exc) {
                throw new HarvestException(exc);
            }
        }

        private void getNextChunk() throws OAIException {
            dataList = dataList.isResumptionTokenSet() ? this.supplier.list(dataList.getResumptionToken()) : null;
        }

        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
        }

    }

    private interface OAIDataSupplier<T> {

        OAIDataList<T> list() throws OAIException;

        OAIDataList<T> list(ResumptionToken resumptionToken) throws OAIException;

    }

    private static abstract class ObjectSupplier<T> implements OAIDataSupplier<T> {

        protected Harvester harvester;

        protected String metadataPrefix;

        protected String from;

        protected String until;

        protected String setSpec;

        ObjectSupplier(Harvester harvester, String metadataPrefix, String from, String until, String setSpec) {
            this.harvester = harvester;
            this.metadataPrefix = metadataPrefix;
            this.from = from;
            this.until = until;
            this.setSpec = setSpec;
        }

    }

    private static class RecordSupplier extends ObjectSupplier<Record> {

        RecordSupplier(Harvester harvester, String metadataPrefix, String from, String until, String setSpec) {
            super(harvester, metadataPrefix, from, until, setSpec);
        }

        public OAIDataList<Record> list() throws BadArgumentException, CannotDisseminateFormatException,
            NoRecordsMatchException, NoSetHierarchyException, HarvestException {
            return this.harvester.listRecords(this.metadataPrefix, this.from, this.until, this.setSpec);
        }

        public OAIDataList<Record> list(ResumptionToken resumptionToken) throws BadResumptionTokenException {
            return this.harvester.listRecords(resumptionToken.getToken());
        }

    }

    private static class IdentifiersSupplier extends ObjectSupplier<Header> {

        IdentifiersSupplier(Harvester harvester, String metadataPrefix, String from, String until,
            String setSpec) {
            super(harvester, metadataPrefix, from, until, setSpec);
        }

        public OAIDataList<Header> list() throws BadArgumentException, CannotDisseminateFormatException,
            NoRecordsMatchException, NoSetHierarchyException, HarvestException {
            return this.harvester.listIdentifiers(this.metadataPrefix, this.from, this.until, this.setSpec);
        }

        public OAIDataList<Header> list(ResumptionToken resumptionToken) throws BadResumptionTokenException {
            return this.harvester.listIdentifiers(resumptionToken.getToken());
        }

    }

    private static class SetsSupplier implements OAIDataSupplier<Set> {

        protected Harvester harvester;

        SetsSupplier(Harvester harvester) {
            this.harvester = harvester;
        }

        @Override
        public OAIDataList<Set> list() throws NoSetHierarchyException {
            return this.harvester.listSets();
        }

        @Override
        public OAIDataList<Set> list(ResumptionToken resumptionToken)
            throws BadResumptionTokenException, NoSetHierarchyException {
            return this.harvester.listSets(resumptionToken.getToken());
        }

    }

}
