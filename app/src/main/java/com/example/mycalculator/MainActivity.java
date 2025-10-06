package com.example.mycalculator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn_mult, btn_sub, btn_div, btn_clear, btn_plus, btn_equal, btn_dot;
    TextView text_display;

    // This is to evaluate the math expression

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializes all the number buttons from 0 - 9
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.button5);
        btn5 = (Button) findViewById(R.id.button6);
        btn6 = (Button) findViewById(R.id.button7);
        btn7 = (Button) findViewById(R.id.button9);
        btn8 = (Button) findViewById(R.id.button10);
        btn9 = (Button) findViewById(R.id.button11);

        //Initializes all operations for the calc
        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_mult = (Button) findViewById(R.id.button12);
        btn_sub = (Button) findViewById(R.id.button8);
        btn_div = (Button) findViewById(R.id.button16);

        //Initializes all special buttons (Clear, add & decimal)
        btn_equal = (Button) findViewById(R.id.btn_equal);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_dot = (Button) findViewById(R.id.btn_dot);
        text_display = (TextView) findViewById(R.id.textview_input_display);
        setClickListeners();
    }

    private void setClickListeners() {
        //Array that holds all button ID's
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.button5, R.id.button6,
                R.id.button7, R.id.button9, R.id.button10, R.id.button11,
                R.id.btn_plus, R.id.button12, R.id.button8, R.id.button16,
                R.id.btn_equal, R.id.btn_clear, R.id.btn_dot
        };
        //enhanced for loop that goes through the previous array and adds a listener to all of them.
        for (int intID : buttonIds) {
            findViewById(intID).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int[] numButtonID = {
                R.id.btn0,
                R.id.btn1,
                R.id.btn2,
                R.id.btn3,
                R.id.button5,
                R.id.button6,
                R.id.button7,
                R.id.button9,
                R.id.button10,
                R.id.button11,
                R.id.btn_plus,
                R.id.button12,
                R.id.button8,
                R.id.button16,
                R.id.btn_dot
        };
        //enhanced for loop that goes through the previous array and makes them text
        for(int id : numButtonID){
            if(id == v.getId()){
                //Makes a temp button that uses the id to find the text associated with the button
                Button temp = findViewById(id);
                //Then uses the add number method to make it a string to parse into the viewer. (Ergo, just displays the number on the screen)
                addNumber(temp.getText().toString());
            }
        }
        //For the equal button case, we instead call the eval method to get the math output
        if(v.getId() == R.id.btn_equal){
            //Included code, should be modified
            String result = null;
            try {
                result = evaluate(text_display.getText().toString());
                text_display.setText(result);
            } catch (Exception e) {
                // Debug message below do not print.
                String message = (e.getMessage() != null && !e.getMessage().isEmpty()) ? e.getMessage() : e.toString();
                text_display.setText("Error");
//                text_display.setText(message);
                android.util.Log.e("MyCalculator", "Evaluation error", e);
            }
            //Lastly, checks and clears the display
        } else if (v.getId() == R.id.btn_clear){
            clear_display();
        }
    }

    private String evaluate(String expression) throws Exception {
        String processedExpression = expression
                .replace("×", "*")
                .replace("÷", "/");
        
        Expression expr = new ExpressionBuilder(processedExpression).build();
        Double result = expr.evaluate();
        // Handle NaN/Infinity 
        if (Double.isNaN(result) || Double.isInfinite(result)) return "Error";
        
        // Check if the result is effectively an integer
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return String.valueOf(result.longValue());
        } else {
            BigDecimal decimal = new BigDecimal(result);
            decimal = decimal.setScale(10, BigDecimal.ROUND_HALF_UP);
            // round to 10 decimal places (HALF_UP), then drop any trailing zeros
            BigDecimal decimal = new BigDecimal(Double.toString(result))
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros();
            return decimal.stripTrailingZeros().toPlainString();
        }
    }

    private void addNumber(String number) {
        text_display.setText(text_display.getText() + number);
    }

    private void clear_display() {
        text_display.setText("");
    }
}
