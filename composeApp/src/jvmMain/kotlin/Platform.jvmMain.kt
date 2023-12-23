class JvmMainPlatform : Platform {
    override val name: String
        get() = "Java ${System.getProperty("java.version")}"
}

