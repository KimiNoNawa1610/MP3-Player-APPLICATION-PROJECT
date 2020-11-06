//Nhan Vo
//Cecs 343- Lab6
import java.io.DataInput;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
Observer interface
 */
interface Observer{
    public void UpdateProcess();// update process method
}

public class DataSet {
    private ArrayList<Double> data;
    private int dataSize;
    private static DataSet singleData=null;
    private ArrayList<Observer> observers= new ArrayList<>();
    private String state;

    public static DataSet getInstance(String path) throws FileNotFoundException {
        if(singleData==null){
            singleData=new DataSet(path);
        }
        return singleData;
    }
    //functions of Subject-Observer: Start
    public String getState(){
        return state;
    }

    public void setState(String newState){
        state=newState;
    }

    public void addObserver(Observer obs){
        observers.add(obs);
        System.out.println("Adding Observer statistics."+obs.toString());
    }

    public void removeObserver(Observer obs){
        observers.remove(obs);
        System.out.println("Removing Observer statistics."+obs.toString());
    }

    public void notifyObservers(){
        for(Observer obs:observers){
            obs.UpdateProcess();
        }
    }
    //functions of Subject-Observer: End

    //DataSet constructs the dataset by reading from an input file via a scanner
//The file name is passed to the constructor as its only parameter
    private DataSet(String filename) throws FileNotFoundException{
        data = new ArrayList(10);
        try {
            File inputFile = new File(filename);
            Scanner sc = new Scanner(inputFile);

            while(sc.hasNextDouble()){
                data.add(sc.nextDouble());
                dataSize++;
            }

        } catch (FileNotFoundException fnf){
            System.out.println(fnf.getMessage());
        };
        System.out.println("There are " + dataSize + " values in the file");

    }
    //Mean() computes the arithmetic mean of the data set
    double Mean(){
        double total = 0.0;
        int count = 0;
        for (Double data1 : data) {
            total += data1;
            count++;
        }
        return total/(count);
    }
    //Median finds the median value of the data set. If the data set has an odd
//number of elements the median is the middle element in a sorted data set
//If the data set has an even number of elements the median is the average
//of the two middle values (which may not be in the data set)
    double Median(){
        int midPoint = data.size() / 2;
        if(data.size()%2==0)
            return (data.get(midPoint) + data.get((midPoint)-1))/2.0;
        else
            return data.get(midPoint);
    }
    //Minimum returns the smallest value of the data set assuming the data set
//is sorted in ascending order
    double Minimum() {
        return data.get(0);
    }
    //Maximum returns the largest value of the data set assuming the data set
//is sorted in ascending order
    double Maximum() {
        return data.get(data.size()-1);
    }
    //Variance computes the average of the variance of elements from the mean
    double Variance(){
        double m = Mean();
        double sum_of_sq = 0;

        for(int i=0;i<data.size();i++){
            sum_of_sq += Math.pow(data.get(i) - m, 2);
        }
        if(data.size()>1)
            return sum_of_sq / (data.size()-1);
        else return 0;

    }
    //StandardDeviation() computes to no great surprise the SD of the data set
    double StandardDeviation(){
        state="Calculating SD";
        this.notifyObservers();
        return Math.sqrt(Variance());
    }
    //Old-timey bubble sort in ascending order!

    void sort() {
        state="Sorting";
        this.notifyObservers();
        boolean swapped = true;
        while(swapped){
            swapped = false;
            for(int i=0;i<data.size()-1;i++)
                if(data.get(i)>data.get(i+1)){
                    double temp=data.get(i);
                    data.set(i,data.get(i+1));
                    data.set(i+1, temp);
                    swapped=true;
                }
        }
    }
    //Print(true) prints the data in a 2-d table format, 10 elements per row
//Print(false) prints the data one element per row
    void Print(boolean table){

        for(int i=0; i < data.size(); i++){
            System.out.printf("%8.2f", data.get(i));
            if(table){
                if(i%10==9)
                    System.out.println();
            }
            else
                System.out.println();
        }
        System.out.println("\n");
        state="Printing";
        this.notifyObservers();
    }
}
/*
observer1 class
 */
class Observer1 implements Observer {
    private DataSet data;
    public Observer1(DataSet dt){
        data=dt;
    }
    @Override
    public void UpdateProcess() {
        System.out.println("obs1 is told that DataSet is "+data.getState());
    }
}
/*
observer2 class
 */
class Observer2 implements Observer {
    private DataSet data;
    public Observer2(DataSet dt){
        data=dt;
    }
    @Override
    public void UpdateProcess() {
        System.out.println("obs2 is trying to ignore the fact that DataSet "+data.getState());
    }
}

/*
Driver
 */
class Statistics {

    static DataSet ds;

    public static void main(String[] args) throws FileNotFoundException {
        ds=DataSet.getInstance("nums.dat");
        Observer1 obs1=new Observer1(ds);
        Observer2 obs2=new Observer2(ds);
        ds.addObserver(obs1);
        ds.addObserver(obs2);
        ds.sort();
        ds.Print(true);
        System.out.println("mean=" + ds.Mean());
        System.out.println("median=" + ds.Median());
        System.out.println("minimum=" + ds.Minimum());
        System.out.println("maximum=" + ds.Maximum());
        System.out.println("variance=" + + ds.Variance());
        System.out.println("standard deviation=" + + ds.StandardDeviation());
        ds.removeObserver(obs2);
        ds.Print(false);
    }

}
