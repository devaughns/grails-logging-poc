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
appender("info", FileAppender){
    file = "${BASE_DIR}/info.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{ISO8601}:%p in %t \\(category %c\\) - %m%n"
    }
    filter(LevelFilter) {
        level = INFO
        onMatch = FilterReply.ACCEPT
    }
}

appender("error", FileAppender){
    file = "${BASE_DIR}/error.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{ISO8601}:%p in %t \\(category %c\\) - %m%n"
    }
    filter(LevelFilter) {
        level = ERROR
        onMismatch = FilterReply.DENY
    }
}

appender("dynamic", FileAppender){
    file = "${BASE_DIR}/dynamic.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{ISO8601}:%p in %t \\(category %c\\) - %m%n"
    }
    filter(LevelFilter) {
        level = WARN
        onMatch = FilterReply.ACCEPT
    }
}

/*
    This logger is applied to the `grails.logging` package and all of its children
    The logger logs everything with a severity of `info` and higher because of `onMatch`
    The items are logged to the console and to the specified log file in the `info` appender
 */
logger 'grails.logging', INFO, ['STDOUT','info'], false


/*
    This logger is not applied to any package as it doesn't match any name.
    It would log only the items with a severity of error and reject everything else
    Because of this, the `error.log` will be an empty file
 */
logger 'some.random.name', ERROR, ['STDOUT', 'error'], false


/*
    This logger is not applied to any package.
    Instead, the logger is invoked dynamically in `FirstLoggingService` and `src/main/groovy/grails/logging`
    It only logs items above a warn severity
 */
logger 'brand.new.logger', WARN, ['dynamic'], false
