package eu.slickbot.scrape.utils.extension

import org.w3c.dom.Element
import org.w3c.dom.Node

fun Node.child(i: Int): Node? {
    return childNodes.item(i)
}

fun Node.childElement(i: Int): Element? {
    return child(i) as? Element
}

fun Node.childValue(i: Int): String? {
    return child(i)?.nodeValue
}
