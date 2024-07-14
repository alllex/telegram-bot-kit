import kotlinx.serialization.encodeToString
import me.alllex.tbot.apigen.BotApiDefinitionParser
import me.alllex.tbot.apigen.jsonSerialization
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

@CacheableTask
abstract class BotApiExtract : DefaultTask() {

    @get:[InputFile PathSensitive(PathSensitivity.RELATIVE)]
    abstract val apiSpecFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val html = apiSpecFile.get().asFile.readText()
        val parser = BotApiDefinitionParser()
        val botApi = parser.run(html)

        val outputJson = jsonSerialization.encodeToString(botApi)
        outputFile.get().asFile.writeText(outputJson)
    }
}
