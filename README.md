# xunit-report-view
source /etc/profile
mvn package
mvn hpi:run

#eclipse
File --> Import --> Existing Maven Projects

#[root@sgao ~]# tail -n 4 /etc/profile
#export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.45-35.b13.el6.x86_64
#export PATH=$JAVA_HOME/bin:$PATH
#export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
#export PATH=/root/workspace/apache-maven-3.3.9/bin:$PATH
