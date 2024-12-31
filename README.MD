# Sungrow API Client

[![Java CI with Maven](https://github.com/Afrouper/sungrow-api-client/actions/workflows/maven.yml/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/maven.yml)
[![CodeQL](https://github.com/Afrouper/sungrow-api-client/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/github-code-scanning/codeql)
[![Dependabot Updates](https://github.com/Afrouper/sungrow-api-client/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/Afrouper/sungrow-api-client/actions/workflows/dependabot/dependabot-updates)

API Client which uses the official API from the [sungrow developer portal](https://developer-api.isolarcloud.com/).

The Client is still in development and not finished.

> [!NOTE]
> Pull requests to complete APIs are welcome 

> [!IMPORTANT]
> I choose a very simple implementation to get this client used in different environments; without coming to "dependency hell".

## ToDos
- [x] Add github actions for compile, dependabot and securiy issues
- [ ] Add release action
- [ ] Mock JUnit Test - and create more 
- [ ] Deploy via Maven Central
- [ ] Complete API

# Usage
You have to create an account at Sungrows iSolarCloud and on their developer portal and get the needed
credentials (see [Sungrow Developer Portal](#Sungrow Developer Portal))

```java
//Create client
SungrowClient sungrowClient = new SungrowClient(
    <URI>,
    <App Key from Developer Portal>,
    <SecretKey from Developer Portal>,
    <Connect timeout>,
    <Request timeout>);

//login
sungrowClient.login(<Account eMail>, <Account Password>);

//Call APIs; First query your plants
PlantList plantList = ApiOperationsFactory.getPlantList(); // <-- Factory for all API methods
sungrowClient.execute(plantList);
plantList.getResponse(); // <-- Work with response
```

# Sungrow Developer Portal

Currently only the access for private customers can be used.
Use your sungrow credentials to login at "https://developer-api.isolarcloud.com".
You need to create an application; go to you applications in the top menu:

![Applications](img/applicationMenu.png)

Click 'Create Application'. After this fill out the needed informations and be sure that you
do **not** choose "OAuth2.0": 

![Authorization](img/authorization.png)

## URLs
This are the base URLs 
* China: https://gateway.isolarcloud.com/
* International: https://gateway.isolarcloud.com.hk/
* Europe: https://gateway.isolarcloud.eu/
* Australia: https://augateway.isolarcloud.com/

# Develop new APIs
Please read the docs at the [Developer Portal](https://developer-api.isolarcloud.com/#/document/md?id=10942&project_id=2&version=V1).

To create a new API refer the existing one such as `de.afrouper.server.sungrow.api.operations.DeviceList`.
You have to create a class per API and the corresponding request and response objects.