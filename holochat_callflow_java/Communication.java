import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Communication {
	private final Lock lock; // to use Condition and also usefull to be sure one is the caller and the other the callee
	//TODO cyclicbarrier
	private final Condition messageCallStart, messageCallStartAck, messageCallOffer, messageCallAnswer, messageCallIceCandidate, messageCallEnd; //model events
	Communication(){
		//this.barrier = new CyclicBarrier(2);
		this.lock = new ReentrantLock();
		messageCallStart = lock.newCondition();
		messageCallStartAck = lock.newCondition();
		messageCallOffer = lock.newCondition();
		messageCallAnswer = lock.newCondition();
		messageCallIceCandidate = lock.newCondition();
		messageCallEnd = lock.newCondition();
	}

	void getReady(){
		lock.lock();
		try{
			Bob callee = (Bob) Client.currentThread();
			callee.state="ready";
			callee.upload_label("B's state: ready");
			try {
				callee.upload_label("B waits for messageCallStart");
				messageCallStart.await();
				callee.upload_label("B received messageCallStart");
			} catch (InterruptedException e) {
				return;
			}		
		} finally{
			lock.unlock();
		}
	}

	void userActionCall(){
		lock.lock(); //a lock so that there is always a caller and a callee and not both trying to call at the same time
		try{
			Alice caller = (Alice) Thread.currentThread();
			if(caller.state!="ready"){
				return;
			}			

			try {
				Thread.sleep(1000); //to be sure that signal is sent after await
				caller.upload_label("A calls B (messageCallStart)");
				messageCallStart.signal(); // Communication is shared by just two clients, I'm sure A will signal B
				caller.state="starting";
				caller.upload_label("A's state: starting");		
				caller.upload_label("A waits for call accept (messageCallStartAck)");
				messageCallStartAck.await(); 
				caller.upload_label("A received call accept (messageCallStartAck)");
				caller.state="started";
				caller.upload_label("A'state: started");	
				Thread.sleep(1000);
				caller.upload_label("A send call offer (messageCallOffer)");	
				messageCallOffer.signal(); 
				caller.state="waitingForAnswer";
				caller.upload_label("A's state: waitingForAnswer");
				caller.upload_label("A waits for call answer (messageCallAnswer)");
				messageCallAnswer.await();
				caller.upload_label("A received call answer (messageCallAnswer)");
				Thread.sleep(1000);
				caller.upload_label("A send ICE message (messageCallIceCandidate)");
				messageCallIceCandidate.signal();
				caller.upload_label("A waits for ICE message (messageCallIceCandidate)");
				messageCallIceCandidate.await();
				caller.upload_label("A received ICE message (messageCallIceCandidate)");
				caller.state="waitingForCallConnexion";
				caller.upload_label("A's state: waitingForCallConnexion");
				//barrier
				caller.upload_label("P2P connexion established (systemWebrtcStable)");
				caller.state="inCall";
				caller.upload_label("A's state: inCall");
				Thread.sleep(1000);
				caller.upload_label("A ends call (messageCallEnd)");
				messageCallEnd.signal();
				caller.state="ready";
				caller.upload_label("A's state: ready");
			} catch (InterruptedException e) {
				return;
			} 
			return;			
		} finally{
			lock.unlock();
		}	
	}

	void userActionCallAccept(){
		lock.lock();
		try{
			Bob callee = (Bob) Client.currentThread();
			if(callee.state!="ready"){
				return;
			}

			try {
				Thread.sleep(1000);
				callee.upload_label("B accepts call (messageCallStartAck)");
				messageCallStartAck.signal();
				callee.state="started";
				callee.upload_label("B's state: started");
				callee.state="waitingForOffer";
				callee.upload_label("B's state: waitingForOffer");
				callee.upload_label("B waits call offer (messageCallOffer)");
				messageCallOffer.await();
				callee.upload_label("B receives call offer (messageCallOffer)");
				Thread.sleep(1000);
				callee.upload_label("B send call answer (messageCallAnswer)");
				messageCallAnswer.signal();	
				callee.upload_label("B waits for ICE message (messageCallIceCandidate)");
				messageCallIceCandidate.await();
				callee.upload_label("B received ICE message (messageCallIceCandidate)");
				Thread.sleep(1000);
				callee.upload_label("B send ICE message (messageCallIceCandidate)");
				messageCallIceCandidate.signal();
				callee.state="waitingForCallConnection";
				callee.upload_label("B's state: waitingForCallConnection");
				//barrier
				callee.upload_label("P2P connexion established (systemWebrtcStable)");
				callee.state="inCall";
				callee.upload_label("B's state: inCall");
				messageCallEnd.await();
				callee.upload_label("B received call end (messageCallEnd)");
				callee.state="ready";
				callee.upload_label("B's state: ready");
			} catch (InterruptedException e) {
				return;
			}
		}finally{
			lock.unlock();
		}



	}

}
