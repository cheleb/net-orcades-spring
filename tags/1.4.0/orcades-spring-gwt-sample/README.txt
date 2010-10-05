Usage:
------
1 - checkout the project
svn checkout http://net-orcades-spring.googlecode.com/svn/trunk/ net-orcades-spring-read-only

2 - gwt-log
Currently gwt-log 3.0.0 isn't available in the maven central repository, you need to download it manually.
You can download it from: 
http://code.google.com/p/gwt-log/downloads/list

And install it in your local repository:
mvn install:install-file -DgroupId=com.google.code.gwt-log -DartifactId=gwt-log -Dversion=3.0.0 -Dpackaging=jar -Dfile=/home/raymond/Downloads/gwt-log-3.0.0.jar

3 - build the projects (from within net-orcades-spring directory):
mvn clean install

4 - build the example (open dir: orcades-spring-gwt-sample)
mvn -Dgwt=true clean install
this will generate a war file in your target dir
deploy this war to your tomcat

5 - Done


Eclipse:
--------
To import the projects in eclipse, you need to do following in each of the directories (orcades-gwt-mvc, orcades-spring-gwt-orcades-spring-gwt-sample,orcades-s0pring-gwt-security):
mvn eclipse:clean eclipse:m2eclipse

Next you can import the directories in eclipse.

To run the example in eclipse you need at least: google gwt plugin and the m2eclipse plugin

After importing the sample, enable the maven dependency management (context menu on project -> maven -> Enable dependency management)

Enable google wtp support, context menu on project -> Google -> Web toolkit settings -> select 'Use google web toolkit'

You're almost done.

Open context menu -> RunAs -> WebApplication (select SampleModule -> SampleModule.html)
Hosted browser should open, choose launche default browser to open te application

You're done.