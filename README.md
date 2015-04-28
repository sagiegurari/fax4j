# fax4j
Java fax library.

## Introduction
The [fax4j](http://sagiegurari.github.io/fax4j/) is a Java fax library.
<br>
This library enables Java applications to invoke fax actions such as submitting fax jobs.
<br>
You can get the latest version from <a href="https://github.com/sagiegurari/fax4j/releases/latest">here</a> - pick the fax4j-[version number]-full.zip to get the binary distribution with all dependencies and documents

Or if you are using maven, simply add the following to your pom.xml:

```xml
<dependency>
  <groupId>net.sf.fax4j</groupId>
  <artifactId>fax4j</artifactId>
  <version>0.43.1</version>
</dependency>
```

## Features
The fax4j provides a standard and consistent faxing API to enable invoking the different fax actions from Java applications.<br>
The fax4j library enables to integrate with fax services via many different SPI (service provider interface) implementations.<br>
Currently fax4j supports the following implementations:<br>
* Windows native fax API (requires windows fax/tapi component installed)
* Windows COM fax API (via VBS called from the Java layer, requires windows fax/tapi component installed)
* Email (via email servers that enable to send fax message based on incoming email messages) including built-in support for many of the leading vendors.
* HTTP
* External Processes
* Linux native fax API (requires linux efax utility)
* Mac native fax API
* HylaFax (via gnu-hylafax library)

And more...
<br>
<br>
In addition the fax4j library also provides the fax bridge API which is a context specific faxing API.<br>
The fax bridge enables to transform emails servers, web servers and more into an online fax service.<br>
The [x2fax](https://github.com/sagiegurari/x2fax) sub project provides a set of standalone servers and deployable components which enable to provide x2fax services out of the box.<br>
The [x2fax](https://github.com/sagiegurari/x2fax) contains built-in email component to enhance email servers to provide email2fax services and HTTP servers and WARs to convert any Java web server into a web2fax bridge.

## Example Usage
```java
//get new instance of a fax client (based on internal + external fax4j.properties file data)
FaxClient faxClient=FaxClientFactory.createFaxClient();
 
//create a new fax job
FaxJob faxJob=faxClient.createFaxJob();

//set fax job values
faxJob.setFile(new File("./my_document.txt"));
faxJob.setPriority(FaxJobPriority.HIGH_PRIORITY);
faxJob.setTargetAddress("555-555");
faxJob.setTargetName("YourName");
faxJob.setSenderEmail("myemail@mycompany.com");
faxJob.setSenderName("MyName");

//submit fax job
faxClient.submitFaxJob(faxJob);

//print submitted fax job ID (may not be supported by all SPIs)
System.out.println("Fax job submitted, ID: "+faxJob.getID());
```
You can see more examples and a tutorial at: [javadocs](https://sagiegurari.github.io/fax4j/apidocs/overview-summary.html#overview_description)

## Building from Sources
The fax4j library comes with a maven pom.xml which can be used to build the Java layer of the library.
<br>
On windows, the fax4j.dll will also be built, therefore the path for the msbuild.exe (in the .NET installation) must be set.
<br>
While this library supports java 1.5 and up, the build process of this library (not of depended libraries) require java 5/6.
<br>
However building this library from source is not required as it provide many extension capabilities via configuration.

## Road Map
This project started at 2009 and was hosted originally on sourceforge (This is also the reason why the groupId starts with net.sf).

Currently this library is in maintenance mode and no future development is currently being planned.

For historical forum posts which are not in github please see the original sourceforge project discussion page.

## License
The fax4j library is distributed under the "The Apache Software License, Version 2.0" license which means that it is possible to distribute this library also in commercial closed source products.

## Change Log
See [Change Log](https://sagiegurari.github.io/fax4j/changes-report.html) for full report.
