package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartFrame extends JFrame {

    public ChartFrame(Course course, UserJsonDatabase userDb) {
        setTitle("Visual Analytics for: " + course.getCourseTitle());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CourseAnalytics analytics = new CourseAnalytics(course, userDb);
        List<LessonAverageData> lessonAverages = getLessonAveragesData(course, analytics);

        DefaultCategoryDataset dataset = createDataset(lessonAverages);
        JFreeChart chart = createChart(dataset, course.getCourseTitle());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 550));

        setContentPane(chartPanel);
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
                    System.out.println("Error fetching chart data for lesson " + e.getMessage());
                }
                data.add(new LessonAverageData(lesson.getLessonTitle(), average));
            }
        }
        return data;
    }


    private DefaultCategoryDataset createDataset(List<LessonAverageData> lessonAverages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (LessonAverageData x : lessonAverages) {
            dataset.addValue(x.getAverageRatio() * 100, "Average Score", x.getLessonTitle());
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset, String courseTitle) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Quiz Averages by Lesson (" + courseTitle + ")", // Chart title
                "Lesson Title",                                // X-axis label
                "Average Score (%)",                           // Y-axis label
                dataset,                                       // Data
                PlotOrientation.VERTICAL,                      // Orientation
                false,                                         // Show legend
                true,                                          // Tooltips
                false                                          // URLs
        );

        chart.getCategoryPlot().getRangeAxis().setRange(0, 100);
        return chart;
    }
}