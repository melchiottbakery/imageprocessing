# MIME: More Image Manipulation and Enhancement (Part 2)

(UPDATED DATE Mar/29/2023)

## Introduction

```diff
! UPDATE
```

The updated program will support new image processing operations based the image processing
application and add support for more conventional file/image formats.
It now can support .jpg .jpeg .png .bmp .ppm files.

## model-view-controller Design

```diff
! UPDATE
```

The new mainprogram can execute a script file as a command-line option.
The model-view-controller design has been revised as following:

- The RGB color data will stored in the Processor part instead in an object of an Image.

  In the past, object called Image was used to store data. after the image was processed, a new obj Image 
  would be returned to be created and stored in the model part. If the new methods are extened
  in the future, the usual way is to create a new object class ImageExtend which extends the Image class.
  Keep the methods of the original Image class unchanged, and the extra methods are in the new ImageExtend class.
  This would introduce a problem: when the extended object is executed by the super class method in the class of
  Image, a new Image obj will be returned instead of ImageExtend object, where there are no such extra methods 
  which the new created object cannot call the extended method further. Therefore, this object design was abandoned 
  and other storage method were considered that using 3D array.

  Storing image data as a 3D array can indeed be a convenient format for processing and manipulating the image data,
  as it allows easy access to individual pixels and their values. Additionally, using a reference name (ID) to 
  identify each image and storing the images in a Map data structure can simplify management and retrieval of the 
  images in the model.

  It is also true that storing image data in this format can make it easier to extend and modify the image processing functions without having to depend on a specific Image extension. The controller will read and translate the image file into the 3D array data and pass them into the model.

  This design has a good extensibility: if there will the subsequent extension of the function be added, only the helperfunction of the corresponding
  function needs to be added to the model is enough. The storage of data does not need to depend on the new
  Image class

- The controller part has been imported the Class ImageIo which can support more conventional file or
  image formats and has changed to the command pattern design which can extend the extra commands
  in the furture.

- The File I/O function has been seperate from model to controller completely. 

  Now the model part only focuses on image processing, while the controller part will focus on accepting user commands and pass them to the model, reading and decoding the image into an array and passing it to the model, extracting the data of image from the storage part of the model and packaging it into a corresponding the image file with the extension is stored in the corresponding address when saving the image.


### Main Program

- Class MainProgram

  This class is designed for the initialization of the application.

    ```diff
    ! UPDATE
    ```

  It can support the ability to accept a script file as a command-line option. If a valid file is provided,
  the program should run with the script. If the program is run without any command line
  options, then it allow interactive entry of script commands as before.

### model

- Interface Processor (Same to the old version)

  This interface represents a model.Processor and its basic functions of image processing.

  Such as: combine and split the image into three grayscale images, brighten and
  darken the image, flip and make a grayscale image. It will process the data of image from which the controller passes in.

- Interface ProcessorExtension

    ```diff
    + NEW 
    ```

  This interface extends the `Processor` have more functions of image processing such as
  colorTransformation, filtering, and dithering incluing the basic functions of
  the `Interface Processor`.

- Class ImageProcesser

  This class is implemented the `Interface Processor`. 
  It will run the operation command from the controller.

    ```diff
    ! UPDATE
    ```

  All the images will be stored as RGB information of an image as a 3D array field and maxValue as a
  int value in the from Controller or processed by functions in the Processor in the map of
  them with the its named ID. 

  The previous function belongs to Class Image (old ) have been moved inside the Processer part.


- Class ImageProcesserExtension

    ```diff
    + NEW
    ```

  This class exends the `Class ImageProcessor` and implements `Interface ProcessorExtension` but has
  extra functions such as color transformation, dither, sepia and etc. it is initialized after the operation of
  MainProgram. 

  Same to the `ImageProcessor` which will receive a command sentence from the controller and controller 
  passes an 3d image data to the ImageProcessor.


- Interface Image

    ```diff
    - REMOVED
    ```
  The RGB information of an image as a 3D array field has been stored in `Processor` part. It is not
  necessary to create this interface.

- Class RegularImage

    ```diff
    - REMOVED
    ```
  Same reason as above

### controller （Package controller）

The revised design of controller has been changed to the COMMAND PATTERN DESIGN. ALL of previous
commands have been moved into the `Package controller.Command`.

- Interface controller

  This interface represents a controller and its execute function.

