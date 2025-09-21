# Sungrow API Client

[![Java CI with Maven](https://github.com/Afrouper/sungrow-api-client/actions/workflows/maven.yml/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/maven.yml)
[![CodeQL](https://github.com/Afrouper/sungrow-api-client/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/github-code-scanning/codeql)
[![Dependabot Updates](https://github.com/Afrouper/sungrow-api-client/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/dependabot/dependabot-updates)
![Tests](https://img.shields.io/endpoint?label=Tests&url=https://gist.githubusercontent.com/Afrouper/4b8ce8c93b2a3a92b777d51914cb1e6b/raw/9352822e575e3f75afef51f26f1b5e9125c526dc/sungrow-api-client-junit-tests.json)
![Tests](https://img.shields.io/endpoint?label=Test%20Coverage&url=https://gist.githubusercontent.com/Afrouper/4b8ce8c93b2a3a92b777d51914cb1e6b/raw/cb93d70bb0200fe0aa820ea4231f625bb71d5c65/sungrow-api-client-jacoco-coverage.json)

API Client which uses the official API from the [sungrow developer portal](https://developer-api.isolarcloud.com/).

The Client is still in development and not finished.

> [!NOTE]
> Pull requests to complete APIs are welcome

> [!IMPORTANT]
> I choose a very simple implementation to get this client used in different environments; without coming to "dependency
> hell".

## ToDos

- [x] Add gitHub actions for compile, dependabot and securiy issues
- [x] Add sample client for basic usage
- [x] Support encrypted API calls (open: Change password for each call)
- [x] Add release action
- [x] Mock JUnit Test
- [x] Deploy via Maven Central
- [x] Complete API Calls for reading
- [x] Add OAuth2 Support
- [ ] Complete API Calls for writing

## Dependency

The JAR is available on [Maven Central](https://central.sonatype.com/artifact/io.github.afrouper/sungrow-api-client)
with the coordinates `io.github.afrouper:sungrow-api-client`:

```xml

<dependency>
    <groupId>io.github.afrouper</groupId>
    <artifactId>sungrow-api-client</artifactId>
    <version>[1.1.0,)</version>
</dependency>
```

# Usage

You have to create an account at Sungrows iSolarCloud and on their developer portal and get the needed
credentials (see [Sungrow Developer Portal](https://developer-api.isolarcloud.com))
To access the iSolarCloud API you can either use a variant with an "API key" or via OAuth2.

For a simple configuration approach the needed values can be read via class
[EnvironmentConfiguration](src/main/java/de/afrouper/server/sungrow/api/EnvironmentConfiguration.java).

| SystemProperty / Environment variable | meaning                                                |
|---------------------------------------|--------------------------------------------------------|
| APP_KEY                               | Your app key from Sungrow Developer Portal             |
| SECRET_KEY                            | Your secret key from Sungrow Developer Portal          |
| ACCOUNT_EMAIL                         | EMailadress used for login to Sungrow Developer Portal |
| ACCOUNT_PASSWORD                      | Password used for login to Sungrow Developer Portal    |
| RSA_PUBLIC_KEY                        | RSA Public key to call APIs E2E encrypted              |
| API_CALL_PASSWORD                     | Password for payload encryption                        |
| AUTHORIZE_URL                         | OAuth2 Authorization URL                               |
| REDIRECT_URL                          | OAuth2 Redirect URL                                    |

The URL for accessing the correct cloud service is determined from
the [Region](src/main/java/de/afrouper/server/sungrow/api/SungrowClientBuilder.java) Enum.

## API Key variant

A simple example can be found at [Client.java](src/test/java/de/afrouper/server/sungrow/Client.java).
Its Output (with a german installation) can be seen [here](docs/Output.MD)

```java
sungrowClient = new SungrowClientBuilder()
    .builder(SungrowClientBuilder.Region.EUROPE)
    .withCredentials(EnvironmentConfiguration.getAppKey(), EnvironmentConfiguration.getSecretKey())
        .withLogin(EnvironmentConfiguration.getAccountEmail(), EnvironmentConfiguration.getAccountPassword());

PlantList plants = sungrowClient.getPlants();
// Handle Plants and devices - see de.afrouper.server.sungrow.Client
```

## OAuth2 variant

The OAuth2 auth code flow requires a server to handle the redirect send after login; the server must be implemented or
provided by you 😇
A sample can be found at [OAuth2Client](src/test/java/de/afrouper/server/sungrow/OAuth2Client.java)

```java
sungrowClient = new SungrowClientBuilder()
    .builder(SungrowClientBuilder.Region.EUROPE)
    .withCredentials(EnvironmentConfiguration.getAppKey(), EnvironmentConfiguration.getSecretKey())
    .withOAuth2(EnvironmentConfiguration.getAuthorizeUrl(), EnvironmentConfiguration.getRedirectUrl())
    .build();

String authCode = "xxxxx"; //TODO: Replace with correct auth code
sungrowClient.authCodeFlow(authCode);

PlantList plants = sungrowClient.getPlants();
// Handle Plants and devices...

sungrowClient.close(); // Stop thread for refreshing the access token
```

# Sungrow Developer Portal

Use your sungrow credentials to login at "https://developer-api.isolarcloud.com".
You need to create an application; go to you applications in the top menu:

![Applications](img/applicationMenu.png)

Click 'Create Application'. This is the sample for a "normal" application - used via "API key" variant.

![Authorization](img/authorization.png)

# API Usage

Please read the docs at
the [Developer Portal](https://developer-api.isolarcloud.com/#/document/md?id=10942&project_id=2&version=V1). THe are
some hints and documentation of the measurement points, etc.
