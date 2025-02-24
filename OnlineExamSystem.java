
import java.awt.*;
import java.util.*;
import javax.swing.*;

class Student {
    String username, password;
    Map<String, Integer> scores = new HashMap<>();

    Student(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class OnlineExamSystem extends JFrame {
    private static final Map<String, Student> students = new HashMap<>();
    private static final Map<String, Map<String, String>> courses = new HashMap<>();
    private static Student currentStudent;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> courseDropdown;
    private JPanel mainPanel;
    
    static {
        // Sample Questions
        Map<String, String> javaQuestions = new LinkedHashMap<>();
        javaQuestions.put("What is the size of int in Java?", "b");
        javaQuestions.put("Which keyword is used to define a class in Java?", "b");
        courses.put("Java", javaQuestions);

        Map<String, String> dsaQuestions = new LinkedHashMap<>();
        dsaQuestions.put("What is the worst-case time complexity of QuickSort?", "b");
        dsaQuestions.put("Which data structure uses LIFO?", "b");
        courses.put("DSA", dsaQuestions);
    }

    public OnlineExamSystem() {
        setTitle("Online Examination System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));
        
        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        registerBtn.addActionListener(e -> showRegisterForm());
        loginBtn.addActionListener(e -> showLoginForm());
        exitBtn.addActionListener(e -> System.exit(0));

        mainPanel.add(new JLabel("Welcome to Online Exam System", SwingConstants.CENTER));
        mainPanel.add(registerBtn);
        mainPanel.add(loginBtn);
        mainPanel.add(exitBtn);

        add(mainPanel);
        setVisible(true);
    }

    private void showRegisterForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(300, 200);
        registerFrame.setLayout(new GridLayout(4, 1));
        registerFrame.setLocationRelativeTo(this);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton registerBtn = new JButton("Register");

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (students.containsKey(username)) {
                JOptionPane.showMessageDialog(registerFrame, "Username already exists!");
            } else {
                students.put(username, new Student(username, password));
                JOptionPane.showMessageDialog(registerFrame, "Registration successful!");
                registerFrame.dispose();
            }
        });

        registerFrame.add(new JLabel("Username:"));
        registerFrame.add(usernameField);
        registerFrame.add(new JLabel("Password:"));
        registerFrame.add(passwordField);
        registerFrame.add(registerBtn);

        registerFrame.setVisible(true);
    }

    private void showLoginForm() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 1));
        loginFrame.setLocationRelativeTo(this);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (students.containsKey(username) && students.get(username).password.equals(password)) {
                currentStudent = students.get(username);
                JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                loginFrame.dispose();
                showStudentMenu();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!");
            }
        });

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(loginBtn);

        loginFrame.setVisible(true);
    }

    private void showStudentMenu() {
        JFrame studentFrame = new JFrame("Student Dashboard");
        studentFrame.setSize(300, 200);
        studentFrame.setLayout(new GridLayout(3, 1));
        studentFrame.setLocationRelativeTo(this);

        JButton takeTestBtn = new JButton("Take Test");
        JButton viewResultsBtn = new JButton("View Results");
        JButton logoutBtn = new JButton("Logout");

        takeTestBtn.addActionListener(e -> showTestScreen());
        viewResultsBtn.addActionListener(e -> showResults());
        logoutBtn.addActionListener(e -> {
            currentStudent = null;
            studentFrame.dispose();
        });

        studentFrame.add(takeTestBtn);
        studentFrame.add(viewResultsBtn);
        studentFrame.add(logoutBtn);

        studentFrame.setVisible(true);
    }

    private void showTestScreen() {
        JFrame testFrame = new JFrame("Take Test");
        testFrame.setSize(400, 300);
        testFrame.setLayout(new GridLayout(5, 1));
        testFrame.setLocationRelativeTo(this);

        courseDropdown = new JComboBox<>(courses.keySet().toArray(new String[0]));
        JButton startTestBtn = new JButton("Start Test");

        startTestBtn.addActionListener(e -> startTest(testFrame));

        testFrame.add(new JLabel("Select Course:"));
        testFrame.add(courseDropdown);
        testFrame.add(startTestBtn);

        testFrame.setVisible(true);
    }

    private void startTest(JFrame testFrame) {
        String course = (String) courseDropdown.getSelectedItem();
        Map<String, String> questions = courses.get(course);
        int score = 0;

        for (Map.Entry<String, String> entry : questions.entrySet()) {
            String question = entry.getKey();
            String correctAnswer = entry.getValue();

            String answer = JOptionPane.showInputDialog(testFrame, question + "\n(a) 2 bytes (b) 4 bytes (c) 8 bytes (d) 1 byte");

            if (answer != null && answer.equalsIgnoreCase(correctAnswer)) {
                score++;
            }
        }

        currentStudent.scores.put(course, score);
        JOptionPane.showMessageDialog(testFrame, "Test completed! Your score: " + score + "/" + questions.size());
        testFrame.dispose();
    }

    private void showResults() {
        StringBuilder results = new StringBuilder("Your Scores:\n");

        if (currentStudent.scores.isEmpty()) {
            results.append("No test records found!");
        } else {
            for (Map.Entry<String, Integer> entry : currentStudent.scores.entrySet()) {
                results.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, results.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new OnlineExamSystem();
    }
}
