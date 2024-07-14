import kotlinx.serialization.encodeToString
import me.alllex.tbot.apigen.BotApi
import me.alllex.tbot.apigen.BotApiArgLists
import me.alllex.tbot.apigen.BotApiElementArgs
import me.alllex.tbot.apigen.jsonSerialization
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

@CacheableTask
abstract class BotApiArgListExtract : DefaultTask() {

    @get:[InputFile PathSensitive(PathSensitivity.RELATIVE)]
    abstract val apiJsonFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val apiJsonText = apiJsonFile.get().asFile.readText(Charsets.UTF_8)
        val botApi = jsonSerialization.decodeFromString<BotApi>(apiJsonText)

        val argLists = BotApiArgLists(
            types = botApi.types.map {
                BotApiElementArgs(name = it.name.value, args = it.fields?.map { it.name }.orEmpty())
            },
            methods = botApi.methods.map {
                BotApiElementArgs(name = it.name.value, args = it.parameters.map { it.name })
            }
        )

        outputFile.get().asFile.writeText(jsonSerialization.encodeToString(argLists))
    }
}
