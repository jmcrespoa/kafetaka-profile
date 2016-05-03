/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>*/

package com.itfraud.kafetaka.profile;

import static com.itfraud.kafetaka.profile.InitProfileServiceContextListener.PROFILE_REPOSITORY_CONTEXT_ATTR_NAME;
import static com.itfraud.kafetaka.profile.ProfileResource.PROFILE_PATH;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;

/**
 * Defines all Profile CRUD endpoints.
 * 
 * @author Space Ghost
 */
@Path(PROFILE_PATH)
public class ProfileResource {
    
    public static final String PROFILE_PATH = "/profile";
    
    private ProfileRepository profileRepository;
    
    @Context 
    public void init(ServletContext servletContext) {
        this.profileRepository = 
                (ProfileRepository)servletContext.getAttribute(PROFILE_REPOSITORY_CONTEXT_ATTR_NAME);
    }
    
    @POST
    @Consumes(APPLICATION_JSON)
    public Response post(Profile profile) {
        profile.validate();
        this.profileRepository.persist(profile);
        return Response.created(URI.create("http://localhost:9998/order/1")).build();
    }
    
    @GET @Path("/{userName}")
    @Produces(APPLICATION_JSON)
    public Response getProfile(@PathParam("userName") String userName) {
        Profile profile = this.profileRepository.getProfile(userName);
        return Response.ok(profile).build();
    }
    
    @PUT
    @Consumes(APPLICATION_JSON)
    public Response put(Profile profile) {
        profile.validate();
        this.profileRepository.merge(profile);
        return Response.noContent().build();
    }
    
    @DELETE @Path("/{userName}")
    public Response delete(@PathParam("userName") String userName) {
        this.profileRepository.remove(userName);
        return Response.noContent().build();
    }

}
