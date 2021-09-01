package cinema

enum class State {
    MENU, MAP, BUY, STATS, EXIT;

    companion object {
        var active = MENU
    }
}

class Cinema {
    private val rows: Int
    private val seats: Int
    private val seatMap: List<Array<Char>>
    private val totalSeats: Int
        get() = rows * seats

    init {
        println("Enter the number of rows:")
        rows = readLine()!!.toInt()
        println("Enter the number of seats in each row:")
        seats = readLine()!!.toInt()

        seatMap = List(rows) { Array(seats) { 'S' } }
    }

    private fun buyTicket() {
        println("\nEnter a row number:")
        val row = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        val seat = readLine()!!.toInt()

        if (row > rows || seat > seats) {
            println("Wrong input!")
            return
        }

        if (seatMap[row - 1][seat - 1] == 'B') {
            println("That ticket has already been purchased!")
            return
        }

        seatMap[row - 1][seat - 1] = 'B'
        showPrice(row)
        State.active = State.MENU
    }

    private fun showSeatMap() {
        println("\nCinema:")
        print(" ")
        for (i in 1..seats) {
            print(" $i")
        }
        println()

        for (r in 1..rows) {
            print("$r ")
            println(seatMap[r - 1].joinToString(" "))
        }
        State.active = State.MENU
    }

    private fun showPrice(row: Int) {
        if (totalSeats <= 60) {
            println("Ticket price: \$10")
            return
        }

        println("Ticket price: \$" + if (row <= rows / 2) "10" else "8")
    }

    private fun menu() {
        println(
            "\n1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit"
        )

        when (readLine()!!.toInt()) {
            1 -> State.active = State.MAP
            2 -> State.active = State.BUY
            3 -> State.active = State.STATS
            0 -> State.active = State.EXIT
        }
    }

    fun start() {
        do {
            when (State.active) {
                State.MENU -> menu()
                State.MAP -> showSeatMap()
                State.BUY -> buyTicket()
                State.STATS -> statistics()
            }
        } while (State.active != State.EXIT)
    }

    private fun statistics() {
        val booked = seatMap.sumOf { it.filter { it == 'B' }.count() }
        println("\nNumber of purchased tickets: $booked")

        val percentage = "%.2f".format(100.00 * booked / totalSeats)
        println("Percentage: $percentage%")

        if (totalSeats > 60) {
            val f = seatMap.subList(0, rows / 2).sumOf { it.filter { it == 'B' }.count() }
            val b = seatMap.subList(rows / 2, seatMap.size).sumOf { it.filter { it == 'B' }.count() }

            println("Current income: \$${f * 10 + b * 8}")
            println("Total income: \$${rows / 2 * seats * 10 + (rows - rows / 2) * seats * 8}")
        } else {
            println("Current income: \$${booked * 10}")
            println("Total income: \$${totalSeats * 10}")
        }

        State.active = State.MENU
    }
}

fun main() {
    val cinema = Cinema()
    cinema.start()
}