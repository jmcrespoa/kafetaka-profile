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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class represents a, let me see... profile? Duh!!!
 *
 * @author Space Ghost
 */
public class Profile {

    @NotNull
    @NotEmpty
    private String userName;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    @NotNull
    @NotEmpty
    private String preference;

    /**
     * Returns this Profile user name, that must be its unique identifier.
     *
     * @return An String representing this Profile user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets up this Profile user name. It will be its unique identifier.
     *
     * @param userName This Profile userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns this Profile email.
     *
     * @return A String representing this Profile email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets up this Profile email.
     *
     * @param email This Profile email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns this Profile preference.
     *
     * @return A String representing this Profile preference.
     */
    public String getPreference() {
        return preference;
    }

    /**
     * Sets up this Profile preference.
     *
     * @param preference This Profile preference
     */
    public void setPreference(String preference) {
        this.preference = preference;
    }

    /**
     * Validates this Profile constraints.
     *
     * @throws ValidationException If one or more constraints are violated.
     */
    public void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Profile>> constraintViolations = validator.validate(this);
        if (!constraintViolations.isEmpty()) {
            String messages = constraintViolations.stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(messages);
        }
    }

    /**
     * Two Profiles are the same if they have the same user name.
     *
     * @param obj The Profile we are going to compare with this one.
     * @return true if both Profiles have the same user name, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        return true;
    }

    /**
     * This profile hashCode is generated from its unique identifier.
     *
     * @return this Profile hash code
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    @Override
    public String toString() {
        return "{userName : " + userName + ", email : " + email + ", preference : " + preference + '}';
    }

    /**
     * A builder to create Profiles elegantly.
     */
    public static class ProfileBuilder {

        private String userName;
        private String email;
        private String preference;

        public ProfileBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public ProfileBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ProfileBuilder preference(String preference) {
            this.preference = preference;
            return this;
        }

        /**
         * Return a new Profile instance.
         *
         * @return A new Profile instance
         * @throws ValidationException If one or more Profile constraints are
         * violated.
         */
        public Profile build() {
            Profile profile = new Profile();
            profile.setUserName(this.userName);
            profile.setEmail(this.email);
            profile.setPreference(this.preference);
            profile.validate();
            return profile;
        }

    }

}
