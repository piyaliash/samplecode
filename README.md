1. Add jar package name and the project package name at the Spring Boot star application
  @SpringBootApplication(scanBasePackages = {"com.ubs.wmap.eisl.inhousekeepinglibrary","com.example.demo"})
2. Add dependency inthe pom file
  <dependency>
			<groupId>com.ubs.wmap.eisl.inhousekeepinglibrary</groupId>
			<artifactId>inhouse-keeping-library</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
3.  Autowire the component
    @Autowired
    InitializeHouseKeepingLibrary houseKeepingLibrary;
4. Call init method and pass the below parameter basic token is mandatory . Pass  eislToken or claims Map which should have serviceId and UserId
  init(basicToken, eislToken, claimstest)

5. There are are methods in the library like isEisltokenValid(), isBasicTokenValid() and other methods which can also be used
