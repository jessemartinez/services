/**
 * This document is a part of the source code and related artifacts
 * for CollectionSpace, an open source collections management system
 * for museums and related institutions:
 *
 * http://www.collectionspace.org
 * http://wiki.collectionspace.org
 *
 * Copyright (c)) 2009 Regents of the University of California
 *
 * Licensed under the Educational Community License (ECL), Version 2.0.
 * You may not use this file except in compliance with this License.
 *
 * You may obtain a copy of the ECL 2.0 License at
 * https://source.collectionspace.org/collection-space/LICENSE.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.collectionspace.services.client.test;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.collectionspace.services.WorkJAXBSchema;
import org.collectionspace.services.client.AbstractCommonListUtils;
import org.collectionspace.services.client.AuthorityClient;
import org.collectionspace.services.client.CollectionSpaceClient;
import org.collectionspace.services.client.PayloadOutputPart;
import org.collectionspace.services.client.PoxPayloadIn;
import org.collectionspace.services.client.PoxPayloadOut;
import org.collectionspace.services.client.WorkAuthorityClient;
import org.collectionspace.services.client.WorkAuthorityClientUtils;
import org.collectionspace.services.jaxb.AbstractCommonList;
import org.collectionspace.services.work.WorkauthoritiesCommon;
import org.collectionspace.services.work.WorksCommon;
// possibly needed
import org.collectionspace.services.work.CreatorGroupList;
import org.collectionspace.services.work.CreatorGroup;
import org.collectionspace.services.work.PublisherGroupList;
import org.collectionspace.services.work.PublisherGroup;
 
import org.jboss.resteasy.client.ClientResponse;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
 
/**
 * WorkAuthorityServiceTest, carries out tests against a
 * deployed and running WorkAuthority Service.
 *
 * $LastChangedRevision: 753 $
 * $LastChangedDate: 2009-09-23 11:03:36 -0700 (Wed, 23 Sep 2009) $
 */
public class WorkAuthorityServiceTest extends AbstractAuthorityServiceTest<WorkauthoritiesCommon, WorksCommon> {
 
	/** The logger. */
	private final String CLASS_NAME = WorkAuthorityServiceTest.class.getName();
	private final Logger logger = LoggerFactory.getLogger(WorkAuthorityServiceTest.class);
	private final String REFNAME = "refName";
	private final static String EMPTY_REFNAME = "";
	private final String DISPLAYNAME = "displayName";
 
	@Override
	public String getServicePathComponent() {
		return WorkAuthorityClient.SERVICE_PATH_COMPONENT;
	}

	@Override
	protected String getServiceName() {
		return WorkAuthorityClient.SERVICE_NAME;
	}
   
	public String getItemServicePathComponent() {
		return AuthorityClient.ITEMS;
	}   
   
	// 	  Instance variables specific to this test.
	final String TEST_WORK_DISPLAY_NAME = "Muppets Take Manhattan (1984)";
	final String TEST_WORK_SCOPE_NOTE = "scope note here";
	final String TEST_WORK_INDEXING_NOTE = "indexing note here";
	final String TEST_WORK_HISTORY_NOTE = "history note here";
	final String TEST_WORK_SOURCE_NOTE = "source note here";
	final String TEST_WORK_STATUS = "complete";
	final String TEST_WORK_GENRE = "comedy";
	final String TEST_WORK_MEDIUM = "film";
	final String TEST_WORK_CREATOR_GROUP_CREATOR = "Frank Oz";
	final String TEST_WORK_CREATOR_GROUP_CREATOR_TYPE = "director";
	final String TEST_WORK_PUBLISHER_GROUP_PUBLISHER = "TriStar Pictures";
	final String TEST_WORK_PUBLISHER_GROUP_PUBLISHER_TYPE = "Distributor";
	final String TEST_WORK_SHORTID = "muppetstakemanhattan";
	final String TEST_TERM_STATUS = "accepted";
	final CreatorGroupList NULL_WORK_CREATOR_GROUP_LIST = null;
	final PublisherGroupList NULL_WORK_PUBLISHER_GROUP_LIST = null;
	
