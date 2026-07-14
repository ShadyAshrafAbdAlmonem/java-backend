import java.util.Scanner;

public class GradeManagementSystem {
    private static final int NUM_STUDENTS = 5;
    private static final int NUM_SUBJECTS = 3;
    private static final String[] SUBJECTS = {"Math", "Science", "English"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] studentNames = new String[NUM_STUDENTS];
        double[][] grades = new double[NUM_STUDENTS][NUM_SUBJECTS];

        System.out.println("=== Welcome to the Student Grade Management System ===");
        System.out.println("Please enter the details for " + NUM_STUDENTS + " students.\n");

        for (int i = 0; i < NUM_STUDENTS; i++) {
            System.out.print("Enter the name of Student " + (i + 1) + ": ");
            studentNames[i] = scanner.nextLine();

            for (int j = 0; j < NUM_SUBJECTS; j++) {
                double grade = -1;
                boolean isValid = false;

                while (!isValid) {
                    System.out.print("  Enter grade for " + SUBJECTS[j] + " (0-100): ");
                    if (scanner.hasNextDouble()) {
                        grade = scanner.nextDouble();
                        if (grade >= 0 && grade <= 100) {
                            isValid = true;
                        } else {
                            System.out.println("  Invalid grade. Please enter a value between 0 and 100.");
                        }
                    } else {
                        System.out.println("  Invalid input. Please enter a numeric grade.");
                        scanner.next(); 
                    }
                }
                grades[i][j] = grade;
            }
            scanner.nextLine(); 
            System.out.println();
        }

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n================ Main Menu ================");
            System.out.println("1. Show All Students names.");
            System.out.println("2. Show all Students grades in each subject (with Bonus stats).");
            System.out.println("3. Search Student by name.");
            System.out.println("4. Count Passed Students.");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                System.out.println();

                switch (choice) {
                    case 1:
                        showAllStudentNames(studentNames);
                        break;
                    case 2:
                        showAllGradesAndStats(studentNames, grades);
                        break;
                    case 3:
                        searchStudentByName(studentNames, grades, scanner);
                        break;
                    case 4:
                        countPassedStudents(studentNames, grades);
                        break;
                    case 0:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a number from the menu.");
                }
            } else {
                System.out.println("\nInvalid input. Please enter a valid menu integer.");
                scanner.next();
            }
        }
        scanner.close();
    }

    private static void showAllStudentNames(String[] names) {
        System.out.println("--- Registered Students ---");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + names[i]);
        }
    }

    private static void showAllGradesAndStats(String[] names, double[][] grades) {
        System.out.println("--- Student Grades Report ---");
        
        for (int i = 0; i < names.length; i++) {
            System.out.print(names[i] + " -> ");
            double studentSum = 0;
            for (int j = 0; j < NUM_SUBJECTS; j++) {
                double g = grades[i][j];
                studentSum += g;
                System.out.print(SUBJECTS[j] + ": " + g + " (" + getLetterGrade(g) + ") | ");
            }
            double avg = studentSum / NUM_SUBJECTS;
            System.out.printf("Average: %.2f (%s)%n", avg, getLetterGrade(avg));
        }
        System.out.println();

        System.out.println("--- Subject Performance Metrics ---");
        for (int j = 0; j < NUM_SUBJECTS; j++) {
            double sum = 0;
            double maxGrade = grades[0][j];
            for (int i = 0; i < NUM_STUDENTS; i++) {
                sum += grades[i][j];
                if (grades[i][j] > maxGrade) {
                    maxGrade = grades[i][j];
                }
            }
            double subjectAverage = sum / NUM_STUDENTS;
            System.out.printf("%-8s -> Average: %.2f | Highest Grade: %.2f (%s)%n", 
                SUBJECTS[j], subjectAverage, maxGrade, getLetterGrade(maxGrade));
        }
    }

    private static void searchStudentByName(String[] names, double[][] grades, Scanner scanner) {
        System.out.print("Enter student name to search: ");
        String searchName = scanner.nextLine().trim();
        boolean found = false;

        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(searchName)) {
                found = true;
                System.out.println("\nMatch Found!");
                System.out.println("Student: " + names[i]);
                double sum = 0;
                for (int j = 0; j < NUM_SUBJECTS; j++) {
                    double g = grades[i][j];
                    sum += g;
                    System.out.println("  " + SUBJECTS[j] + ": " + g + " [" + getLetterGrade(g) + "]");
                }
                double avg = sum / NUM_SUBJECTS;
                System.out.printf("  Overall Average: %.2f [%s]%n", avg, getLetterGrade(avg));
                break;
            }
        }

        if (!found) {
            System.out.println("Student \"" + searchName + "\" not found in the system.");
        }
    }

    private static void countPassedStudents(String[] names, double[][] grades) {
        int passCount = 0;
        System.out.println("--- Passing Students Status (Average >= 50) ---");
        for (int i = 0; i < names.length; i++) {
            double sum = 0;
            for (int j = 0; j < NUM_SUBJECTS; j++) {
                sum += grades[i][j];
            }
            double average = sum / NUM_SUBJECTS;
            if (average >= 50) {
                passCount++;
                System.out.printf("  %-15s : PASSED (Avg: %.2f)%n", names[i], average);
            } else {
                System.out.printf("  %-15s : FAILED (Avg: %.2f)%n", names[i], average);
            }
        }
        System.out.println("\nTotal Passed Students: " + passCount + " out of " + NUM_STUDENTS);
    }

    private static String getLetterGrade(double grade) {
        if (grade >= 85) return "A";
        if (grade >= 75) return "B";
        if (grade >= 65) return "C";
        if (grade >= 50) return "D";
        return "F";
    }
}