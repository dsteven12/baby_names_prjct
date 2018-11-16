/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

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
        int countBoys = 0;
        int countGirls = 0;
        int totalCount = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                countBoys++;
            }
            else {
                totalGirls += numBorn;
                countGirls++;
            }
        }
        totalCount = countBoys + countGirls;
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls = " + totalGirls);
        System.out.println("male boys = " + totalBoys);
        System.out.println("total births (by name) = " + totalCount);
        System.out.println("female girls (by name)= " + countGirls);
        System.out.println("male boys (by name) = " + countBoys);
    }
    
    public int getRank(int year, String name, String gender, FileResource fr) {
            int rank = 0;
            boolean found = false;       
            
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
    
    public String getName(int year, int rank, String gender, FileResource fr) {
            int tempRank = 0;
            String name = " ";
            boolean found = false;       
            
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
        FileResource fr1 = new FileResource();
        int rank = getRank(year, name, gender, fr1);
        
        FileResource fr = new FileResource();
        String nameInOtherFile = getName(newYear, rank, gender, fr);
        
        System.out.println(name + " born in " + year + " would be " + 
            nameInOtherFile + " if she was born in " + newYear + ".");
    }
    
    public int yearOfHighestRank(String name, String gender) {
        int tempRank = 0;
        int yearWithBestRank = 0;
        
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            //Gets fileName from file.
            String fileName = f.getName();
            //Gets startIndex of yob of fileName.
            int startPos = fileName.indexOf("yob");
            //Gets currYear by finding position of yob.
            int currYear = Integer.parseInt(fileName.substring(startPos + 3, startPos + 7));
            //Gets rank of file based on year, name, gender, and file resource.
            int currRank = getRank(currYear, name, gender, fr);
            
            if(yearWithBestRank == 0) {
                yearWithBestRank = currYear;
                tempRank = currRank;
            } 
            
            if (currRank != -1) {
                if (currRank < tempRank) {
                    yearWithBestRank = currYear;
                    tempRank = currRank;
                }
            }
        }
        return yearWithBestRank;
    }
    
    public double getAverageRank(String name, String gender) {
        double avgRank = 0.0;
        double totalRank = 0.0;
        double count = 0.0;
        boolean found = false; 
        int rank = 0;
        
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            count += 1.0;
            FileResource fr = new FileResource(f);
            //Gets fileName from file.
            String fileName = f.getName();
            //Gets startIndex of yob of fileName.
            int startPos = fileName.indexOf("yob");
            //Gets currYear by finding position of yob.
            int currYear = Integer.parseInt(fileName.substring(startPos + 3, startPos + 7));
            //Gets rank of file based on year, name, gender, and file resource.
            int currRank = getRank(currYear, name, gender, fr);
            
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
            
            if (currRank != -1) {
                totalRank += currRank;
            }
        }
        
        if(found == false) {
            return -1;
        }
        
        avgRank = totalRank/count;
        return avgRank;
    }
            
    public int getTotalBirthsRankedHigher(int year, String name, String gender, FileResource fr) {
        int totalBirthsHigherThanRank = 0;
        int rank = 0;
        boolean found = false;       
        
        for(CSVRecord rec : fr.getCSVParser(false)) {
            String cName = rec.get(0);
            String cGender = rec.get(1);
            int cNumBirths = Integer.parseInt(rec.get(2));
                         
            if(cGender.equals(gender)) {
                rank += 1;
                totalBirthsHigherThanRank += cNumBirths;
                if(cName.equals(name)) {
                    found = true;
                    totalBirthsHigherThanRank -= cNumBirths;
                    break;
                }
            }            
        }
        
        if(found == false) {
            return -1;
        }      
        return totalBirthsHigherThanRank;
    }
          
    public void testTotalBirths () {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    public void testGetRank() {
        FileResource fr = new FileResource();
        System.out.println(getRank(1971, "Frank", "M", fr));
        
    }
    
    public void testGetName() {
        FileResource fr = new FileResource();
        System.out.println(getName(1980, 350, "F", fr));
        
        FileResource fr2 = new FileResource();
        System.out.println(getName(1982, 450, "M", fr2));
    }
    
    public void testGetTotalBirthsRankedHigher() {
        FileResource fr = new FileResource();
        System.out.println(getTotalBirthsRankedHigher(1990, "Drew", "M", fr));
    }

    /* In which year from 1880 to 2014 does the boy’s name 
     * "Mich" have the highest rank (over all the data files)?
     * If there is more than one year with the highest rank, choose the earliest one.
     * Some names may appear in some years but not in others. 
     * Make sure your code correctly handles the case where a name appears 
     * in some years but not in others. */
    // TotalBirthsRankedHigher
}
