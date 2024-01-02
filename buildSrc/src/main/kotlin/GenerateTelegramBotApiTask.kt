import me.alllex.tbot.apigen.BotApiGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class GenerateTelegramBotApiTask : DefaultTask() {

    @get:[InputFile PathSensitive(PathSensitivity.RELATIVE)]
    abstract val apiSpecFile: RegularFileProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val telegramClientPackage: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        BotApiGenerator.generateFromSpec(
            apiSpecFile.get().asFile.readText(),
            outputDirectory.get().asFile,
            packageName.get(),
            telegramClientPackage.get()
        )
    }

}
