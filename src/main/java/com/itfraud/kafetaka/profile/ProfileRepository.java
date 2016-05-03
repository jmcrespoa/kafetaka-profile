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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Provides basic interfaces to a repository of Profiles.
 *
 * @author Space Ghost
 */
public class ProfileRepository {

    private final JedisPool pool;

    public ProfileRepository(JedisPool pool) {
        this.pool = pool;
    }

    public void persist(Profile profile) {
        try (Jedis jedis = pool.getResource()) {
            ObjectMapper mapper = new ObjectMapper();
            if (jedis.setnx(profile.getUserName(), mapper.writeValueAsString(profile)) 
                    == 0)
                throw new IllegalArgumentException("You are trying to persist a profile that already exists!");
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(
                    "Something went wrong while trying to convert the profile: " + 
                            profile + " to a json String", ex);
        }
    }

    public Profile getProfile(String userName) {
        try (Jedis jedis = pool.getResource()) {
            String jsonProfile = jedis.get(userName);
            if (jsonProfile == null)
                throw new IllegalArgumentException("You are trying to retrieve a profile that does not exist!");
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonProfile, Profile.class);
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "Something went wrong while trying to get a json String for the user name: " + 
                            userName, ex);
        }
    }

    public void merge(Profile profile) {
        try (Jedis jedis = pool.getResource()) {
            ObjectMapper mapper = new ObjectMapper();
            jedis.set(profile.getUserName(), mapper.writeValueAsString(profile), "XX");
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(
                    "Something went wrong while trying to convert the profile: " + 
                            profile + " to a json String", ex);
        }
    }

    public void remove(String userName) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(userName);
        }
    }

}
