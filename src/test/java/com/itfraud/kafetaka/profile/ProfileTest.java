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

import javax.validation.ValidationException;
import org.junit.Test;

/**
 * Tests Profile class constraints.
 * 
 * @author Space Ghost
 */
public class ProfileTest {
    
    @Test(expected = ValidationException.class)
    public void testMandatoryId() {
        new Profile.ProfileBuilder()
                .email("space@ghost.com")
                .preference("first class")
                .build();
    }
    
    @Test(expected = ValidationException.class)
    public void testMandatoryEmail() {
        new Profile.ProfileBuilder()
                .userName("space ghost")
                .preference("first class")
                .build();
    }
    
    @Test(expected = ValidationException.class)
    public void testMandatoryPreference() {
        new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .build();
    }
    
    @Test(expected = ValidationException.class)
    public void testEmailFormat() {
        new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("email")
                .preference("first class")
                .build();
    }
    
    @Test(expected = ValidationException.class)
    public void testEmailNotEmpty() {
        new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("")
                .preference("first class")
                .build();
    }
    
    @Test(expected = ValidationException.class)
    public void testPreferenceNotEmpty() {
        new Profile.ProfileBuilder()
                .userName("space ghost")
                .email("space@ghost.com")
                .preference("")
                .build();
    }

}