- Class ProcessorController

  This class is implemented the `Interface controller`. It can accept the typing input from users or script and make splitting and
  judgement to pass the response command which conforms to the input specification to the Processor
  to perform the corresponding processing function eventually. Commands that do not conform to the
  input specification will be output with an error. The errors received from the processor are also
  be output. After the model.Processor handles the processing correctly, it will output the success
  prompt of the response command.
  It can suppport loading and saving the .ppm files from the correct path.

    ```diff
    ! UPDATE
    ```
  It has been seperate from the command which in Package Command.
  It will pass the command and tranlated 3d array data into the model part to process.


- Class ProcessorControllerExtension

    ```diff
    + NEW
    ```

  This class extends Class ProcessorController which inherits the basic command function map and it
  owns new command for the extra function in the command map. It is initialized after the operation
  of `Class MainProgram`.

  It can suport functions of reading and loading the image ending .png .bmp .jpeg using `Class ImageIO`.
  Then images will be decoded and passed and stored via a helper function in 3D array as RGB color information in the map with
  its named ID to the model part of processor.

### controller - Commands （Package controller.Command）

```diff
+ NEW 
```

The design of controller has been revised as COMMAND PATTERN DESIGN which all the command function
have been separated from the controller. The commands are incluing controller commands in the old
version.

- Interface Command

  This is an interface to represents a single command and its exectedCommand function.

- Class(Abstract) AbstractCommand

  This class implements the `Interface Command` which will receive a sentence of command.

- Class BrightenDarken

  This class extends `AbstractCommand` in order to varify and execute the bright/darken command.

- Class Combine

  This class extends `AbstractCommand` in order to varify and execute the Combine command.

- Class GrayScale

  This class extends `AbstractCommand` in order to varify and execute the GrayScale command.

- Class HorizontalFlip

  This class extends `AbstractCommand` in order to varify and execute the HorizontalFlip command.

- Class Load

  This class extends `AbstractCommand` in order to varify and execute the Load command.

  It will check the extension of the file, read the file correctly, decode the raw file into 3D RGB array and pass to the processor.

- Class Save

  This class extends `AbstractCommand` in order to varify and execute the Save command.

  It will extract the array data from the model of processor and transfer the coresponding format and save into the correct path.

- Class Split

  This class extends `AbstractCommand` in order to varify and execute the Split command.

- Class VerticalFlip

  This class extends `AbstractCommand` in order to varify and execute the VerticalFlip command.

### controller - CommandExtension （Package controller.commandextension）

```diff
+ NEW 
```

The command pattern design is also used for extended commands. The new package is just to
distinguish that these are newly added.

- CommandExtension

  This is an interface to represents a single command and its exectedCommand function.

- AbstractCommand

  This class implements the `Interface CommandExtension` which will receive a sentence of command.

- ColorTransformation

  This class extends `AbstractCommand` in order to varify and execute the ColorTransformation
  command.

- Dither

  This class extends `AbstractCommand` in order to varify and execute the Dither command.

- Filter

  This class extends `AbstractCommand` in order to varify and execute the Filter command.

- GrayScale

  This class extends `AbstractCommand` in order to varify and execute the GrayScale command.

## How to run the program

Please see in the useme.md.

## CS5010Assignment4  (lastest update 15/03/2023 )

## Introduction

This image processing application based on text-based user interfaces is designed for the
functions of image processing, such as visualizing components, image flipping, brightening
or darkening an image, converting to greyscale, combining channels into one image and accepting
the input from users to run these operations.

## model-view-controller.controller Design

Adherence to an MVC design is our first priory. Within this assignment, our model is represented
as the combination of model.ImageProcessor and model.Image, where model.ImageProcessor stores a map
that maps to
the individual image, and consist of individual function call to process a specific image, however
the detail on the modification lay within the image class. For our controller, we have created an
controller.ProcessorController class, which serves the purpose of taking in the input either from
the user
or from a specific file, if such input command is valid, which means that the command consist of
correct corresponding amount of parameter, and it is a command that is supported, then it will hand
it to the processor model that it stored. For the view part, since there's no specification on the
view, the view for this assignment will be just printing out to the outputstream object, in the
user's perspective, the command prompt where the user
executes the command, therefore if the processor returned with any error or
the controller detected any error while parsing user input, the controller will catch
such error and represent it to the user prompt, if not, after successful execution, an execution
successful message will also be provided.<br><br>
In general, the flow of the program will be:<br>
controller Reads the input, present error message if the command is valid, else hand the command
over to the processor model<br><br>
model.Processor executes such command and throw any error encountered<br><br>
controller catches any error and present it, or present the message of execution successful.