	/** The known resource id. */
	private String knownResourceShortIdentifer = null;
	private String knownResourceRefName = null;
	private String knownWorkTypeRefName = null;
   
	/* (non-Javadoc)
     * @see org.collectionspace.services.client.test.BaseServiceTest#getClientInstance()
     */
	@Override
	protected CollectionSpaceClient getClientInstance() {
	   return new WorkAuthorityClient();
	}
 
	/**
	 * Creates the item in authority.
	 *
	 * @param vcsid 		The vcsid
	 * @param authRefName 	The auth ref name
	 * @return 				The string
	 */
	private String createItemInAuthority(String vcsid, String authRefName) {
		final String testName = "createItemInAuthority("+vcsid+","+authRefName+")";
		
		// Submit the request to the service and store the response.
		WorkAuthorityClient client = new WorkAuthorityClient();
		Map<String, String> workMap = new HashMap<String,String>();
		
		// Fields present in all authority records.
		workMap.put(WorkJAXBSchema.SHORT_IDENTIFIER, TEST_WORK_SHORTID);
		//TODO Make term status be controlled vocab.
		workMap.put(WorkJAXBSchema.TERM_STATUS, TEST_TERM_STATUS);
		workMap.put(WorkJAXBSchema.DISPLAY_NAME, TEST_WORK_DISPLAY_NAME);
		workMap.put(WorkJAXBSchema.SHORT_IDENTIFIER, TEST_WORK_SHORTID);
		workMap.put(WorkJAXBSchema.WORK_SCOPE_NOTE, TEST_WORK_SCOPE_NOTE);
		workMap.put(WorkJAXBSchema.WORK_INDEXING_NOTE, TEST_WORK_INDEXING_NOTE);
		workMap.put(WorkJAXBSchema.WORK_HISTORY_NOTE, TEST_WORK_HISTORY_NOTE);
		workMap.put(WorkJAXBSchema.WORK_SOURCE_NOTE, TEST_WORK_SOURCE_NOTE);
		workMap.put(WorkJAXBSchema.WORK_GENRE, TEST_WORK_GENRE);
		workMap.put(WorkJAXBSchema.WORK_MEDIUM, TEST_WORK_MEDIUM);

		// complex field types

        CreatorGroupList creatorGroupList = new CreatorGroupList();
        List<CreatorGroup> creatorGroups = creatorGroupList.getCreatorGroup();
        CreatorGroup creatorGroup = new CreatorGroup();
        creatorGroup.setCreator(TEST_WORK_CREATOR_GROUP_CREATOR);
        creatorGroup.setCreatorType(TEST_WORK_CREATOR_GROUP_CREATOR_TYPE);
        creatorGroups.add(creatorGroup);

        PublisherGroupList publisherGroupList = new PublisherGroupList();
        List<PublisherGroup> publisherGroups = publisherGroupList.getPublisherGroup();
        PublisherGroup publisherGroup = new PublisherGroup();
        publisherGroup.setPublisher(TEST_WORK_PUBLISHER_GROUP_PUBLISHER);
        publisherGroup.setPublisherType(TEST_WORK_PUBLISHER_GROUP_PUBLISHER_TYPE);
        publisherGroups.add(publisherGroup);
		
		String newID = WorkAuthorityClientUtils.createItemInAuthority(vcsid, authRefName, workMap, creatorGroupList, publisherGroupList, client);

		// Store the ID returned from the first item resource created
		// for additional tests below.
		if (knownItemResourceId == null){
			setKnownItemResource(newID, TEST_WORK_SHORTID);
			if (logger.isDebugEnabled()) {
				logger.debug(testName + ": knownItemResourceId=" + newID);
			}
		}

		// Store the IDs from any item resources created
		// by tests, along with the IDs of their parents, so these items
		// can be deleted after all tests have been run.
		allResourceItemIdsCreated.put(newID, vcsid);

		return newID;
	}
 
