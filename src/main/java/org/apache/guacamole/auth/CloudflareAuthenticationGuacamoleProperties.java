package org.apache.guacamole.auth;

import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * Provides properties required for the user of the Cloudflare
 * Access authentication provider. These properties will be read
 * from guacamole.properties when this authentication provider is used.
 */
public class CloudflareAuthenticationGuacamoleProperties {
     /**
      * This class should not be instantiated
      */
     private CloudflareAuthenticationGuacamoleProperties() {
     }

     /**
      * The name of the Cloudflare Access team
      */
     public static final StringGuacamoleProperty CLOUDFLARE_ACCESS_TEAM = new StringGuacamoleProperty() {
          @Override
          public String getName() {
               return "cloudflare-access-team";
          }
     };

     /**
      * The `aud` value associated with this team used to authenticate the JWT
      */
     public static final StringGuacamoleProperty CLOUDFLARE_ACCESS_AUDIENCE = new StringGuacamoleProperty() {
          @Override
          public String getName() {
               return "cloudflare-access-audience";
          }
     };

     /**
      * The name of the roles claim in the JWT to build user groups
      */
     public static final StringGuacamoleProperty CLOUDFLARE_ACCESS_ROLES_CLAIM = new StringGuacamoleProperty() {
          @Override
          public String getName() {
               return "cloudflare-access-roles-claim";
          }
     };
}
