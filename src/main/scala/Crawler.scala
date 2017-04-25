import java.io.{BufferedOutputStream, FileOutputStream}
import java.net.URL

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, Days}

import scala.util.Try

/**
  * Created by najus on 4/23/17.
  */
object Crawler {
  val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
  val baseUrl = "http://www.anything.com/upload/"

  def main(args: Array[String]): Unit = {
    val start = dateFormat.parseDateTime("2014-01-01")
    val end = dateFormat.parseDateTime("2016-01-01")
    val dateRange = getDateRange(start, end)
    val dateRangeWithNumbers = addNumbersToDate(dateRange)
    val fullUrl = dateRangeWithNumbers.map(x => baseUrl + x)
    val urlWithErrors = fullUrl.map(x => Try(new URL(x).openStream()))
    val output = urlWithErrors.filter(_.isSuccess).foreach(x => {
      var out: BufferedOutputStream = null;
      val localFile = "all/sample" + Math.random()*1000 + ".jpg"
      out = new BufferedOutputStream(new FileOutputStream(localFile))
      val byteArray = Stream.continually(x.get.read()).takeWhile(-1 !=).map(_.toByte).toArray
      println(byteArray.isEmpty)
      out.write(byteArray)
    })

    //    val url = new URL("http://www.avc.com/upload/2014-01-01-01-01.jpg")
    //    val output = url.openStream()
    println(output)
    //    temp.foreach(println)
  }


  private def getDateRange(start: DateTime, end: DateTime) = {
    val daysCount = Days.daysBetween(start, end).getDays() + 1
    (0 until daysCount).map(start.plusDays(_)).map(x => dateFormat.print(x))
  }

  private def addNumbersToDate(dateSeq: IndexedSeq[String]): IndexedSeq[String] = {
    val output = dateSeq.map(x => {
      for (i <- 1 to 10) yield {
        for (j <- 1 to 10) yield {
          if (i == 10 && j != 10) {
            x + "-" + Integer.toString(i) + "-0" + Integer.toString(j) + ".jpg"
          }
          else if (i != 10 && j == 10) {
            x + "-0" + Integer.toString(i) + "-" + Integer.toString(j) + ".jpg"
          }
          else if (i == 10 && j == 10) {
            x + "-" + Integer.toString(i) + "-" + Integer.toString(j) + ".jpg"
          }
          else {
            x + "-0" + Integer.toString(i) + "-0" + Integer.toString(j) + ".jpg"
          }
        }
      }
    }

    )
    output.flatten.flatten
  }

}
