package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CategoryDto;
import it.corso.exceptions.ObjectNotFoundException;
import it.corso.exceptions.UnauthorizedException;
import it.corso.service.CategoryService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GET
	@Path("/get/all")
	public Response getAllCategories() {
		try {
			List<CategoryDto> categoriesDto = categoryService.getAll();
			return Response.status(Response.Status.OK)
	                .entity(categoriesDto)
	                .build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteCategory(@PathParam("id") Integer id) {
		try {
			categoryService.delete(id);
			return Response.status(Response.Status.OK).build();
		} catch (ObjectNotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch(UnauthorizedException e) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
	}
	
}
