import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.text.DecimalFormat;

import com.sun.javafx.robot.impl.BaseFXRobot;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainClass extends Application {
	
	Socket socket = null;
	DataOutputStream output = null;
	static boolean isSocketAvailable = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Baxter Control");
		
		Pane pane = new Pane();
		
		// Background image Loading
		File f = new File("background.jpg");
		Image image = new Image(f.toURI().toString());
		ImageView iv = new ImageView();
		iv.setImage(image);
		pane.getChildren().add(iv);
		
		/*************************************************************
		 * BUTTONS
		 *************************************************************/
		
		Button btnHumanOn = buttonCreator("Human Follow", 120, 30, 40, 100, pane);
		Button btnHumanOff = buttonCreator("Off", 120, 30, 170, 100, pane);
		
		Button btnDefault = buttonCreator("Default", 120, 50, 40, 150, pane);
		Button btnHappy = buttonCreator("Happy", 120, 50, 170, 150, pane);
		
		Button btnSad = buttonCreator("Sad", 120, 50, 40, 220, pane);
		Button btnAngry = buttonCreator("Angry", 120, 50, 170, 220, pane);
		
		Button btnConfused = buttonCreator("Confused", 120, 50, 40, 290, pane);
		Button btnPanic = buttonCreator("Panic", 120, 50, 170, 290, pane);
		
		Button btnBored = buttonCreator("Bored", 120, 50, 40, 360, pane);
		Button btnCrafty = buttonCreator("Crafty", 120, 50, 170, 360, pane);
		
		Button btnWakeUp = buttonCreator("Wake Up", 120, 50, 40, 430, pane);
		Button btnSleep = buttonCreator("Sleep", 120, 50, 170, 430, pane);
		
		Button btnEnable = buttonCreator("Enable Robot", 260, 50, 350, 220, pane);
		
		/*************************************************************
		 * PORT AND ID
		 *************************************************************/
		TextField txtPort = txtCreator("Port", 400, 350, 100, pane);
		TextField txtIP = txtCreator("IP", 400, 350, 135, pane);
		Button btnConnect = buttonCreator("Connect", 85, 60, 525, 100, pane);
		Text txtWarning = stringCreator("Connection", 350, 190, pane);
		Text txtSituation = stringCreator("Situation", 350, 300, pane);
		
		
		/*************************************************************
		 * WOBBLE
		 *************************************************************/
		Slider sliderWobble = sliderCreator(250, 10, 350, 325, -1.3, 1.3, 0, true, pane);
		Button btnWobble = buttonCreator("Wobble", 100, 50, 350, 375, pane);
		Button btnReset = buttonCreator("Wobble -> 0", 100, 50, 500, 375, pane);
		Button btnQuit = buttonCreator("QUIT", 250, 50, 350, 430, pane);
		
		/*************************************************************
		 * ARM FOLLOW
		 *************************************************************/
		Button btnLeftArm = buttonCreator("Left", 75, 30, 670, 25, pane);
		Button btnOff = buttonCreator("OFF", 75, 30, 750, 25, pane);
		Button btnRightArm = buttonCreator("Right", 75, 30, 830, 25, pane);
		
		/*************************************************************
		 * ACTIONS
		 *************************************************************/
		Slider sliderHor = sliderCreator(250, 10, 670, 120, -80, 80, 0, true, pane);
		Slider sliderVer = sliderCreator(10, 250, 665, 160, -120, 120, 0, false, pane);
		Button btnLookSlider = buttonCreator("Look Slider", 200, 25, 700, 160, pane);
		
		Button btnUp = buttonCreator("Up", 75, 30, 765, 210, pane);
		Button btnCenter = buttonCreator("Center", 75, 30, 765, 250, pane);
		Button btnDown = buttonCreator("Down", 75, 30, 765, 290, pane);
		Button btnLeft = buttonCreator("<-", 50, 30, 705, 250, pane);
		Button btnRight = buttonCreator("->", 50, 30, 850, 250, pane);
		
		TextField txtHor = txtCreator("Horizontal [-80, 80]", 200, 720, 350, pane);
		TextField txtVer = txtCreator("Vertical [-120, 120]", 200, 720, 380, pane);
		Button btnLook = buttonCreator("Look", 168, 30, 720, 410, pane);
		
		RadioButton rdStable = rdCreator("Stable", 720, 460, pane);
		RadioButton rdDynamic = rdCreator("Head Movment", 720, 480, pane);
		
		ToggleGroup tg = new ToggleGroup();
		
		rdStable.setToggleGroup(tg);
		rdDynamic.setToggleGroup(tg);
		rdStable.setSelected(true);
		
		
		
		Scene scene = new Scene(pane, 960, 540);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		/*************************************************************
		 * BUTTON ACTIONS
		 *************************************************************/
		
		btnHumanOn.setOnAction(e-> {
			if(isSocketAvailable) {
				try {
					output.writeChars("human_follow_on");
					txtSituation.setText("human_follow_on");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		btnHumanOff.setOnAction(e-> {
			if(isSocketAvailable) {
				try {
					output.writeChars("human_follow_off");
					txtSituation.setText("human_follow_off");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	});
		
		btnConnect.setOnAction(e-> {
			String ip = txtIP.getText();
			int port = Integer.parseInt(txtPort.getText());
			try {
				socket = new Socket(ip, port);
				output = new DataOutputStream(socket.getOutputStream());
				isSocketAvailable = true;
				txtWarning.setText("Connection Succesfull :)");
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				txtWarning.setText("Connection Error!");
			}
			
		});
		
		btnWobble.setOnAction(e-> {
			if(isSocketAvailable) {
				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				double value = Double.valueOf(decimalFormat.format(sliderWobble.getValue()));
				try {
					output.writeChars("wobble_" + value);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				txtSituation.setText("Situation: " + "wobble_" + value);
			}
			
		});
		
		btnReset.setOnAction(e-> {
			
			if(isSocketAvailable) {
				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				double value = Double.valueOf(decimalFormat.format(sliderWobble.getValue()));
				try {
					output.writeChars("wobble_0");
					txtSituation.setText("Situation: " + "wobble_0");
					sliderWobble.setValue(0);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		btnLookSlider.setOnAction(e-> {
			if(isSocketAvailable) {

				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				int x = Double.valueOf(decimalFormat.format(sliderHor.getValue())).intValue();
				int y = Double.valueOf(decimalFormat.format(sliderVer.getValue())).intValue() * (-1);
				
				String command = "look_" + x + "_" + y;
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnLook.setOnAction(e-> {
			if(isSocketAvailable) {

				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				int x = Integer.parseInt(txtHor.getText());
				int y = Integer.parseInt(txtVer.getText());
				
				String command = "look_" + x + "_" + y;
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnUp.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "look_0_-120";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnCenter.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "look_0_0";
				sliderHor.setValue(0);
				sliderVer.setValue(0);
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnDown.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "look_0_120";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnLeft.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "look_-80_0";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnRight.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "look_80_0";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnLeftArm.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "left_arm_follow_on";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnRightArm.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "right_arm_follow_on";
				
				try {
					if(rdStable.isSelected()) {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
					}else {
						output.writeChars("dynamic_" + command);
						txtSituation.setText("Situation: " + "dynamic_" + command);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		btnOff.setOnAction(e-> {
			if(isSocketAvailable) {
				
				String command = "arm_follow_off";
				
				try {
						output.writeChars(command);
						txtSituation.setText("Situation: " + command);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		buttonAction(btnDefault, "default", txtSituation);
		buttonAction(btnHappy, "happy", txtSituation);
		buttonAction(btnSad, "sad", txtSituation);
		buttonAction(btnAngry, "angry", txtSituation);
		buttonAction(btnConfused, "confused", txtSituation);
		buttonAction(btnPanic, "panic", txtSituation);
		buttonAction(btnBored, "bored", txtSituation);
		buttonAction(btnCrafty, "crafty", txtSituation);
		buttonAction(btnWakeUp, "wake_up", txtSituation);
		buttonAction(btnSleep, "sleep", txtSituation);
		buttonAction(btnEnable, "enable", txtSituation);
		buttonAction(btnQuit, "exit", txtSituation);
		
		
		
	}
	
	public void buttonAction(Button btn, String msg, Text txt) {
		
		
		btn.setOnAction(e-> {
			System.out.println("Socket: " + isSocketAvailable);
			if(isSocketAvailable) {
				try {
					output.writeChars(msg);
					txt.setText("Situation: " + msg);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		
	}
	
	public Button buttonCreator(String name, double width, double height, double x, double y, Pane pane) {
		Button btn = new Button(name);
		btn.setPrefWidth(width);
		btn.setPrefHeight(height);
		btn.setLayoutX(x);
		btn.setLayoutY(y);
		pane.getChildren().add(btn);
		return btn;
	}
	
	public RadioButton rdCreator(String name, double x, double y, Pane pane) {
		RadioButton btn = new RadioButton(name);
		btn.setLayoutX(x);
		btn.setLayoutY(y);
		pane.getChildren().add(btn);
		return btn;
	}
	
	public Slider sliderCreator( double width, double height, double x, double y, double min, double max, double def,  boolean isHor, Pane pane) {
		Slider sliderHor = new Slider();
		sliderHor.setMin(min);
		sliderHor.setMax(max);
		sliderHor.setValue(def);
		sliderHor.setShowTickLabels(true);
		//sliderHor.setShowTickMarks(true);
		//sliderHor.setMajorTickUnit(10);
		//sliderHor.setMinorTickCount(5);
		//sliderHor.setBlockIncrement(10);
		sliderHor.setPrefWidth(width);
		sliderHor.setPrefHeight(height);
		if(isHor)
			sliderHor.setOrientation(Orientation.HORIZONTAL);
		else
			sliderHor.setOrientation(Orientation.VERTICAL);
		sliderHor.setLayoutX(x);
		sliderHor.setLayoutY(y);
		pane.getChildren().add(sliderHor);
		return sliderHor;
	}
	
	public TextField txtCreator(String name, double width, double x, double y, Pane pane) {
		TextField txt = new TextField();
		txt.maxWidth(width);
		txt.setLayoutX(x);
		txt.setLayoutY(y);
		txt.setPromptText(name);
		pane.getChildren().add(txt);
		return txt;
	}
	
	public Text stringCreator(String name, double x, double y, Pane pane) {
		Text txt = new Text(name);
		txt.setLayoutX(x);
		txt.setLayoutY(y);
		pane.getChildren().add(txt);
		return txt;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
