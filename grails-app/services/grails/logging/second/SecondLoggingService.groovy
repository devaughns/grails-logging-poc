package grails.logging.second

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

import java.time.LocalDateTime

@Slf4j
@CompileStatic
class SecondLoggingService {

    static lazyInit = false

    @Scheduled(fixedRate=5000L)
    void info() {
        log.info "info from second: ${LocalDateTime.now()}"
    }

    @Scheduled(fixedRate=15000L)
    void error() {
        log.error "error from second: ${LocalDateTime.now()}"
    }

    @Scheduled(fixedRate=3000L)
    void warn() {
        log.warn "warn from second: ${LocalDateTime.now()}"
    }

}
