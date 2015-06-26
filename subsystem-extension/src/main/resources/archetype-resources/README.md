# ${extensionName}

This is the maven project for the `${rootArtifactId}` HAL extension. The project consists of the following modules:
  
- `gui`: Contains the GWT code for the extension
- `app`: Provides a GWT module to run and test the extension

## Don't use two hash characters. This is a Velocity template and two hashes are a comment in Velocity! 
Develop
-------

Write some great extension code in the `gui` module. See the [HAL documentation](http://hal.gitbooks.io/dev/content/building-blocks/index.html) on how to use the relevant APIs. 

Run
---

The extension can be launched as part of the HAL management console. Switch to the `app` directory and execute one of the following:
 
- `mvn gwt:run` for GWT SuperDevMode
- `mvn gwt:run|debug -Dgwt.superDevMode=false` to use the old DevMode. Please note that you'll need [Firefox <= 26](http://ftp.mozilla.org/pub/mozilla.org/firefox/releases/26.0/) and the [GWT plugin](http://www.gwtproject.org/missing-plugin/) to use DevMode.

You'll need a running WildFly 10.x instance which is configured to allow access from http://localhost:8888. Use one of the following CLI commands to configure the management endpoint:

- standalone mode: 

        /core-service=management/management-interface=http-interface:list-add(name=allowed-origins,value=http://localhost:8888)
        reload
    
- domain mode:

        /host=master/core-service=management/management-interface=http-interface:list-add(name=allowed-origins,value=http://localhost:8888)
        reload --host=master

Please note that the extension will only show up in the UI if the subsystem is configured in standalone mode or is part of the selected profile in domain mode. 
 
Integrate
---------

