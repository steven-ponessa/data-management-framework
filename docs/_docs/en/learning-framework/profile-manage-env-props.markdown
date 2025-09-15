---
title: Profile to Manage Environment Specific Properties
permalink: en/learning-framework/profile-manage-env-properties
features:
  - mermaid
  - zoom
abstract: >- # this means to ignore newlines until "baseurl:"
   In a Java Spring Boot application, you can handle environment-specific properties by using **Spring Profiles**. Spring Profiles allow you to define different sets of configuration properties for different environments such as `dev`, `sit`, `uat`, and `prod`. Here's how you can implement it:
---

# Steps to Handle Environment-Specific Properties

1. **Create Profile-Specific Property Files**
   - Define separate property files for each environment, named `application-{profile}.properties`. For example:
     ```
     application-dev.properties
     application-sit.properties
     application-uat.properties
     application-prod.properties
     ```
   - In each file, add the environment-specific properties. For example:
     - **`application-dev.properties`**
       ```properties
       server.port=8080
       spring.datasource.url=jdbc:mysql://localhost:3306/devdb
       spring.datasource.username=dev_user
       spring.datasource.xxxxword=dev_password
       ```
     - **`application-prod.properties`**
       ```properties
       server.port=9090
       spring.datasource.url=jdbc:mysql://prod-db:3306/proddb
       spring.datasource.username=prod_user
       spring.datasource.xxxxword=prod_password
       ```

2. **Specify the Active Profile**
   - Use the `spring.profiles.active` property to activate the desired profile. You can set this in:
     - **`application.properties` (default file)**:
       ```properties
       spring.profiles.active=dev
       ```
     - **Environment Variables or System Properties**:
       When running the application, pass the profile as an argument:
       ```bash
       java -jar your-application.jar --spring.profiles.active=prod
       ```
     - **IDE Run Configuration**:
       In your IDE (e.g., IntelliJ IDEA or Eclipse), add `--spring.profiles.active=dev` to the program arguments for the run configuration.

3. **Provide a Fallback (Optional)**
   - You can keep common properties in `application.properties`. These properties will be overridden by the profile-specific files if a profile is active.

4. **Use the Properties in Your Application**
   - Use the configured properties in your application through `@Value` or `@ConfigurationProperties` annotations. For example:
     <pre name="code" class="js">
     @Value("${spring.datasource.url}")
     private String dataSourceUrl;

     @Value("${server.port}")
     private int serverPort;
     </pre>

5. **Testing in Each Environment**
   - Test each environment by activating the corresponding profile and ensuring that the appropriate properties are applied.

---

## Additional Considerations
- **Secure Secrets**: Avoid storing sensitive information like passwords directly in property files. Use environment variables or a secret management solution (e.g., AWS Secrets Manager, HashiCorp Vault).
- **Default Profiles**: Configure a default profile in `application.properties` for fallback if no profile is active:
  ```
  spring.profiles.default=dev
  ```
- **YAML Format**: If you prefer, you can use a single `application.yml` file with profile-specific sections:

  <pre name="code" class="js">
  spring:
    profiles:
      active: dev
    datasource:
      url: jdbc:mysql://localhost:3306/devdb
      username: dev_user
      xxxword: dev_xxxword

  ---
  spring:
    profiles: prod
    datasource:
      url: jdbc:mysql://prod-db:3306/proddb
      username: prod_user
      xxxword: prod_xxxword
  </pre>

This setup ensures your application adapts seamlessly to different environments by loading the right configuration dynamically.

# Determine the active profile at run-time

The `Environment` interface contains methods to access the active profiles. Inject it into your component or service:

<pre name="code" class="java">
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileChecker {

    @Autowired
    private Environment environment;

    public void printActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("Active Profiles: " + String.join(", ", activeProfiles));
    }
}
</pre>