package eu.slickbot.vremedo.extension

inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
  return if (condition) run(block) else this
}
