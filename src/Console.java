package com.studyAll.test0;
import android.widget.*;
import android.text.method.*;
import android.view.*;
import android.text.*;
import android.view.View.*;

public class Console implements TextWatcher
{
	private EditText m_shareConsole;//Share Console
	private int m_ioStatus;//IO status
	private int m_endType = ConsoleStatus.STOP_CHAR;
	private String m_stopToken = "\n";
	private String non_input;//Before Input,a back-up of the edit text
	private OnEndInputListener m_recall_listener = null;
	//Now R Useless
	//private boolean errInput = false;//The User's bad behaviors
	
	public String inputing = "";//Recorded the last success input
	
	public void Input(){//Set The Mode to Input Mode
		m_ioStatus = ConsoleStatus.CONSOLE_INPUT;
		non_input = m_shareConsole.getText().toString();
		m_shareConsole.setText(non_input);
	}
	//WeMustUseThisFuctionOrIt'sExtensionsToChangeTheText
	public void ChangeTextSafely(String in,boolean uNI){
		m_shareConsole.removeTextChangedListener(this);
		m_shareConsole.setText(in);
		m_shareConsole.addTextChangedListener(this);
		if(uNI){
			non_input = in;
		}
	}
	
	public void setOnEndInputListener(OnEndInputListener lsnr){
		m_recall_listener = lsnr;
	}
	
	public String GetText(){
		return m_shareConsole.getText().toString();
	}
	
	//Make CALL easier (・∀・)
	public String GT(){return GetText();}
	public void CTS(String in,boolean updateNI){ChangeTextSafely(in,updateNI);}
	public void CTS(String in){CTS(in,true);}
	
	public void append(String newString){
		CTS(GT() + newString);
	}
	
	public void newLine(){
		append("\n");
	}
	
	public void setAutoNewLine(boolean autoLine){//Android Have BUG until 4.1
		m_shareConsole.setHorizontallyScrolling(autoLine);
	}
	
	public void setStopInputingToken(char token){
		m_endType = ConsoleStatus.STOP_CHAR;
		m_stopToken = token + "";
	}
	
	public void setStopInputingToken(String tokenString){
		m_endType = ConsoleStatus.STOP_STRING;
		m_stopToken = tokenString;
	}
	
	public Console(final EditText shareConsole){
		m_shareConsole = shareConsole;//Get the instance
		m_ioStatus =ConsoleStatus.CONSOLE_OUTPUT;//Initialize the status with default value OUTPUT
		//Mistake0:I forget to initialize non_input ¯_(ツ)_/¯
		non_input = "";
		final KeyListener oldListener = shareConsole.getKeyListener();
		//Get the old key listener in order to make the edit text editable
		shareConsole.setKeyListener(new KeyListener(){

				@Override
				public int getInputType()
				{
					return oldListener.getInputType();
				}

				@Override
				public boolean onKeyDown(View p1, Editable p2, int p3, KeyEvent p4)
				{
					//Checking Inputs
					if(m_ioStatus != ConsoleStatus.CONSOLE_INPUT){
						return false;//We can't edit the text
					}else if(m_shareConsole.getText().length() == non_input.length() && 
					         p4.getKeyCode() == p4.KEYCODE_DEL){
						m_shareConsole.setText(non_input);
						m_shareConsole.setSelection(non_input.length());
						return false;//The Delete is out of range
					}
					return oldListener.onKeyDown(p1,p2,p3,p4);
				}

				@Override
				public boolean onKeyUp(View p1, Editable p2, int p3, KeyEvent p4)
				{
					return oldListener.onKeyUp(p1,p2,p3,p4);
				}

				@Override
				public boolean onKeyOther(View p1, Editable p2, KeyEvent p3)
				{
					return oldListener.onKeyOther(p1,p2,p3);
				}

				@Override
				public void clearMetaKeyState(View p1, Editable p2, int p3)
				{
					oldListener.clearMetaKeyState(p1,p2,p3);
				}
		});
		shareConsole.addTextChangedListener(this);
	}
	
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		inputing = GT();
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		if(m_shareConsole.getSelectionStart() < non_input.length()){
			//Now R Useless
			//errInput = true;
			CTS(inputing,false);
			m_shareConsole.setSelection(non_input.length());
		}
	}

	@Override
	public void afterTextChanged(Editable p1)
	{
		String inputResult = GT();
		inputResult = inputResult.substring(non_input.length());//GetTheInput Message
		try{
			if(m_endType == ConsoleStatus.STOP_CHAR){//OneLen
				char end = inputResult.charAt((inputResult.length() - 1));
				if(end == m_stopToken.charAt(0)){//The User Entered the stop token break
					if(m_recall_listener != null){
						m_recall_listener.perform(inputResult.substring(0,inputResult.length()-1));
					}
				}
			}
		}catch(Exception e){
			//Ignore it,because the trycode causes the excpetion of IndexOutOfBounds
		}
	}
	
	public interface OnEndInputListener
	{
		public void perform(String result);
	}
}
