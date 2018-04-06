import io.ninja.Item
import java.io.File
import java.io.InputStream

// Get french month
fun generateMonth(month: String): String {
    when (month) {
        "01" -> return "Janvier"
        "02" -> return "Fevrier"
        "03" -> return "Mars"
        "04" -> return "Avril"
        "05" -> return "Mai"
        "06" -> return "Juin"
        "07" -> return "Juillet"
        "08" -> return "Aout"
        "09" -> return "Septembre"
        "10" -> return "Octobre"
        "11" -> return "Novembre"
        "12" -> return "Decembre"
        else -> return "None"
    }
}
/* All files to process

aout-1.csv      decembre-2.csv  juillet-1.csv  mai-2.csv        spetembre-2.csv
aout-2.csv      fevrier-1.csv   juillet-2.csv  mars.csv
avril-1.csv     fevrier-2.csv   juin-1.csv     octobre-1.csv
avril-2.csv     janvier-1.csv   juin-2.csv     octobre-2.csv
decembre-1.csv  janvier-2.csv   mai-1.csv      spetembre-1.csv

 */
// Aout ended with 2108 id.

// Septembre ended with 21584

fun main(args: Array<String>) {

    // Set each files parameter
    var id=0

    val filename = "bulk_aout-1"

    val month = "08"

    val sb = StringBuffer()

    val folder = "/opt/USERNAME/ninja-raw-data/"

    val inputStream: InputStream = File(folder + "aout-1.csv").inputStream()
    val lineList = mutableListOf<String>()

    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }

    // Save years
    val years = lineList.get(0).split(",")
    lineList.removeAt(0)

    // Reverse list to start with the first days of the month
    lineList.reverse()

    // Pre-load essential data
    val days = ArrayList<String>()

    years.forEach{
        days.add("0")
    }

    val month_string = generateMonth(month)
    val date = "2018-04-06T14:10:41.000Z"
    val endDate = "T12:00:00.000Z"


    // For each elements
    lineList.forEach{
        //println(days)

        val items = it.split(",")

        for (i in items.indices) {

            // Check items-date, and increase date if it a new day
            if (items[i].startsWith("Le 13 heures du ")) {

                // increment date - load current day
                val removeTitle = items[i].removePrefix("Le 13 heures du ")
                var day = removeTitle.split(" ").get(0).removeSuffix("er")

                if (day.length < 2) {
                    day = "0" + day
                }

                // Save day
                days[i] = day
            } else if (!days[i].equals("0")) {
                id++
                val item_date = "${years[i]}-$month-${days[i]}$endDate"

                val currentItem = Item(item_date, items[i].replace("\"", ""), "Manual upload", "manual", month_string, "ninja13h",id, "non")
                sb.append(currentItem.toString())
                sb.append("\n")
            }
            //println(items[i])
        }

        //println(">  " + it)
    }
    //  println(sb.toString())

    println(id)

    // Write current bulk in a file
    val bulk = File(filename)
    bulk.writeText(sb.toString())
}
