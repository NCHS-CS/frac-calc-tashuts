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
   public static String processExpression(String input) {
      // Requirement 4 & 9: Manual parsing using spaces [cite: 172, 194]
      int startSpace = input.indexOf(" ");
      int endSpace = input.lastIndexOf(" ");

      String firstNumString = input.substring(0, startSpace);
      String operator = input.substring(startSpace + 1, endSpace);
      String secondNumString = input.substring(endSpace + 1);

      // Convert operands to improper form [cite: 80, 83]
      int num1 = getImpropNumerator(firstNumString);
      int den1 = getDenominator(firstNumString);
      int num2 = getImpropNumerator(secondNumString);
      int den2 = getDenominator(secondNumString);

      int resNum = 0;
      int resDen = 1;

      // Perform math based on the operator
      if (operator.equals("+")) {
         resNum = (num1 * den2) + (num2 * den1);
         resDen = den1 * den2;
      } else if (operator.equals("-")) {
         resNum = (num1 * den2) - (num2 * den1);
         resDen = den1 * den2;
      } else if (operator.equals("*")) {
         resNum = num1 * num2;
         resDen = den1 * den2;
      } else if (operator.equals("/")) {
         // Division is multiplying by the reciprocal
         resNum = num1 * den2;
         resDen = den1 * num2;
      }

      return format(resNum, resDen);
   }

   public static int getImpropNumerator(String numString) {
      int whole = 0;
      int numer = 0;
      int denom = 1;

      if (numString.contains("_")) {
         whole = Integer.parseInt(numString.substring(0, numString.indexOf("_")));
         String fract = numString.substring(numString.indexOf("_") + 1);
         numer = Integer.parseInt(fract.substring(0, fract.indexOf("/")));
         denom = Integer.parseInt(fract.substring(fract.indexOf("/") + 1));
      } else if (numString.contains("/")) {
         numer = Integer.parseInt(numString.substring(0, numString.indexOf("/")));
         denom = Integer.parseInt(numString.substring(numString.indexOf("/") + 1));
      } else {
         whole = Integer.parseInt(numString);
      }

      // Proper negative handling: if whole is negative, subtract the numerator [cite:
      // 183]
      if (whole < 0) {
         return (whole * denom) - numer;
      }
      return (whole * denom) + numer;
   }

   public static int getDenominator(String numString) {
      if (numString.contains("/")) {
         return Integer.parseInt(numString.substring(numString.indexOf("/") + 1));
      }
      return 1;
   }

   public static String format(int n, int d) {
      if (n == 0)
         return "0"; // Rule 4 & 5 [cite: 97-99]

      // Rule 3: Denominators will never be negative [cite: 96]
      if (d < 0) {
         n *= -1;
         d *= -1;
      }

      // Rule 1: Fractions must be reduced using GCD [cite: 82, 94]
      int commonD = Math.abs(getGCD(n, d));
      n /= commonD;
      d /= commonD;

      int mixWhole = n / d;
      int mixNum = Math.abs(n % d);

      if (mixNum == 0)
         return "" + mixWhole; // Rule 4 [cite: 97]

      if (mixWhole == 0) {
         return (n < 0 ? "-" : "") + mixNum + "/" + d; // Rule 5 [cite: 99]
      }

      // Rule 2: Output with a space, not an underscore [cite: 95, 198]
      return mixWhole + " " + mixNum + "/" + d;
   }

   public static int getGCD(int a, int b) {
      return b == 0 ? a : getGCD(b, a % b);
   }

   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {
      // TODO: Update this help text!

      String help = "Fraction Calculator Instructions:\n";
      help += "~ Format: Number - space - Operator - space - Number\n";
      help += "~ Mixed numbers use underscores: 8_3/5\n";
      help += "~ Possible operators: +, -, *, /\n";
      help += "~ Answers are returned fully reduced and normalized.\n";
      help += "~ To exit type 'quit'.";
      return help;
   }
}
