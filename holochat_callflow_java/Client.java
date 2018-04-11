import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends Thread{ //usually I use Runnable but I want to access the state (see below) of the current thread, I thought the best solution was to extends thread
	
	Communication com;
	JFrame window;
	String state;
	String name;
	JPanel pan ;
	String lab;
	JLabel label;
	int x,y;
	Client(Communication com){
		window = new JFrame();
		pan = new JPanel();
		label = new JLabel(lab);
		this.com=com;
		this.state="notready";
	}
	
	public void upload_label(String s){
		label.setText("<html>"+lab+"<br/>"+s+"</html>");
		lab = lab+"<br/>"+s;
	}
	
	@Override
	public void run(){ //display GUI
		
		window.setTitle(name);
	    window.setSize(400, 600);
	    window.setLocation(x,y);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setLayout(new BorderLayout());
		//window.setContentPane(pan);
	    window.getContentPane().add(label, BorderLayout.PAGE_START);
		//pan.add(label);
		
		
	}
			
}
