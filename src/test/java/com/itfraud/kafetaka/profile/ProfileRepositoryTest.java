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

import org.junit.Test;
import static org.mockito.Mockito.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author Space Ghost
 */
public class ProfileRepositoryTest {
    
    private final Profile profile = new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .preference("first class")
                .build();
    private final JedisPool mockJedisPool = mock(JedisPool.class);
    private final Jedis mockJedis = mock(Jedis.class);
    private final ProfileRepository profileRepository = new ProfileRepository(mockJedisPool);
    
    @Test
    public void persistAProfile() {
        when(mockJedisPool.getResource()).thenReturn(mockJedis);
        when(mockJedis.setnx(profile.getUserName(), "{\"userName\":\"space ghost\",\"email\":\"space@ghost.com\",\"preference\":\"first class\"}")).thenReturn(1L);
        profileRepository.persist(profile);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistAnExistingProfile() {
        when(mockJedisPool.getResource()).thenReturn(mockJedis);
        when(mockJedis.setnx(profile.getUserName(), "{\"userName\":\"space ghost\",\"email\":\"space@ghost.com\",\"preference\":\"first class\"}")).thenReturn(0L);
        profileRepository.persist(profile);
    }
    
    @Test
    public void mergeAProfile() {
        when(mockJedisPool.getResource()).thenReturn(mockJedis);
        profileRepository.merge(profile);
        verify(mockJedis).set(profile.getUserName(), "{\"userName\":\"space ghost\",\"email\":\"space@ghost.com\",\"preference\":\"first class\"}", "XX");
    }
    
    /*@Test(expected = IllegalArgumentException.class)
    public void mergeANonExistingProfile() {
        when(mockJedisPool.getResource()).thenReturn(mockJedis);
        when(mockJedis.multi()).thenReturn(mockTransaction);
        when(mockTransaction.exists(profile.getUserName())).thenReturn(mockResponse);
        when(mockResponse.get()).thenReturn(false);
        profileRepository.merge(profile);
    }*/

}
