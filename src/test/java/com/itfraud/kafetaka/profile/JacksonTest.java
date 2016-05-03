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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Space Ghost
 */
public class JacksonTest {
    
    @Test
    public void testWriteObjectToString() throws JsonProcessingException {
        String profileUserName = "space ghost";
        String profileEmail = "space@ghost.com";
        String profilePreference = "first class";
        Profile profile = new Profile.ProfileBuilder()
                .userName(profileUserName)
                .email(profileEmail)
                .preference(profilePreference)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        assertThat("The returned json is not the expected one for a profile", 
                mapper.writeValueAsString(profile), 
                is("{\"userName\":\"space ghost\",\"email\":\"space@ghost.com\",\"preference\":\"first class\"}"));
    }
    
    @Test
    public void testWriteStringToObject() throws IOException {
        String profileJson = "{\"userName\":\"space ghost\",\"email\":\"space@ghost.com\",\"preference\":\"first class\"}";
        String profileUserName = "space ghost";
        String profileEmail = "space@ghost.com";
        String profilePreference = "first class";
        ObjectMapper mapper = new ObjectMapper();
        Profile retrievedProfile = mapper.readValue(profileJson, Profile.class);
        assertThat("The returned profile has the proper user name", 
                (retrievedProfile.getUserName()), is(profileUserName));
        assertThat("The returned profile has the proper email", 
                (retrievedProfile.getEmail()), is(profileEmail));
        assertThat("The returned profile has the proper preference", 
                (retrievedProfile.getPreference()), is(profilePreference));
    }

}
