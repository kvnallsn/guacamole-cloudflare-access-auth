package org.apache.guacamole.auth;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;

import com.google.inject.Inject;

public class ConfigurationService {
    /**
     * The Guacamole server environment
     */
    @Inject
    private Environment environment;

    /**
     * Returns the name of the cloudflare access team
     * 
     * @return
     *         The team name associated with this cloudflare access request
     * 
     * @throws GuacamoleException
     *                            If guacamole.properties cannot be parsed
     */
    public String getCloudflareAccessTeam() throws GuacamoleException {
        return environment.getRequiredProperty(
                CloudflareAuthenticationGuacamoleProperties.CLOUDFLARE_ACCESS_TEAM);
    }

    /**
     * Returns the audience of the cloudflare access team
     * 
     * @return
     *         The `aud` value to validate against in the JWT
     * 
     * @throws GuacamoleException
     *                            If guacamole.properties cannot be parsed
     */
    public String getCloudflareAccessAudience() throws GuacamoleException {
        return environment.getRequiredProperty(
                CloudflareAuthenticationGuacamoleProperties.CLOUDFLARE_ACCESS_AUDIENCE);
    }

    /**
     * Returns the name of the roles claim use to build user groups
     * 
     * @return
     *         The name of the roles claim in the JWT
     * 
     * @throws GuacamoleException
     *                            If guacamole.properties cannot be parsed
     */
    public String getCloudflareAccessRolesClaim() throws GuacamoleException {
        return environment.getProperty(
                CloudflareAuthenticationGuacamoleProperties.CLOUDFLARE_ACCESS_ROLES_CLAIM,
                "roles");
    }
}
