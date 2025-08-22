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
> I choose a very simple implementation to get this client used in different environments; without coming to "dependency hell".

## ToDos
- [x] Add gitHub actions for compile, dependabot and securiy issues
- [x] Add sample client for basic usage
- [x] Support encrypted API calls (open: Change password for each call)
- [x] Add release action
- [x] Mock JUnit Test
- [x] Deploy via Maven Central
- [x] Complete API Calls for reading
- [ ] Complete API Calls for writing
- [ ] Add OAuth2 Support

## Dependency
The JAR is available on [Maven Central](https://central.sonatype.com/artifact/io.github.afrouper/sungrow-api-client)
with the coordinates `io.github.afrouper:sungrow-api-client`:
```xml
<dependency>
    <groupId>io.github.afrouper</groupId>
    <artifactId>sungrow-api-client</artifactId>
    <version>[1.0.0,)</version>
</dependency>
```

# Usage
You have to create an account at Sungrows iSolarCloud and on their developer portal and get the needed
credentials (see [Sungrow Developer Portal](https://developer-api.isolarcloud.com))

A sample can be found at [Client.java](src/test/java/de/afrouper/server/sungrow/Client.java). If not provided the needed properties are read from
a Java SystemProperty and (if not found) from an environment variable:

| SystemProperty / Environment variable | meaning                                                |
|---------------------------------------|--------------------------------------------------------|
| APP_KEY                               | Your app key from Sungrow Developer Portal             |
| SECRET_KEY                            | Your secret key from Sungrow Developer Portal          |
| ACCOUNT_EMAIL                         | EMailadress used for login to Sungrow Developer Portal |
| ACCOUNT_PASSWORD                      | Password used for login to Sungrow Developer Portal    |
| RSA_PUBLIC_KEY                        | RSA Public key to call APIs E2E encrypted              |
| API_CALL_PASSWORD                     | Password for payload encryption                        |

The URL for accessing the correct cloud service is determined from the [Region](src/main/java/de/afrouper/server/sungrow/api/SungrowClientFactory.java) Enum. 

## Java Sample

A simple sample can be found at [Client.java](src/test/java/de/afrouper/server/sungrow/Client.java).
Its Output (with a german installation) can be seen [here](docs/Output.MD)

Or a very simple step:
```java
sungrowClient = SungrowClientFactory.createSungrowClientWithEncryption(SungrowClientFactory.Region.EUROPE);
sungrowClient.login();

PlantList plants = sungrowClient.getPlants();
// Handle Plants and devices - see de.afrouper.server.sungrow.Client
```

# Sungrow Developer Portal

Currently only the access for private customers can be used.
Use your sungrow credentials to login at "https://developer-api.isolarcloud.com".
You need to create an application; go to you applications in the top menu:

![Applications](img/applicationMenu.png)

Click 'Create Application'. After this fill out the needed information and be sure that you
do **not** choose "OAuth2.0": 

![Authorization](img/authorization.png)

# Develop new APIs
Please read the docs at the [Developer Portal](https://developer-api.isolarcloud.com/#/document/md?id=10942&project_id=2&version=V1).

To create a new API refer the existing one such as `de.afrouper.server.sungrow.api.operations.DeviceList_D`.
You have to create a class per API and the corresponding request and response objects.