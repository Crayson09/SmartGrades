package dev.crayson.smartgrades.config

import de.exlll.configlib.YamlConfigurationProperties
import de.exlll.configlib.YamlConfigurationStore
import dev.crayson.smartgrades.config.serializer.KotlinStringSerializer
import java.io.File
import java.lang.String

object ConfigHandler {
    private val configFile: File = File("./run", "config.yml")

    private var configStore: YamlConfigurationStore<Config> =
        YamlConfigurationStore(
            Config::class.java,
            YamlConfigurationProperties
                .newBuilder()
                .addSerializer(String::class.java, KotlinStringSerializer())
                .build(),
        )

    var config: Config = configStore.update(configFile.toPath())
}