	/**
	 * Verify item display name.
	 *
	 * @param testName the test name
	 * @throws Exception the exception
	 */
	@Test(dataProvider="testName",
		dependsOnMethods = {"readItem", "updateItem"})
	public void verifyItemDisplayName(String testName) throws Exception {
		// Perform setup.
		setupRead();
		//
		// First, read our known item resource
		//
		WorkAuthorityClient client = new WorkAuthorityClient();
		ClientResponse<String> res = client.readItem(knownResourceId, knownItemResourceId);
		WorksCommon work = null;
		try {
			assertStatusCode(res, testName);
			// Check whether work has expected displayName.
			PoxPayloadIn input = new PoxPayloadIn(res.getEntity());
			work = (WorksCommon) extractPart(input,
				client.getItemCommonPartName(), WorksCommon.class);
			Assert.assertNotNull(work);
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}
		//
		// Now prepare an updated payload.
		//
		String displayName = work.getDisplayName();
		// Make sure displayName matches computed form
		String expectedDisplayName =
			WorkAuthorityClientUtils.prepareDefaultDisplayName(TEST_WORK_DISPLAY_NAME);
		Assert.assertNotNull(displayName, "Display name was null.  Expected it to be: " + expectedDisplayName);

		// Update the shortName and verify the computed name is updated.
		work.setCsid(null);
		work.setDisplayNameComputed(true);
		
		work.setDisplayName("updated-" + TEST_WORK_DISPLAY_NAME);
		expectedDisplayName =
			WorkAuthorityClientUtils.prepareDefaultDisplayName("updated-" + TEST_WORK_DISPLAY_NAME);
		
		// Submit the updated resource to the service and store the response.
		PoxPayloadOut output = new PoxPayloadOut(WorkAuthorityClient.SERVICE_ITEM_PAYLOAD_NAME);
		PayloadOutputPart commonPart = output.addPart(client.getItemCommonPartName(), work);

		setupUpdate();        
		res = client.updateItem(knownResourceId, knownItemResourceId, output);
		WorksCommon updatedWork = null;
		try {
			assertStatusCode(res, testName);
			// Retrieve the updated resource and verify that its contents exist.
			PoxPayloadIn input = new PoxPayloadIn(res.getEntity());
			updatedWork = (WorksCommon) extractPart(input,
				client.getItemCommonPartName(), WorksCommon.class);
			Assert.assertNotNull(updatedWork);
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}
		// Verify that the updated resource received the correct data.
	 	Assert.assertEquals(updatedWork.getDisplayName(), work.getDisplayName(),
			"Updated DisplayName in Work did not match submitted data.");

		// Verify that the updated resource computes the right displayName.
		Assert.assertEquals(updatedWork.getDisplayName(), expectedDisplayName,
			"Updated DisplayName in Work not reflected in computed DisplayName.");
		//
		// Now Update the displayName, not computed and verify the computed name is overriden.
		//
		work.setDisplayNameComputed(false);
		expectedDisplayName = "TestName";
		work.setDisplayName(expectedDisplayName);

		// Submit the updated resource to the service and store the response.
		output = new PoxPayloadOut(WorkAuthorityClient.SERVICE_ITEM_PAYLOAD_NAME);
		commonPart = output.addPart(client.getItemCommonPartName(), work);
		setupUpdate();        
		res = client.updateItem(knownResourceId, knownItemResourceId, output);
		try {
				assertStatusCode(res, testName);
				// Retrieve the updated resource and verify that its contents exist.
				PoxPayloadIn input = new PoxPayloadIn(res.getEntity());
				updatedWork = (WorksCommon) extractPart(input,
								client.getItemCommonPartName(), WorksCommon.class);
				Assert.assertNotNull(updatedWork);
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}

		// Verify that the updated resource received the correct data.
		Assert.assertEquals(updatedWork.isDisplayNameComputed(), false,
			   "Updated displayNameComputed in Work did not match submitted data.");
		// Verify that the updated resource computes the right displayName.
		Assert.assertEquals(updatedWork.getDisplayName(),
				expectedDisplayName,
			   	"Updated DisplayName (not computed) in Work not stored.");
   }
 
