---
title: Authentication
permalink: en/learning-framework/authentication
abstract: >- # this means to ignore newlines until "baseurl:"
  The object model visualizes the elements in a software application in terms of objects and their relationships to one another.
---

# IBM Single Sign-On
w3id single sign-on (SSO) is IBM's corporate authentication service for internal users. It is designed to support the enterprise transformation to cloud and mobile and is built from IBM's own commercial cloud services.

As an **Identity and Access Management (IAM)** solution provider, **SSO on IBM Security Verify (ISV)** supports two primary options to configure authentication connections to applications. 

1. **Security Access Markup Language (SAML)**
1. **OpenID Connect (OIDC)**

Both SAML and OIDC are identity protocols. They provide a means by which users can be authenticated and user information can be securely transmitted between the system that is doing the authentication, otherwise known as the **Identity Provider (IdP)** and the service or application the user is trying to access. One of the key steps in enabling this form of Single Sign-On (SSO) is that a trust has to be set up between the IdP and the application. SAML and OIDC are key protocols used in any SSO solution because the purpose of an SSO solution is users will need to only authenticate once with the IdP and then they are able to access any of the applications that have been configured to trust the IdP.

The login flow could also look like this.

1. A user attempts to log directly into the application.
1. The application redirects their login request through the user’s browser to the IdP.
1. The user logs in to the IdP or is confirmed to already be logged in to the IdP
1. The IdP confirms the user has access to the application that sent the request.
1. The user’s information is passed from the IdP to the user’s browser.
1. Their information is then passed on to the application.
1. The application confirms they are authorized to access resources.
1. The user is allowed into the application.

![SAML / OIDC Flow]({{ site.baseurl }}/assets/images/docs/saml-oidc-flow.png)

SAML is older than OIDC. SAML first came on the scene in 2005. It transmits the data like users’ usernames, first names, last names, etc. using XML.

OIDC is built off of the **OAuth 2.0** protocol. Whereas OAuth 2.0 is used to set up so that two applications such as two websites can trust each other and send data back and forth, OIDC works at the individual or user level, using JSON.

This framework will use OIDC. OAuth is not an API or a service: it’s an open standard, [delegation protocol that is useful for conveying authorization decisions across a network of web-enabled applications and APIs](https://oauth.net/articles/authentication/).

With OIDC the end user talks to the identity provider (SSO), and the identity provider generates a cryptographically signed token which it hands off to the application to authenticate the user. The application trusts the identity provider. As long as that trust relationship works with the signed assertion, the application is good to go. It enables apps to obtain limited access (scopes) to a user’s data without giving away a user’s password. It decouples authentication from authorization and supports multiple use cases addressing different device capabilities. It supports server-to-server apps, browser-based apps, mobile/native apps, and consoles/TVs. 

You can think of this like hotel key cards, but for apps. If you have a hotel key card, you can get access to your room, the gym, the pool, and any hotel resource that you are entitled to. How do you get a hotel key card? You have to do an authentication process at the front desk to get it. After authenticating and obtaining the key card, you can access resources across the hotel.

## WFM DMF Authentication Sequence Diagram

The sequence diagram below shows an a request 

![Authentication sequence diagram]({{ site.baseurl }}/assets/images/docs/authentication-sequence-diagram.png)


## Password-less authentication

**w3id SSO** has introduced password-less authentication.  This gives applications the ability to authenticate using fingerprint readers, facial recognition, and security keys instead of using your username and password.  However, the need for passwords will not end.  The w3id Credentials option can be used for traditional password-based authentication.

There are two ways to enable password-less authentication with IBM's **SSO on ISV (IBM Security Verify)**.

1. **Fast Identity Online (FIDO)** devices such as Security Keys, TouchID, and Windows Hello
2. **Quick Response (QR)** Codea

### Security Keys, TouchID, and Windows Hello

These options are known as FIDO devices. A security key is a physical device that is paired with a fingerprint or PIN that you create. TouchID and Windows Hello are Apple and Microsoft's respective password-less authentication methods which require setup on your device before you can register them with w3id SSO. FIDO devices can be used for first factor (password-less) and second factor authentication (2FA).

### QR Code

Use the IBM Verify application to scan a unique Quick Response (QR) Code to authenticate. You will need to register with IBM Verify to use this method. IBM Verify can be used as a first factor by scanning the QR code and for second factor authentication via a push notification or a **TOTP (Time-based One Time Passcode)**.

# Securing the Application with SSO and Spring Security

To make the application secure, you can add Spring Security (**OAuth2 Client**) as a dependency. Since we'll be delegating authentication to an authentication provider (SSO), we'll include the Spring Security OAuth 2.0 Client starter in the **pom.xml** file:

<pre name="code" class="xml">

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

</pre>

By adding that, it will secure your app with OAuth 2.0 by default.

Next, you need to configure your app to use an authentication provider. For this, we'll use both **GitHub** and **SSO** to test, since GitHub allows us to configure the endpoint to return the authentication to `http://localhost:8080`.

1. Provision a [New SSO registration](http://w3.ibm.com/tools/sso)
1. Add a [New GitHub application](https://github.com/settings/developers)
1. Configure `application.yml`

## Provision a new SSO registration

1. Go to the [SSO Self-Service Provisioner](http://w3.ibm.com/tools/sso) site
1. Select [Register a w3Id application](https://ies-provisioner.prod.identity-services.intranet.ibm.com/tools/sso/w3id/application/register)
1. Enter the required information and click **Next**
1. Choose **Use w3id (BluePages) OpenID Connect 1.0.** and click **Next**
1. Select the Select OIDC Identity Provider (e.g., w3id Preproduction on IBM Security Verify)
1. _continue later_

## Add a new GitHub Application
To use GitHub’s OAuth 2.0 authentication system for login, you must first [Add a new GitHub app](https://github.com/settings/developers).

Select "New OAuth App" and then the "Register a new OAuth application" page is presented. Enter an app name and description. Then, enter your app’s home page, which should be `http://localhost:8080`, in this case. Finally, indicate the Authorization callback URL as `http://localhost:8080/login/oauth2/code/github` and click **Register Application**.

The OAuth redirect URI is the path in the application that the end-user’s user-agent is redirected back to after they have authenticated with GitHub and have granted access to the application on the Authorize application page.

## Configure `application.yml`

<pre name="code" class="java">
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: 8fb41be65f213387af09
            clientSecret: a7bc00e014bd254ef01b05c3d9146c1dfa81f053
          w3id:
            client-name: wfm-data-managment
            client-id: YjQ3MzhhYjgtMDkzOC00
            client-secret: ZDY2NTg3YjEtYWQwNi00
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid
        provider:
          w3id:
            issuer-uri: https://preprod.login.w3.ibm.com/oidc/endpoint/default
# ...
</pre>