package rohitSaha;

import java.io.*;
import java.lang.*;
import java.util.Scanner;

/* This program is used for reading .txt file, given a kind of relation and 
 * save them in an array of dimension <number of emotions>*<3>. Once the array
 * is made, it is saved in the form of a serialized object. This saved file 
 * can be used in future references. Serialized helps to access the file and 
 * load it faster in the program.
 * 
 * Format of .txt file :
 * <Emotion of person performing the verb> <Verb> <Emotion of the person experiencing the verb>
 * 
 * */

public class SaveRules {
	String details[][] = new String[14][3];
	String get_word = "";
	
	//Function to read a .txt file and storing the contents in a 2-D array.
	public void read(){
		try{
			Scanner sc = new Scanner(System.in);
			File file = new File("/Users/rohitsaha/Documents/MIT/6.100/Repository/gen/genesis/students/rohitSaha/EmotionRules/ComplexEmotions");
			sc = new Scanner(file);
			for(int i=0;i<14;i++){
				for(int j=0;j<3;j++){
					if(sc.hasNext()){
						get_word = sc.next();
					}
					details[i][j] = get_word;
				}
			}
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//to display
	public void display(){
		for(int i=0;i<14;i++){
			for(int j=0;j<3;j++){
				System.out.print(details[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	//Function to save array as a .ser file.
		public void save_array(){
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/Users/rohitsaha/Documents/MIT/6.100/Repository/gen/genesis/students/rohitSaha/ComplexEmotions.ser"));
				out.writeObject(details);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public static void main(String [] args) throws ClassNotFoundException{
		SaveRules obj = new SaveRules();
		obj.read();
		obj.display();
		obj.save_array();
	}
}