	/**
	 * Verify illegal item display name.
	 *
	 * @param testName the test name
	 * @throws Exception the exception
	 */
	@Test(dataProvider="testName", dependsOnMethods = {"verifyItemDisplayName"})
	public void verifyIllegalItemDisplayName(String testName) throws Exception {
		// Perform setup for read.
		setupRead();

		// Submit the request to the service and store the response.
		WorkAuthorityClient client = new WorkAuthorityClient();
		ClientResponse<String> res = client.readItem(knownResourceId, knownItemResourceId);
		WorksCommon work = null;
		try {
			assertStatusCode(res, testName);        
			PoxPayloadIn input = new PoxPayloadIn(res.getEntity());
			work = (WorksCommon) extractPart(input,	client.getItemCommonPartName(), WorksCommon.class);
			Assert.assertNotNull(work);
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}

		// Try to Update with computed false and no displayName
		work.setDisplayNameComputed(false);
		work.setDisplayName(null);

		// Submit the updated resource to the service and store the response.
		PoxPayloadOut output = new PoxPayloadOut(WorkAuthorityClient.SERVICE_ITEM_PAYLOAD_NAME);
		PayloadOutputPart commonPart = output.addPart(client.getItemCommonPartName(), work);
		setupUpdateWithInvalidBody(); // we expected a failure here.
		res = client.updateItem(knownResourceId, knownItemResourceId, output);
		try {
			assertStatusCode(res, testName);
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}
	}
 
	/**
	 * Read item list.
	 */
	@Test(dataProvider = "testName", groups = {"readList"}, dependsOnMethods = {"readList"})
	public void readItemList(String testName) {
		readItemList(knownAuthorityWithItems, null);
	}
 
	/**
	 * Read item list by authority name.
	 */
	@Test(dataProvider = "testName", groups = {"readList"}, dependsOnMethods = {"readItemList"})
	public void readItemListByAuthorityName(String testName) {
		readItemList(null, READITEMS_SHORT_IDENTIFIER);
	}
   
	/**
	 * Read item list.
	 * 
	 * @param vcsid	The vcsid
	 * @param name	The name
	 */
	private void readItemList(String vcsid, String shortId) {
		String testName = "readItemList";

		// Perform setup.
		setupReadList();

		// Submit the request to the service and store the response.
		WorkAuthorityClient client = new WorkAuthorityClient();
		ClientResponse<AbstractCommonList> res = null;
		if (vcsid != null) {
			res = client.readItemList(vcsid, null, null);
		} else if (shortId != null) {
			res = client.readItemListForNamedAuthority(shortId, null, null);
		} else {
			Assert.fail("readItemList passed null csid and name!");
		}

		AbstractCommonList list = null;
		try {
			assertStatusCode(res, testName);
			list = res.getEntity();
		} finally {
			if (res != null) {
				res.releaseConnection();
			}
		}
                
		List<AbstractCommonList.ListItem> items = list.getListItem();
		int nItemsReturned = items.size();
		// There will be 'nItemsToCreateInList'
		// items created by the createItemList test,
		// all associated with the same parent resource.
		int nExpectedItems = nItemsToCreateInList;
		if (logger.isDebugEnabled()) {
			logger.debug(testName + ": Expected " + nExpectedItems
				+ " items; got: " + nItemsReturned);
		}
		Assert.assertEquals(nItemsReturned, nExpectedItems);

		for (AbstractCommonList.ListItem item : items) {
			String value = AbstractCommonListUtils.ListItemGetElementValue(
						item, REFNAME);
			Assert.assertTrue((null != value), "Item refName is null!");
			value = AbstractCommonListUtils.ListItemGetElementValue(item,
						DISPLAYNAME);
			Assert.assertTrue((null != value), "Item displayName is null!");
		}
		if (logger.isTraceEnabled()) {
			AbstractCommonListUtils.ListItemsInAbstractCommonList(list, logger,
						testName);
		}
	}
 
