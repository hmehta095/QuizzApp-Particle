// This #include statement was automatically added by the Particle IDE.
#include <InternetButton.h>

InternetButton button = InternetButton();
int DELAY = 200;
void setup() {
 button.begin();

Particle.function("score",displayScore);
Particle.function("answer",showCorrectOrIncorrect);

    for(int i=0;i<3;i++){
    button.allLedsOn(200,0,0);
    delay(500);
    button.allLedsOff();   
    delay(500);
    }
}


int displayScore(String cmd){
   //reseting the score
    button.allLedsOff();
    
    int score = cmd.toInt();
    if(score < 0 || score > 11){
        // retuens -1 means error occured
        return -1;
    }
    
    //print the score
    for(int i = 1;i<= score;i++){
        button.ledOn(i,255,255,0);
    }
}


int showCorrectOrIncorrect(String cmd){
    if(cmd == "circle"){
        button.allLedsOn(0,255,0);
        
    }
    if(cmd == "triangle"){
        button.ledOn(3,0,255,0);
        button.ledOn(7,0,255,0);
        button.ledOn(11,0,255,0);
        
    }
    
    else if (cmd == "red"){
        button.allLedsOn(255,0,0);
        delay(200);
        button.allLedsOff();
    }
    else{
        return -1;
    }
    return 1;
    
}



void loop() {
  
  
  if(button.buttonOn(4)){
      // choice A
      Particle.publish("playerChoice","A",60,PRIVATE);
      delay(DELAY);
  }
  if(button.buttonOn(2)){
      //choice B
      Particle.publish("playerChoice","B",60,PRIVATE);
      delay(DELAY);
  }
  if(button.buttonOn(3)){
      //Next question
      Particle.publish("playerChoice","true",60,PRIVATE);
      delay(DELAY);
  }
  if(button.buttonOn(1)){
      
      Particle.publish("playerChoice","C",60,PRIVATE);
      delay(DELAY);
  }











}




