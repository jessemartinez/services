package org.collectionspace.services.client;
 
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
 
import org.apache.commons.io.FileUtils;
import org.collectionspace.services.WorkJAXBSchema;
import org.collectionspace.services.client.test.ServiceRequestType;
import org.collectionspace.services.work.WorkauthoritiesCommon;
import org.collectionspace.services.work.CreatorGroupList;
import org.collectionspace.services.work.CreatorGroup;
import org.collectionspace.services.work.PublisherGroupList;
import org.collectionspace.services.work.PublisherGroup;
import org.collectionspace.services.work.UseForList;
import org.collectionspace.services.work.WorksCommon;
import org.dom4j.DocumentException;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkAuthorityClientUtils {
    private static final Logger logger =
        LoggerFactory.getLogger(WorkAuthorityClientUtils.class);

    /**
     * Creates a new Work Authority
     * @param displayName	The displayName used in UI, etc.
     * @param refName		The proper refName for this authority
     * @param headerLabel	The common part label
     * @return				The PoxPayloadOut payload for the create call
     */
    public static PoxPayloadOut createWorkAuthorityInstance(
			String displayName, String shortIdentifier, String headerLabel ) {
		WorkauthoritiesCommon workAuthority = new WorkauthoritiesCommon();
		workAuthority.setDisplayName(displayName);
		workAuthority.setShortIdentifier(shortIdentifier);
		String refName = createWorkAuthRefName(shortIdentifier, displayName);
		workAuthority.setRefName(refName);
		workAuthority.setVocabType("WorkAuthority"); //FIXME: REM - Should this really be hard-coded?
		PoxPayloadOut multipart = new PoxPayloadOut(WorkAuthorityClient.SERVICE_PAYLOAD_NAME);
		PayloadOutputPart commonPart = multipart.addPart(workAuthority, MediaType.APPLICATION_XML_TYPE);
		commonPart.setLabel(headerLabel);

		if(logger.isDebugEnabled()){
			logger.debug("to be created, workAuthority common ", 
				workAuthority, WorkauthoritiesCommon.class);
		}

		return multipart;
	}

	/**
	 * @param workAuthRefName 	The proper refName for this authority
	 * @param workInfo 			The properties for the new Work. Can pass in one condition note and date string.
	 * @param CreatorGroupList 		Creator group list (values of a repeatable group in the term record)
 	 * @param PublisherGroupList 	Publisher group list (values of a repeatable group in the term record)
 	 * @param UseForList 			'Use For' list (values of a repeatable 'Use for' field in the term record)
	 * @param headerLabel		The common part label
	 * @return  				The PoxPayloadOut payload for the create call
	 */
	public static PoxPayloadOut createWorkInstance(
			String workAuthRefName, Map<String, String> workInfo,
			CreatorGroupList creatorGroupList, PublisherGroupList publisherGroupList, UseForList useForList,
			String headerLabel){
		WorksCommon work = new WorksCommon();
		String shortId = workInfo.get(WorkJAXBSchema.SHORT_IDENTIFIER);
		String displayName = workInfo.get(WorkJAXBSchema.DISPLAY_NAME);
		work.setShortIdentifier(shortId);
		String workRefName = createWorkRefName(workAuthRefName, shortId, displayName);
		work.setRefName(workRefName);
		
		String value = null;
		value = workInfo.get(WorkJAXBSchema.DISPLAY_NAME_COMPUTED);
		boolean displayNameComputed = (value==null) || value.equalsIgnoreCase("true");
		work.setDisplayNameComputed(displayNameComputed);
		
		if((value = (String)workInfo.get(WorkJAXBSchema.DISPLAY_NAME))!=null)
			work.setDisplayName(value);
		value = workInfo.get(WorkJAXBSchema.DISPLAY_NAME_COMPUTED);
		displayNameComputed = (value==null) || value.equalsIgnoreCase("true");
		work.setDisplayNameComputed(displayNameComputed);

		if ((value = (String) workInfo.get(WorkJAXBSchema.TERM_STATUS)) != null) {
               work.setTermStatus(value);
		}


		// Fields specific to this authority record type.
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_NAME)) != null) {
               work.setWorkName(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_SCOPE_NOTES)) != null) {
			work.setScopeNotes(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_INDEXING_NOTES)) != null) {
			work.setIndexingNotes(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_HISTORY_NOTES)) != null) {
			work.setHistoryNotes(value);
		}

		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_SOURCE_NOTES)) != null) {
			work.setSourceNotes(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_STATUS)) != null) {
			work.setStatus(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_GENRE)) != null) {
			work.setGenre(value);
		}
		
		if ((value = (String) workInfo.get(WorkJAXBSchema.WORK_MEDIUM)) != null) {
			work.setMedium(value);
		}

		if (creatorGroupList != null) {
			work.setCreatorGroupList(creatorGroupList);
		}

		if (publisherGroupList != null) {
			work.setPublisherGroupList(publisherGroupList);
		}
		
		if (useForList != null) {
			work.setUseForList(useForList);
		}

		PoxPayloadOut multipart = new PoxPayloadOut(WorkAuthorityClient.SERVICE_ITEM_PAYLOAD_NAME);
		PayloadOutputPart commonPart = multipart.addPart(work,	MediaType.APPLICATION_XML_TYPE);
		commonPart.setLabel(headerLabel);

		if(logger.isDebugEnabled()){
			logger.debug("to be created, work common ", work, WorksCommon.class);
		}
	 
		return multipart;
	}
    
   /**
     * @param vcsid 				CSID of the authority to create a new work item
     * @param workAuthorityRefName 	The refName for the authority
     * @param workMap 				The properties for the new Work
 	 * @param CreatorGroupList 		Creator group list (values of a repeatable group in the term record)
 	 * @param PublisherGroupList 	Publisher group list (values of a repeatable group in the term record)
 	 * @param UseForList 			'Use For' list (values of a repeatable 'Use for' field in the term record)
     * @param client 				The service client
     * @return 						The CSID of the new item
     */
   public static String createItemInAuthority(String vcsid,
               String workAuthorityRefName, Map<String,String> workMap,
			   CreatorGroupList creatorGroupList, PublisherGroupList publisherGroupList, UseForList useForList,
               WorkAuthorityClient client ) {
       // Expected status code: 201 Created
       int EXPECTED_STATUS_CODE = Response.Status.CREATED.getStatusCode();
       // Type of service request being tested
       ServiceRequestType REQUEST_TYPE = ServiceRequestType.CREATE;
       
       String displayName = workMap.get(WorkJAXBSchema.DISPLAY_NAME);
       String displayNameComputedStr = workMap.get(WorkJAXBSchema.DISPLAY_NAME_COMPUTED);
       boolean displayNameComputed = (displayNameComputedStr==null) || displayNameComputedStr.equalsIgnoreCase("true");
       if( displayName == null ) {
               if(!displayNameComputed) {
                        throw new RuntimeException(
                        "CreateItem: Must supply a displayName if displayNameComputed is set to false.");
               }
       }
       
       if(logger.isDebugEnabled()){
               logger.debug("Import: Create Item: \""+displayName
                               +"\" in workAuthority: \"" + workAuthorityRefName +"\"");
       }
       PoxPayloadOut multipart =
               createWorkInstance(workAuthorityRefName, workMap, creatorGroupList, publisherGroupList, useForList, client.getItemCommonPartName() );
       String newID = null;
       ClientResponse<Response> res = client.createItem(vcsid, multipart);
       try {
                int statusCode = res.getStatus();
        
                if(!REQUEST_TYPE.isValidStatusCode(statusCode)) {
                        throw new RuntimeException("Could not create Item: \""
                                        +workMap.get(WorkJAXBSchema.SHORT_IDENTIFIER)
                                        +"\" in workAuthority: \"" + workAuthorityRefName
                                        +"\" "+ invalidStatusCodeMessage(REQUEST_TYPE, statusCode));
                }
                if(statusCode != EXPECTED_STATUS_CODE) {
                        throw new RuntimeException("Unexpected Status when creating Item: \""
                                        +workMap.get(WorkJAXBSchema.SHORT_IDENTIFIER)
                                        +"\" in workAuthority: \"" + workAuthorityRefName +"\", Status:"+ statusCode);
                }
                newID = extractId(res);
       } finally {
               res.releaseConnection();
       }
 
       return newID;
   }
 
   public static PoxPayloadOut createWorkInstance(
               String commonPartXML, String headerLabel)  throws DocumentException {
       PoxPayloadOut multipart = new PoxPayloadOut(WorkAuthorityClient.SERVICE_ITEM_PAYLOAD_NAME);
       PayloadOutputPart commonPart = multipart.addPart(commonPartXML, MediaType.APPLICATION_XML_TYPE);
       commonPart.setLabel(headerLabel);
 
       if(logger.isDebugEnabled()){
               logger.debug("to be created, work common ", commonPartXML);
       }
 
       return multipart;
   }

	public static String createItemInAuthority(String vcsid,
				String commonPartXML,
				WorkAuthorityClient client) throws DocumentException {
		// Expected status code: 201 Created
		int EXPECTED_STATUS_CODE = Response.Status.CREATED.getStatusCode();
		// Type of service request being tested
		ServiceRequestType REQUEST_TYPE = ServiceRequestType.CREATE;

		PoxPayloadOut multipart =
			createWorkInstance(commonPartXML, client.getItemCommonPartName());
		String newID = null;
		ClientResponse<Response> res = client.createItem(vcsid, multipart);
		try {
				int statusCode = res.getStatus();

				if(!REQUEST_TYPE.isValidStatusCode(statusCode)) {
					throw new RuntimeException("Could not create Item: \""+commonPartXML
										+"\" in workAuthority: \"" + vcsid
										+"\" "+ invalidStatusCodeMessage(REQUEST_TYPE, statusCode));
				}
				if(statusCode != EXPECTED_STATUS_CODE) {
						throw new RuntimeException("Unexpected Status when creating Item: \""+commonPartXML
										+"\" in workAuthority: \"" + vcsid +"\", Status:"+ statusCode);
				}
				newID = extractId(res);
		} finally {
			res.releaseConnection();
		}

		return newID;
	}

	/**
	 * Creates the from xml file.
	 *
	 * @param fileName 	The file name
	 * @return 			New CSID as string
	 * @throws 			Exception the exception
	 */
	private String createItemInAuthorityFromXmlFile(String vcsid, String commonPartFileName,
			WorkAuthorityClient client) throws Exception {
		byte[] b = FileUtils.readFileToByteArray(new File(commonPartFileName));
		String commonPartXML = new String(b);
		return createItemInAuthority(vcsid, commonPartXML, client );
	}    

	/**
	 * Creates the workAuthority ref name.
	 *
	 * @param shortId 		The workAuthority shortIdentifier
	 * @param displaySuffix DisplayName to be appended, if non-null
	 * @return 				The string
	 */
	public static String createWorkAuthRefName(String shortId, String displaySuffix) {
		String refName = "urn:cspace:org.collectionspace.demo:workauthority:name("
						+shortId+")";
		if(displaySuffix!=null&&!displaySuffix.isEmpty())
			refName += "'"+displaySuffix+"'";
		return refName;
	}

	/**
	 * Creates the work ref name.
	 *
	 * @param workAuthRefName 	The workAuthority ref name
	 * @param shortId 			The work shortIdentifier
	 * @param displaySuffix 	DisplayName to be appended, if non-null
	 * @return 					The string
	 */
	public static String createWorkRefName(String workAuthRefName, String shortId, String displaySuffix) {
		String refName = workAuthRefName+":work:name("+shortId+")";
		if(displaySuffix!=null&&!displaySuffix.isEmpty())
			refName += "'"+displaySuffix+"'";
		return refName;
	}

	public static String extractId(ClientResponse<Response> res) {
		MultivaluedMap<String, Object> mvm = res.getMetadata();
		String uri = (String) ((ArrayList<Object>) mvm.get("Location")).get(0);
		if(logger.isDebugEnabled()){
			logger.debug("extractId:uri=" + uri);
		}
		String[] segments = uri.split("/");
		String id = segments[segments.length - 1];
		if(logger.isDebugEnabled()){
			logger.debug("id=" + id);
		}
		return id;
	}

	/**
	 * Returns an error message indicating that the status code returned by a
	 * specific call to a service does not fall within a set of valid status
	 * codes for that service.
	 *
	 * @param serviceRequestType  	A type of service request (e.g. CREATE, DELETE).
	 *
	 * @param statusCode  			The invalid status code that was returned in the response,
	 *                    			from submitting that type of request to the service.
	 *
	 * @return 						An error message.
	 */
	public static String invalidStatusCodeMessage(ServiceRequestType requestType, int statusCode) {
		return "Status code '" + statusCode + "' in response is NOT within the expected set: " +
				requestType.validStatusCodesAsString();
	}



	/**
	 * Produces a default displayName from one or more supplied field(s).
	 * @see WorkAuthorityDocumentModelHandler.prepareDefaultDisplayName() which
	 * duplicates this logic, until we define a service-general utils package
	 * that is neither client nor service specific.
	 * @param 	workName 
	 * @return 	A display name
	 */
	public static String prepareDefaultDisplayName(String workName) {
		StringBuilder newStr = new StringBuilder();
		newStr.append(workName);
		return newStr.toString();
	}

}