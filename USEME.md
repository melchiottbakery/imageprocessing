# MIME: More Image Manipulation and Enhancement (Part 2)

## Initialization of program:

In the directory where there the jar file is:

    java -jar Assignment5.jar

The program can support the ability to accept a script file as a command-line option when executing
the jar files.

    java -jar Assignment5.jar -file [name-of-script.txt]  

If a valid file is provided, the program should run the script and exit. If the program is run
without any command line options, then it should allow interactive entry of script commands as
before.

Error prompt:

- Script file with `name-of-script.txt` cannot be found in the directory.
- Invalid Command Line Argument with no text file directory.
- Command word is wrong.

## Acceptance format of Image

The program can support load and save the Image format not only be with the ending of .ppm, but also
be with the ending of .bmp .jpg and .png.

The images processed .bmp/ .jpg/ .png files by this program need to comply with the following rules:

+ File cannot be empty

Here is the reference of the introduction of PPM file:
https://netpbm.sourceforge.net/doc/ppm.html

The images processed .ppm files by this program need to comply with the following rules:

+ Must be a PPM (P3) file
+ File cannot be empty
+ length and width are greater than zero
+ The color range of the maximum value needs to be between 0 and 255 inclusive.
+ The RGB value of the pixel is a positive number and cannot exceed the maximum color value
+ The number of pixels conforms to the length and width values in the file
+ Blank lines in the file are not allowed
+ Every string should be started with #

Errors which Images that do not meet the specifications will be output during the loading process.

## Command Instruction

Adherence to these guidelines will facilitate your use of this program.

Adding spaces before and after the command is allowed. In the command statement, only one space is
allowed between adjacent words, and the statement is ended in a new line executed with the Enter
key. In the script, statements should be separated by blank lines. After the statement is
successfully executed, the user will be prompted that the function has run successfully.

Instructions that do not conform to the rules or are incomplete will be reported as errors。
Correct example:

    [COMMAND] [WhiteSpace] [PARAMETER] [WhiteSpace] [PARAMETER]... [ENTER KEY / NEW LINE]

The first word is the instruction to be executed by the statement, which is not case-sensitive.

You need to set a reference name when loading an image and set a sourceID and destID when doing the
image processing, please notice that, the name of them are case-sensitive. If the name of destID
have existed in the program, after processing, it will be overwritten which is irreversible. Same
thing happened when saving as a file.

**Please note that if the file name exists, the file will be overwritten, please check carefully
before executing the command.**

**You need to load an image into the program to start the subsequent processing steps**

### exit

You can exit the program at any time.

un-saved images will be disappeared when the program is terminated.

### run

    run [path-script-file]

Load and run the script with the following commands in the specified file from `path-script-file`.
The program support hybrid input which means if there is such no exit command in the file, after
running, the program can still be active until the input from user is `exit`.

Error prompt:

- Cannot find the file in the `path-script-file`.
- Recursive running of this file in the text script.
- Any error prompt when running the file.

### load

    load [image-path] [stored-image-name]

Load an image from the specified path and refer it to henceforth in the program by thegiven
reference image name. It can support the conventional files format which the ending of .png .jpg
.bmp and .ppm.

The stored-image-name mentioned next refers to the image name after loading or image processing,
which is case-sensitive.

Error prompt:

- Any other extension except mentioned 4 types, or none of extension in the `image-path`.
- Cannot find the image in the `image-path`.
- Image cannot match the acceptance rules.
- Image cannot be read.

### save

    save [image-path] [stored-image-name]

Save the `stored-image-name` image into the specified path with customized name in the ending of one
of four extensions: .jpg .png .ppm .bmp

Error prompt:

- Any other extension except mentioned 4 types, or none of extension in the `image-path`.
- Cannot find the dirctorary in the `image-path`.
- Image with `stored-image-name` cannot be found in the model of program.

### brighten

    brighten [increment] [stored-image-name] [dest-image-name]

brighten the `stored-image-name` image by the given increment to create a new image, referred to
henceforth by the given destination name `dest-image-name`.

The increment may be positive (brightening) or negative (darkening).
The increment should be any digit (that it can over the maximum color value or less than 0), but the
range of RGB value after brighten processing is from 0 to maximum color.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### horizontal-flip

    horizontal-flip [stored-image-name] [dest-image-name]

Flip an image with the name of `stored-image-name` horizontally to create a new image, referred to
henceforth by the given destination name `dest-image-name`.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### vertical-flip

    vertical-flip [stored-image-name] [dest-image-name]

