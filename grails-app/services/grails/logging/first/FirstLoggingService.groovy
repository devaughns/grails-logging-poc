package grails.logging.first

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

import java.time.LocalDateTime

@Slf4j
@CompileStatic
class FirstLoggingService {

    static lazyInit = false

    @Scheduled(fixedRate=5000L)
    void info() {
        log.info "info from first: {}",  LocalDateTime.now()
    }

    @Scheduled(fixedRate=15000L)
    void error() {
        log.error "error from first:  ${LocalDateTime.now()}"
    }

}
