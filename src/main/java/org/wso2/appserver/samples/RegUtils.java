package org.wso2.appserver.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.api.Association;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.api.RegistryException;


public class RegUtils {
	
	
public static List<Resource> getResourceList(Registry registry, String pathResource) throws org.wso2.carbon.registry.api.RegistryException {
		List<Resource> result = new ArrayList<Resource>();
		Resource resource = registry.get(pathResource);
 
		if (resource instanceof Collection) {
			Object content = resource.getContent();
			for (Object path : (Object[])content) {
				result.addAll(getResourceList(registry,(String)path));
			}
		} else if (resource instanceof Resource){
			result.add(resource);
		} 
		return result;
	}  // end of method getResourceList
	
public static String outputResources(List<Resource> paths,Registry oRegistry) throws RegistryException {
	String retVal = null;
	
	 for (Resource oResource : paths) {
			// we've got all the services here
		 	System.out.println("-------");
		 	System.out.println("resource path:"+oResource.getPath());
		 	System.out.println("description:"+oResource.getDescription());
		 	System.out.println("media type:"+oResource.getMediaType());
		 	System.out.println("created time:"+oResource.getCreatedTime());
		 	System.out.println("last modified time:"+oResource.getLastModified());
		 	Object oObjectR = oResource.getContent();
		 	String stRegValue = null;
		 	if (oObjectR.getClass() == String.class )
				stRegValue = (String)oObjectR;
			else
				stRegValue = new String((byte[])oObjectR);
		 	System.out.println("content:"+stRegValue);

	    	Properties props = oResource.getProperties();
		    for (Object prop : props.keySet()) {
		    	//		System.out.println(prop + " - " + props.get(prop));
				String stPropName = (String)prop;
				String stPropertyValue = oResource.getProperty(stPropName);
				System.out.println(stPropName+":"+stPropertyValue);
			}

		    Association[] associations = oRegistry.getAssociations(oResource.getPath(), "Documentation");
		    for (Association association : associations) {
				System.out.println(association.getAssociationType());
			}
		}
	 
	 return retVal;
	}  // end of path outputresources

public static void DumpRegistryToSysOut(Registry oRegistry,String path) throws RegistryException {
	List<Resource> oList = getResourceList(oRegistry,path);
	outputResources(oList, oRegistry);
}  // end of method DumpRegustrytoSysOut

}  // end of class