package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartFrame extends JFrame {

    private static final int PASSING_SCORE = 50;

    public ChartFrame(Course course, UserJsonDatabase userDb) {
        setTitle("Visual Analytics for: " + course.getCourseTitle());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CourseAnalytics analytics = new CourseAnalytics(course, userDb);
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));

        List<LessonAverageData> lessonAverages = getLessonAveragesData(course, analytics);
        DefaultCategoryDataset barDataset = createBarDataset(lessonAverages);
        JFreeChart barChart = createBarChart(barDataset, course.getCourseTitle());
        chartsPanel.add(new ChartPanel(barChart));

        DefaultPieDataset pieDataset = createAllQuizzesPassFailDataset(course, analytics);
        String pieTitle = "Overall Quiz Pass/Fail Distribution";
        JFreeChart pieChart = createPieChart(pieDataset, pieTitle);
        chartsPanel.add(new ChartPanel(pieChart));

        setContentPane(chartsPanel);
        setVisible(true);
    }

    private List<LessonAverageData> getLessonAveragesData(Course course, CourseAnalytics analytics) {
        List<LessonAverageData> data = new ArrayList<>();

        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                double average = 0.0;
                try {
                    average = analytics.calculateLessonAverage(lesson.getLessonID());
                } catch (Exception e) {
                    System.err.println("Error fetching chart data for lesson " + lesson.getLessonTitle() + ": " + e.getMessage());
                }
                data.add(new LessonAverageData(lesson.getLessonTitle(), average));
            }
        }
        return data;
    }

    private DefaultCategoryDataset createBarDataset(List<LessonAverageData> lessonAverages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (LessonAverageData x : lessonAverages) {
            dataset.addValue(x.getAverageRatio(), "Average Score", x.getLessonTitle());
        }
        return dataset;
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String courseTitle) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Quiz Averages by Lesson (" + courseTitle + ")",
                "Lesson Title",
                "Average Score (%)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        chart.getCategoryPlot().getRangeAxis().setRange(0, 100);
        return chart;
    }

    private DefaultPieDataset createAllQuizzesPassFailDataset(Course course, CourseAnalytics analytics) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        int totalPasses = 0;
        int totalFailsOrNotTaken = 0;

        List<Student> students = analytics.getCourseStudents();

        List<String> quizIds = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                quizIds.add(lesson.getQuiz().getQuizId());
            }
        }

        if (quizIds.isEmpty()) {
            dataset.setValue("No Quizzes Available", 1);
            return dataset;
        }

        for (Student student : students) {
            for (String quizId : quizIds) {
                if (student.getQuizResults().containsKey(quizId)) {
                    QuizAttemptData data = student.getQuizResults().get(quizId);
                    int score = (data != null) ? data.getBestScore() : 0;

                    if (score >= PASSING_SCORE) {
                        totalPasses++;
                    } else {
                        totalFailsOrNotTaken++;
                    }
                } else {
                    totalFailsOrNotTaken++;
                }
            }
        }

        if (totalPasses + totalFailsOrNotTaken > 0) {
            dataset.setValue("Total Times Passed (>= 50%)", totalPasses);
            dataset.setValue("Total Times Failed/Not Taken (< 50%)", totalFailsOrNotTaken);
        } else {
            dataset.setValue("No Quiz Attempts Recorded", 1);
        }

        return dataset;
    }

    private JFreeChart createPieChart(DefaultPieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );
        return chart;
    }
}