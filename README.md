# MVC Framework

## Features
  - Create views from a single extends.
  - Configure controller and model classes for each view class.
  - No need to create controller and model objects, the instances are automatically generated for you.
  - Log runtime data in .txt files.
  - Configure max file size for the log files.

## Installation

### Prerequisites

- Java JDK version 11 or higher. If don't  have it, you can get one here [https://adoptopenjdk.net/](https://adoptopenjdk.net/)

- Your favorite IDE, for this tutorial I'm going to use VSCode

  

**Step 1.** [Download the jar file](https://github.com/alanmoraales/mvc-framework/releases/tag/0.1) of this library.

**Step 2.** Add the jar to the classpath of your Java project. I'm gonna show you how to do it in VSCode.



###### Adding the library in VSCode

**Step 1.** Install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) in VSCode.

**Step 2.** Activate the extension by creating a new **.java** (for example *Client.java*)file in your project. You will notice that a "Java Projects" tab is now showing in your side bar. 

![image-20210115021458428](https://i.imgur.com/rdK0KXg.pngg)

**Step 3.** Create a *lib* folder in the root directory of your project and drop the jar file there. This will automatically add the library to you project's classpath.

![image-20210115021940947](https://i.imgur.com/BTbNQe8.png)



###### Adding the library in VSCode - Alternative

If for any reason the first method didn't worked, you can try the manual installation.

**Step 1.** Install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) in VSCode.

**Step 2.** Open the Java projects tab, you'll see your project name inside.

**Step 3.** Open your project, you'll see a Referenced Libraries section and a plus sign, click it and find the jar file.

![alternative-installation-step2](https://i.imgur.com/eaWItik.png)



This is it for the installation part, you should be ready to use the library.


## Usage example
In this example we're gonna build a "Hello world", using the classes provide by the framework.

### Creating a model and a controller
To create a controller you only need to extend your class from Controller class.

````java
import mvc.Controller;

public class HelloWorldController extends  Controller {
  ...
}
````

you can add whatever you want to the class, but for this example we're just gonna have the extends.

To create a model, you extend from the Model class.

````java
import mvc.Model;

public class HelloWorld extends Model {
  public HelloWorld() {
    this.setData("Not Hello World");
  }
}
````

### Creating a view
To build our view we need to extend from the View class.

````java
import mvc.View;

public class HelloWorldView extends View {

  @Override
  public void updateData() {
    String helloWorld = (String) this.model.getData();
    System.out.println(helloWorld);
  }

  public void sayHello() {
    this.modelController.updateModel("Hello World");
  }
}
````
After you extend from View, the compiler will ask you to override the updateData function. This function runs everytime the data in the model updates.
We also defined a sayHello functions, we´re gonna run this function in the main class to update the data in the model and watch the updateData function in action.

#### Adding config to the view
The next step is to connect the view with its corresponding controller and model. To do that, we need to create a properties file with the same name of the view class in the root directory.

![properties-in-root](https://i.imgur.com/mEDqHGD.png)

Inside the file you have to define the controller and model class like this:

````
model=models.HelloWorld
controller=controllers.HelloWorldController
````

Notice that you need to define the package where the class is, like when you make an import.

That's it, when you create a instance of the view, the controller and model class will be automatically created by the framework.

To close the example, here's the main class.

````java
import views.HelloWorldView;

public class Main {
  public static void main(String[] args) {
    var helloWorldView = new HelloWorldView();
    helloWorldView.sayHello();
  }
}
````

To run the project just go to the main class and click run in the main method.

## The logger

The logger functionality works without any configuration, but you are still able to deactivated or to change the max file size of the generated files. To do it, you just need to created a `log.properties` files in the root directory.

````
activated=true
maxFileSize=3000
````

the maxFileSize represents the max number of bytes a single log file can be.

## Components

![components-diagram](https://raw.githubusercontent.com/alanmoraales/mvc-framework/9151c4a9dcf73a383c55243ae881afa2518b1ae5/docs/component_diagram_framework.svg)

### Model
Stores the data necessary for your application to work.

#### Dependencies:
This components does not have any dependency from another component.

#### Out interfaces:
  - Set data: provides functionality to update the current store data.
  - get updated data: helps view get updated data when a change happen.

#### In interfaces: 
None.

#### Artifacts:
None.

### Controller
Helps views to update data in the models.

#### Dependencies:
  - From Model: set data.

#### Out interfaces:
  - Update model: helps view updated data in the models.

#### In interfaces: 
  - Set data: updated data in the models.

#### Artifacts:
None.

### View
Can get and update data from the models.

#### Dependencies:
  - From Model: get updated data.
  - From Controller: update model.
  - From ConfigLoader: load configuration.

#### Out interfaces:
None.

#### In interfaces: 
  - get updated data: get updated data from the models.
  - update model: sets new data in the models.
  - load configuration: reads configuration files, create instances of controller and model for the view.

#### Artifacts:
None.

### ConfigLoader
Reads and load configuration from the properties files. Also creates instances of controllers and models.

#### Dependencies:
  - From Logger: log messages.

#### Out interfaces:
  - load configuration: reads configuration files, create instances of controller and model for the view.

#### In interfaces: 
  - log messages: add a message to the log.

#### Artifacts:
  - model configuration (properties file for the view)

### Logger
Reads logger configuration file, creates and writes log files.

#### Dependencies:
None.

#### Out interfaces:
  - log messages: add a message to the log.

#### In interfaces: 
None.

#### Artifacts:
  - logger configuration (properties file for the logger)


## Classes

![class-diagram](https://raw.githubusercontent.com/alanmoraales/mvc-framework/9151c4a9dcf73a383c55243ae881afa2518b1ae5/docs/class_diagram_framework.svg)

### Model
Stores data and provide a way to get updated data and to update the data.

#### Dependencies

This class has no dependencies.

#### Attributes
| name | type | visibility | default value | description |
|------|-----------| ---- | ---- | ---- |
|data| Object | private | null | the data that you want to store in the model |
|observers| ModelObserver[] | private | empty array | a list of observers that can know when the data is updated (ends up being the view classes) |

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
|setData| data: Object | void | public | update model data | let user change the stored data |
|getData| none | Object | public | get updated data | let user get the data |
|sendUpdateNotification| none | void | private | get updated data | send notification to the views when the data is updated |

### Controller
Provide functionality to let user update the data in the model

#### Dependencies

This class has no dependencies

#### Attributes
| name | type | visibility | default value | description |
|------|-----------| ---- | ---- | ---- |
|model| Model | private | the model you provide in the configuration | the model provide in the configuration |

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
| setModel | model: Model | void | public | update model | let user change the model object |
|updateData| data: Object | void | public | update model | let user change the model data |

### ModelObserver
An interface that let classes listen for changes in the data of its model

#### Dependencies

This class has no dependencies

#### Attributes

Since this is an interface, it has no attributes.

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
|onUpdateData| none | void | public | get updated data | runs everytime the data in the model changes |

### Logger
Reads logger configuration and provides functionality to log messages.

#### Dependencies

This class has no dependencies

#### Attributes
| name | type | visibility | default value | description |
|------|-----------| ---- | ---- | ---- |
|activated| boolean | private | true | stores if the logger is activated or not. |
|instance| Logger | private | null | the logger instance, the logger is a singleton to avoid problems when having multiples views running at the same time. |

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
|log| message: string | void | public | log messages | the message you send will be log in the log files and in the console |
|getIntance| none | Logger | public | log messages | returns the instance of the logger |

### ConfigLoader
Loads the view configuration, creates instances for the controller and model.

#### Dependencies

Has a dependency with the logger, messages are logged when running the load process.

#### Attributes

Has no attributes

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
|loadConfiguration| view: View | void | public | load configuration | Takes a view and load its corresponding configuration from the properties file. |

### View
Can update the data in the model and be notified when the data in the model changes.

#### Dependencies

Has dependencies with:

- Controller: let view update the data in the model.
- Model: let view know when the data is updated and let view get updated data.
- ConfigLoader: loads the view configuration, set the model and controller classes.
- ModelObserver: inherit the onUpdateData function that runs everytime the data in the model is updated.

#### Attributes
| name | type | visibility | default value | description |
|------|-----------| ---- | ---- | ---- |
|controller| Controller | private | the class in the configuration | let view updated the data in the model |
|model| Model | private | the class in the configuration | let view get the data in the model |

#### Functions
| name | arguments | return value | visibility | service | description |
|------|-----------| --- | ---- | ---- | ---- |
| setController | controller: Controller | void | publcic | load configuration | the load config uses it to set the controller instance |
|setModel| model: Model | void | public | load configuration | the load config uses it to set the model instance |

## Sequence

#### How the framework loads configuration

![creating-instance](https://raw.githubusercontent.com/alanmoraales/mvc-framework/a702be86eaae875ba1e2f7e16eccc1143d18fea5/docs/creating_instances.svg)

#### How the flow works for the end user (hello world example)

![hello-world](https://raw.githubusercontent.com/alanmoraales/mvc-framework/a702be86eaae875ba1e2f7e16eccc1143d18fea5/docs/hello%20world.svg)
