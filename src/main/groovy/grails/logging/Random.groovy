package grails.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Random {

    Logger logger = LoggerFactory.getLogger("brand.new.logger")

    void logIt(String item) {
        logger.warn("logged outside of grails context")
        logger.info(item)
    }


}
