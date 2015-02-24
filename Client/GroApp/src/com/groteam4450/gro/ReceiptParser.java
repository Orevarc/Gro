package com.groteam4450.gro;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
 
public class ReceiptParser {
 
        public static void main(String[] args) throws IOException {
               // String filePathA = "//Users/Raiden/Downloads/OCRTextCaptured1.txt";
                //String filePathB = "/Users/Connor/Documents/GroceryExampleText.txt";
               
               // parseTextToArray(filePathA);
                //(filePathB);
        }
       
        public static ArrayList<BasicFoodItem> parseTextToArray (String filePath) throws IOException
        {
                ArrayList<BasicFoodItem> itemList = new ArrayList<BasicFoodItem>();
                ArrayList<String> rawStringList = new ArrayList<String>();
                ArrayList<String> rawStringListWal = new ArrayList<String>();
                FileReader fr = new FileReader (filePath);
                BufferedReader textReader = new BufferedReader (fr);
               
                //This loop parses the lines we want from the text file and adds
                //the string of each line to a list
                for (int i=1; i<100; i++) {
                        String line = textReader.readLine();
                        if (line == null) {
                        }
                        else {
                                Pattern regularItemPattern = Pattern.compile("\\d{11}\\s{1}[A-Z&]*\\s{1}");
                                Matcher m = regularItemPattern.matcher(line);                  
                                if (m.find()) {
                                        System.out.println(line);
                                        rawStringList.add(line);
                                }
                                else {
                                        Pattern produceItemPattern = Pattern.compile("\\d{4}\\s{1}[A-Z&]*\\s{1}");     
                                        m = produceItemPattern.matcher(line);
                                        if (m.find()) {
                                                System.out.println(line);
                                                rawStringList.add(line);
                                        }
                                }
                                Pattern walmartItemPattern = Pattern.compile("\\d{12}");
                                Matcher match = walmartItemPattern.matcher(line);
                                if (match.find()) {
                                        System.out.println(line);
                                        rawStringListWal.add(line);
                                }
                        }              
                }      
               
                System.out.println("\n");
               
                //This loop iterates through the list of raw strings and populates a list of BasicFoodItems
                //to be checked against the database
                for (String line : rawStringList) {
                        String upc;
                        String name;
                       
                        Pattern upcPatternA = Pattern.compile("\\d{11}");
                        Pattern upcPatternB = Pattern.compile("\\d{4}");
                        Pattern namePattern = Pattern.compile("[A-Z&]+\\s{1}[A-Z\\s&]+");
                       
                        Matcher upcMatcherA = upcPatternA.matcher(line);
                        Matcher upcMatcherB = upcPatternB.matcher(line);
                        Matcher nameMatcher = namePattern.matcher(line);
                       
                        if (upcMatcherA.find() && nameMatcher.find())
                        {
                                itemList.add(new BasicFoodItem(upcMatcherA.group(0), nameMatcher.group(0)));
                                System.out.println("UPC CODE IS " + upcMatcherA.group(0) + " AND NAME IS " + nameMatcher.group(0));
                        }
                        else if (upcMatcherB.find() && nameMatcher.find())
                        {
                                itemList.add(new BasicFoodItem(upcMatcherB.group(0), nameMatcher.group(0)));
                                System.out.println("UPC CODE IS " + upcMatcherB.group(0) + " AND NAME IS " + nameMatcher.group(0));
                        }
                }
               
                for (String line : rawStringListWal) {
                        String upc;
                        String name;
                       
                        Pattern upcPatternA = Pattern.compile("\\d{12}");
                        Pattern upcPatternB = Pattern.compile("\\d{4}");
                        Pattern namePattern = Pattern.compile("[A-Z& ]+");
                       
                        Matcher upcMatcherA = upcPatternA.matcher(line);
                        Matcher upcMatcherB = upcPatternB.matcher(line);
                        Matcher nameMatcher = namePattern.matcher(line);
                       
                        if (upcMatcherA.find() && nameMatcher.find())
                        {
                                itemList.add(new BasicFoodItem(upcMatcherA.group(0), nameMatcher.group(0)));
                                System.out.println("UPC CODE IS " + upcMatcherA.group(0) + " AND NAME IS " + nameMatcher.group(0));
                        }
                        else if (upcMatcherB.find() && nameMatcher.find())
                        {
                                itemList.add(new BasicFoodItem(upcMatcherB.group(0), nameMatcher.group(0)));
                                System.out.println("UPC CODE IS " + upcMatcherB.group(0) + " AND NAME IS " + nameMatcher.group(0));
                        }
                }
               
                return itemList;
        }
}