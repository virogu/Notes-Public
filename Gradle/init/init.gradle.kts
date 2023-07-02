fun RepositoryHandler.enableMirror() {
    all {
        if (this is MavenArtifactRepository) {
            val originalUrl = this.url.toString().removeSuffix("/")
            repoMap?.get(originalUrl)?.also {
                logger.lifecycle("Repository[$url] is mirrored to [$it]")
                this.setUrl(it)
            } ?: run {
                logger.lifecycle("Repository[$url] retained")
            }
        }
    }
}

private val repoMaps = mapOf(
    "ALI" to mapOf(
        "https://repo.maven.apache.org/maven2" to "https://maven.aliyun.com/repository/public/",
        //"https://repo.maven.apache.org/maven2" to "https://maven.aliyun.com/repository/central/",
        //"https://repo1.maven.org/maven2" to "https://maven.aliyun.com/repository/central/",
        "https://dl.google.com/dl/android/maven2" to "https://maven.aliyun.com/repository/google/",
        "https://plugins.gradle.org/m2" to "https://maven.aliyun.com/repository/gradle-plugin/"
    ),
    "TENCENT" to mapOf(
        "https://repo.maven.apache.org/maven2" to "https://mirrors.tencent.com/nexus/repository/maven-public/",
        "https://dl.google.com/dl/android/maven2" to "https://mirrors.tencent.com/nexus/repository/maven-public/",
        "https://plugins.gradle.org/m2" to "https://mirrors.tencent.com/nexus/repository/gradle-plugins/"
    ),
)

val repoMap = repoMaps["TENCENT"]

gradle.allprojects {
    buildscript {
        repositories.enableMirror()
    }
    repositories.enableMirror()
    gradle.beforeSettings {
        pluginManagement.repositories.enableMirror()
        @Suppress("UnstableApiUsage")
        dependencyResolutionManagement.repositories.enableMirror()
    }
}