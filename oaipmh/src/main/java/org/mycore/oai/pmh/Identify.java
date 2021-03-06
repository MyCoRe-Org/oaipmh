package org.mycore.oai.pmh;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to retrieve information about a repository.
 * 
 * @author Matthias Eichner
 */
public interface Identify {

    /**
     * If a record is no longer available then it is said to be deleted. Repositories must declare one of three levels
     * of support for deleted records in the deletedRecord element of the Identify response.
     * <ol>
     * <li><b>No</b> the repository does not maintain information about deletions. A repository that indicates this
     * level of support must not reveal a deleted status in any response.</li>
     * <li><b>Persistent</b> the repository maintains information about deletions with no time limit. A repository that
     * indicates this level of support must persistently keep track of the full history of deletions and consistently
     * reveal the status of a deleted record over time.</li>
     * <li><b>Transient</b> the repository does not guarantee that a list of deletions is maintained persistently or
     * consistently. A repository that indicates this level of support may reveal a deleted status for records.</li>
     * </ol>
     */
    enum DeletedRecordPolicy {
        No, Persistent, Transient;

        public String value() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public static DeletedRecordPolicy get(String drp) {
            String lower = drp.toLowerCase(Locale.ENGLISH);
            String first = lower.substring(0, 1).toUpperCase(Locale.ENGLISH);
            return DeletedRecordPolicy.valueOf(first + lower.substring(1));
        }
    }

    /**
     * A list of email addresses of administrators of the repository.
     * 
     * @return list of emails
     */
    List<String> getAdminEmailList();

    /**
     * The base URL of the repository. e.g. http://memory.loc.gov/cgi-bin/oai
     * 
     * @return base url as string
     */
    String getBaseURL();

    /**
     * The manner in which the repository supports the notion of deleted records. Legitimate values are no, transient,
     * persistent.
     * 
     * @return policy of deleted records
     */
    DeletedRecordPolicy getDeletedRecordPolicy();

    /**
     * A date that is the guaranteed lower limit of all datestamps recording changes, modifications, or deletions in the
     * repository. A repository must not use datestamps lower than the one specified by the content of the
     * earliestDatestamp element.
     * 
     * @return earliest date in repository
     */
    Instant getEarliestDatestamp();

    /**
     * The finest harvesting granularity supported by the repository. The legitimate values are YYYY-MM-DD and
     * YYYY-MM-DDThh:mm:ssZ.
     * 
     * @return granularity of the repository
     */
    Granularity getGranularity();

    /**
     * The version of the oai protocol. This should be "2.0".
     * 
     * @return protocoal version as string
     */
    String getProtocolVersion();

    /**
     * The human readable name of the repository.
     * 
     * @return name of the repository
     */
    String getRepositoryName();

    /**
     * An extensible mechanism for communities to describe their repositories. For example, the description list could
     * be used to include collection-level metadata in the response to the Identify request (
     * {@link OAIIdentifierDescription}).
     * 
     * @return a list of descriptions
     */
    List<Description> getDescriptionList();

    /**
     * A compression encoding supported by the repository. The recommended values are those defined for the
     * Content-Encoding header in Section 14.11 of RFC 2616 describing HTTP 1.1. A compression element should not be
     * included for the identity encoding, which is implied.
     * 
     * @return compression as string
     */
    String getCompression();

}
