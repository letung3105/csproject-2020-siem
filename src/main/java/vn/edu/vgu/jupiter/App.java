package vn.edu.vgu.jupiter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import vn.edu.vgu.jupiter.dashboard.TextAreaAppender;

public class App extends Application {
    public static void main(String[] args) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        PatternLayout.Builder patternBuilder = PatternLayout.newBuilder();
        patternBuilder.withPattern("%d{HH:mm:ss.SSS} [%level]: %msg%n");
        Filter filter = ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
        Appender textAreaAppender = TextAreaAppender.createAppender("TextAreaAppender", patternBuilder.build(), filter);
        ctx.getRootLogger().addAppender(textAreaAppender);
        ctx.updateLoggers();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //set up scene and show
        var root = (Parent) FXMLLoader.load(getClass().getResource("dashboard/StartupConfig.fxml"));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("SIEM Dashboard");
        stage.setScene(scene);
        stage.show();
    }

}
