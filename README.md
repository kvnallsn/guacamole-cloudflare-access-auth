# Guacamole Cloudflare Access Authentication
Auth plugin for [Apache Guacamole](https://guacamole.apache.org/) that supports placing the web client behind [Cloudflare Zero Trust](https://developers.cloudflare.com/cloudflare-one/) for user authentication.

## Usage

### Requirements

####  Cloudflare Zero Trust Account
This plugin requires a Cloudflare Zero Trust account exists and is configured.  Additionally, the domain must be set to use Cloudflare DNS resolvers.  Setting this up is outside the scope of this document.  Some helpful links are included below.

##### Cloudflare Zero Trust Guides:
- [Getting Started](https://developers.cloudflare.com/cloudflare-one/setup/)
- [Identity Guide](https://developers.cloudflare.com/cloudflare-one/identity/)
- [Tunnel Guide](https://developers.cloudflare.com/cloudflare-one/connections/connect-networks/)
- [Application Guide](https://developers.cloudflare.com/cloudflare-one/applications/configure-apps/)

### Installation

#### Native
1. Download plugin corresponding to your Gaucamole version (i.e. `1.5.2`)
2. Place the plugin in your `$GUACAMOLE_HOME/extensions` directory and it will be automatically loaded when Guacamole starts

#### Docker
A prebuilt docker image (`ghcr.io/kvnallsn/guacamole:<version>`) is available from this repository.  Simply pull the image and use as you would the regular Gaucamole docker image.

### Configuration

The following two fields must be set in `guacamole.properties` (or the environemnt if `enable-environment-properties: true` is set in `guacamole.properties`)

| Field                        | Environment Variable         | Description                                               |
| ---------------------------- | ---------------------------- | --------------------------------------------------------- |
| `cloudflare-access-team`     | `CLOUDFLARE_ACCESS_TEAM`     | The name of the team.  Used to build the appropriate URLs |
| `cloudflare-access-audience` | `CLOUDFLARE_ACCESS_AUDIENCE` | The value of the `aud` setting in the Application Token JWT.  Can be found on the Zero Trust Dashboard under `Access / Applications / <Application> / Overview` |