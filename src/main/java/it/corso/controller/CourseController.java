package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CourseDto;
import it.corso.dto.CourseUpdateDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.model.Course;
import it.corso.service.CourseService;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@JWTTokenNeeded
@Secured(role = "Admin")
@Path("/course")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
    @GET
    @Path("/{id}")
    public Response getCourseById(@PathParam("id") int id) {
        try {
            CourseDto courseDto = courseService.getCourseDTOById(id);
            return Response.status(Response.Status.OK).entity(courseDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/get/all")
    public Response getCourses() {
        try {
            List<CourseDto> courses = courseService.getCourses();
            return Response.status(Response.Status.OK).entity(courses).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/create")
    public Response createCourse(@Valid CourseDto courseDto) {
        try {
            Course createdCourse = courseService.createCourse(courseDto);
            return Response.status(Response.Status.OK).entity(createdCourse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/update")
    public Response updateCourse(@Valid CourseUpdateDto courseUpdateDto) {
        try {
            courseService.updateCourse(courseUpdateDto);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteCourse(@PathParam("id") int id) {
        try {
            courseService.deleteCourse(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
