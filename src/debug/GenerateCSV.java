package debug; /**
 * Created by Shais Shaikh on 4/19/2016.
 */

import data.Record;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GenerateCSV {


    public static void generateCsvFile(String sFileName, ArrayList<Record> records)
    {
        try
        {
            FileWriter writer = new FileWriter(sFileName);

//            writer.println("Algorithm - Score - Success - Bombs Planted - Time Taken(seconds)- Deaths by");


            writer.append("Algorithm number");
            writer.append(',');
            writer.append("Algorithm");
            writer.append(',');
            writer.append("Score");
            writer.append(',');
            writer.append("Success");
            writer.append(',');
            writer.append("Bombs Planted");
            writer.append(',');
            writer.append("Time Taken(seconds)");
            writer.append(',');
            writer.append("Death by");

            writer.append('\n');


            for (Record rec: records) {
//                writer.println(rec.toString(0));
//                writer.println(rec.toString(1));
//                writer.println(rec.toString(2));
//
                appendtoCSV(0,rec, writer);
                appendtoCSV(1,rec, writer);
                appendtoCSV(2,rec, writer);

            }
//            writer.append("MKYONG");
//            writer.append(',');
//            writer.append("26");
//            writer.append('\n');
//
//            writer.append("YOUR NAME");
//            writer.append(',');
//            writer.append("29");
//            writer.append('\n');

            //generate whatever data you want

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void appendtoCSV(int i, Record rec, FileWriter writer) {
        String tokens[] = rec.toString(i).split(" ");


        try {
            writer.append(tokens[0]);
            writer.append(',');
            writer.append(tokens[2]);
            writer.append(',');
            writer.append(tokens[4]);
            writer.append(',');
            writer.append(tokens[6]);
            writer.append(',');
            writer.append(tokens[8]);
            writer.append(',');
            writer.append(tokens[10]);
            writer.append(',');
            writer.append(tokens[12]);

            writer.append('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
