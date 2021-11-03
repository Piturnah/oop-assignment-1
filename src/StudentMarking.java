import java.util.Scanner;
// DO NOT import anything else

/**
 * This class forms Java Assignment 1, 2021
 */
public class StudentMarking {

    /**
     * The message that the main menu must display --- DO NOT CHANGE THIS
     */
    public final String MENU_TEMPLATE =
            "%nWelcome to the Student System. Please enter an option 0 to 3%n"
                    + "0. Exit%n"
                    + "1. Generate a student ID%n"
                    + "2. Capture marks for students%n"
                    + "3. List student IDs and average mark%n";
    /**
     * DO NOT CHANGE THIS
     */
    public final String NOT_FOUND_TEMPLATE =
            "No student id of %s exists";


   /* Do NOT change the two templates ABOVE this comment.
      DO CHANGE the templates BELOW.
   */

    public final String ENTER_MARK_TEMPLATE = "Please enter mark %d for student %s%n"; // mark no., student ID
    public final String STUDENT_ID_TEMPLATE = "Your studID is %s"; // student ID
    public final String NAME_RESPONSE_TEMPLATE = "You entered a given name of %s and a surname of %s%n"; // first name, family name
    public final String LOW_HIGH_TEMPLATE = "Student %s has a lowest mark of %d%nA highest mark of %d%n"; // student ID, lowest mark, highest mark
    public final String AVG_MARKS_TEMPLATE = "Student ***%s*** has an average of %5.2f%n"; // avg mark
    public final String COLUMN_1_TEMPLATE = "%4s"; // *
    public final String COLUMN_2_TEMPLATE = "%12s"; // *
    public final String CHART_KEY_TEMPLATE = "%4d%12d%n"; // high mark, low mark
    public final String REPORT_PER_STUD_TEMPLATE = "| Student ID %3d is %s | Average is %5.2f |%n"; // index, student ID, avg mark

    /**
     * Creates a student ID based on user input
     *
     * @param sc Scanner reading {@link System#in} re-used from {@link StudentMarking#main(String[])}
     * @return a studentID according to the pattern specified in {@link StudentMarking#STUDENT_ID_TEMPLATE}
     */
    public String generateStudId(Scanner sc) {
        System.out.printf(
                "Please enter your given name and surname (Enter 0 to return to main menu)%n");
        String studentName = sc.nextLine();

        if (studentName.equals("0")) return null;

        String firstName = studentName.split(" ")[0];
        String secondName = studentName.split(" ")[1];
        System.out.printf(NAME_RESPONSE_TEMPLATE, firstName, secondName);

        String studId = String.format("%s%s%02d%s%s", Character.toString(firstName.charAt(0) & 0x5f),     // binary & to get uppercase version ASCII
                Character.toString(secondName.charAt(0) & 0x5f),    // binary & to get uppercase version ASCII
                secondName.length(),
                firstName.charAt(firstName.length() / 2),
                secondName.charAt(secondName.length() / 2));

        System.out.printf(STUDENT_ID_TEMPLATE, studId);

        return studId;
    }

    /**
     * Reads three marks (restricted to a floor and ceiling) for a student and returns their mean
     *
     * @param sc     Scanner reading {@link System#in} re-used from {@link StudentMarking#main(String[])}
     * @param studId a well-formed ID created by {@link StudentMarking#generateStudId(Scanner)}
     * @return the mean of the three marks entered for the student
     */
    public double captureMarks(Scanner sc, String studId) {
        // DO NOT change MAX_MARK and MIN_MARK
        final int MAX_MARK = 100;
        final int MIN_MARK = 0;

        // Initial highest and lowest
        int lowestMark = Integer.MAX_VALUE;
        int highestMark = Integer.MIN_VALUE;

        double avg = 0;
        for (int i = 0; i < 3; i++) {
            System.out.printf(ENTER_MARK_TEMPLATE, i + 1, studId);
            int newMark = Integer.parseInt(sc.nextLine());
            if (newMark > MAX_MARK || newMark < MIN_MARK) {
                i--;
            } else {
                if (newMark < lowestMark) lowestMark = newMark;
                if (newMark > highestMark) highestMark = newMark;

                avg += newMark / 3f;
            }
        }

        System.out.printf(LOW_HIGH_TEMPLATE, studId, lowestMark, highestMark);
        System.out.printf(AVG_MARKS_TEMPLATE, studId, avg);

        System.out.printf("Would you like to print a bar chart? [y/n]%n");
        if (sc.nextLine().toUpperCase().equals("Y")) printBarChart(studId, highestMark, lowestMark);

        return avg;
    }

