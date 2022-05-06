package grails.logging.first

import grails.logging.Random
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled

import java.time.LocalDateTime

@Slf4j
@CompileStatic
class FirstLoggingService {

    static lazyInit = false
    Logger logger = LoggerFactory.getLogger("brand.new.logger")

    @Scheduled(fixedRate=5000L)
    void info() {
        log.info "info from first: {}",  LocalDateTime.now()
        Random random = new Random()
        random.logIt("sent from FirstLoggingService")
    }

    @Scheduled(fixedRate=15000L)
    void error() {
        log.error "error from first:  ${LocalDateTime.now()}"
        logger.error ("testing 1234")
    }

}
