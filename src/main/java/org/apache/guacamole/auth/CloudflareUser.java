package org.apache.guacamole.auth;

import org.apache.guacamole.net.auth.AbstractAuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;

public class CloudflareUser extends AbstractAuthenticatedUser {

    /**
     * The credentials passed when this user was authenticated
     */
    private Credentials credentials;

    /**
     * The authentication provider used to create this user
     */
    @Inject
    private AuthenticationProvider authProvider;

    public void init(DecodedJWT token, Credentials credentials) {
        this.credentials = credentials;
        setIdentifier(token.getClaim("email").asString());
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authProvider;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
}
