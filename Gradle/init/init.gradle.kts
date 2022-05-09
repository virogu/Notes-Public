allprojects {
    repositories {
        mavenLocal()
        //阿里公共代理仓库 https://developer.aliyun.com/mvn/guide
        //maven {
        //    name "central"
        //    url "https://maven.aliyun.com/repository/central"
        //}
        //maven {
        //    name "jcenter"
        //    url "https://maven.aliyun.com/repository/public"
        //}
        // central和jcenter聚合仓
        maven {
            name = "public"
            setUrl("https://maven.aliyun.com/repository/public")
        }
        maven {
            name = "google"
            setUrl("https://maven.aliyun.com/repository/google")
        }
        maven {
            name = "gradle-plugin"
            setUrl("https://maven.aliyun.com/repository/gradle-plugin")
        }
    }
}

// apply<AliyunMavenRepositoryPlugin>()

// class AliyunMavenRepositoryPlugin : Plugin<Gradle> {

//     override fun apply(dependency: Gradle) {
//         println("dependency apply: ${dependency}")
//     }
// }
