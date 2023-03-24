# AssemblyLine

Project I made at ITMO university for Java programming course.

Code can be a little bit messy, so sorry about that.

## How to compile this code?

1. Get org.json package from https://github.com/stleary/JSON-java and add it to the root of the project under the name **json.jar**
2. Run **compile.bat** under Windows or **compileunix.bat** under Linux

## How to run my compiled code?

Run **runclient.bat** or **runserver.bat** depending on which one you need.

### Server parameters

* -p \[number]: Port
* \[fileName]: Load save file

Without parameters listens to localhost:5400

### Client parameters

1. \[hostname]: Hostname
2. \[port]: Port
Only in this order!

Without parameters connects to localhost:5400

## How to assemble .jar file?

Run either jarclient.bat or jarserver.bat, depending on what you need.  

## Are there any demo files?

Of course! demoFiles folder has a couple of demo files

* **saveFileDemo.json** - demo save file of a collection (like what you get when you save collection on server yourself)
* **scriptDemo.txt** - script that can be ran on client on server to add a lot of vehicles to the collection
* **scriptDemoGenerator.py** - python script to generate scripts that create a lot of vehicles. Just like in **scriptDemo.txt**

---

This code uses The Unlicense license, so feel free to use anything in this repository for whatever you need - which is probably your own project for ITMO, so good luck with that.