Flip an image with the name of `stored-image-name` vertically to create a new image, referred to
henceforth by the given destination name `dest-image-name`.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### grayscale

    greyscale [greyscale-method-command] [stored-image-name] [dest-image-name]

Create a greyscale image named `dest-image-name` with the `greyscale-method-command`
command-processed of the image with the given name `stored-image-name`.

The option of the none case-sensitive `[greyscale-method-command]` are following:

+ `red-component`: to process the red-component of the image only in the red channel

+ `blue-component`: to process the blue-component of the image only in the blue channel

+ `green-component`: to process the green-component of the image only in the green channel

+ `value-component`: to process a greyscale image within the maximum value of the three components
  for each pixel from the original image

+ `luma-component`: to process a greyscale image based on the following formula from the original
  image : 0.2126 * R + 0.7152 * G + 0.0722 * B (R,G,B is the value of the R,G,B color in each
  pixels)

+ `intensity-component`: to process a greyscale image within the average of the three components for
  each pixel from the original image

  greyscale [stored-image-name] [dest-image-name]

It can support to create a grayscale image data within ColorTransformation of Luma. It is the same
effect as the `luma-component` in the `greyscale-method-command`

Content that does not belong to the above directives will be output as an error。

Error prompt:

- Any other method does not belong to the above directives.
- Image with `stored-image-name` cannot be found in the model of program.

### rgb-split

    rgb-split [stored-image-name] [dest-image-name-red] [dest-image-name-green] 
    [dest-image-name-blue]

split the given `stored-image-name` image into three greyscale images in the order of its red, green
and blue components respectively.

There is no restriction for the name of dest-image-name: although you set the name of the
`dest-image-name-red` is image-blue, the program still will process the image in the red
component one.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### rgb-combine

    rgb-combine [dest-image-name] [stored-red-image] [stored-green-image] [stored-blue-image]

Combine the three greyscale images into a single image named `dest-image-name` via each color
components in the order of red, green and blue components from the three images.

Error prompt:

- Different size of three images.
- Images with `stored-red-image`,`stored-green-image`,`stored-blue-image` cannot be found in the
  program.

### blur

    blur [stored-image-name] [dest-image-name]

Blurring can be done by applying this filter of 'Gaussian" blur kernel to every channel of every
pixel to produce the output image. The filter can be repeatedly applied to an image to blur it
further.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### sharpen

    sharpen [stored-image-name] [dest-image-name]

Sharpening accentuates edges (the boundaries between regions of high contrast), thereby giving the
image a "sharper" look by applying filter of 'sharpen' kernel. Same as blur operation, it is
possible to repeatedly sharpen the image.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### sepia

    sepia [stored-image-name] [dest-image-name]

It will convert a normal color image into a sepia-toned image. This conversion can be done using
color transformation matrix in the processor.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

### dither

    dither [stored-image-name] [dest-image-name]

It will convert a image in the name of `stored-image-name` to greyscale with dithering with the new
name of `dest-image-name`.

Error prompt:

- Image with `stored-image-name` cannot be found in the model of program.

## Execution Examples

testRun.txt and RUNME.txt is under the res/ folder.

testRun.txt is made to test `run` command in the script file.
RUNME.txt is a file with a series of command are including the original command the extension
command.

### Examples with ‘Run’ Command to Execute Script File

The same command are stored in a script called script.txt (except exit command), such file contains
exit command,
which means that the program will terminate after to execute of the script file,
after the program is running by directly starting the program from MainProgram.java, or
by command prompt with initialized .class file, if the following command can be utilized to
execute the script.

    run RUNME.txt

### Examples of Initialization with Command File

    java -jar Assignment5.jar -file RUNME.txt

