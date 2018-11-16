/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
            }
            else {
                totalGirls += numBorn;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls = " + totalGirls);
        System.out.println("male boys = " + totalBoys);
    }
    
    public int getRank(int year, String name, String gender) {
            int rank = 0;
            boolean found = false;
                        
            FileResource fr = new FileResource();           
            
            for(CSVRecord rec : fr.getCSVParser(false)) {
                String cName = rec.get(0);
                String cGender = rec.get(1);
                             
                if(cGender.equals(gender)) {
                    rank += 1;
                    if(cName.equals(name)) {
                        found = true;
                        break;
                    }
                }            
            }
            
            if(found == false) {
                return -1;
            }
            
            return rank;
    }      
    
    public String getName(int year, int rank, String gender) {
            int tempRank = 0;
            String name = " ";
            boolean found = false;
                        
            FileResource fr = new FileResource();           
            
            for(CSVRecord rec : fr.getCSVParser(false)) {
                String cName = rec.get(0);
                String cGender = rec.get(1);
                             
                if(cGender.equals(gender)) {
                    tempRank += 1;
                    if(tempRank == rank) {
                        found = true;
                        name = cName;
                    }
                }            
            }
            
            if(found == false) {
                return "NO NAME";
            }
            
            return name;
    }    
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        String nameInOtherFile = getName(newYear, rank, gender);
        
        System.out.println(name + " born in " + year + " would be " + 
            nameInOtherFile + " if she was born in " + newYear + ".");
    }
    
    public void testTotalBirths () {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("data/yob2014.csv");
        totalBirths(fr);
    }
    
    public void testGetRank() {
        System.out.println(getRank(2012, "Mason", "M"));
        System.out.println(getRank(2012, "Mason", "F"));
    }
    
    public void testGetName() {
        System.out.println(getName(2012, 2, "M"));
        System.out.println(getName(2012, 4, "F"));
    }
}
