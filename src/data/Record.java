package data;

/**
 * Created by Shais Shaikh on 4/17/2016.
 */
public class Record {
    public int runCount;
    public int score[] = new int[3];
    public int bombs[] = new int[3];
    public int timeInSeconds[] = new int[3];
    public boolean success[] = new boolean[3];
    public Record(int a ){
        runCount = a;
    }

    public void incrementScore(int algo, int amount){
        switch(algo){
            case(0):
                score[0] += amount;
                break;
            case(1):
                score[1] += amount;
                break;
            case(2):
                score[2] += amount;
                break;
        }
    }
    public void isSuccess(int algo, boolean result){
        switch(algo){
            case(0):
                success[0] = result;
                break;
            case(1):
                success[1] = result;
                break;
            case(2):
                success[2] = result;
                break;
        }
    }

    public void numOfBombsPlanted(int algo, int number){
        switch(algo){
            case(0):
                bombs[0] = number;
                break;
            case(1):
                bombs[1] = number;
                break;
            case(2):
                bombs[2] = number;
                break;
        }
    }
    public void timeTaken(int algo, long time){
        switch(algo){
            case(0):
                timeInSeconds[0] = (int) time;
                break;
            case(1):
                timeInSeconds[1] = (int) time;
                break;
            case(2):
                timeInSeconds[2] = (int) time;
                break;
        }
    }


        public String toString(int i){
            String eachRecord = null;
            switch(i){
                case(0):
                    eachRecord = "1 - NOSIGNAL - " + String.valueOf(score[0]) + " - " + success[0]+ " - " + bombs[0]+ " - " +  timeInSeconds[0];
                    break;
                case(1):
                    eachRecord = "2 - PRECISION - " + String.valueOf(score[1]) + " - "+ success[1] + " - "+ bombs[1] + " - "+  timeInSeconds[1];
                    break;
                case(2):
                    eachRecord = "3 - AMPLITUDE - " + String.valueOf(score[2]) + " - "+ success[2] + " - "+ bombs[2]+ " - " +  timeInSeconds[2];
                    break;
            }


            return eachRecord;
        }


}
