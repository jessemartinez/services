/**
 *  This document is a part of the source code and related artifacts
 *  for CollectionSpace, an open source collections management system
 *  for museums and related institutions:

 *  http://www.collectionspace.org
 *  http://wiki.collectionspace.org

 *  Copyright 2009 University of California at Berkeley

 *  Licensed under the Educational Community License (ECL), Version 2.0.
 *  You may not use this file except in compliance with this License.

 *  You may obtain a copy of the ECL 2.0 License at

 *  https://source.collectionspace.org/collection-space/LICENSE.txt

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.collectionspace.services.work;

import org.collectionspace.services.client.WorkAuthorityClient;
import org.collectionspace.services.common.vocabulary.AuthorityResource;
import org.collectionspace.services.work.nuxeo.WorkDocumentModelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path(WorkAuthorityClient.SERVICE_PATH)
@Consumes("application/xml")
@Produces("application/xml")
public class WorkAuthorityResource 
	extends AuthorityResource<WorkauthoritiesCommon, 
								WorkDocumentModelHandler> {

    private final static String workAuthorityServiceName = "workauthorities";
	private final static String WORKAUTHORITIES_COMMON = "workauthorities_common";
    
    private final static String workServiceName = "works";
	private final static String WORKS_COMMON = "works_common";
    
    final Logger logger = LoggerFactory.getLogger(WorkAuthorityResource.class);

    public WorkAuthorityResource() {
		super(WorkauthoritiesCommon.class, WorkAuthorityResource.class,
				WORKAUTHORITIES_COMMON, WORKS_COMMON);
    }

    @Override
    public String getServiceName() {
        return workAuthorityServiceName;
    }

    public String getItemServiceName() {
        return workServiceName;
    }

    @Override
    public Class<WorkauthoritiesCommon> getCommonPartClass() {
    	return WorkauthoritiesCommon.class;
    }
}