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

import static com.itfraud.kafetaka.profile.ProfileResource.PROFILE_PATH;
import java.net.URI;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static com.itfraud.kafetaka.profile.MockInitProfileServiceContextListener.*;
import javax.persistence.EntityNotFoundException;
import org.junit.Test;

/**
 * Tests the kafetaka Profile REST end point.
 * 
 * @author Space Ghost
 */
public class ProfileResourceTest extends JerseyTest {
    
    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext.builder(
                new ResourceConfig())
                .addListener(MockInitProfileServiceContextListener.class) // This mock avoids reading configurations from files
                .initParam(
                      ServerProperties.PROVIDER_CLASSNAMES,
                              ProfileResource.class.getName() + ";" + 
                              ValidationExceptionMapper.class.getName() + ";" + 
                              EntityNotFoundExceptionMapper.class.getName() + ";" + 
                              IllegalArgumentExceptionMapper.class.getName())
                .build();
    }
    
    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }
 
    @Test
    public void testPostRequest() {
        Profile profile = new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .preference("first class")
                .build();
        
        Entity<Profile> profileData = 
                Entity.entity(profile, MediaType.APPLICATION_JSON);
        Response postResponse = target(PROFILE_PATH).request()
                .post(profileData, Response.class);
        
        verify(mockProfileRepository).persist(profile);
        assertThat("The post response has a 201 code", 
                postResponse.getStatus(), is(201));
        assertThat("The post response has location header pointing to the created resource", 
                postResponse.getLocation(), is(equalTo(URI.create("http://localhost:9998/order/1"))));
    }
    
    /**
     * If the endpoint gets an invalid Profile, that is, a Profile that does not comply
     * with at least one of its constraints, it returns a 400.
     * 
     * In this test, the profile does not include a user name, and user name is mandatory.
     */
    @Test
    public void testPostRequestWithInvalidProfile() {
        // A profile without a preference
        // Remember, jersey won't use our fancy builder, but the setters!!!
        Profile profile = new Profile();
        profile.setEmail("space@ghost.com");
        profile.setPreference("first class");
        
        Entity<Profile> profileData = 
                Entity.entity(profile, MediaType.APPLICATION_JSON);
        Response postResponse = target(PROFILE_PATH).request()
                .post(profileData, Response.class);
        
        assertThat("The post response has a 400 code", 
                postResponse.getStatus(), is(400));
    }
    
    @Test
    public void testGetProfileRequest() {
        String profileUserName = "space ghost";
        String profileEmail = "space@ghost.com";
        String profilePreference = "first class";
        Profile profile = new Profile.ProfileBuilder()
                .userName(profileUserName)
                .email(profileEmail)
                .preference(profilePreference)
                .build();
        when(mockProfileRepository.getProfile(profileUserName)).thenReturn(profile);
        Response getResponse = target(PROFILE_PATH + "/" + profileUserName).request()
                .get(Response.class);
        Profile retrievedProfile = getResponse.readEntity(Profile.class);
        
        assertThat("The get response has a 200 code", 
                getResponse.getStatus(), is(200));
        assertThat("The returned profile has the proper user name", 
                (retrievedProfile.getUserName()), is(profileUserName));
        assertThat("The returned profile has the proper email", 
                (retrievedProfile.getEmail()), is(profileEmail));
        assertThat("The returned profile has the proper preference", 
                (retrievedProfile.getPreference()), is(profilePreference));
    }
    
    @Test
    public void testTryingToGetNonExistingProfile() {
        String nonExistingProfileUserName = "nonExisting";
        when(mockProfileRepository.getProfile(nonExistingProfileUserName)).thenThrow(EntityNotFoundException.class);
        Response getResponse = target(PROFILE_PATH + "/" + nonExistingProfileUserName).request()
                .get(Response.class);
        
        assertThat("The post response has a 404 code", 
                getResponse.getStatus(), is(404));
    }
    
    @Test
    public void testPutRequest() {
        Profile profile = new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .preference("first class")
                .build();
        
        Entity<Profile> profileData = 
                Entity.entity(profile, MediaType.APPLICATION_JSON);
        Response putResponse = target(PROFILE_PATH).request()
                .put(profileData, Response.class);
        
        verify(mockProfileRepository).merge(profile);
        assertThat("The post response has a 204 code", 
                putResponse.getStatus(), is(204));
    }
    
    @Test
    public void testPutInvalidRequest() {
        // A profile without a preference
        // Remember, jersey won't use our fancy builder, but the setters!!!
        Profile profile = new Profile();
        profile.setEmail("space@ghost.com");
        profile.setPreference("first class");
        
        Entity<Profile> profileData = 
                Entity.entity(profile, MediaType.APPLICATION_JSON);
        Response putResponse = target(PROFILE_PATH).request()
                .put(profileData, Response.class);
        
        assertThat("The post response has a 400 code", 
                putResponse.getStatus(), is(400));
    }
    
    @Test
    public void testPutNonExisitingProfile() {
        Profile profile = new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .preference("first class")
                .build();
        
        Entity<Profile> profileData = 
                Entity.entity(profile, MediaType.APPLICATION_JSON);
        doThrow(IllegalArgumentException.class).when(mockProfileRepository).merge(profile);
        Response putResponse = target(PROFILE_PATH).request()
                .put(profileData, Response.class);
        
        assertThat("The post response has a 404 code", 
                putResponse.getStatus(), is(404));
    }
    
    @Test
    public void testDeleteRequest() {
        String profileUserName = "space ghost";
        Response deleteResponse = target(PROFILE_PATH+ "/" + profileUserName).request()
                .delete();
        
        verify(mockProfileRepository).remove(profileUserName);
        assertThat("The post response has a 204 code", 
                deleteResponse.getStatus(), is(204));
    }
    
    @Test
    public void testDeleteNonExisitingProfile() {
        String nonExistingProfileUserName = "non existing";
        doThrow(IllegalArgumentException.class).when(mockProfileRepository).remove(nonExistingProfileUserName);
        Response deleteResponse = target(PROFILE_PATH+ "/" + nonExistingProfileUserName).request()
                .delete();
    
        assertThat("The post response has a 404 code", 
                deleteResponse.getStatus(), is(404));
    }

}