### Examples with Command on the Terminal

    #load winnie.ppm and call it 'test'
    load winnie.ppm test

    #save winnie images in different extension
    save resultFiles/winnie.jpg test
    save resultFiles/winnie.bmp test
    save resultFiles/winnie.jpeg test
    save resultFiles/winnie.png test
    
    #reload them
    load resultFiles/winnie.jpeg testJpeg
    load resultFiles/winnie.jpg testJpg
    load resultFiles/winnie.png testPng
    load resultFiles/winnie.bmp testBmp

    #flip the winnie horizontally and call it 'testHorizontalFlip' 
    horizontal-flip test testHorizontalFlip

    #flip the winnie vertically and call it 'testVerticalFlip' 
    vertical-flip test testVerticalFlip

    #split the winnie in three grayscale image and call them 'test1' 'test2' 'test3'
    rgb-split test test1 test2 test3

    #combine the winnie with three grayscale image
    rgb-combine test test1 test2 test3

    #brighten winnie by adding 10  
    brighten 10 test testBrighten

    #darken winnie by adding 10  
    brighten -10 test testDarken
    
    #create a greyscale using only the red component, as an image testRed
    greyscale red-component test testRed

    #create a greyscale using only the green component, as an image testGreen
    greyscale green-component test testGreen

    #create a greyscale using only the blue component, as an image testBlue
    greyscale blue-component test testBlue

    #create a greyscale using only the luma component, as an image testLuma
    greyscale luma-component test testLuma

    #create a greyscale using only the intensity component, as an image testIntensity
    greyscale intensity-component test testIntensity

    #create a greyscale using only the value component, as an image testValue
    greyscale value-component test testValue

    #create a greyscale using color transformation of luma-component (same effect), as an image testGreyScaleColorTrans
    greyscale test testGreyScaleColorTrans

    #create a sepia image and call testSepia
    sepia test testSepia

    #create a dither image and call testDither
    dither test testDither

    #create a blur image and call testBlur
    blur test testBlur

    #create a blur image again and call testBlur2nd
    blur testBlur testBlur2nd

    #create a sharpen image and call testSharpen
    sharpen test testSharpen

    #create a blur image again and call testSharpen2nd
    sharpen testSharpen testSharpen2nd

    #run the run.txt file
    run testRun.txt
    (inside the testRun.txt)
    (# load manhattan.png as testManhatthan)
    (load manhattan.png testManhatthan)

    #save the winnie file in the extension of .png
    save resultFiles/winnieOrigin.png test

    #save the winnieBlur file in the extension of .png
    save resultFiles/winnieBlur.png testBlur

    #save the winnieBlur2nd file in the extension of .jpg
    save resultFiles/winnieBlur2nd.jpg testBlur2nd

    #save the winnieDither file in the extension of .png   
    save resultFiles/winnieDither.png testDither

    #save the winnieGreyScaleColorTrans file in the extension of .png  
    save resultFiles/winnieGreyScaleColorTrans.png testGreyScaleColorTrans

    #save the winnieSepia file in the extension of .png  
    save resultFiles/winnieSepia.png testSepia

    #save the winnieSharpen file in the extension of .png 
    save resultFiles/winnieSharpen.png testSharpen

    #save the winnieSharpen2nd file in the extension of .bmp 
    save resultFiles/winnieSharpen2nd.bmp testSharpen2nd

    #load winnieSharpen2nd.bmp as bmpTransfer
    load resultFiles/winnieSharpen2nd.bmp bmpTransfer

    #save the file into .ppm
    save resultFiles/winnieSharpen2ndbmpToppm.ppm bmpTransfer

    #save the file into .jpg
    save resultFiles/winnieSharpen2ndbmpTojpg.jpg bmpTransfer


    (#exit the program
    exit)
    (it can still receive the input until "exit" command.)

### The successful output

    Load Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Load Operation Success
    Load Operation Success
    Load Operation Success
    Load Operation Success
    Horizontal-Flip Operation Success
    Vertical-Flip Operation Success
    Split Operation Success
    Combine Operation Success
    Brighten Operation Success
    Brighten Operation Success
    GrayScale red-component Operation Success
    GrayScale green-component Operation Success
    GrayScale blue-component Operation Success
    GrayScale luma-component Operation Success
    GrayScale intensity-component Operation Success
    GrayScale value-component Operation Success
    Color Transformation GrayScale Success
    Color transformation sepia Operation Success
    Dither Operation Success
    Blur Operation Success
    Blur Operation Success
    Sharpen Operation Success
    Sharpen Operation Success
    Load Operation Success
    File Execution Complete (The successful prompt for running testRun.txt)
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Save Operation Success
    Load Operation Success
    Save Operation Success
    Save Operation Success
    File Execution Complete (The successful prompt for running RUNME.txt)

    --
    (it can still receive the input until "exit" command.)

## Citation

The example image is winnie-the-pooh.

https://fr.fanpop.com/clubs/winnie-the-pooh/images/2022941/title/winnie-pooh-blustery-day-screencap

The copyright protection for Winnie-the-Pooh have been expired in 2022.

It's open to the public.



