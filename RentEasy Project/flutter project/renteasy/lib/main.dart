import 'package:flutter/material.dart';
import 'package:renteasy/HomeScreen.dart';
import 'package:renteasy/splashscreen.dart';


void main() {
  runApp(MyApp());
}

 class MyApp extends StatelessWidget {
   @override
   Widget build(BuildContext context) {
     return MaterialApp(
       debugShowCheckedModeBanner: false,
       title: "RentEasy",
// mi splash screen file ni ikkada add chayandi as home
       home: TutorialHome(), 
     );
   }
}