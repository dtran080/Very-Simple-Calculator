package secondPack;

import java.awt.EventQueue;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class Calculator {

	private JFrame frame;
	private JTextArea textArea;
	private StringBuilder num1,num2;
	private char operator;
	private boolean isNum1,isNum2,resultDisplay;
	private ActionListener numberHandler,operatorHandler,functionHander;
	private NumberFormat nf;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Calculator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void clearValue(){
		num1.setLength(0); //clear buffer
		num2.setLength(0); //clear buffer
		operator = ' ';		//clear 
		textArea.setText("");
		resultDisplay = false; //clear result
		isNum1=true;	//num1 is available
		isNum2=false;	//num2 is not
	}
	private void errorMessage(String message){
		textArea.setText(message);
		isNum1 = isNum2 = false;
		resultDisplay = true;
		return;
	}
	private void initialize() {
		nf = NumberFormat.getInstance();
		isNum1 = true;
		isNum2 = false;
		resultDisplay = false;
		num1 = new StringBuilder();
		num2 = new StringBuilder();
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		numberHandler = new ActionListener() {
			//handle digits and '.'
			@Override
			public void actionPerformed(ActionEvent e) {
				if (resultDisplay){ //result on screen
					clearValue(); //clear value
				}
				JButton btn= (JButton)e.getSource();
				String text = btn.getText();
				if (isNum1){
					textArea.append(text);
					num1.append(text); //append the num to num1
				} else if(isNum2){ //append the text to num2
					textArea.append(text);
					num2.append(text);
				}
			}
		};
		operatorHandler = new ActionListener() {
			//handle operator +-*/% and switching Number 
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn= (JButton)e.getSource();			
				if (isNum1){ //get operator from input
					isNum1 = false;	
					operator = btn.getText().charAt(0);
					textArea.append(String.valueOf(operator)); //operator change
				}else if (num2.length()==0){//allow to change operator if value 2 hasn't enter
					operator = btn.getText().charAt(0); //operator change
					textArea.setText(num1.toString()+String.valueOf(operator));
				}
				isNum2 = true;
				//isNum2 only trigger when an operator is selected
			}
		};
		functionHander = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!resultDisplay){ 
					JButton btn= (JButton)e.getSource();
					String text = btn.getText();
					double curr = 0.0;
					try{ //select the current number to get the function
						if (isNum1||(!isNum1 && num2.length()==0)) //change num1 value
							curr = nf.parse(num1.toString()).doubleValue();
						else if (isNum2) //change num2 value
							curr = nf.parse(num2.toString()).doubleValue();
					}catch(ParseException pe){
						textArea.setText("Format Error");
						return;
					}
					switch (text){
					case "sin":
						curr = Math.sin(curr);
						break;
					case "cos":
						curr = Math.cos(curr);
						break;
					case "tan":
						curr = Math.tan(curr);
						break; 
					case "x^2":
						curr = curr*curr;
						break;
					case "sqrt":
						if (curr>=0)
							curr = Math.sqrt(curr);
						else {
							errorMessage("Imaginary thing");							
							return;
						}
						break;
					case "+/-":
						curr = -curr;
						break;
					case "pi":
						if (curr!=0.0){
							curr*=Math.PI;
						} else curr=Math.PI;
						break;
					case "e^x":
						curr = Math.exp(curr);
						break;
					case "log":
						curr = Math.log10(curr);
						break;
					case "ln":
						curr = Math.log(curr);
						break;
					case "1/x":
						curr = 1/curr;
						break;
					case "n!":
						if (curr%1==0){ //integer
							if (curr>15){
								errorMessage("Too big to display");
								return;
							}
							int res = 1;
							for (int i=2;i<=curr;i++){
								res*=i;
							}
							curr =res;
						} else {
							errorMessage("Can't calculate factorial with double");							
							return;
						}
						break;
					default:
						errorMessage("Unfinished code!");
						return;
					}
					if (Double.isNaN(curr)||Double.isInfinite(curr)){
						errorMessage("Math error");
						return;
					}
					if (isNum1||(!isNum1 && num2.length()==0)){ //display changes in num1
						num1.setLength(0);
						if (curr%1==0)
							num1.append(String.valueOf((int)curr));	//change buff
						else num1.append(String.valueOf(curr));
						
						textArea.setText(num1.toString()); //change display
						if (operator!=' ') //may include buffer
							textArea.append(String.valueOf(operator));
						isNum1=false;
					} else if (isNum2){ //display changes in num2
						num2.setLength(0);
						if (curr%1==0)
							num2.append(String.valueOf((int)curr));	//change buff
						else num2.append(String.valueOf(curr)); //change buffer
						textArea.setText(num1.toString()+String.valueOf(operator)+String.valueOf(curr));
					}
				}
			}
		};
		textArea = new JTextArea();
		textArea.setFont(new Font("Dialog", Font.BOLD, 15));
		textArea.setTabSize(0);
		frame.getContentPane().add(textArea, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new MigLayout("", "[44.00][44.0][44.0]", "[][][][][][]"));
		
		JButton button = new JButton("7");
		button.addActionListener(numberHandler);
		panel.add(button, "cell 0 0,growx,aligny top");
		
		JButton button_3 = new JButton("8");
		button_3.addActionListener(numberHandler);
		panel.add(button_3, "cell 1 0,growx");
		
		JButton button_6 = new JButton("9");
		button_6.addActionListener(numberHandler);
		panel.add(button_6, "cell 2 0,growx");
		
		JButton button_1 = new JButton("4");
		button_1.addActionListener(numberHandler);
		panel.add(button_1, "cell 0 1,growx");
		
		JButton button_4 = new JButton("5");
		button_4.addActionListener(numberHandler);
		panel.add(button_4, "cell 1 1,growx");
		
		JButton button_7 = new JButton("6");
		button_7.addActionListener(numberHandler);
		panel.add(button_7, "cell 2 1,growx");
		
		JButton button_2 = new JButton("1");
		button_2.addActionListener(numberHandler);
		panel.add(button_2, "cell 0 2,growx");
		
		JButton button_5 = new JButton("2");
		button_5.addActionListener(numberHandler);
		panel.add(button_5, "cell 1 2,growx");
		
		JButton button_8 = new JButton("3");
		button_8.addActionListener(numberHandler);
		panel.add(button_8, "cell 2 2,growx");
		
		JButton button_9 = new JButton("+/-");
		button_9.addActionListener(functionHander);
		panel.add(button_9, "cell 0 3,growx");
		
		JButton button_10 = new JButton("0");
		button_10.addActionListener(numberHandler);
		panel.add(button_10, "cell 1 3,growx");
		
		JButton button_11 = new JButton(".");
		button_11.addActionListener(numberHandler);
		panel.add(button_11, "cell 2 3,growx");
		
		JButton button_12 = new JButton("+");
		button_12.addActionListener(operatorHandler);
		panel.add(button_12, "cell 0 4,growx");
		
		JButton button_13 = new JButton("-");
		button_13.addActionListener(operatorHandler);
		panel.add(button_13, "cell 1 4,growx");
		
		JButton btnC = new JButton("C");
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearValue();
			}
		});
		panel.add(btnC, "cell 2 4,growx");
		
		JButton btnX = new JButton("x");
		btnX.addActionListener(operatorHandler);
		panel.add(btnX, "cell 0 5,growx");
		
		JButton button_14 = new JButton("/");
		button_14.addActionListener(operatorHandler);
		panel.add(button_14, "cell 1 5,growx");
		
		JButton button_15 = new JButton("=");
		button_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (isNum2){ //= only select when there are num1 and num2 has value
					resultDisplay = true;
					double a = 0.0,b=0.0;
					try {
						a = nf.parse(num1.toString()).doubleValue();
						b = nf.parse(num2.toString()).doubleValue();
					} catch (ParseException e1) {
						errorMessage("Number Format Error");
						return;
					}
					textArea.append("=");
					System.out.printf("%f %f\n", a,b);
					//calculation goes here
					switch (operator){
					case '+':
						a+=b;
						break;
					case '-':
						a-=b;
						break;
					case 'x':
						a*=b;
						break;
					case '/': 
						if (b!=0)
							a/=b;
						else{
							errorMessage("Can't Divided By 0");
							return;
						}
						break;
					case '%':	
						if (b!=0)
							if (a%1==0&&b%1==0)
								a%=b;
							else{
								errorMessage("Can't mod with double");
								return;
							}
						else{
							errorMessage("Can't Divided By 0");
							return;
						}
						break;
					case '^':						
						if (Math.pow(a, b)>=Integer.MAX_VALUE){
							errorMessage("Too big to display");
							return;
						}
						a = Math.pow(a, b);
						break;
					default:
						errorMessage("This can't happened");
						return;
					}
					if(a%1==0) //check if a is integer
						textArea.append(String.valueOf((int)a));
					else textArea.append(String.valueOf(a));
				}
			}
		});
		panel.add(button_15, "cell 2 5,growx");
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new MigLayout("", "[][][]", "[][][][][]"));
		
		JButton btnX_1 = new JButton("x^2");
		btnX_1.addActionListener(functionHander);
		panel_1.add(btnX_1, "cell 0 0,growx");
		
		JButton btnXy = new JButton("^");
		btnXy.addActionListener(operatorHandler);
		panel_1.add(btnXy, "cell 1 0,growx");
		
		JButton btnMod = new JButton("%");
		btnMod.addActionListener(operatorHandler);
		panel_1.add(btnMod, "cell 2 0,growx");
		
		JButton btnSin = new JButton("sin");
		btnSin.addActionListener(functionHander);
		panel_1.add(btnSin, "cell 0 1,growx");
		
		JButton btnCos = new JButton("cos");
		btnCos.addActionListener(functionHander);
		panel_1.add(btnCos, "cell 1 1,growx");
		
		JButton btnTan = new JButton("tan");
		btnTan.addActionListener(functionHander);
		panel_1.add(btnTan, "cell 2 1,growx");
		
		JButton btnSqrt = new JButton("sqrt");
		btnSqrt.addActionListener(functionHander);
		panel_1.add(btnSqrt, "cell 0 2,growx");
		
		JButton btnLog = new JButton("log");
		btnLog.addActionListener(functionHander);
		panel_1.add(btnLog, "cell 1 2,growx");
		
		JButton btnLn = new JButton("ln");
		btnLn.addActionListener(functionHander);
		panel_1.add(btnLn, "cell 2 2,growx");
		
		JButton btnx = new JButton("10^x");
		btnx.addActionListener(functionHander);
		panel_1.add(btnx, "cell 0 3,growx");
		
		JButton btnPi = new JButton("pi");
		btnPi.addActionListener(functionHander);
		panel_1.add(btnPi, "cell 1 3,growx");
		
		JButton btnEx = new JButton("e^x");
		btnEx.addActionListener(functionHander);
		panel_1.add(btnEx, "cell 2 3,growx");
		
		JButton btnN = new JButton("n!");
		btnN.addActionListener(functionHander);
		panel_1.add(btnN, "cell 0 4,growx");
		
		JButton btnx_1 = new JButton("1/x");
		btnx_1.addActionListener(functionHander);
		panel_1.add(btnx_1, "cell 1 4,growx");
	}

}
