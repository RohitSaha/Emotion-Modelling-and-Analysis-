package rohitSaha;

import java.io.File;
import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import bridge.reps.entities.Entity;
import bridge.reps.entities.Relation;
import innerese.RoleFrames;
import innerese.Rules;
import translator.Translator;
import utils.Mark;
import storyProcessor.StoryProcessor;
import innerese.Rules.*;

/*This program is used to extract story elements from sentences and use those elements to create new relations and emotion models of the characters.
 * It extends Information class which holds all valuable information about the relations. This program has 2 parts : 1. Emotion Modeling 2. Emotion Analysis.
 * 1. Emotion Modeling deals with relations and characters involved, makes the emotion models/space of every character with each other.
 * 2. Emotion Analysis analyzes the current emotion models. Once the emotion models have been created, the emotion outcome of a new event can be predicted.
 * */
public class EmotionModelingAndAnalysis extends Information{
	
	//To hold information about different relations in the story.
	List<Information> Relations = new ArrayList<Information>();
	
	//Specifying all the tags of different categories of relations.
	List<String> family = Arrays.asList("parent", "child", "father", "mother", "son", "daughter", "grandparent");
	List<String> work = Arrays.asList("boss", "employee");
	List<String> school = Arrays.asList("teacher", "student", "professor", "lecturer", "supervisor", "tutor");
	List<String> couples_siblings = Arrays.asList("husband", "wife", "couple", "brother", "sister", "sibling", "siblings");
	List<String> friends = Arrays.asList("friend", "colleague", "peer");
	List<String> others = Arrays.asList("doctor", "plumber", "carpenter", "driver", "rival");
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////EMOTION MODELING////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//This function is called to create an object everytime a new relation occurs in the story.
	public void new_relation(String subject, String object, String relation){
		
		Information obj = new Information();
		obj.character_1 = subject;
		obj.character_2 = object;
		obj.Relation = relation;
		Relations.add(obj);
	}
	
	//This function checks if a relation between 2 characters already exists in Information. Returns the object which contains the 2 characters.
	public Information is_relation_present(String subject, String object){
		
		Information _temporary = new Information();
		
		for(Information var : Relations){
			if((((var.get_character1_name()).compareTo(subject)==0) && ((var.get_character2_name()).compareTo(object)==0)) || 
					(((var.get_character1_name()).compareTo(object)==0) && ((var.get_character2_name()).compareTo(subject)==0))){
				_temporary = var;
				break;
			}
		}
		return _temporary;
	}
	
	//This function returns the type of relation for future use.
	public String getTypeOfRelation(String rel){
		
		if(family.contains(rel))
			return "ParentChild";
		else if(work.contains(rel))
			return "BossEmployee";
		else if(school.contains(rel))
			return "TeacherStudent";
		else if(couples_siblings.contains(rel))
			return "Couples_Siblings";
		else if(friends.contains(rel))
			return "FriendsColleaguesPeers";
		else if(others.contains(rel))
			return "Others";
		else
			return "";
			
	}
	
