package Minesweeper.src;

public class Timer{

  long startTime;
  long endTime;
  long duration;
  boolean running = false;

  void start(){
    startTime = System.currentTimeMillis();
    running = true;
  }

  double stop(){
    endTime = System.currentTimeMillis();
    running = false;
    duration = endTime -startTime;
    //returns in seconds
    return ((double)duration)/ 1000;
  }

  public String toString(){
    if(running){
      return "The timer is running";
    }else{
      return "It took " + ((double)duration)/ 1000+ " seconds";
    }
  }


}
