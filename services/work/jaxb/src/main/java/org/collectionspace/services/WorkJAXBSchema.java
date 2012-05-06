/**
 * 
 */
package org.collectionspace.services;
import org.collectionspace.services.common.vocabulary.AuthorityItemJAXBSchema;

/**
 * @author 
 *
 */
public interface WorkJAXBSchema extends AuthorityItemJAXBSchema {
	final static String WORKS_COMMON = "works_common";
	final static String WORK_SCOPE_NOTE = "scopeNote";
	final static String WORK_INDEXING_NOTE = "indexingNote";
	final static String WORK_HISTORY_NOTE = "historyNote";
	final static String WORK_SOURCE_NOTE = "sourceNote";
	final static String WORK_GENRE = "genre";
	final static String WORK_MEDIUM = "medium";

	final static String WORK_CREATOR_GROUP_LIST = "creatorGroupList";
	final static String WORK_CREATOR_GROUP_CREATOR = "creator";
	final static String WORK_CREATOR_GROUP_CREATOR_TYPE = "creatorType";

	final static String WORK_PUBLISHER_GROUP_LIST = "publisherGroupList";
	final static String WORK_PUBLISHER_GROUP_PUBLISHER = "publisher";
	final static String WORK_PUBLISHER_GROUP_PUBLISHER_TYPE = "publisherType";
	
	final static String DISPLAY_NAME = "displayName";
	final static String DISPLAY_NAME_COMPUTED = "displayNameComputed";
	final static String SHORT_DISPLAY_NAME = "shortDisplayName";
	final static String SHORT_DISPLAY_NAME_COMPUTED = "shortDisplayNameComputed";
}

