package grails.logging

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

import java.time.LocalDateTime

@Slf4j
@CompileStatic
class ScheduledLoggingService {

    static lazyInit = false

    @Scheduled(fixedRate=5000L)
    void info() {
        log.info "The local time is: {}",  LocalDateTime.now()
    }

    @Scheduled(fixedRate=15000L)
    void error() {
        log.error "This shows an error at ${LocalDateTime.now()}"
    }

}
