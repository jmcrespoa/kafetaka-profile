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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static org.mockito.Mockito.*;

/**
 * This is a mock ServletContextListener implementation for the JerseyTests.
 *
 * Using it those test do not need to read configuration files from disk.
 *
 * @author Space Ghost
 */
public class MockInitProfileServiceContextListener implements ServletContextListener {

    //This static member makes the mock accesible from the test classes
    static final ProfileRepository mockProfileRepository = mock(ProfileRepository.class);
    public MockInitProfileServiceContextListener() {}

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(
                PROFILE_REPOSITORY_CONTEXT_ATTR_NAME,
                mockProfileRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
