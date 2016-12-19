// Updated 10 June 2015

package rohitSaha;

import generator.Generator;
import genesis.GenesisGetters;
import innerese.RoleFrames;
import innerese.Rules;
import start.Start;
import storyProcessor.StoryProcessor;
import summarizer.Summarizer;
import translator.Translator;
import utils.Mark;
import bridge.reps.entities.Entity;
import bridge.reps.entities.Relation;
import bridge.reps.entities.Sequence;
import connections.*;
import constants.Markers;
import demoMentalModel.MentalModelDemo;

/**
 * A local processor class that just receives a complete story description, takes apart the wrapper object to fetch
 * various parts of the complete story description, and prints them so you can see what is in there.
 */
public class LocalProcessor extends AbstractWiredBox {
	
	// EXamples of how ports are named, not used here
	public final String MY_INPUT_PORT = "my input port";
	public final String MY_OUTPUT_PORT = "my output port";

	/**
	 */
	public LocalProcessor() {
		super("Local story processor");
		// Receives story processor when story has been processed
		Connections.getPorts(this).addSignalProcessor(Start.STAGE_DIRECTION_PORT, this::reset);

		Connections.getPorts(this).addSignalProcessor(StoryProcessor.STORY_PROCESSOR_SNAPSHOT, this::processStoryProcessor);
	}

	/**
	 * You have to make all signal processors void methods of one argument, which must be of the Object class, so there
	 * will be a bit of casting.
	 * <p>
	 * This one writes information extracted from the story processor received on the STORY_PROCESSOR_SNAPSHOT port.
	 */
	public void processStoryProcessor(Object signal) {
		boolean debug = true;
		// Make sure it is what was expected
		// Make sure it is what was expected
		Mark.say("Entering processStoryProcessor");

		if (signal instanceof StoryProcessor) {
			StoryProcessor processor = (StoryProcessor) signal;

			Sequence story = processor.getStory();
			Sequence explicitElements = processor.getExplicitElements();
			Sequence inferences = processor.getInferredElements();
			Sequence concepts = processor.getInstantiatedConceptPatterns();
			Mark.say(debug, "\n\n\nStory elements");
			story.getElements().stream().forEach(f -> Mark.say(debug, f));
			
			for(Entity e : processor.getRuleMemory().getRuleList()){
				Mark.say(e);
			}
			/*
			Mark.say("Emotion Modeling and Analysis by rohitSaha and Prof.PHW : ");
			EmotionModelingAndAnalysis ob = new EmotionModelingAndAnalysis();
			for(Entity obj : story.getElements()){
				ob.getting_elements(obj);
			}
		
			//ob.display_Information();
			ob.analyze_emotion();
			ob.updated_information();
			ob.introducing_new_story_elements(processor);
			*/
			/*Test code for creating causal connections between entities.
			Entity temp = Translator.getTranslator().translateToEntity("XX loves YY");
			Entity temp2 = Translator.getTranslator().translateToEntity("zz loves YY");
			
			processor.injectElementWithDereference(temp);
			processor.injectElementWithDereference(temp2);
			Relation r = Rules.makeCause(temp, temp2);
			processor.injectElementWithDereference(r);
			*/
			/*
			
			Mark.say(debug, "\n\n\nExplicit story elements");
			explicitElements.stream().forEach(e -> Mark.say(debug, e));
			Mark.say(debug, "\n\n\nInstantiated commonsense rules");
			inferences.stream().forEach(e -> Mark.say(debug, e));
			Mark.say(debug, "\n\n\nInstantiated concept patterns");
			concepts.stream().forEach(e -> Mark.say(debug, e));
			Mark.say(debug, "\n\n\nAll story elements, in English");
			Generator generator = Generator.getGenerator();
			story.stream().forEach(e -> Mark.say(debug, generator.generate(e)));
			
			processor.getRuleMemory().getRuleSequence().getElements().stream().filter(r -> r.getProbability() == null ? true : false)
			        .forEach(r -> Mark.say(debug, "Rule:", r.getProbability(), r));
			
			Mark.say("Recorded stories", GenesisGetters.getMentalModel1().getStoryMemory().size());

			Mark.say("Map size", GenesisGetters.getMentalModel1().getStoryMemory().getMemory().size());
			
			GenesisGetters.getMentalModel1().getStoryMemory().getMemory().values().stream().forEach(m -> Mark.say("Title", m.getTitle()));
			*/
			
			
			
		}
	}

	public void reset(Object signal) {
		// Does nothing right now
	}
	

	/**
	 * Merely calls main method in LocalGenesis, a shortcut
	 */
	public static void main(String[] args) {
		MentalModelDemo.main(args);
	}
}