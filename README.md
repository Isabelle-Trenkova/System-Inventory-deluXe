# System Inventory deluXe
Mobile Inventory Management App
 
//////////////////////////////////////////////////////////////////////////////

Code in this project is based off of code from outside sources:

Code written by Michał Kołnierzak, code is licenced using a MIT licence and free for commerical/private use and modifications.

Code was predominately used for the database and CRUD operations with item attributes and images. 
View code for more detailed comments of how the source was used and where. 

https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 
This stackoverflow source was used for permissions with camera and gallery.
Code snippets written by Dileep Patel for gallery permissions and Praveen Nikhare for permissions and use of taking an image with camera.
Both snippets where edited and combined to work in conjunction.

All source code posted on Stack Overflow is licensed under CC BY-SA 3.0.

https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m

/////////////////////////////////////////////////////////////////////////////

Outside Library used: 

Copyright 2014 Manabu Shimobe
Licensed under the Apache License, Version 2.0 (the "License").
Used for expandable text capability in the help menu.

https://github.com/Manabu-GT/ExpandableTextView


//////////////////////////////////////////////////////////////////////////////

Basic Features of the app:

* A user can add, update, and delete inventory
* A user can add, update, and delete recipes
* The ability to mark an item as a 'priority item' 
* Once an items stock quantity was less than a 'low threshold' an item will be automatically marked to be 'low stock', and automatically unmarked when quantity is above the 'threshold'
* Quantity can be incremented and decremented either by typing in a value, or using increment/decrement buttons
* The ability to search for items and recipes
* Can view 'priority' and 'low-stock' in seperate pages, segregated from the rest of the inventory
* Ease of view items in multiple ways/places, and any changes to items will be reflected everywhere else in the app
* Can add images from gallery and camera to display with the item icon
* Menu bar options to add, save, and delete an item and to return to previous page 
* Help menu with expandable text
