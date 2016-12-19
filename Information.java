package rohitSaha;

import java.util.*;
import bridge.reps.entities.Entity;
import bridge.reps.entities.Sequence;

/*This program holds all the details required to create emotion models of 2 characters.
 * Basic Emotions have been listed (with reference to Plutchik's Wheel of Emotions and Parrott's emotion classification) :
 * 1. Joy
 * 2. Anger
 * 3. Fear
 * 4. Disgust
 * 5. Sadness
 * 6. Trust
 * */

public class Information {
	
	String character_1 = "";
	String character_2 = "";
	String Relation = "";
	
	String LatestEmotionOfChar1 = "";
	String LatestEmotionOfChar2 = "";
	
	String PredictedEmotionOfChar1 = "";
	String PredictedEmotionOfChar2 = "";
	
	HashMap<String, Integer> char1_emotions = new HashMap<String, Integer>();
	HashMap<String, Integer> char2_emotions = new HashMap<String, Integer>();
	
	List<String> events = new ArrayList<String>();
	
	List<Entity> entities_between_2_characters = new ArrayList<Entity>();
	
	HashMap<String, List<Entity>> char1_emotion_entity_relation = new HashMap<String, List<Entity>>();
	HashMap<String, List<Entity>> char2_emotion_entity_relation = new HashMap<String, List<Entity>>();
	
	List<Entity> causal_connection_char1 = new ArrayList<Entity>();
	List<Entity> causal_connection_char2 = new ArrayList<Entity>();
	
	String sentence_from_emotion_char1 = "";
	String sentence_from_emotion_char2 = "";
	
	public Information(){
		
		char1_emotions.put("joy", 0);
		char1_emotions.put("anger", 0);
		char1_emotions.put("fear", 0);
		char1_emotions.put("disgust", 0);
		char1_emotions.put("sadness", 0);
		char1_emotions.put("trust", 0);

		char2_emotions.put("joy", 0);
		char2_emotions.put("anger", 0);
		char2_emotions.put("fear", 0);
		char2_emotions.put("disgust", 0);
		char2_emotions.put("sadness", 0);
		char2_emotions.put("trust", 0);
		
	}
	
	public String get_character1_name(){
		return character_1;
	}
	
	public String get_character2_name(){
		return character_2;
	}
	
	public String get_relation(){
		return Relation;
	}
	
	//This function updates the emotion model of character1.
	public void update_char1_emotion(String emotion){
		char1_emotions.put(emotion, char1_emotions.get(emotion) + 1);
	}
	
	//This function updates the emotion model of character2.
	public void update_char2_emotion(String emotion){
		char2_emotions.put(emotion, char2_emotions.get(emotion) + 1);
	}
	
	//This function is used to update the relation of an emotion and an entity for character1.
	public void update_char1_emotion_entity_relation(String e, Entity ent){
		if(char1_emotion_entity_relation.containsKey(e)){
			char1_emotion_entity_relation.get(e).add(ent);
		}
		else{
			List<Entity> temporary = new ArrayList<Entity>();
			temporary.add(ent);
			char1_emotion_entity_relation.put(e, temporary);
		}
	}
	
	//This function is used to update the relation of an emotion and an entity for character2.
	public void update_char2_emotion_entity_relation(String e, Entity ent){
		if(char2_emotion_entity_relation.containsKey(e)){
			char2_emotion_entity_relation.get(e).add(ent);
		}
		else{
			List<Entity> temporary = new ArrayList<Entity>();
			temporary.add(ent);
			char2_emotion_entity_relation.put(e, temporary);
		}
	}
	
	//This function displays the emotion and entity relationship.
	public void display_emotion_entity_relation(HashMap<String, List<Entity>>temp){
		for(String s : temp.keySet()){
			System.out.print(s + " : " );
			System.out.println(temp.get(s));
		}
	}
	
	//This function is used to display the causal connections.
	public void display_causal_connections(List <Entity> temp){
		System.out.println(temp);
	}

	//This function returns the most frequent emotion other than the one specified in the parameter.
	public String get_most_frequent_emotion(HashMap<String, Integer> temp, String latest_emotion){
		int highest_frequency_emotion = 0;
		String highest_emotion = "";
		
		for(String s : temp.keySet()){
			if(s.compareTo(latest_emotion)!=0){
				if(temp.get(s) > highest_frequency_emotion){
					highest_frequency_emotion = temp.get(s);
					highest_emotion = s;
				}
			}
		}
		if(highest_frequency_emotion == 0){
			//If no other emotion is present in the emotion space, then return "".
			return "";
		}
		else{
			//Return the emotion that has occured the most apart from latest_emotion.
			return highest_emotion;
		}
	}
	
	//This function updates the predicted emotion of character1.
	public void update_predicted_emotion_of_char1(String updated_emotion){
		PredictedEmotionOfChar1 = updated_emotion;
	}
	
	//This function updates the predicted emotion of character2.
	public void update_predicted_emotion_of_char2(String updated_emotion){
		PredictedEmotionOfChar2 = updated_emotion;
	}
	
	//This function keeps track of all the events in the form of entities.
	public void update_entities_between_2_characters(Entity ent){
		entities_between_2_characters.add(ent);
	}
	
	//This function updates the events that have taken place between character1 and character2.
	public void update_event(String verb){
		events.add(verb);
	}
	
	//This function displays all the events that have happened between 2 characters.
	public void display_events(){
		System.out.println(events);
	}
	
	public void display_updated_entities(List<Entity> ent){
		for(Entity temp : ent){
			System.out.println(temp);
		}
	}
	
	//This function displays the emotion space of a character.
	public void display_character_emotion_space(HashMap<String, Integer> temp) {
		Set set = temp.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	    }
	}
	
	
}
