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
* Android Emulator | Android Version 10.0

## Setup
To run this project, install it locally using ______

```
$ cd ../lorem
$ npm install
$ npm start
```
ADMIN NOTE: SETUP IS WORK IN PROGRESS.

## Getting Started
1. Launch the application
2. Tap on "New User? Sign Up Here"
3. Enter Full Name, Email, and Password in the respective fields. Password must be at least 8 characters long and no fields are allowed to be empty.
4. Login using the Email and Password that was entered previously.
5. Access should now be granted to the application and the user now has access to its functions. 
