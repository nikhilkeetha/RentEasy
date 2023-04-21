import 'package:flutter/material.dart';


void main() {
  runApp(
    const MaterialApp(
      title: 'Flutter Tutorial',
      home: TutorialHome(),
    ),
  );
}

class TutorialHome extends StatelessWidget {
  const TutorialHome({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Scaffold is a layout for
    // the major Material Components.
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.red,          
        title: Container(
          margin: EdgeInsets.only(top: 7,left: 0),
          child: Text(
            'RentEasy',
            style: TextStyle(fontSize: 25,fontWeight: FontWeight.w300 , color: Colors.white,fontFamily: "LuckiestGuy"),          
          ),
        ),  
        actions: const [
          IconButton(
            icon: Icon(Icons.notifications ,color: Colors.white,),
            tooltip: 'Notifications',
            onPressed: null,           
          ),          
        ],
      ),
      // body is the majority of the screen.
      body: const Center(
        
      ),
    );
  }
}