	@Override
	public void delete(String testName) throws Exception {
		// Do nothing.  See localDelete().  This ensure proper test order.
	}
   
	@Test(dataProvider = "testName", dependsOnMethods = {"localDeleteItem"})    
	public void localDelete(String testName) throws Exception {
		super.delete(testName);
	}
 
	@Override
	public void deleteItem(String testName) throws Exception {
		// Do nothing.  We need to wait until after the test "localDelete" gets run.  When it does,
		// its dependencies will get run first and then we can call the base class' delete method.
	}
   
	@Test(dataProvider = "testName", groups = {"delete"},
		dependsOnMethods = {"verifyIllegalItemDisplayName"})
	public void localDeleteItem(String testName) throws Exception {
		super.deleteItem(testName);
	}
   
	// ---------------------------------------------------------------
	// Cleanup of resources created during testing
	// ---------------------------------------------------------------
   
	/**
	 * Deletes all resources created by tests, after all tests have been run.
	 *
	 * This cleanup method will always be run, even if one or more tests fail.
	 * For this reason, it attempts to remove all resources created
	 * at any point during testing, even if some of those resources
	 * may be expected to be deleted by certain tests.
	 */
 
	@AfterClass(alwaysRun=true)
	public void cleanUp() {
		String noTest = System.getProperty("noTestCleanup");
		if(Boolean.TRUE.toString().equalsIgnoreCase(noTest)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Skipping Cleanup phase ...");
			}
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Cleaning up temporary resources created for testing ...");
		}
		String parentResourceId;
		String itemResourceId;
		// Clean up contact resources.
		WorkAuthorityClient client = new WorkAuthorityClient();
		parentResourceId = knownResourceId;
		// Clean up item resources.
		for (Map.Entry<String, String> entry : allResourceItemIdsCreated.entrySet()) {
			itemResourceId = entry.getKey();
			parentResourceId = entry.getValue();
			// Note: Any non-success responses from the delete operation
			// below are ignored and not reported.
			client.deleteItem(parentResourceId, itemResourceId).releaseConnection();
		}
		// Clean up parent resources.
		for (String resourceId : allResourceIdsCreated) {
			// Note: Any non-success responses from the delete operation
			// below are ignored and not reported.
			client.delete(resourceId).releaseConnection();
		}
	}
 
	// ---------------------------------------------------------------
	// Utility methods used by tests above
	// ---------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.collectionspace.services.client.test.BaseServiceTest#getServicePathComponent()
	 */

	/**
	 * Returns the root URL for the item service.
	 *
	 * This URL consists of a base URL for all services, followed by
	 * a path component for the owning parent, followed by the
	 * path component for the items.
	 *
	 * @param  parentResourceIdentifier  An identifier (such as a UUID) for the
	 * parent authority resource of the relevant item resource.
	 *
	 * @return The root URL for the item service.
	 */
	protected String getItemServiceRootURL(String parentResourceIdentifier) {
		return getResourceURL(parentResourceIdentifier) + "/" + getItemServicePathComponent();
	}
 
	/**
	 * Returns the URL of a specific item resource managed by a service, and
	 * designated by an identifier (such as a universally unique ID, or UUID).
	 *
	 * @param  parentResourceIdentifier  An identifier (such as a UUID) for the
	 * parent authority resource of the relevant item resource.
	 *
	 * @param  itemResourceIdentifier  An identifier (such as a UUID) for an
	 * item resource.
	 *
	 * @return The URL of a specific item resource managed by a service.
	 */
	protected String getItemResourceURL(String parentResourceIdentifier, String itemResourceIdentifier) {
		return getItemServiceRootURL(parentResourceIdentifier) + "/" + itemResourceIdentifier;
	}
 
	@Override
	public void authorityTests(String testName) {
		// TODO Auto-generated method stub
	}

	//
	// Work specific overrides
	//

	@Override
	protected PoxPayloadOut createInstance(String commonPartName,
					String identifier) {
		// Submit the request to the service and store the response.
		String shortId = identifier;
		String displayName = "displayName-" + shortId;
		// String baseRefName = WorkAuthorityClientUtils.createWorkAuthRefName(shortId, null);        
		PoxPayloadOut result =
			WorkAuthorityClientUtils.createWorkAuthorityInstance(
			displayName, shortId, commonPartName);
		return result;
	}
        
	@Override
	protected PoxPayloadOut createNonExistenceInstance(String commonPartName, String identifier) {
		String displayName = "displayName-NON_EXISTENT_ID";
		PoxPayloadOut result = WorkAuthorityClientUtils.createWorkAuthorityInstance(
						displayName, "nonEx", commonPartName);
		return result;
	}
 
	@Override
	protected WorkauthoritiesCommon updateInstance(WorkauthoritiesCommon workauthoritiesCommon) {
		WorkauthoritiesCommon result = new WorkauthoritiesCommon();

		result.setDisplayName("updated-" + workauthoritiesCommon.getDisplayName());
		result.setVocabType("updated-" + workauthoritiesCommon.getVocabType());

		return result;
	}
 
	@Override
	protected void compareUpdatedInstances(WorkauthoritiesCommon original,
					WorkauthoritiesCommon updated) throws Exception {
		Assert.assertEquals(updated.getDisplayName(),
					original.getDisplayName(),
					"Display name in updated object did not match submitted data.");
	}
 
	protected void compareReadInstances(WorkauthoritiesCommon original,
					WorkauthoritiesCommon fromRead) throws Exception {
		Assert.assertNotNull(fromRead.getDisplayName());
		Assert.assertNotNull(fromRead.getShortIdentifier());
		Assert.assertNotNull(fromRead.getRefName());
	}
        
	//
	// Authority item specific overrides
	//
        
	@Override
	protected String createItemInAuthority(String authorityId) {
		return createItemInAuthority(authorityId, null /*refname*/);
	}
 
	@Override
	protected WorksCommon updateItemInstance(WorksCommon worksCommon) {
		WorksCommon result = new WorksCommon();

		result.setDisplayName("updated-" + worksCommon.getDisplayName());

		return result;
	}
 
	@Override
	protected void compareUpdatedItemInstances(WorksCommon original,
					WorksCommon updated) throws Exception {
		Assert.assertEquals(updated.getDisplayName(), original.getDisplayName(),
               "Data in updated Work did not match submitted data.");
	}
 
	@Override
	protected void verifyReadItemInstance(WorksCommon item)
				throws Exception {
		// Copied from Taxon
		Assert.assertNotNull(item.getDisplayName(), "Field value is unexpectedly null.");
		Assert.assertEquals(item.getDisplayName(), TEST_WORK_DISPLAY_NAME,
			"Field value " + item.getDisplayName()
			+ "does not match expected value " + TEST_WORK_DISPLAY_NAME);
	}
 
	@Override
	protected PoxPayloadOut createNonExistenceItemInstance(
					String commonPartName, String identifier) {
		Map<String, String> nonexMap = new HashMap<String,String>();
		nonexMap.put(WorkJAXBSchema.DISPLAY_NAME, TEST_WORK_DISPLAY_NAME);
		nonexMap.put(WorkJAXBSchema.SHORT_IDENTIFIER, "nonEx");
		nonexMap.put(WorkJAXBSchema.TERM_STATUS, TEST_TERM_STATUS);
		final String EMPTY_REFNAME = "";

		PoxPayloadOut result =
				WorkAuthorityClientUtils.createWorkInstance(EMPTY_REFNAME, nonexMap, 
					NULL_WORK_CREATOR_GROUP_LIST, NULL_WORK_PUBLISHER_GROUP_LIST,
					commonPartName);
		return result;
	}
}