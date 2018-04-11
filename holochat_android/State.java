package com.fickinger.arnaud.holochat;

import java.util.HashMap;
public enum State {
    InCall,
    FailedFromUserRejection,
    waitingForCallConnection(createTransitions(Event.systemWebrtcStable, InCall)),
    waitingForCandidate(createTransitions(Event.messageCallIceCandidate, waitingForCallConnection)),
    waitingForAnswer(createTransitions(Event.messageCallAnswer,  waitingForCallConnection), Event.messageCallIceCandidate),
    sendingAnswer(createTransitions(Event.messageCallIceCandidate, waitingForCallConnection), Event.messageCallAnswer),

    Started(createTransitions(null, waitingForAnswer), Event.messageCallOffer),

    Starting(createTransitions(new Event[]{Event.messageCallStartAck, Event.messageCallReject}, new State[]{State.Started, State.FailedFromUserRejection})),
    Ready(createTransitions(new Event[] {Event.userActionCall, Event.userActionCallAccept}, new State[]{Starting, Started})),

    waitingForOffer(createTransitions(Event.messageCallOffer, sendingAnswer)),
    failedWithTimeout,

    failedFromBusyRemoteUser;

    //The graph of call flow 1 contains a cycle! I can't define the transitions of inCall directly



    HashMap<Event, State> transitions; //all possible next states
    Event eventToSend; //what to do on state change


    public static HashMap<Event, State> createTransitions(Event e, State s){ //to easily define transitions in state enum
        HashMap<Event, State> hm = new HashMap<>();
        hm.put(e, s);
        return hm;
    }

    public static HashMap<Event, State> createTransitions(Event[] e, State[] s){
        HashMap<Event, State> hm = new HashMap<>();
        for(int i=0; i<e.length; i++){
            hm.put(e[i], s[i]);
        }
        return hm;
    }


    public boolean validChange(Event e) {
        return (this.transitions.get(e) != null);
    }

    State() { // States without next States and without actions
        this.transitions = new HashMap<Event, State>();
        this.eventToSend = null;
    }

    State(Event e) { // States with action but without next State
        this.transitions = new HashMap<Event, State>();
        this.eventToSend = e;
    }

    State(HashMap<Event, State> transitions) { // States with next States but without actions
        this.transitions = transitions;
        this.eventToSend = null;
    }

    State(HashMap<Event, State> transitions, Event e) { //
        this.transitions = transitions;
        this.eventToSend = e;
    }

}