### Interface Processor

This interface represents a model.Processor and its function of image processing.

### Interface controller

This interface represents a controller.controller and its function.

### Interface Image

This interface represents an model.Image and its function.

### Class MainProgram

This class is designed for the initialization of the application.

### Class RegularImage

This class is implemented the interface of Image which is a cornerstone where image was stored
with its Image ID(name), the width and the height, the maximum color value and RGB data in 3D-array.
It will operate the functions for image processing when receiving the corresponding
command from model.Processor.

### Class model.ImageProcessor

This class is implemented the interface of Processor and initialized after the operation of
MainProgram. It will run the operation command from the controller. All the images will be stored
as objects from an existed path or processed by functions in the Processor. After the Processor
confirms that the path/image exists, it passes the corresponding command to the image for
processing. Commands that do not conform to the Processor specification will be thrown as an
exception to the controller.

### Class ProcessorController

This class is implemented the interface of controller and initialized after the operation of
MainProgram. It can accept the typing input from users or script and make splitting and
judgement to pass the response command which conforms to the input specification to the Processor
to perform the corresponding processing function eventually. Commands that do not conform to
the input specification will be output with an error. The errors received from the processor
are also be output. After the model.Processor handles the processing correctly, it will output the
success prompt of the response command.

## Command Instruction

Adherence to these guidelines will facilitate your use of this program.

### Initialization of program:

