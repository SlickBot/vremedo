package eu.slickbot.arso.model

data class ArsoDataGroup(
    val name: String,
    val items: List<Item>,
) {

    data class Item(
        val name: String,
        val link: String,
    )

}
