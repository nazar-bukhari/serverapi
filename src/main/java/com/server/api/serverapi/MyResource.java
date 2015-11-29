package com.server.api.serverapi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.server.api.service.ApiService;
import csci4311.chat.UserCommand;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	ApiService service = new ApiService();
    
	@GET
	@Path("/users")
	@Produces(MediaType.TEXT_PLAIN)
	public String LoggedInUsers(){
		
		return service.getLoggedInUsers();
	}
	
    @GET
    @Path("/groups")
    @Produces(MediaType.TEXT_PLAIN)
    public String groups(){

    	return service.getAllGroup();
    }
    
    @GET
    @Path("/group/{groupId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String userByGroup(@PathParam ("groupId") long groupId){
//    	System.out.println("user");
    	return service.getUserByGroup(groupId);
    }   
    
    @POST
    @Path("/group/{groupId}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String addUser(@FormParam("user") String user,@PathParam ("groupId")long groupId){
    	
//    	return user;
    	return service.createUser(user,groupId);
    }
    
//    @POST
//    @Path("/message")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
////    @Produces(MediaType.TEXT_PLAIN)
//    public String postMessage(@FormDataParam("file") InputStream fileInputStream,
//            @FormDataParam("file") FormDataContentDisposition fileMetaData){
//    	
//    	System.out.println("fileDetail="+fileMetaData.getFileName());
//
//    	return fileMetaData.getFileName();
////    	return service.createUser(user,groupId);
//    }
    
    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadPdfFile(@FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException 
    {
    	System.out.println("******************* pdf *******************************");
//    	http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
//    	http://stackoverflow.com/questions/5772225/trying-to-upload-a-file-to-a-jax-rs-jersey-server
    	return fileDetail.getFileName();
    	
    }
    
    @GET
    @Path("/messages/{groupId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGroupMessage(@PathParam ("groupId")long groupId){
    	
    	return service.getGroupMessage(groupId);
    }
    
    @GET
    @Path("/messages/@{userName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPersonalMessage(@PathParam ("userName") String userName){
    	
    	return service.getUserMessage(userName);
    }
    
    @DELETE
    @Path("/group/{groupID}/{userName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteUser(@PathParam ("groupID") long groupId,@PathParam ("userName") String userName){
//    	System.out.println("delete");
    	return service.removeUser(groupId, userName);
    }
    
}
