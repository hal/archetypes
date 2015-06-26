# HAL Subsystem Extension Archetype

A maven archetype to create an extension for the HAL management console. The archetype creates an extension with a presenter / view tuple to edit the top level attributes of a given subsystem. 

## Prerequisites

The version number of the archetype maps to the lowest version number of the HAL management console it needs as a target. This in turn implies a specific WildFly version. The following table shows the dependencies between the versions:  
  
| Archetype Version | HAL Version Range | WildFly Version Range |
|-------------------|-------------------|-----------------------|
| 2.8.0             | [2.8.0,)          | [10.0.0.Alpha1,)      |

## Usage

Besides the regular maven coordinates like `groupId`, `artifactId` et al the archetypes uses the following parameters:

- `extensionName`: The name / title of the extension as it appears in the UI. Should be a human friendly term which can contains spaces. 
- `gwtModule` The name of the GWT module w/o the `.gwt.xml` suffix. Must not contain whitespace. 
- `subsystem`: The name of the subsystem. The extension will only show up in the UI if the subsystem is configured in standalone mode or is part of the selected profile in domain mode. 
- `nameToken`: An unique name token used to identify the extension's page in the UI. Should be an all lowercase string separated with dashes. Defaults to the subsystem name. See the [GWTP documentation](http://dev.arcbees.com/gwtp/features/PlaceManager.html) for more infos about place management.
 
The archetype is deployed to the [JBoss Maven Repository](https://repository.jboss.org). In order to use it, you need set the catalog flag: 

```
mvn archetype:generate \
    -DarchetypeCatalog=https://repository.jboss.org \
    -DarchetypeGroupId=org.jboss.hal.archetypes \
    -DarchetypeArtifactId=hal-subsystem-extension-archetype
```

Once you've created an extension, take a look at the generated README.md for further instructions.
