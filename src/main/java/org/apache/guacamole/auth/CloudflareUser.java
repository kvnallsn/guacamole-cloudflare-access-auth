package org.apache.guacamole.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.guacamole.net.auth.AbstractAuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;

public class CloudflareUser extends AbstractAuthenticatedUser {

    /**
     * The credentials passed when this user was authenticated
     */
    private Credentials credentials;

    /**
     * The list of roles/groups this user is a member of.
     */
    private HashSet<String> roles;

    /**
     * The authentication provider used to create this user
     */
    @Inject
    private AuthenticationProvider authProvider;

    @SuppressWarnings("unchecked")
    public void init(DecodedJWT token, Credentials credentials, String roleClaimName) {
        this.credentials = credentials;
        this.roles = new HashSet<>();

        Claim customClaims = token.getClaim("custom");
        if (!customClaims.isMissing()) {
            Object maybeArray = customClaims.asMap().get(roleClaimName);
            if (maybeArray.getClass().isArray() || maybeArray instanceof Collection) {
                this.roles.addAll((Collection<String>) maybeArray);
            }
        }

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

    @Override
    public Set<String> getEffectiveUserGroups() {
        return roles;
    }
}
