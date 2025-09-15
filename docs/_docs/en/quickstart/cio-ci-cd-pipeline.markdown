---
title: CIO CI/CD Pipeline
permalink: /en/quickstart/cio-ci-cd-pipeline
features:
  - mermaid
  - d3
  - select2
  - datatables
abstract: >- # this means to ignore newlines until "baseurl:"
    The CIO CI/CD pipeline is a custom-built DevSecOps solution, designed to enable CIO engineers to deploy software into the Cirrus platform as easily as possible, while still conforming to all the CIO specific requirements and regulations. The goal of the pipeline is to allow engineers and managers to concentrate on development while allowing automation to perform the toil of deployment.
---

# Overview

While there are variations on how the pipeline operates, to match the specific technical and business requirements of different teams, the following diagram demonstrates the standard stages of the pipeline which virtually all types of pipeline will perform:

![CIO CI/CD Pipeline Overview]({{ site.baseurl }}/assets/images/docs/CI_CD_Pipelines_Overview.png){: class="center zoom"}

[![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/CI_CD_Pipelines_Overview.png){: target="_blank"}

## Documentation 

- [CIO CI/CD Technical Documentation](https://pages.github.ibm.com/cio-ci-cd/documentation/){:target="_blank"}

## Slack

- Distributed engineers:[#dx-platform-support](https://my.slack.com/archives/C02AM16DR19){:target="_blank"}
- Mainframe engineers: [#cio-zos-ci-cd-support](https://ibm-cio.slack.com/archives/C034Y7URH97){:target="_blank"}

## Dockerfile
One of the CIO's design goals for the CI/CD is consistency and therefore it does not support DockerFile. DockerFile allows a high level of customization which is not possible to implement or support in a single system. In addition, ITSS compliance is challenging and most often not possible with Dockerfile. 

# Steps

## 0. Pre-requisites

1. When your application is built, the result should be a single container image pushed to Cirrus. Currently, applications that output multiple images are not supported.
1. Make sure you have an IBM Bluepay consumer account: [register for an IBM Bluepay account](https://w3.ibm.com/w3publisher/cio-hybrid-cloud-services/get-started/create-accounts){:target="_blank"}, if you have not. Your manager may have already set one up for you.
1. Register for an [Enterprise Application Library (EAL)](https://w3.ibm.com/enterprise-application-library){:target="_blank"} account. You will need the created IMAP number later (EAL-XXXXXX).

See [Requirements](https://pages.github.ibm.com/cio-ci-cd/documentation/requirements/){:target="_blank"}

## 1. Decide on a pipeline

See [Deciding on a Pipeline](https://pages.github.ibm.com/cio-ci-cd/documentation/decision/){:target="_blank"}

There are two deployment strategies:

1. **Standard** (`cirrus-deployment`): deployment automatically proceeds from pre-production to production on the default branch.
1. **Container Release Management** (`container-release`): deployment on the default branch stops between pre-production and production allowing stakeholder review.

There are two types of pipelines: **official** and **community**.  Official pipelines are actively maintained by the CIO CI/CD team. Community supported pipelines are not.  The CIO supports 4 pipeline types.

1. **Container** - Constructs a container image for web services and deploys the application or job to Cirrus.
1. **Compliance** - Runs some ITSS tools and helps achieve ITSS Chapter 5 compliance, such as Detect Secrets, SonarQube and Mend.
1. **Library** - Builds software libraries/modules and publishes them to repositories like Artifactory.
1. **UI** - Constructs a container image for user interface components or applications (ie. React) served with Nginx to Cirrus.


| | **Container** | **Compliance** | **Library** | **UI** |
|-|:-------------:|:--------------:|:-----------:|:------:|
| Node.js | ✓ | ✓ | ✓ | ✓ |
| Java / Open Liberty | ✓ | ✓ |  | 
| Python | ✓ | ✓ |  | ✓ |

All official Pipelines that deploy to Cirrus can use the **container release** feature.

**SpringBoot** are applications that produce a JAR file and run by the JRE in a container image.  Therefore, for the WFM-DMF, we'll use [`java-v17-mvn-springboot-layertools-container-image`](https://github.ibm.com/cio-ci-cd/pipeline-catalog/tree/main/catalog/community/pipelines/java-v17-mvn-springboot-layertools-container-image/v1){:target="_blank"}.  This is a community pipeline so we are looking at migrating to [`java-v17-mvn-ol-container-image`](https://github.ibm.com/cio-ci-cd/pipeline-catalog/tree/main/catalog/official/pipelines/java-mvn-ol-container-image/v2){:target="_blank"}.

## 2. Install the GitHub Application

1. Install the [CIO CI/CD GitHub app](https://pages.github.ibm.com/cio-ci-cd/documentation/github-app/){:target="_blank"} in your GitHub organization.
    1. Go to the CIO DevEx GitHub App [installation page](https://github.ibm.com/github-apps/cio-devex/installations/new){:target="_blank"}.
    1. Select the organization to which you will push code.
    1. Select all the repositories you work in. Note that you should create a `build.yml` for all repos you work in.
1. A `build.yml` file is required including a valid EAL IMAP Number.
    <pre name="code" class="js">
      apiVersion: automation.cio/v1alpha1
      kind: RepositoryConfig
      ealImapNumber: EAL-xxxxxx
      build:
      strategy: cirrus-deployment
      pipeline: xxx
      version: v2
      config:
         cirrus-pipeline-name: xxx
         cirrus-project-name: xxx
         cirrus-api-username: xxx
         cirrus-api-password: xxx
         cirrus-region: <dal-containers | wdc-containers>*
    </pre>
1. A `deploy` directory with folders for `production`, `pre-production`, and `test`.  Each folder will contain
      1. `application.yaml`
      1. `routes.yaml`

## 3. Generate a GitHub Access Token
The following process allows you to encrypt Secrets and commit them securely to your repository.

1. Follow [this guide](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token){:target="_blank"} written by GitHub to generate a token for cio-ci-cd, with permissions:
    - `repo`
    - `user/read:user`
    
    For your convenience, click [this link](https://github.ibm.com/settings/tokens/new?scopes=repo,read:user&description=cio-ci-cd){:target="_blank"} and it will automatically set up the token pre-populating the permissions for you.
1. Save the **access token** as you'll need to use it later.


## 4. Create your Project and Pipeline on Cirrus
1. Go to [Cirrus](https://cirrus.ibm.com/){:target="_blank"}.
1. Create a project in Cirrus. Give the project a name that makes sense. Choose **Containers** under **Flavors**. Note: If you already have a project, you can use your existing project with CI/CD.
1. Navigate to the Pipelines tab and create a new pipeline. Select **CIO CI/CD pipeline**. Give the pipeline a name related to your project and repository such as `<github_org_name>-<repository_name>`. Do not use the same pipeline for multiple repositories.
1. For your record, copy the credentials shown to somewhere safe when you click "**Create pipeline**". Take note of the project name.

![ci-cd-cirrus-pipeline-credentials]({{ site.baseurl }}/assets/images/docs/ci-cd-cirrus-pipeline-credentials.png){: class="center zoom"}
[![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/ci-cd-cirrus-pipeline-credentials.png){: target="_blank"}

## 5. Make Code Changes

1. Create a **`build.yml`** file in project root with the following structure.
    <pre name="code" class="js">
      apiVersion: automation.cio/v1alpha1
      kind: RepositoryConfig
      ealImapNumber: EAL-xxxxxx
      build:
      strategy: cirrus-deployment
      pipeline: xxx
      version: v2
      config:
         cirrus-pipeline-name: xxx
         cirrus-project-name: xxx
         cirrus-api-username: xxx
         cirrus-api-password: xxx
         cirrus-region: <dal-containers | wdc-containers>*
    </pre>

    **For example**
    <pre name="code" class="js">
      apiVersion: automation.cio/v1alpha1
         kind: RepositoryConfig
         ealImapNumber: ZIM-000001
         build:
         strategy: cirrus-deployment
         pipeline: java-v17-mvn-springboot-layertools-container-image  
         version: v1
         config:
            cirrus-pipeline-name: wfm-dmf-ga
            application-name: wfm-dmf-ga
            use-open-liberty: "false"
            service-port: "9090"
            cirrus-project-name: prom-greenstar-integration
            cirrus-api-username: encrypted:v1:AAABksU2GKWS...
            cirrus-api-password: encrypted:v1:AAABksU2GKVb...
            cirrus-region: us-south2
    </pre>

2. Enterprise Application Library (EAL) numbers are a LEGACY from when these IDs were hosted on [Decision Focus](https://ibm.decisionfocus.com/administrate/workspaces){: target="_blank"}.

    The new service, [Application Portfolio Management (APM)](https://w3.ibm.com/w3publisher/enterprise-application-library){: target="_blank"}, hosts all the old EAL records and allows you to get new ones. Access the APM portal [here](https://ibm.service-now.com/home?id=apm_home){: target="_blank"}.

3. Choose a pipeline and fill into the pipeline field.
    - `java-v21-mvn-ol-container-image` - Java 21 microservice built on Maven and served with Open Liberty
    - `java-v17-mvn-ol-container-image` - Java 17 microservice built on Maven and served with Open Liberty
    - `java-v11-mvn-ol-container-image` - Java 11 microservice built on Maven and served with Open Liberty
    - `java-v8-mvn-ol-container-image` - Java 8 microservice built on Maven and served with Open Liberty
    - `java-v11-mvn-jdk-container-image` - Java 11 application built on Maven to run a JAR artifact with the JRE
    - `java-v8-mvn-jdk-container-image` - Java 8 application built on Maven to run a JAR artifact with the JRE
    - **`java-v17-mvn-springboot-layertools-container-image`** - Java 17 microservice built on Maven and served with Spring Boot layertools

4. Fill in **cirrus-pipeline-name** with the name of your Pipeline in Cirrus. Each repository configuration requires a unique Cirrus pipeline. Do not use the same Cirrus pipeline in multiple repositories.

5. Grant the pipeline deployment privileges by enabling Openshift CLI access to the clusters in the target region. If you are using the `wdc-containers` region, you will need to enable CLI access for your project's assigned `wdc-containers-nonprod` and `wdc-containers-prod` clusters.
    In this example, `us-south1-nonprod` has already been granted privileges. Now enable `us-south1-prod` by clicking the slide bar next to it.

    ![CIO CI/CD Grant Pipeline Deployment]({{ site.baseurl }}/assets/images/docs/ci-cd-grant-pipeline-deployment.png){: class="center zoom"}

    [![go-to]({{ site.baseurl }}/assets/images/go-to.svg){: style="height:24px;" } open full image]({{ site.baseurl }}/assets/images/docs/ci-cd-grant-pipeline-deployment.png){: target="_blank"}

    Confirm the username displayed is the same username used to deploy your pipeline.

6. **Create an API Key for Cirrus**. 

    You can create the API key on [this page](https://cirrus.ibm.com/user){: target="_blank"}. Be aware that keys automatically expire after 90 days and there is no way to renew them, so you'll need to periodically replace this key. You should follow the steps on this guide to create an API key: [Accessing Via API Key](https://pages.github.ibm.com/CIOCloud/cio-blog/cli/#accessing-via-api-key){: target="_blank"}. Ensure you keep a copy of the key created, it will be used in the next step.

7. Encrypt the Cirrus API Username and Password.

    Use the [Encryption Tool](https://cirrus.ibm.com/developer/tools/secrets-encryption){: target="_blank"} on the Developer Portal.

    Copy and paste the result into your **`build.yml`** file. Note: each encrypted string for a particular GitHub repository cannot be shared across multiple repositories.

8. Set up the Detect Secrets tool by following the steps in the [IBM Detect Secrets](https://w3.ibm.com/w3publisher/detect-secrets){: target="_blank"} page. Make sure that you configure the pre-commit hook. Be aware that scan failures can cause your pipeline to fail.

9. Review the **`build.yml`** file.

10. To run Integration Tests, the pipeline executes **mvn verify** command. The following is a sample configuration defined within **`pom.xml`** that uses Maven's Failsafe plugin:
    <pre name="code" class="xml">
      <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <version>2.22.2</version>
      <executions>
      <execution>
         <id>integration</id>
         <phase>integration-test</phase>
         <goals>
            <goal>integration-test</goal>
         </goals>
         </execution>
         <execution>
            <id>verify</id>
            <phase>verify</phase>
            <goals>
            <goal>verify</goal>
         </goals>
         </execution>
      </executions>
    </pre>

11. To learn about the deployment testing process, review this page [Cirrus Deployment Testing](https://pages.github.ibm.com/cio-ci-cd/documentation/cirrus-deployment-testing/){: target="_blank"}.

12. Configure a **health check** endpoint. You can learn more about [health checks here](https://cloud.google.com/blog/products/containers-kubernetes/kubernetes-best-practices-setting-up-health-checks-with-readiness-and-liveness-probes){: target="_blank"}. By default the pipeline creates a liveness and readiness probe calling the endpoint `/health/ping` in order to validate your application is up and running. Your application needs to be configured to respond from this endpoint. The endpoint can be changed by configuring a custom **liveness** and **readiness** probe following this guide: [Cirrus Deployment Customization](https://pages.github.ibm.com/cio-ci-cd/documentation/cirrus-deployment-customization/){: target="_blank"}.

13. The **Deploy Resource Directory**
    Follow the steps outlined in [Cirrus Deployment Customization](https://pages.github.ibm.com/cio-ci-cd/documentation/cirrus-deployment-customization/){: target="_blank"}. 
    Create a directory named `deploy` at the top-level of your repository. Inside of the `deploy` directory you can create a sub-directory named for the environments you wish to target (`test` / `pre-production` / `production`). Add resource definition yaml files inside the respective sub-directory. CD automation will scan all yaml files in target environment's directory and add them to the deployment.

    See the following example of the directory structure from a Node.js repository that has customized deployment files for the test environment. It contains Application and Route resource definition files for the test environment. The empty `pre-production` and `production` directories are shown for the example and can be omitted if unused.

     ```
      .
      ├── README.md
      ├── build.yml
      ├── deploy
      │   ├── pre-production
      │   ├── production
      │   └── test
      │       └── application.yaml
      │       └── route.yaml
      ├── package-lock.json
      └── package.json
    ```
    > **Note**: The suggested naming convention of yaml files is the resource kind name in lower-kebab case. For example, application.yaml is for a file containing a resource definition for an Application. Automation adds all files ending in .yaml found in the target environment directory to the deployment. A kustomization.yaml file found in an environment's directory is acknowledged but is not required.

### Additional Customizations

#### Naming
We suggest the naming convention `<orgname>-<reponame>-<environment>` for your Applications and other resources. The name of the Application is set in the .metadata.name field. The Application name is also used as part of the sub-domain when routes are being created. For example if the `.metadata.name` of a production Application hosted in `us-south` is `ciocicd-demo-production` then the Route would be `https://ciocicd-demo-production.dal1a.ciocloud.prod.intranet.ibm.com`.

Resource names must be all lower-case, alpha-numeric and can include the dash character `-`.

When deploying to the test environment (feature branches), automation suffixes the provided `.metadata.name` in the repository with the branch name for all resources in the deployment. For example if the `.metadata.name` on a `test` Application is set to `ciocicd-demo-test` on a branch named `feature-1`, the deployed Application's `.metadata.name` will be `ciocicd-demo-test-feature-`1. Thus, the generated route for the test Application hosted in `us-south` will be `https://ciocicd-demo-test-feature-1.dal1a.ciocloud.nonprod.intranet.ibm.com.`

#### Service Port
The `.spec.port` field of an Application resource definition defaults to the value of the `service-port` parameter from the `build.yml` and should therefore be omitted for simplicity. The service-port parameter is used to expose the port in the application's container image and has a platform specific default value.

#### Health Checks
Pipelines deploying applications to Cirrus **require health check support**. By default, an Application's health checks are configured as HTTP requests to GET `/health/ping` over the Application's `service-port`.

There are two types of health checks - the **liveness probe** and the **readiness probe**. Both are required and can be set to the same endpoint. Refer to the Kubernetes documentation [Configure Liveness, Readiness and Startup Probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/){: target="_blank"} for explanation on types of health checks and how they can be configured.

#### Routes
External routes are not automatically exposed for Applications. The quick start example above contains a snippet on how to expose a route for the production Application. A yaml file, **`routes.yaml`** with the following fields are all that is needed inside of a target environment's directory.

<pre name="code" class="js">
apiVersion: route.openshift.io/v1
kind: Route
metadata:
  name: orgname-reponame-environment
</pre>

<pre name="code" class="js">
apiVersion: route.openshift.io/v1
kind: Route
metadata:
  name: wfm-dmf-ga-test
</pre>

As mentioned in the Naming section above, the generated route's sub-domain will be suffixed with the branch name for the test environment only.

The `.metadata.name` of the Route is for identification only and is not used as part of the sub-domain. The sub-domain of the Route is based on the Application's name. If the Application name exceeds 63 characters, it will be truncated to the first 57 characters and appended with a single dash - character followed by the first 5 characters of the original name's MD5 hash to make it unique.

#### Environment Variables

> **Note:** Ensure that sensitive data is stored in **Secrets**; non-sensitive configurations can be stored in Kubernetes `ConfigMaps` or environment variables.

<pre name="code" class="js">
apiVersion: cirrus.ibm.com/v1alpha1
kind: Application
metadata:
  name: orgname-reponame-test
spec:
  env:
    - name: FOO
      value: BAR
  envFrom:
    # (optional) injecting environment variables from Kubernetes ConfigMaps
    - configMapRef:
        name: name-of-your-configmap
  # ...
</pre>

#### Secrets
Kubernetes `Secret` resource definitions found in a deploy directory get deployed. The service used to encrypt strings for the `build.yml` is **NOT** supported anywhere in the `deploy` directory.

**DO NOT** put unencrypted sensitive data in deploy files like `Secret` resource definitions. Create `Secrets` for sensitive data directly on the Cirrus Portal and simply reference them in your `Application` or `CronJob`.

Cirrus Application object example:

<pre name="code" class="js">
apiVersion: cirrus.ibm.com/v1alpha1
kind: Application
metadata:
  name: orgname-reponame-test
spec:
  envFrom:
    - secretRef:
        name: name-of-your-secret
  # ...
</pre>

#### Memory Quota
When you deploy an application as a container, you need to select an initial quota for your application. This is the maximum amount of memory that 1 pod of your application can consume.

To set the memory of the application, use the `.spec.quota` field.

Examples of valid values are q64mb, q128mb, q256mb, q512mb, q1gb. You can see the valid values in the pull down list when managing the memory tier on Cirrus. You can also run the command `oc get quotas` on the cluster to see the available quotas.

### Private dependencies

See [cio-ci-cd documentation java-and-cirrus private-dependencies](https://pages.github.ibm.com/cio-ci-cd/documentation/java-and-cirrus/#private-dependencies){:target="_blank"}

### Local Maven Dependencies
If your project uses private dependencies and TaaS Jfrog Artifactory is not in option, you can use local maven repository. Setup Local maven Repository in your root directory of project.

Use below command to create Local Maven Repository for required dependencies.

```
mvn install:install-file -DgroupId=YOUR_GROUP -DartifactId=YOUR_ARTIFACT -Dversion=YOUR_VERSION -Dfile=YOUR_JAR_FILE -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=./LocalMavenRepo -DcreateChecksum=true
```

Replace **YOUR_GROUP**, **YOUR_ARTIFACT**, **YOUR_VERSION** and **YOUR_JAR_FILE** as required. YOUR_JAR_FILE should point to an existent jar file in accessible directory in system's local path. Once all jars are added using above command you can move the LocalMavenRepo directory to project root path if created in different directory.

Create checksum for all files in mentioned local repository path using `shasum -a 256`, `md5sum` and `sha1sum`.

#### Update build.yml

Add following in `build.yml`. The below config will run mvn install goal during compile and package phase which is required when using Local Maven Dependencies

<pre name="code" class="js">
    config:
        mvn-goal: 'install'
        # other configurations
</pre>

#### Update pom.xml

Add following in `pom.xml`. Following configuration in pom.xml specifies path of local repository (e.g. LocalMavenRepo directory).

<pre name="code" class="js">
  <repositories>
    <repository>
      <id>maven-repo</id>
      <url>file://${project.basedir}/LocalMavenRepo</url>
    </repository>
  </repositories>
</pre>

## 6. Push Code and Wait for Build
When you commit and push code changes from your feature branch on you local repository to github.ibm.com, the CIO CI/CD pipeline is triggered to build the code. Wait approximately one minute.

You can access the Checks page via your Pull Request, or the commits section of your branch.

Click [here](https://pages.github.ibm.com/cio-ci-cd/documentation/java-and-cirrus/#step-6-push-code-and-wait-for-build){:target="_blank"} for additional details.


# Re-synching local development branch

To override your local `feature/3-cirrus-pipeline` branch with the latest version from GitHub, you can follow these steps:

1. **Fetch** the latest changes from GitHub for all branches:
   ```bash
   git fetch origin
   ```

2. **Reset** your local branch to match the remote branch, discarding any local changes:
   ```bash
   git reset --hard origin/feature/3-cirrus-pipeline
   ```

3. **Clean up** any untracked files or directories, if necessary:
   ```bash
   git clean -fd
   ```

This process will:
- Pull the latest changes from the remote branch.
- Forcefully reset your local branch to match the remote branch, removing any local changes or commits that are not present in the remote branch.
- Delete any untracked files or directories that might be lingering in your working directory.

> **Note**: you don’t need to run `git pull` to get `origin/feature/3-cirrus-pipeline` locally. Running `git fetch origin` will update your local references for all branches, including `origin/feature/3-cirrus-pipeline`, without merging or modifying your working branch.

Here’s a breakdown:

- **`git fetch origin`** updates your local references, so `origin/feature/3-cirrus-pipeline` will reflect the latest state of the branch from GitHub.
- **`git reset --hard origin/feature/3-cirrus-pipeline`** then moves your current branch to match the remote branch exactly, discarding any local changes.

Using `git pull` would combine a `fetch` and `merge`, but since you want to override local changes, a `reset --hard` after `fetch` is a safer, more precise approach.

To overwrite your `feature/3-cirrus-pipeline-sp` branch with the latest content from `feature/3-cirrus-pipeline`, you can follow these steps:

1. **Check out** the `feature/3-cirrus-pipeline-sp` branch:
   ```bash
   git checkout feature/3-cirrus-pipeline-sp
   ```

2. **Reset** `feature/3-cirrus-pipeline-sp` to match `feature/3-cirrus-pipeline`, discarding any changes:
   ```bash
   git reset --hard feature/3-cirrus-pipeline
   ```

3. Optionally, **push** the updated `feature/3-cirrus-pipeline-sp` branch to GitHub if you need to update the remote branch as well:
   ```bash
   git push -f origin feature/3-cirrus-pipeline-sp
   ```

This will:
- Move `feature/3-cirrus-pipeline-sp` to match the state of `feature/3-cirrus-pipeline`.
- Discard any changes specific to `feature/3-cirrus-pipeline-sp`.
- Force-push (if needed) to overwrite the remote branch.