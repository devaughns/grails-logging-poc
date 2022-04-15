import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.core.spi.FilterReply
import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.StandardCharsets

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = StandardCharsets.UTF_8

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            charset = StandardCharsets.UTF_8
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
root(ERROR, ['STDOUT'])


def daily = timestamp("yyyy-MM-dd")
def BASE_DIR = "./logs/${daily}"
appender("base", FileAppender){
    file = "${BASE_DIR}/info.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{ISO8601}:%p in %t \\(category %c\\) - %m%n"
    }
    filter(LevelFilter) {
        level = INFO
        onMismatch = FilterReply.DENY
    }
}

appender("framework", FileAppender){
    file = "${BASE_DIR}/error.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{ISO8601}:%p in %t \\(category %c\\) - %m%n"
    }
    filter(LevelFilter) {
        level = ERROR
        onMismatch = FilterReply.DENY
    }
}

logger 'root', INFO, ['STDOUT','framework', 'base'], false
