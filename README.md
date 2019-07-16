# next2019-pasoe-spring

As presented at Progress NEXT 2019 - Doing more with PASOE and Spring, May 2019. 

----

## Mirrored Repository

This repository is maintained [@ GitLab](https://gitlab.com/ChadThomsonPSC/next2019-pasoe-spring) and it mirrored repository here.

## Changes

This project will continue to reflect the one originally demonstrated at the Progress NEXT 2019 session, with the following differences  

1. minor updates, enhancements, and cleanup making the project easier to build.
1. the slides used during the session have been added

No other changes will be made to this project. However, feedback and pull-requests are welcomed.

---

## Assumptions

These instructions walk through the setup and deployment of the NextScheduler. If you wish, you may also choose to _color outside-the-lines_.

This project makes the assumption that you:
1. Have your own OpenEdge installation that includes the PASOE product
1. Are familiar with OpenEdge and PASOE -- enough to start and stop a PASOE instance.
1. Will be using either the Progress Developer Studio for OpenEdge (**PDSOE**), or a simillary configured **Eclipse IDE** -- referred to as `IDE` or `the IDE` below
1. Have left the project named `NextScheduler`
1. Will read all instructions and take full responsibility for anything you break ;^)

---

## Setup

Following initial clone/check-out/load of this project, it will _very-likely_ fail to build.  You might even be presented with errors.

There are a few steps that must be taken to prepare the project for use.

### Dependencies

The project references the Java libraries shipped with a **PASOE 11.7.4** installation.  If you are using a different version, you may need to update the dependencies configuration.

All the required dependencies must be resolved before a build will successfully complete.

#### Build Path

The Java Build Path has been pre-configured with a **Java User Library**. 

While there are other, and maybe better, dependency management techniques, to keep this project portable, with minimal setup requirements, all of the dependancy .jar files, relative to the **PASOE installation folder**, are included in the pre-defined Java User Library.

#### PASOE_REF Folder

To avoid duplication of resources, and to promote flexibility, all paths within the Java User Library have been configured _relative_ to the root folder of this project.

This project has a sub-folder called `PASOE_REF`.  The `PASOE_REF` folder is merely a _pointer_ to a location that contains the necessary dependencies.

#### PASOE_REF Variable

The _location_ of the pointer is configured using a project path variable, also called `PASOE_REF`.  

The value of the `PASOE_REF` variable **must** be set to the location containing the **PASOE 11.7.4** `common/lib` sub-folder structure.  

Within the IDE, access the **Project Properties** to set the correct value for the `PASOE_REF` variable.

For example, assuming a local OpenEdge installation (referred to as `DLC`) that includes the PASOE product, the `PASOE_REF` variable can be set _relative_ to that installation folder as: `PASOE_REF = DLC/servers/pasoe` 

Once the `PASOE_REF` variable value has been set correctly, refreshing the project will reveal the dependency files within the `PASOE_REF` project folder.  

**Do not continue until this is working as expected** 

#### Load The User Library 

The Java User Library has been exported to a file in the project called `userlib/pasoe_ref_local.userlibraries`.  This library must be **imported** before the Java Build Path configuration can resolve all project dependencies.

_You may wish to note the full path to the user library file before continuing._

There are several ways to load a user library. One possible method is, within the IDE:

* choose `Window -> Preferences -> Java -> Build Path -> User Libraries`
* click `import`
* browse and locate, or enter the path for the user library file from project
* ensure the checkbox for `pasoe_ref_local` is marked
* click **OK** or **Apply and Close** until back in the IDE the project window

If all goes well, and if you have `Build Automatically` set, you may notice the error markers disappear from the project as it is built.

**Do not continue until this is working as expected** 

## Build

Once built, depending on your IDE filter settings, you may not be able to view the generated .class files, but it's still a good sign if there aren't any error markers within the project.

#### Package

To simplify deployment, a .jar file can be used to ease the packaging of resources and classes.

To assist with the preparation of the .jar file, a Jar Export Descriptor file was provided in the project `nextscheduler.jardesc`.
 
Simply execute the **JAR Export Wizard**, and follow the prompts. Once completed, the JAR Export Wizard should have create a new file: `/deploy/pasoe/NextScheduler.jar`.

Depending on your IDE settings, _some contents may be hidden_. You may find it _beneficial_ to view the entire contents of the `/deploy` folder. Either configure the IDE filters to show all files, or view the contents of the folder outside of IDE.

## Deploy

Now that the project has been successfully built and packaged, the deployment files in the `/deploy` folder need to be copied to **specific locations** within a **PASOE instance**, and the NextScheduler enabled within a webapp.

### OpenEdge

The `/deploy/openedge/**/*.cls` files are **OpenEdge ABL** class files, and must be placed somewhere on the runtime `PROPATH`, keeping the folder structure.

For example, the `QueueProcessor.cls` file should appear as: `/PASInst/openedge/psc/queue/QueueProcessor.cls`.

The file `/deploy/openedge/logging.config` can be used to control the messages output by the OpenEdge class files.  The `logging.config` should appear within the PASOE instance as: `/PASInst/openedge/logging.config`.

### Java
The `/deploy/pasoe/common/lib/NextScheduler.jar` file should be copied to the instance and appear as `PASOEInst/common/lib/NextScheduler.jar`.

The `/deploy/webapps/WEB-INF/taskexec.xml` file should be placed into the WEB-INF of your webapp as: `PASOEInst/webapps/[yourwebapp]/WEB-INF/taskexec.xml`.

## Enable

The files in the `/deploy/ref/` folder are for reference only and demonstrate how to enable the NextScheduler within a webapp, as well as control the logging verbosity.

### WebApp

To enable the NextScheduler for your webapp:

* open the `web.xml` from **your web app**, usually located
    <code>
    /PASInst/webapps/[yourwebapp]/WEB-INF/web.xml
    </code>
* locate the following line  
	<code>&lt;param-value/WEB-INF/oeablSecurity.xml&lt;/param-value&gt;
    </code>
* change it the following four lines
    <pre><code>
    &lt;param-value&gt;
       /WEB-INF/oeablSecurity.xml
       /WEB-INF/taskexec.xml
    &lt;/param-value&gt;
    </code></pre>
 
### Logging

If you would like to increase logging of the NextScheduler, copy the following line from `/deploy/ref/loggin.xml` and paste it to the logging.xml of your webapp `<logger name="psc.services.pasoescheduler" level="TRACE" />`.

## Restart

You will want to restart your PASOE instance to reload the new webapp configuration.

## Enjoy

Have fun with your new Spring-powered ABL task scheduler!
