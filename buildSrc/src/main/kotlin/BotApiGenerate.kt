import me.alllex.tbot.apigen.BotApiGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

@CacheableTask
abstract class BotApiGenerate : DefaultTask() {

    @get:[InputFile PathSensitive(PathSensitivity.RELATIVE)]
    abstract val apiJsonFile: RegularFileProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val telegramClientPackage: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        BotApiGenerator().run(
            apiJsonFile.get().asFile,
            outputDirectory.get().asFile,
            packageName.get(),
            telegramClientPackage.get(),
        )
    }

}
