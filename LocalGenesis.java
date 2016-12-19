// Updated 10 June 2015

package rohitSaha;

import genesis.*;
import start.*;
import storyProcessor.StoryProcessor;
import utils.Mark;
import connections.Connections;
import constants.Markers;
import demoMentalModel.*;

/**
 * This is a personal copy of Genesis I can play with without endangering the code of others. I will also want to look
 * at the main methods in Entity, for examples of how the representational substrate works, and Generator, for examples
 * of how to go from English to Genesis's inner language and back.
 * 
 * @author phw
 */

@SuppressWarnings("serial")
public class LocalGenesis extends Genesis {

	LocalProcessor localProcessor;

	public LocalGenesis() {
		super();
		Mark.say("Local constructor");

		// This one connects start preprocessor and passes signal when reader encounters "Start experiment."
		Connections.wire(Start.STAGE_DIRECTION_PORT, StartPreprocessor.getStartPreprocessor(), Start.STAGE_DIRECTION_PORT, getLocalProcessor());

		// This one connects a new port (as of 13 Jan 2015) from the story processor to your processor. Delivers the
		// story processor itself.

		Connections.wire(StoryProcessor.STORY_PROCESSOR_SNAPSHOT, getMentalModel1()
		        .getStoryProcessor(), StoryProcessor.STORY_PROCESSOR_SNAPSHOT, getLocalProcessor());
		
	}

	/*
	 * Get an instance of LocalProcessor to do something with the output of a complete story object from a story
	 * processor.
	 */
	public LocalProcessor getLocalProcessor() {
		if (localProcessor == null) {
			
			
			localProcessor = new LocalProcessor();
		}
		return localProcessor;
	}
	

	/*
	 * Fires up my copy of Genesis in a simple Java frame. It can also be started up in other ways; that is the reason
	 * for the startInFrame call.
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		LocalGenesis myGenesis = new LocalGenesis();
		myGenesis.startInFrame();
	}
}
