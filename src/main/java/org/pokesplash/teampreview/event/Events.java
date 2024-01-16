package org.pokesplash.teampreview.event;

import org.pokesplash.teampreview.event.events.ExampleEvent;
import org.pokesplash.teampreview.event.obj.Event;

/**
 * Class that holds all of the events.
 */
public abstract class Events {
	public static Event<ExampleEvent> EXAMPLE = new Event<>();

}