    /**
     * outputs a simple character-based vertical bar chart with 2 columns
     *
     * @param studId a well-formed ID created by {@link StudentMarking#generateStudId(Scanner)}
     * @param high   a student's highest mark
     * @param low    a student's lowest mark
     */
    public void printBarChart(String studId, int high, int low) {
        System.out.printf("Student id statistics: %s%n", studId);

        for (int i = 0; i < high; i++) {
            System.out.printf(COLUMN_1_TEMPLATE + ((high - i <= low) ? COLUMN_2_TEMPLATE : "") + "%n", "*", "*");
        }

        System.out.printf("Highest     Lowest%n");
        System.out.printf(CHART_KEY_TEMPLATE, high, low);
    }

    /**
     * Prints a specially formatted report, one line per student
     *
     * @param studList student IDs originally generated by {@link StudentMarking#generateStudId(Scanner)}
     * @param count    the total number of students in the system
     * @param avgArray mean (average) marks
     */
    public void reportPerStud(String[] studList,
                              int count,
                              double[] avgArray) {
        for (int i = 0; i < count; i++) {
            // | Student ID   1 is TW04tl | Average is  3.33 |
            System.out.printf(REPORT_PER_STUD_TEMPLATE, i + 1, studList[i], avgArray[i]);
        }
    }

    /**
     * The main menu
     */
    public void displayMenu() {
        System.out.printf(MENU_TEMPLATE);
    }

    /**
     * The controlling logic of the program. Creates and re-uses a {@link Scanner} that reads from {@link System#in}.
     *
     * @param args Command-line parameters (ignored)
     */
    public static void main(String[] args) {
        // DO NOT change sc, sm, EXIT_CODE, and MAX_STUDENTS
        Scanner sc = new Scanner(System.in);
        StudentMarking sm = new StudentMarking();
        final int EXIT_CODE = 0;
        final int MAX_STUDENTS = 5;

        String[] keepStudId = new String[MAX_STUDENTS];
        double[] avgArray = new double[MAX_STUDENTS];
        int studCount = 0;

        while (true) {
            sm.displayMenu();
            int menuChoice = Integer.parseInt(sc.nextLine());

            switch (menuChoice) {
                case 0:
                    System.out.printf("Goodbye%n");
                    System.exit(EXIT_CODE);
                    break;
                case 1:
                    // Case where array of students is already full
                    if (studCount >= MAX_STUDENTS) {
                        System.out.printf("You cannot add any more students to the array");
                        break;
                    }

                    String generateStudIdRes = sm.generateStudId(sc);
                    if (generateStudIdRes != null)
                        keepStudId[studCount++] = generateStudIdRes; // Add new student ID to array and increment counter
                    break;
                case 2:
                    System.out.printf(
                            "Please enter the studId to capture their marks (Enter 0 to return to main menu)%n");
                    String studId = sc.nextLine();

                    // Break if input is 0
                    if (studId.equals("0")) break;

                    // Attempt to find in list with linear search
                    int studIdIndex = -1; // invalid array index to denote no value found
                    for (int i = 0; i < studCount; i++) {
                        if (keepStudId[i].equals(studId)) {
                            studIdIndex = i;
                            break;
                        }
                    }

                    if (studIdIndex == -1) {
                        System.out.printf(sm.NOT_FOUND_TEMPLATE, studId);
                        break; // go back to menu
                    }

                    avgArray[studIdIndex] = sm.captureMarks(sc, studId);
                    break;
                case 3:
                    sm.reportPerStud(keepStudId, studCount, avgArray);
                    break;
                default:
                    System.out.printf(
                            "You have entered an invalid option. Enter 0, 1, 2 or 3");// Skeleton: keep, unchanged
            }
        }
    }
}

/*
    TODO Before you submit:
         1. ensure your code compiles
         2. ensure your code does not print anything it is not supposed to
         3. ensure your code has not changed any of the class or method signatures from the skeleton code
         4. check the Problems tab for the specific types of problems listed in the assignment document
         5. reformat your code: Code > Reformat Code
         6. ensure your code still compiles (yes, again)
 */