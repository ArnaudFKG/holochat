package com.fickinger.arnaud.holochat;

public enum Event {
    messageCallStart, messageCallStartAck, messageCallOffer, messageCallIceCandidate, messageCallAnswer, messageCallReject, messageCallBusy, messageCallEnd, userActionCall, userActionCallAccept, userActionCallReject, userActionCallDisconnect, systemTimeout, systemWebrtcStable, systemWebRtcDisconnect, userActionCancelFailedCall, userActionRetryFailedCall, notValid;

}
