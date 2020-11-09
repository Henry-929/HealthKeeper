# HealthKeeper
Mobile development technology based on JAVA language, creating healthy diet and calorie management APP.

# COMP5216 Mobile Computing - Project
**University of Sydney**
*Semester 2, 2020*

## Description
An Android App that makes record people’s calorie intake and provides daily health reports including diet proportion, nutrient intake, and predict user weight.

## Description
Healthkeeper keeps track of the user's diet and analyses the user's health status. The main functions are to record food information by taking photos or adding  meals manually, and to provide health reports of the user's daily intake and predict his weight.

## Installation
1. Download the app source code, or clone it to your directory, or get from version control: https://github.com/Henry-929/HealthKeeper.git
2. Open Android Studio - Open file from your directory
3. After Gradle successfully built, the app should be able to run in an Android Virtual Device compatible with the Testing Device, or on a compatible Android Device.

### Testing Environment
- Device1: OnePlus 7 pro
    - Display Resolution: 3120x 1440pixels, 19.5:9 ratio (~516 ppi density)
    - Android OS: Hydrogen OS 10.0.7.GM21
- Device2: HUAWEI P30
    - Display Resolution: 2340 x 1080 pixels, 19.5:9 ratio (~398 ppi density)
    - Android OS: version 10.0.0.316
- Device3: Xiaomi CC 9 
    - Display Resolution: 2340 x 1080 pixels, 19.5:9 ratio (~403 ppi density)
    - Android OS: version 9 (MIUI-V11.3.4.PFCCNXM)
- Device4: Razer Phone 2.0
    - Display Resolution: 2560 x 1440 pixels, 16:9 ratio
    - Android OS: version 9
- API: Android under API 29
- Java: Java 8  
- IDE: Android Studio 4.0.1

### Android Studio
- Modify the project-level build.gradle file to use Gradle's Google service plugin. 
Add classpath 'com.google.gms:google-services:4.3.4'. 
Import the Firebase BoM, through
implementation platform('com.google.firebase:firebase-bom:25.11.0')
Locate build.gradle (Module.app) to be able to preview the layout, through implementation 'com.android.support:design:25.0.1’,  
implementation 'com.android.support:support-v4:25.0.1',
implementation group:'com.google.android.material', name:'material', version: '1.1.0-alpha05'.
- Layout Preview uses Pixel 2 as a default device
- For proper operation of the application camera functions, the emulator API should be API 29 or less.

## Libraries
- aip-java-sdk-4.15.1
- json-20160810
- slf4j-api-1.7.25
- slf4j-simple-1.7.25

##Food stored in database
We currently have stored 35 types of food in our database, including: Apple, Avocado, Banana, 
Beetroot, Blueberry, Brinjal, Broccoli, Cake, Capsicum, Carrot, Cheese, Chips, Cucumber, Curry, 
Fish, Garlic, Hamburger, Honey, Kiwifruit, Milk, Onion, Orange, Pancake, Pea, Pizza, Potato, 
Pumpkin, Rice, Sandwich, Sausage, Shrimp, Steak, Strawberry, Sweet Potato, Tomato

Most of them can be image recognized, and all of them can be recorded manually.

