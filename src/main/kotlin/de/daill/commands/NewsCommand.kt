package de.daill.commands

import de.daill.BotProps
import de.daill.CommandProps
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class NewsCommand : Command {
    val LOG = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var props: CommandProps

    override fun process() {

    }


}