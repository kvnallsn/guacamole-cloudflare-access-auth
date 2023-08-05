**Deprecated: Please see [guacamole-jwt-auth](https://github.com/kvnallsn/guacamole-jwt-auth) for a replacement**

# Guacamole Cloudflare Access Authentication
Auth plugin for [Apache Guacamole](https://guacamole.apache.org/) that supports placing the web client behind [Cloudflare Zero Trust](https://developers.cloudflare.com/cloudflare-one/) for user authentication.

## Requirements

###  Cloudflare Zero Trust Account
This plugin requires a Cloudflare Zero Trust account exists and is configured.  Additionally, the domain must be set to use Cloudflare DNS resolvers.  Setting this up is outside the scope of this document.  Some helpful links are included below.

#### Cloudflare Zero Trust Guides:
- [Getting Started](https://developers.cloudflare.com/cloudflare-one/setup/)
- [Identity Guide](https://developers.cloudflare.com/cloudflare-one/identity/)
- [Tunnel Guide](https://developers.cloudflare.com/cloudflare-one/connections/connect-networks/)
- [Application Guide](https://developers.cloudflare.com/cloudflare-one/applications/configure-apps/)

## Configuration

### `guacamole.properties`

The following two fields must be set in `$GUACMOLE_HOME/guacamole.properties`:

| Field                           | Required | Description                                               |
| ------------------------------- | -------- | --------------------------------------------------------- |
| `cloudflare-access-team`        | Yes      | The name of the team.  Used to build the appropriate URLs |
| `cloudflare-access-audience`    | Yes      | The value of the `aud` claim in the Application Token.  Can be found on the Zero Trust Dashboard under `Access / Applications / <Application> / Overview` |
| `cloudflare-access-roles-claim` | No       | The name of the roles claim to load group information from.  Defaults to `roles` |

#### Example 
```
# ... omitted other configure ...
cloudflare-access-team: myteam
cloudflare-access-audience: ceb84cb2a6d418999fea0ebac41b86ba37f747c3990dace944fc0f78f3dcae25
```

### Environment
If you'd prefer to configure using environemnt variables, you must `enable-environment-properties: true` in your `guacamole.properties` file. This is useful when using the docker image. You can then use the below variables to configure this plugin.

| Field                           | Environment Variable            |
| ------------------------------- | ------------------------------- |
| `cloudflare-access-team`        | `CLOUDFLARE_ACCESS_TEAM`        |
| `cloudflare-access-audience`    | `CLOUDFLARE_ACCESS_AUDIENCE`    |
| `cloudflare-access-roles-claim` | `CLOUDFLARE_ACCESS_ROLES_CLAIM` |

## Running

### Native
1. Download plugin corresponding to your Gaucamole version (i.e. `1.5.2`)
2. Place the plugin in your `$GUACAMOLE_HOME/extensions` directory and it will be automatically loaded when Guacamole starts
3. Configure your `$GUACAMOLE_HOME/guacamole.properties` file (or environment) as specified above
4. Start Guacamole

### Docker
A prebuilt docker image (`ghcr.io/kvnallsn/guacamole:<version>`) is available from this repository.  Simply pull the image and use as you would the regular Gaucamole docker image.

#### Example Command 

The example command makes the following assumptions:
- `guacd` is running on the localhost and listening on `4822/tcp`
- `postgresql` is running on the localhost and listening on `5432/tcp`
    - username is set to `guacd`
    - password is set to `password`
    - database is set to `guacd`
    - automatically creates postgresql accounts to store connection information/groups/etc.
- A Cloudflare Zero Trust team name `myteam` exists and the has the specified audience claim

```bash
docker run \
    -d \
    --rm \
    --name guacamole-cloudflare \
    --add-host host.docker.internal:host-gateway \
    -e ENABLE_ENVIRONMENT_PROPERTIES=true \
    -e POSTGRESQL_DATABASE=guacd \
    -e POSTGRESQL_USER=guacd \
    -e POSTGRESQL_PASSWORD=password \
    -e POSTGRESQL_HOSTNAME=host.docker.internal \
    -e POSTGRESQL_AUTO_CREATE_ACCOUNTS=true \
    -e GUACD_HOSTNAME=host.docker.internal \
    -e GUCAD_PORT=4822 \
    -e CLOUDFLARE_ACCESS_TEAM=myteam \
    -e CLOUDFLARE_ACCESS_AUDIENCE=ceb84cb2a6d418999fea0ebac41b86ba37f747c3990dace944fc0f78f3dcae25 \
    -p "8080:8080" \
    kvnallsn/guacamole:1.5.2
```
