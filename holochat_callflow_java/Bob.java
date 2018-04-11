import javax.swing.JButton;
import javax.swing.JLabel;

public class Bob extends Client{
	Bob(Communication com) {
		super(com);
		this.name="Bob";
		y=0;
		x=400;
		lab ="Bob's log";
	}

	@Override
	public void run(){
		super.run();
		window.setVisible(true);
		
		com.getReady();
		com.userActionCallAccept();
		
		
		
		
	}
}
