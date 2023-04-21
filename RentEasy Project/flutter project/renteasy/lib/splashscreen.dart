import 'dart:async';

import 'package:flutter/material.dart';
import 'package:renteasy/HomeScreen.dart';
import 'package:renteasy/main.dart';

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();

    Timer(Duration(seconds: 3), () {
      Navigator.pushAndRemoveUntil(context,
          MaterialPageRoute(builder: (c) =>TutorialHome()), (route) => false);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color.fromARGB(255, 239, 44, 33),
        // backgroundColor: Colors.red,
        body: Center(          
          child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Image.asset('assets/images/iconsplash.png', scale: 2.0,),                                         
                  ],
                ),
        ),  
    );
  }
}