The execution of the program is stored within MainProgram.java, if wish to run from command prompt,
the following initialization is required since the `.class` file is not provided:
In the root directory:

    javac -d out src/*.java
    java -cp ./out MainProgram

### Acceptance of commends and their formats

This image application could only process the PPM image.

Here is the reference of the introduction of PPM file:
https://netpbm.sourceforge.net/doc/ppm.html

The images processed by this program need to comply with the following rules:

+ Must end with .ppm
+ Must be a PPM (P3) file
+ File cannot be empty
+ length and width are greater than zero
+ The color range of the maximum value needs to be between 0 and 255 inclusive.
+ The RGB value of the pixel is a positive number and cannot exceed the maximum color value
+ The number of pixels conforms to the length and width values in the file
+ Blank lines in the file are not allowed
+ Every string should be started with #
  Errors which Images that do not meet the specifications will be output during the loading process.

Adding spaces before and after the command is allowed. In the command statement, only one space
is allowed between adjacent words, and the statement is ended in a new line executed with the Enter
key. In the script, statements should be separated by blank lines. After the statement is
successfully executed, the user will be prompted that the function has run successfully.

Correct example

    [COMMAND] [PARAMETER]... [ENTER KEY / NEW LINE]

The first word is the instruction to be executed by the statement, which is not case-sensitive.
Unless otherwise specified, parts of `[PARAMETER]` are case-sensitive words or digits.

**Please note that if the file name exists, the file will be overwritten, please check carefully
before executing the command.**

#### exit

exit the program

#### load

    load [image-path] [image-name]

Load an image from the specified path and refer it to henceforth in the program by the
given image name.

When you do image processing, you must load the image before you can perform subsequent steps.
If the path cannot be found and the image does not meet the image rules, an error will be prompted.

The stored-image-name mentioned next refers to the image name after loading or image processing,
which is case-sensitive. If it does not exist in the model.Processor, an error will be output.

#### brighten

    brighten [increment] [stored-image-name] [dest-image-name]

brighten the image by the given increment to create a new image, referred to henceforth by
the given destination name. The increment may be positive (brightening) or negative (darkening).
The increment should be any digit(that it can over the maximum color value or less than 0 ),
but the range of RGB value after brighten processing is from 0 to maximum color.

#### horizontal-flip

    horizontal-flip [stored-image-name] [dest-image-name]

Flip an image horizontally to create a new image, referred to henceforth by the given
destination name.

#### vertical-flip

    vertical-flip [stored-image-name] [dest-image-name]

Flip an image vertically to create a new image, referred to henceforth by the given destination
name.

#### greyscale

    greyscale [greyscale-commend] [stored-image-name] [dest-image-name]

Create a greyscale image with the commend-processed of the image with the given name, and refer to
it henceforth in the program by the given destination name.

The option of the none case-sensitive `[greyscale-commend]`:

+ red-component: to process the red-component of the image
+ blue-component: to process the blue-component of the image
+ green-component: to process the green-component of the image
+ value-component: to process a greyscale image within the maximum value of the three components for
  each pixel from the original image
+ luma-component :to process a greyscale image based on the following formula from the original
  image : 0.2126R + 0.7152G + 0.0722B
+ intensity-component: to process a greyscale image within the average of the three components for
  each pixel from the original image

Content that does not belong to the above directives will be output as an error。

#### rgb-split

    rgb-split [stored-image-name] [dest-image-name-red] [dest-image-name-green] 
    [dest-image-name-blue]

split the given image into three greyscale images in the order of its red, green and
blue components respectively.

There is no restriction for the name of dest-image-name: although you set the name of the
dest-image-name-red is image-blue, the program still will process the image in the red
greyscale one.

#### rgb-combine

    rgb-combine [dest-image-name] [stored-red-image] [stored-green-image] [stored-blue-image]

Combine the three greyscale images into a single image that in the order of red, green and blue
components from the three images in the same size respectively.

#### save

    save [image-path] [stored-image-name]

Save the PPM image with the given name to the specified path which should include the name
of the stored file.
currently the program only supports the saving of PPM files, saving executed on other
types of files will result in an error

Please save the images that you want before exiting the program, un-saved images will be
disappeared when the program is terminated.
Please make sure that your path is right and the stored-image-name is in loaded or processed.
The saved images will be the format of PPM image file.

#### run

    run [path-script-file]

Load and run the script commands in the specified file.

### Examples with command on the terminal

    #load winnie.ppm and call it 'winnie'
    load res/winnie.ppm winnie

    #brighten winnie by adding 10  
    brighten 10 winnie winniebBrighten

    #darken winnie by minus 10  
    brighten -10 winnie winniebDarken

    #flip winnie vertically
    vertical-flip winnie winnieVertical

    #flip the winnie horizontally
    horizontal-flip winnie winnieHorizontal

    #create a greyscale using only the value component, as an image winnieValueComp
    greyscale value-component winnie winnieValueComp

    #create a greyscale using only the Intensity component, as an image winnieIntensityComp
    greyscale intensity-component winnie winnieIntensityComp

    #create a greyscale using only the Luma component, as an image winnieLumaComp
    greyscale luma-component winnie winnieLumaComp

    #split the winnie in three grayscale image
    rgb-split winnie winnieRedComp winnieGreenComp winnieBlueComp

    #combine the winnie with three grayscale image
    rgb-combine winnie winnieRedComp winnieGreenComp winnieBlueComp

    #save all the files
    save res/winnie.ppm winnie
    save res/winniebBrighten.ppm winniebBrighten
    save res/winniebDarken.ppm winniebDarken
    save res/winnieVertical.ppm winnieVertical
    save res/winnieHorizontal.ppm winnieHorizontal
    save res/winnieValueComp.ppm winnieValueComp
    save res/winnieIntensityComp.ppm winnieIntensityComp
    save res/winnieLumaComp.ppm winnieLumaComp
    save res/winnieRedComp.ppm winnieRedComp
    save res/winnieGreenComp.ppm winnieGreenComp
    save res/winnieBlueComp.ppm winnieBlueComp
    
    #exit the program
    exit

### Examples with command in the script

The same command are stored in a script called script.txt, such file contains exit command,
which means that the program will terminate after to execute of the script file,
after the program is running by directly starting the program from MainProgram.java, or
by command prompt with initialized .class file, if the following command can be utilized to
execute the script,

    run script.txt

if wished to run from command prompt but the .class file are not generated, the initialization
of the program is needed, therefore the user is required to locate to
the root directory of the program, and after such,
to execute the script file, the full command necessary to be executed will be

    javac -d out src/*.java
    java -cp ./out MainProgram
    run script.txt

### The successful output

    Load Operation Success
    Brighten Operation Success
    Brighten Operation Success
    Vertical-Flip Operation Success
    Horizontal-Flip Operation Success
    GrayScale value-component Operation Success
    GrayScale intensity-component Operation Success
    GrayScale luma-component Operation Success
    Split Operation Success
    Combine Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success

## Citation

The example image is winnie-the-pooh.

https://fr.fanpop.com/clubs/winnie-the-pooh/images/2022941/title/winnie-pooh-blustery-day-screencap

The copyright protection for Winnie-the-Pooh have been expired in 2022.

It's open to the public.



