package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class MyLog {

    
    
    private static MyLog myLog = null;
    private static Logger myLogger = null;
    
    private static String PREFIXLOGFILENAME = "myLog_";
    public static Level STATE = Level.FINEST;
    public static Level FLOW = Level.FINER;
    public static Level EXPLORE = Level.FINE;

    private MyLog(){
            myLogger = Logger.getLogger("MyLog");
            myLogger.setUseParentHandlers(false);
            myLogger.addHandler(getConsoleHandler());
            myLogger.addHandler(getTEXTFileHandler());
            myLogger.addHandler(getXMLFileHandler());
            myLogger.setLevel(Level.FINEST);
    }

    public static MyLog getMyLog() {
        if (null == myLog){
            myLog = new MyLog();
        }
        return myLog;
    }

    public static Logger getMyLogger(){
        return myLogger;
    }

    private static ConsoleHandler getConsoleHandler () {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public String formatMessage(LogRecord record) {
                return String.format(format, new Date(record.getMillis()), record.getLevel().getLocalizedName(),
                        record.getMessage());
            }
        });
        return consoleHandler;
    }

    private static FileHandler getXMLFileHandler(){
        FileHandler xmlFileHandler = null;
        try {
            // Something in XML for parsing
            xmlFileHandler = new FileHandler(PREFIXLOGFILENAME+"XMLLOG.xml");
        } catch (SecurityException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return xmlFileHandler;
    }

    private static FileHandler getTEXTFileHandler(){
        FileHandler textFileHandler = null;
        try {
            // A copy of the console output
            textFileHandler = new FileHandler(PREFIXLOGFILENAME+"TEXTLOG.txt");
            textFileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public String formatMessage(LogRecord record) {
                    return String.format(format, new Date(record.getMillis()), record.getLevel().getLocalizedName(),
                            record.getMessage());
                }
            });
            myLogger.addHandler(textFileHandler);

        } catch (SecurityException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return textFileHandler;
    }


}
