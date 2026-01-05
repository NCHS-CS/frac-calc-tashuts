// Tanisha Shankpal
// Period 6
// Fraction Calculator Project

import java.util.*;

// This is a progam that accepts an input and checks whether it is and calculation or a string.
// If it is a string it prints the string and if it is a calculation it prints the answer.
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);

   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   // DO NOT CHANGE THIS METHOD!!
   public static void main(String[] args) {

      // initialize to false so that we start our loop
      boolean done = false;

      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();

         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
            // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);

            // print the result of processing the command
            System.out.println(result);
         }
      }

      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      System.out.println("Enter: ");
      String input = console.nextLine();
      return input;

   }

   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   // DO NOT CHANGE THIS METHOD!!!
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }

      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }

   // Lots work for this project is handled in here.
   // Of course, this method will call LOTS of helper methods
   // so that this method can be shorter.
   // This will calculate the expression and RETURN the answer.
   // This will NOT print anything!
   // Input: an expression to be evaluated
   // Examples:
   // 1/2 + 1/2
   // 2_1/4 - 0_1/8
   // 1_1/8 * 2
   // Return: the fully reduced mathematical result of the expression
   // Value is returned as a String. Results using above examples:
   // 1
   // 2 1/8
   // 2 1/4
   
   //Splits the string into 3 parts and calculates the answer
   public static String processExpression(String input) {
      //Finds tokens based on spaces
      int startSpace = input.indexOf(" ");
      int endSpace = input.lastIndexOf(" ");
      // Isolate the first number, the operator, and the second number
      String firstNumString = input.substring(0, startSpace);
      String operator = input.substring(startSpace + 1, endSpace);
      String secondNumString = input.substring(endSpace + 1);
      // Convert the string parts into only numerators and denominators.
      int num1 = getImpropNumerator(firstNumString);
      int den1 = getDenominator(firstNumString);
      int num2 = getImpropNumerator(secondNumString);
      int den2 = getDenominator(secondNumString);
      int resNum = 0;
      int resDen = 1;

      // Perform math based on the operator
      // Addition: Cross-multiply numerators and add
      if (operator.equals("+"))
      {
         resNum = (num1 * den2) + (num2 * den1);
         resDen = den1 * den2;
      } 
      // Subtraction: Cross-multiply numerators and subtract
      else if (operator.equals("-")) 
      {
         resNum = (num1 * den2) - (num2 * den1);
         resDen = den1 * den2;
      } 
      // Multiplication: Top times top, bottom times bottom
      else if (operator.equals("*")) 
      {
         resNum = num1 * num2;
         resDen = den1 * den2;
      } 
      // Division: Multiply by the reciprocal (flip the second fraction)
      else if (operator.equals("/")) 
      {
         resNum = num1 * den2;
         resDen = den1 * num2;
      }
      // Return the final answer after it has been simplified
      return format(resNum, resDen);
   }

   // Parses mixed numbers, fractions, and whole numbers
   public static int getImpropNumerator(String numString) {
      int whole = 0;
      int numer = 0;
      int denom = 1;

      // Check if the number is a mixed number (has an underscore)
      if (numString.contains("_")) {
         whole = Integer.parseInt(numString.substring(0, numString.indexOf("_")));
         String fract = numString.substring(numString.indexOf("_") + 1);
         numer = Integer.parseInt(fract.substring(0, fract.indexOf("/")));
         denom = Integer.parseInt(fract.substring(fract.indexOf("/") + 1));
      } 
      // Check if it is a simple fraction (has a slash but no underscore)
      else if (numString.contains("/")) {
         numer = Integer.parseInt(numString.substring(0, numString.indexOf("/")));
         denom = Integer.parseInt(numString.substring(numString.indexOf("/") + 1));
      } 
      // Otherwise, it is just a whole number
      else {
         whole = Integer.parseInt(numString);
      }
      // To create an improper fraction, we do (whole * den) + numerator.
      // If the whole number is negative, we must subtract the numerator to keep it negative.
      if (whole < 0) {
         return (whole * denom) - numer;
      }
      return (whole * denom) + numer;
   }
   // Helper to find the denominator (default is 1 if no fraction exists)
   public static int getDenominator(String numString) {
      if (numString.contains("/")) {
         return Integer.parseInt(numString.substring(numString.indexOf("/") + 1));
      }
      return 1;
   }
   // Reduces and formats the expression.
   public static String format(int n, int d) {
      // If the numerator is 0, the whole answer is just "0"
      if (n == 0){
         return "0";
      }
      // Denominators will never be negative.
      if (d < 0) {
         n *= -1;
         d *= -1;
      }

      // Fractions must be reduced using GCD.
      int commonD = Math.abs(getGCD(n, d));
      n /= commonD;
      d /= commonD;
      // Separate the improper fraction back into a whole number and a remainder
      int mixWhole = n / d;
      int mixNum = Math.abs(n % d);
      // If there is no remainder, it's a whole number
      if (mixNum == 0){
         return "" + mixWhole;
      }
      // If whole is 0, only output the fraction
      if (mixWhole == 0) {
         return (n < 0 ? "-" : "") + mixNum + "/" + d;
      }

      // Final output uses a space between whole and fraction
      return mixWhole + " " + mixNum + "/" + d;
   }
   // Standard Euclidean algorithm to find the GCD for fraction reduction
   public static int getGCD(int a, int b) {
      return b == 0 ? a : getGCD(b, a % b);
   }
   // Returns a string that is helpful to the user about how to use the program. 
   // These are instructions to the user.
   public static String provideHelp() {
      String help = "Fraction Calculator Instructions:\n";
      help += "~ Format: Number - space - Operator - space - Number\n";
      help += "~ Mixed numbers use underscores: 8_3/5\n";
      help += "~ Possible operators: +, -, *, /\n";
      help += "~ Answers are returned fully reduced and normalized.\n";
      help += "~ To exit type 'quit'.";
      return help;
   }
}
