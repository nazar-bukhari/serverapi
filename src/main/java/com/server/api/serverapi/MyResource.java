package com.server.api.serverapi;
import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.server.api.service.ApiService;

import csci4311.chat.UserCommand;

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
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String addUser(String user,@PathParam ("groupId")long groupId){
    	 	
    	return service.createUser(user,groupId);
    }
    
    @GET
    @Path("/messages/{groupId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGroupMessage(@PathParam ("groupId")long groupId){
    	
    	return service.getGroupMessage(groupId);
    }
    
    @DELETE
    @Path("/group/{groupID}/{userName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteUser(@PathParam ("groupID") long groupId,@PathParam ("userName") String userName){
//    	System.out.println("delete");
    	return service.removeUser(groupId, userName);
    }
    
}
