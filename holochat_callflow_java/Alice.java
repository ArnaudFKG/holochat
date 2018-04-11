import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Alice extends Client{
	
	Alice(Communication com) {
		super(com);
		this.name="Alice";
		x = y = 0;
		lab = "Alice's log";
	}
	

	@Override
	public void run(){
		super.run();
		window.setVisible(true);
		this.state="ready";
		this.upload_label("A's state: ready");
		com.userActionCall();
	}
}
