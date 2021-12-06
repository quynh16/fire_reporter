# Fire Reporter: A real-time fire-reporting app
An android applicaiton that allows for users to take a photo of an active fire, which is then sent to local firefighters so that they may respond to it in a timely manner.
Developed with Android Studio and written in Java. 

## Table of Contents
* [General Info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Getting Started](#getting-started)

## General Info
This application takes a photo of a live fire and verifies that it is a real fire via a machine learning algorithm. It will then send this photo to a firefighter,
who will then send a response back regarding the fire's status. This application will also require users to login and create an account with an associated profile.
Profile will then store user information and keep a record of his/her recent fire reports. This data will be stored using Firebase. 

## Technologies
* Android Studio Arctic Fox | 2020.3.1 Patch 3
* Firebase for Android | Version 29.0.0
* Android Emulator | Android Version 12.0
* Java

## Setup
### Installing on your Android device directly:
1. Open the browser on your phone and download the APK file
2. Go to Downloads and tap on the downloaded APK file.
3. Tap "Yes" when prompted to install
4. App installation should then begin

### Installing onto the Android device from a computer:
1. Open the browser on your computer and download the APK file. 
2. For older versions of Android: Make sure third-party apps are allowed on your Android device. Go to Menu > Settings > Security > and check "Unknown Sources". This allows for your device to install apps from sources other than the Google Play Store.
3. Connect your Android device to your computer and have it be connected as a "Media Device".
4. If prompted, click on "Open Device to View Files." If not, find your device's folder on the computer. This is usually listed under "My Computer" or "Computer" for a Windows PC.
5. Copy the APK file from your computer and paste it into a folder of your choice on the Android device. 
6. On your Android device, go to the "My Files" or "Files" folder and find the folder that you pasted the APK file into. 
7. Tap on the APK file and then tap on "Install". Depending on how new your Android version is, you may be prompted to allow APK installation. If this occurs, press "Yes" or "Allow".
8. App installation should then begin

## Getting Started
1. Launch the application
2. Tap on "New User? Sign Up Here"
3. Enter Full Name, Email, and Password in the respective fields. Password must be at least 8 characters long and no fields are allowed to be empty.
4. Login using the Email and Password that was entered previously.
5. Access should now be granted to the application and the user now has access to its functions. 
