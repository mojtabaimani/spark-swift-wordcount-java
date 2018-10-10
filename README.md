# Spark Word Count Sample with Swift in Java 
This java program reads a text file from Openstack Swift storage and 
counts the repetition of words by using a local Spark compute engine.

The computation part of Java code is from [Apache Spark](https://github.com/apache/spark) 
repository.

## Build and Create Package: 
```
mvn package
```

## Run the program 

Before running the program you should load the environment variables of 
your Openstack project with Authentication version 2. 

```
source openrc_v2.sh 
```

You need these environment variables: 
```
OS_TENANT_NAME
OS_REGION_NAME
OS_USERNAME
OS_PASSWORD
```
Also you need to have a text file in your Openstack Swift storage and 
give the address of that file as an input argument to the program. 
Then you can run the program by this command line: 
```
java -jar sparkwordcountswift-1.0-SNAPSHOT-fat.jar "swift://CONTAINER.PROVIDER/<path-to-textfile>"
```
PROVIDER can be anything. 
for example: 
```
java -jar sparkwordcountswift-1.0-SNAPSHOT-fat.jar "swift://novel.OVH/novel.txt"
```

