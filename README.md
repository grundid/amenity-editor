== This project is no longer maintained. ==






== Amenity Editor for OSM ==

The Amenity Editor is written in Java. It is build using the 
SpringFramework http://www.springframework.org/.
Apache Maven http://maven.apache.org is used as the build system.

== Building ==

To build the Amenity Editor launch:

mvn package

This will create a WAR file in the target folder with the name "amenity-editor.war".
Place this file in the webapps folder of your 
servlet container http://tomcat.apache.org and launch the server.

You can then access the amenity editor with

http://localhost:8080/amenity-editor

The default profile will build an Amenity Editor which will use a H2 
In-Memory Database http://www.h2database.com and access the Developer OSM API. 
The database will be cleared after each restart.

If you want to download some nodes, you will have to change the 
profile settings an build the project with:

mvn package -P osm-prd

This will create a WAR file which can access the production API. This means that you will 
be able to download nodes from the OSM server and see them on the map.

If you want to be able to edit nodes, you will have to register an OAuth App on the OSM webpage 
(http://www.openstreetmap.org/user/username/oauth_clients/new).
Then create a new profile in you Maven settings.xml like this:


<profile>
	<id>osm-oauth</id>
	<properties>
		<oauth.consumerKey>...</oauth.consumerKey>
		<oauth.consumerSecret>...</oauth.consumerSecret>
	</properties>
</profile>


Now build the project with the osm-prd profile and the osm-oauth profile:

mvn package -P osm-prd,osm-oauth

The H2 database might be not suitable for production. You can install a db of your choice 
and create a schema from the src/main/resources/database.sql file.

Here is an example profile for PostgreSQL (is already included in the pom.xml):

<profile>
	<id>ae-postgresql</id>
	<properties>
		<db.driver>org.postgresql.Driver</db.driver>
		<db.url>jdbc:postgresql://localhost/ae</db.url>
		<db.user>osm</db.user>
		<db.password>osm</db.password>
	</properties>
	<dependencies>
		<dependency>
			<groupId>postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>8.4-702.jdbc4</version>
		</dependency>
	</dependencies>
</profile>


Now build the project with:

mvn package -P osm-prd,osm-oauth,ae-postgresql

This gives you a full production release.

== Hacking ==

The source code has some test case coverage so you should be fine to be 
able to do some hacking without braking major functionality.

As for the IDE, I recommend the SpringSource Tool Suite: http://www.springsource.com/downloads/sts
It already has some useful plugins on board. 

Basically you can also use the Web Developer Editon of Eclipse and install all the plugins you need yourself.

Here is a list of plugins you'll definitely need:

M2Eclipse - http://www.eclipse.org/m2e/
JAXB2 Extension for M2Eclipse - https://github.com/hwellmann/m2eclipse-extras/raw/master/p2
Use the above URL as an update site in Eclipse. If you open it in the browser it will give you 404 - file not found.
EGit - http://eclipse.org/egit/

To set things up, please follow the steps below:

1. Setup the Git repo in eclipse.
2. Checkout the amenity-editor project
3. Right click on the amenity-editor project and choose "Configure > Convert to Maven Project" 
4. Right click on the amenity-editor project and choose "Run As... > maven generate-sources"

At this point the project should compile fine. If it does not, please do the following:

Right click on the amenity-editor project and choose "Maven > Update Project Configuration..."

Now you should be able to run the tests:

5. Right click on the amenity-editor project and choose "Run As... > JUnit test"

Is the bar green? Good.

== Running in Eclipse ==

You can launch the Amenity Editor directly in Eclipse. You will need to have a Servlet Container setup.
If everything is OK, you can right click on the file src/main/webapp/index.jsp and choose "Run As... > Run on Server".

Please note that you might want to activate some of the profiles mentioned above. 
To do that right click on the amenity-editor project and click on "Properties". Now select the "Maven" section and 
enter the profiles you want into the "Active Maven Profiles" field.



