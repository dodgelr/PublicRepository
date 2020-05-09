package lyon.future.LYON_future4;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
//import javax.servlet.http.*;

//import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Root resource (exposed at "businesscard" path)
 */
@Path("businesscard")
public class businesscardService {
	
    
    @GET
    @Path("GetById")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@QueryParam("id") String id) {
    	String[] ID = id.split(":");
    	String ICD;
    	String entID;
    	//HttpServletResponse response;
		//PrintWriter printWriter =  response.getWriter();

    	if (ID.length != 2) {
    		//printWriter.write("Invalid ID.");
    		return "Invalid ID";
    	}else {
    		ICD = ID[0];
    		entID = ID[1];
    		Businesscard b = Businesscard.GetById(ICD, entID);
    		Gson gson = new Gson();
    		String bCardJSON =  gson.toJson(b);
    		return bCardJSON;
    	}
    }
    
    
    @GET
    @Path("SearchByName") 
    @Produces(MediaType.APPLICATION_JSON)
    public String SearchByName(@QueryParam("name")String name) {
    	ArrayList<Businesscard.nameEntity> nameEnts = Businesscard.SearchByName(name);
		Gson gson = new Gson();
		String bCardJSON =  gson.toJson(nameEnts);
		return bCardJSON;
    } 
    

    @GET
    @Path("GetByName")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetByName(@QueryParam("name") String name) {	
    	Businesscard b = Businesscard.getByName(name);
		Gson gson = new Gson();
		String bCardJSON =  gson.toJson(b);
		return bCardJSON;
    } 
    	
    	      
}
