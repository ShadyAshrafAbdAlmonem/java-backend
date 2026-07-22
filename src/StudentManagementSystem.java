import java.util.Scanner;

class Student {
    private int id;
    private String name;
    private double[] subjectGrades; 

    public Student(int id, String name, double grade1, double grade2) {
        this.id = id;
        this.name = name;
        this.subjectGrades = new double[]{grade1, grade2};
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getFinalGrade() {
        return (subjectGrades[0] + subjectGrades[1]) / 2.0;
    }

    public String getGradeStatus() {
        double finalGrade = getFinalGrade();
        if (finalGrade >= 90) return "Excellent";
        if (finalGrade >= 75) return "Very Good";
        if (finalGrade >= 60) return "Pass";
        return "Fail";
    }

    public void displayStudentInfo() {
        System.out.printf("ID: %-5d | Name: %-15s | Subject 1: %-6.2f | Subject 2: %-6.2f | Final: %-6.2f | Status: %s%n",
                id, name, subjectGrades[0], subjectGrades[1], getFinalGrade(), getGradeStatus());
    }
}

public class StudentManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== STUDENT MANAGEMENT SYSTEM ===");
        
        int n = readIntWithMin(scanner, "Enter the number of students (N): ", 1, "Number of students must be greater than 0.");

        Student[] students = new Student[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Entering details for Student " + (i + 1) + " ---");
            
            int id = readUniqueId(scanner, students, i);

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            double grade1 = readValidGrade(scanner, "Enter Grade for Subject 1 (0-100): ");
            double grade2 = readValidGrade(scanner, "Enter Grade for Subject 2 (0-100): ");

            students[i] = new Student(id, name, grade1, grade2);
        }

        int choice;
        do {
            displayMenu();
            choice = readMenuChoice(scanner, 0, 6);

            switch (choice) {
                case 1:
                    displayAllStudents(students);
                    break;
                case 2:
                    calculateAverageGrade(students);
                    break;
                case 3:
                    findHighestGrade(students);
                    break;
                case 4:
                    searchStudentById(students, scanner);
                    break;
                case 5:
                    countPassFail(students);
                    break;
                case 6:
                    sortStudentsByGrade(students);
                    break;
                case 0:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }


    private static void displayMenu() {
        System.out.println("\n================ MAIN MENU ================");
        System.out.println("1. Display Students");
        System.out.println("2. Calculate Average Grade");
        System.out.println("3. Find Highest Grade");
        System.out.println("4. Search Student by ID");
        System.out.println("5. Count Passed & Failed Students (Bonus)");
        System.out.println("6. Sort Students by Grade (Bonus)");
        System.out.println("0. Exit");
        System.out.println("===========================================");
    }

    private static void displayAllStudents(Student[] students) {
        System.out.println("\n--- All Students List ---");
        for (Student student : students) {
            student.displayStudentInfo();
        }
    }

    private static void calculateAverageGrade(Student[] students) {
        if (students.length == 0) return;
        double sum = 0;
        for (Student student : students) {
            sum += student.getFinalGrade();
        }
        double classAverage = sum / students.length;
        System.out.printf("%nClass Overall Average Grade: %.2f%n", classAverage);
    }

    private static void findHighestGrade(Student[] students) {
        if (students.length == 0) return;

        Student topStudent = students[0];
        for (int i = 1; i < students.length; i++) {
            if (students[i].getFinalGrade() > topStudent.getFinalGrade()) {
                topStudent = students[i];
            }
        }

        System.out.println("\n--- Top Student ---");
        topStudent.displayStudentInfo();
    }

    private static void searchStudentById(Student[] students, Scanner scanner) {
        int searchId = readIntWithMin(scanner, "\nEnter Student ID to search: ", 0, "ID must be a non-negative integer.");

        boolean found = false;
        for (Student student : students) {
            if (student.getId() == searchId) {
                System.out.println("\nStudent Found:");
                student.displayStudentInfo();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    private static void countPassFail(Student[] students) {
        int passed = 0;
        int failed = 0;

        for (Student student : students) {
            if (student.getFinalGrade() >= 60) {
                passed++;
            } else {
                failed++;
            }
        }

        System.out.println("\n--- Class Pass/Fail Summary ---");
        System.out.println("Passed Students: " + passed);
        System.out.println("Failed Students: " + failed);
    }

    private static void sortStudentsByGrade(Student[] students) {
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - i - 1; j++) {
                if (students[j].getFinalGrade() < students[j + 1].getFinalGrade()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
        System.out.println("\nStudents sorted by Final Grade (Highest to Lowest):");
        displayAllStudents(students);
    }


    private static int readUniqueId(Scanner scanner, Student[] students, int currentIndex) {
        while (true) {
            int id = readIntWithMin(scanner, "Enter ID: ", 0, "ID must be a non-negative integer.");
            
            boolean exists = false;
            for (int j = 0; j < currentIndex; j++) {
                if (students[j].getId() == id) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return id;
            }
            System.out.println("Error: Student ID " + id + " already exists. Please enter a unique ID.");
        }
    }

    private static int readMenuChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= min && choice <= max) {
                    scanner.nextLine();
                    return choice;
                }
            } else {
                scanner.next();
            }
            System.out.println("Invalid choice! Please enter a number between " + min + " and " + max + ".");
        }
    }

    private static double readValidGrade(Scanner scanner, String prompt) {
        double grade;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                grade = scanner.nextDouble();
                if (grade >= 0 && grade <= 100) {
                    scanner.nextLine(); 
                    return grade;
                }
            } else {
                scanner.next(); 
            }
            System.out.println("Invalid entry. Please enter a grade between 0 and 100.");
        }
    }

    private static int readIntWithMin(Scanner scanner, String prompt, int minValue, String errorMsg) {
        int val;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                val = scanner.nextInt();
                if (val >= minValue) {
                    scanner.nextLine();
                    return val;
                }
            } else {
                scanner.next();
            }
            System.out.println(errorMsg);
        }
    }
}