	//This function is used to get the emotion outcome of the subject and object, given the verb and the relation between them.
	public void getEmotion(Information _temporary, String verb, String subject, Entity ent){
		
		String [][] verbs_emotions;
		ObjectInputStream in;
		
		try{
			String _getrel = _temporary.get_relation();
			String path = System.getProperty("user.dir");
			if(_getrel.compareTo("ParentChild")==0 || _getrel.compareTo("BossEmployee")==0 || _getrel.compareTo("TeacherStudent")==0){
				in = new ObjectInputStream(new FileInputStream(String.format(path + "/students/rohitSaha/Relations/HigherLowerAuthority/%s.ser", _getrel)));
			}
			else{
				in = new ObjectInputStream(new FileInputStream(String.format(path + "/students/rohitSaha/Relations/SimilarAuthority/%s.ser", _getrel)));
			}
			verbs_emotions = (String [][])in.readObject();
			in.close();
			
			//Checking for the verb. If found, return emotion of subject and object.
			for(int i=0;i<69;i++){
				if((verbs_emotions[i][1].toLowerCase()).compareTo(verb.toLowerCase()) == 0){
					if(subject.compareTo(_temporary.get_character1_name())==0){
						_temporary.update_char1_emotion(verbs_emotions[i][0].toLowerCase());
						_temporary.update_char2_emotion(verbs_emotions[i][2].toLowerCase());
						_temporary.LatestEmotionOfChar1 = verbs_emotions[i][0].toLowerCase();
						_temporary.LatestEmotionOfChar2 = verbs_emotions[i][2].toLowerCase();
						_temporary.update_char1_emotion_entity_relation(verbs_emotions[i][0].toLowerCase(), ent);
						_temporary.update_char2_emotion_entity_relation(verbs_emotions[i][2].toLowerCase(), ent);
					}
					else{
						_temporary.update_char1_emotion(verbs_emotions[i][2].toLowerCase());
						_temporary.update_char2_emotion(verbs_emotions[i][0].toLowerCase());
						_temporary.LatestEmotionOfChar1 = verbs_emotions[i][2].toLowerCase();
						_temporary.LatestEmotionOfChar2 = verbs_emotions[i][0].toLowerCase();
						_temporary.update_char1_emotion_entity_relation(verbs_emotions[i][2].toLowerCase(), ent);
						_temporary.update_char2_emotion_entity_relation(verbs_emotions[i][0].toLowerCase(), ent);
					}
					_temporary.update_event(verbs_emotions[i][1]);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	//This function forms the entity and extracts subject, object and type (relation/verb) from it.
	public void getting_elements(Entity story){
		
		String s = "", o = "";
		
		//Entity story = Translator.getTranslator().translateToEntity(sentence);
		Entity subject = RoleFrames.getSubject(story);
		Entity object = RoleFrames.getObject(story);
		String rel = story.getType();
		
		try{
			s = subject.toEnglish();
			o = object.toEnglish();
		}catch(Exception e){
			subject = story.getSubject();
			object = story.getObject();
			try{
				s = subject.toEnglish();
				o = object.toEnglish();
			}catch(Exception f){
				
			}
		}
		
		Mark.say("Entity:", story);
		Mark.say("Subject and object:", s, o);
		Mark.say("Type:", rel);
		
		//Checking if any relation already exists between the subject and the object.
		Information temp = is_relation_present(s, o);
		
		if(temp.character_1.compareTo("") == 0){
			//Relation not present. Hence, get the proper type of relation and make a new one.
			String getRel = getTypeOfRelation(rel);
			if(getRel.compareTo("") != 0){
				new_relation(s, o, getRel);
			}	
		}
		else{
			//Relation already exists. Update the emotion models of the characters in the relation.
			getEmotion(temp, rel, s, story);
		}
		
		/*This line stores the entities shared by 2 characters. This is used for making causal connections between
		* events and emotions.*/
		temp.update_entities_between_2_characters(story);
	}
	
	//This function reads every sentence from a file and passes to the above function for extracting the story elements.
	public void read_from_story(){
		String sentence = "";
		try{
			String path = System.getProperty("user.dir");
			path += "/students/rohitSaha/test";
			File file = new File(path);
			Scanner sc = new Scanner(file);
			//Delimiter to get sentences from paragraphs.
			sc.useDelimiter("[.!]");
			while(sc.hasNext()){
				sentence = sc.next().trim();
				System.out.println(sentence);
				//getting_elements(sentence);
			}
			sc.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Uncomment to debug.
		//display_Information();
	}
	
	//This function is used to display all the relations and its information that the program founds using Genesis.
	public void display_Information(){
		for(Information temp : Relations){
			System.out.println(temp.get_character1_name() + " " + temp.get_character2_name() + " " + temp.get_relation());
			System.out.println("Latest emotion of " + temp.get_character1_name() + " : " + temp.LatestEmotionOfChar1);
			System.out.println("Latest emotion of " + temp.get_character2_name() + " : " + temp.LatestEmotionOfChar2);
			System.out.print("Events between the 2 characters : ");
			temp.display_events();
			System.out.println();
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////EMOTION ANALYSIS////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//This function takes all the emotion models and uses Plutchik's and Parrott's emotion rules on them to predict future emotions.
	public void analyze_emotion(){
		String get_highest_emotion_char1 = "", get_highest_emotion_char2 = "";
		
		for(Information temp : Relations){
			String temp_get_rel = temp.get_relation();
			
			if ((temp_get_rel.compareTo("ParentChild") == 0) || (temp_get_rel.compareTo("Couples_Siblings")==0)){
				/*Personal relationship. Hence, importance is given to all the past events.
				 * The emotion of the latest event along with the emotion of highest frequency are chosen.
				 * Latest emotion + Most frequent emotion (apart from the latest emotion) = Secondary emotion.*/
				
				//Getting hold of the emotions with the highest frequency in the emotion space other than the latest emotion.
				get_highest_emotion_char1 = temp.get_most_frequent_emotion(temp.char1_emotions, temp.LatestEmotionOfChar1);
				get_highest_emotion_char2 = temp.get_most_frequent_emotion(temp.char2_emotions, temp.LatestEmotionOfChar2);
				
			}
			
			/*Analyzing the current emotions of the character to predict the new complex emotion.
			 * Latest emotion + get_highest_emotion_char = Secondary emotion.*/
			String _getComplexEmotionChar1 = get_new_emotion(temp.LatestEmotionOfChar1, get_highest_emotion_char1);
			String _getComplexEmotionChar2 = get_new_emotion(temp.LatestEmotionOfChar2, get_highest_emotion_char2);
			temp.update_predicted_emotion_of_char1(_getComplexEmotionChar1);
			temp.update_predicted_emotion_of_char2(_getComplexEmotionChar2);
			
			//Updates the causal connections.
			update_causal(temp.get_relation(), temp.LatestEmotionOfChar1, get_highest_emotion_char1, temp.char1_emotion_entity_relation, temp.causal_connection_char1, temp);
			update_causal(temp.get_relation(), temp.LatestEmotionOfChar2, get_highest_emotion_char2, temp.char2_emotion_entity_relation, temp.causal_connection_char2, temp);
		}
		
		_getSentenceFromEmotion();
		//Displaying all the updated values.
		//updated_information();
	}
	
	//This function updates the causal connections for a character with respect to the highest and most frequent emotion. 
	public void update_causal(String r, String le, String he, HashMap<String, List<Entity>> eer, List<Entity> cc, Information i){
		
		if ((r.compareTo("ParentChild") == 0) || (r.compareTo("Couples_Siblings")==0)){
			for(String get_emotion : eer.keySet()){
				if((get_emotion.compareTo(le) == 0) || (get_emotion.compareTo(he) == 0)){
					List<Entity> temporary = eer.get(get_emotion);
					for(Entity ent : temporary){
						cc.add(ent);
					}
				}
			}
		}
		else{
		cc.add(i.entities_between_2_characters.get(i.entities_between_2_characters.size() - 1));
		}
	}
	
	//This function takes in 2 emotions and returns the secondary/tertiary emotion.
	public String get_new_emotion(String _latestemotion, String _highestemotion){
		
		String [][] complex_emotions;
		ObjectInputStream in;
		
		if(_highestemotion.compareTo("") == 0){
			return _latestemotion;
		}
		else{
			try{
				//Loading the file that contains secondary emotions as a combination of primary emotions.
				String path = System.getProperty("user.dir");
				path += "/students/rohitSaha/ComplexEmotions.ser";
				in = new ObjectInputStream(new FileInputStream(path));
				complex_emotions = (String [][]) in.readObject();
				in.close();
				
				//Accessing the file and checking for combination of basic emotions.
				for(int i=0;i<14;i++){
					if( ((complex_emotions[i][0].compareTo(_latestemotion) == 0) && (complex_emotions[i][1].compareTo(_highestemotion) == 0)) ||
							(complex_emotions[i][0].compareTo(_highestemotion) == 0) && (complex_emotions[i][1].compareTo(_latestemotion) == 0) ){
						return complex_emotions[i][2];
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/*Write a method that takes the predicted emotion of a character and forms a sentence around it.
	 * example : 
	 * XX : joy -> XX is happy.
	 * YY : sadness -> YY is sad.
	 * */
	public void _getSentenceFromEmotion(){
		
		for(Information temp : Relations){
			String _get1 = temp.PredictedEmotionOfChar1;
			String _get2 = temp.PredictedEmotionOfChar2;
			temp.sentence_from_emotion_char1 = update_sentence(_get1, temp.get_character1_name(), temp.get_character2_name(), temp);
			temp.sentence_from_emotion_char2 = update_sentence(_get2, temp.get_character2_name(), temp.get_character1_name(), temp);
		}
	}
	
	//This function forms the sentence.
	public String update_sentence(String emotion, String name, String relname, Information t){
		
		String s = "";
		
		if(emotion.compareTo("joy") == 0)
			s = name + " is happy towards " + relname;
		else if(emotion.compareTo("sadness") == 0)
			s = name + " is sad towards " + relname;
		else if(emotion.compareTo("anger") == 0)
			s = name + " is angry towards " + relname;
		else if(emotion.compareTo("fear") == 0 || emotion.compareTo("trust") == 0 || 
				emotion.compareTo("shame") == 0 || emotion.compareTo("remorse") == 0 || 
				emotion.compareTo("pride") == 0 || emotion.compareTo("despair") == 0 ||
				emotion.compareTo("love") == 0)
			s = name + " is " + emotion + "ful towards " + relname;
		else if(emotion.compareTo("disgust") == 0)
			s = name + " is disgusted towards " + relname;
		else if(emotion.compareTo("morbid") == 0 || emotion.compareTo("submissive") == 0 || 
				emotion.compareTo("dominant") == 0 || emotion.compareTo("contempt") == 0)
			s = name + " is " + emotion + " towards " + relname;
		else if(emotion.compareTo("guilt") == 0)
			s = name + " is " + "guilty towards" + relname;
		else if(emotion.compareTo("envy") == 0)
			s = name + " is " + "envious towards " + relname;
		
		return s;
	}
	
	//This function holds all the updated values after emotion analysis is complete.
	public void updated_information(){
		for(Information temp : Relations){
			System.out.println();
			System.out.println("Character 1 : " + temp.get_character1_name());
			System.out.println("Character 2 : " + temp.get_character2_name());
			System.out.println("Relation : " + temp.get_relation());
			System.out.println("Events that took place between " + temp.get_character1_name() + " and " + temp.get_character2_name() + " : ");
			temp.display_events();
			/*
			System.out.println("Emotion space of " + temp.get_character1_name() + " : ");
			temp.display_character_emotion_space(temp.char1_emotions);
			System.out.println("Emotion space of " + temp.get_character2_name() + " : ");
			temp.display_character_emotion_space(temp.char2_emotions);
			System.out.println("Predicted emotion of " + temp.get_character1_name() + " : " + temp.PredictedEmotionOfChar1);
			System.out.println("Predicted emotion of " + temp.get_character2_name() + " : "  +temp.PredictedEmotionOfChar2);
			System.out.println("Relation between emotion and entity for " + temp.get_character1_name() + " : ");
			temp.display_emotion_entity_relation(temp.char1_emotion_entity_relation);
			System.out.println("Relation between emotion and entity for " + temp.get_character2_name() + " : ");
			temp.display_emotion_entity_relation(temp.char2_emotion_entity_relation);*/
			System.out.println("Causal connections for " + temp.get_character1_name() + " : ");
			temp.display_causal_connections(temp.causal_connection_char1);
			System.out.println("Causal connections for " + temp.get_character2_name() + " : ");
			temp.display_causal_connections(temp.causal_connection_char2);
			System.out.println("Good sentence for " + temp.get_character1_name() + " : " + temp.sentence_from_emotion_char1);
			System.out.println("Good sentence for " + temp.get_character2_name() + " : " + temp.sentence_from_emotion_char2);
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////INTRODUCING NEW ENTITIES AND CREATING CAUSAL CONNECTIONS/////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//This function is used to introduce new entities into Genesis using the story processor.
	public void introducing_new_story_elements(StoryProcessor processor){
		
		for(Information temp : Relations){
			Entity e1 = Translator.getTranslator().translateToEntity(temp.sentence_from_emotion_char1);
			List<Entity> causal_char1 = temp.causal_connection_char1;
			Entity e2 = Translator.getTranslator().translateToEntity(temp.sentence_from_emotion_char2);
			List<Entity> causal_char2 = temp.causal_connection_char2;
			
			//Injecting new elements.
			processor.injectElementWithDereference(e1);
			processor.injectElementWithDereference(e2);
			
			//Creating connections.
			create_connections(processor, e1, causal_char1);
			create_connections(processor, e2, causal_char2);
		}
	}
	
	//This function is used to create the causal connections between the new and the existing story elements.
	public void create_connections(StoryProcessor processor, Entity e, List<Entity> le){
		for(Entity t : le){
			Relation r = Rules.makeCause(e, t);
			processor.injectElementWithDereference(r);
		}
	}
	
	/*
	public static void main(String [] args){
		EmotionModelingAndAnalysis ob = new EmotionModelingAndAnalysis();
		ob.read_from_story();
		ob.analyze_emotion();
	}
	*/
	
}
