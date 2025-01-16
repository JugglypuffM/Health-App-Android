package utils

class CircularList<T>(private val items: List<T>) {
    private var currentIndex = 0

    fun next(): T {
        currentIndex = (currentIndex + 1) % items.size
        return current()
    }

    fun previous(): T {
        currentIndex = (currentIndex - 1 + items.size) % items.size
        return current()
    }

    fun current(): T = items[currentIndex]
}