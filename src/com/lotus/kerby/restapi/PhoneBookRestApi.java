package com.lotus.kerby.restapi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotus.kerby.dao.OraclePhoneBook;
import com.lotus.kerby.domain.Company;
import com.lotus.kerby.domain.Contact;





@Path("/phonebook")
public class PhoneBookRestApi {
		private OraclePhoneBook oraclePhoneBook = OraclePhoneBook.getInstance();

	
	
	@GET
	@Produces("application/json")
	public Response list() throws JSONException, JsonGenerationException, JsonMappingException, IOException, SQLException {
		List<Contact> contact= oraclePhoneBook.list();
		List<String> contactAndCompany = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		String response = "{}"; 
		if(!contact.isEmpty()) {
			for(Contact contacts: contact){
				contactAndCompany.add(contacts + ", " + oraclePhoneBook.getCompany(contacts));
					
			}
			response = mapper.writeValueAsString(contactAndCompany) ;
			
		}
		return Response.status(200).entity(response).build();
	}

	@Path("/show/{name}")
	@GET
	@Produces("application/json")
	public Response show(@PathParam("name") String name) throws JSONException, JsonGenerationException, JsonMappingException, IOException, SQLException {
		Contact contact = oraclePhoneBook.getByName(name);
		ObjectMapper mapper = new ObjectMapper();
		String response = "{}";
		
		if(contact != null) {
			response = mapper.writeValueAsString(contact + ", " + oraclePhoneBook.getCompany(contact));
		}
		return Response.status(200).entity(response).build();
	}
	
	@Path("/search/{keyword}")
	@GET
	@Produces("application/json")
	public Response search(@PathParam("keyword") String keyword) throws JSONException, JsonGenerationException, JsonMappingException, IOException, SQLException {
		
		List<Contact> contact = oraclePhoneBook.search(keyword);
		List<String> contactAndCompany = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		String response = "{}";
		if(!contact.isEmpty()){
			for(Contact contacts: contact){
				contactAndCompany.add(contacts + ", " + oraclePhoneBook.getCompany(contacts));
			}
			response = mapper.writeValueAsString(contactAndCompany);
		}
		return Response.status(200).entity(response).build();
	}
	@Path("/delete/{name}")
	@DELETE
	@Produces("application/json")
	public Response delete(@PathParam("name") String name) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Contact contact = oraclePhoneBook.getByName(name);
			oraclePhoneBook.delete(contact);
			jsonObject.put("success", true);
		} catch (Exception e) {
			jsonObject.put("success", false);
			jsonObject.put("errorMessage", "cannot delete contact");
		}

		return Response.status(200).entity(jsonObject.toString()).build();
	}

	@POST
	@Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response save(@FormParam("name") String name, @FormParam("birthday") String birthdate,
			@FormParam("isVIP") String isVip, @FormParam("companyCode") String companyCode, @FormParam("contactNumber") String contactNumber) throws JSONException, SQLException {
		JSONObject jsonObject = new JSONObject();
		Date birthday = null;
		Company company = oraclePhoneBook.getCompanyByCode(companyCode);
		Long companyId = null;
		boolean isVIP = false;
		if(name == null || contactNumber == null) {
			jsonObject.put("error", "bad request, name & contact number parameter is required");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		
		try {
			if(!birthdate.isEmpty()){
				birthday = new Date(birthdate);
			}
			if(!companyCode.isEmpty()){
				companyId = company.getId();
			}
			if(!isVip.isEmpty()){
				isVIP = Boolean.valueOf(isVip);
			}
			Contact contact = new Contact(name, birthday, isVIP, companyId, contactNumber);
			Contact existingContact = oraclePhoneBook.getByName(name);
			for(Contact contacts: oraclePhoneBook.list()){
				if(contacts.getId() == (existingContact.getId())){
					oraclePhoneBook.update(contact);
					jsonObject.put("update success", true);
					return Response.status(200).entity(jsonObject.toString()).build();
				}
			}
			
			oraclePhoneBook.create(contact);
			jsonObject.put("create success", true);
		} catch (Exception e) {
			jsonObject.put("success", false);
			jsonObject.put("errorMessage", "Cannot add contact.");
		}

		return Response.status(200).entity(jsonObject.toString()).build();
	}

}
