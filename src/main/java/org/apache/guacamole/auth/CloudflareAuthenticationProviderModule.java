package org.apache.guacamole.auth;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.AuthenticationProvider;

import com.google.inject.AbstractModule;

public class CloudflareAuthenticationProviderModule extends AbstractModule {
    /**
     * Guacamole server environment
     */
    private final Environment environment;
   
    /**
     * A reference to the CloudflareAuthenticationProvider on behalf of which this
     * module has configured injection
     */
    private final AuthenticationProvider authProvider;

    /**
     * Creates a new Cloudflare Access authentication provider module which configures
     * injection for the CloudflareAuthenticationProvider.
     * 
     * @param authProvider
     *      The AuthenticationProvider for which injection is being configured
     * 
     * @throws GuacamoleException
     *  If an error occurs while retrieving the Guacamole server environment
     */
    public CloudflareAuthenticationProviderModule(AuthenticationProvider authProvider)
        throws GuacamoleException {
            // Get local environment
            this.environment = LocalEnvironment.getInstance();

            // Store associated auth provider
            this.authProvider = authProvider;
        }

    @Override
    public void configure() {
        // Bind core implementations of guacamole-ext classes
        bind(AuthenticationProvider.class).toInstance(authProvider);
        bind(Environment.class).toInstance(environment);

        // bind cloudflare specific classes
        bind(ConfigurationService.class);
    }
}
