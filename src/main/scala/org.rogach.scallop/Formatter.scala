package org.rogach.scallop

object Formatter {

  /** Distance between argument line description column */
  private val COLUMN_PADDING = 3
  private val DEFAULT_WIDTH = 80
  private val INDENT = 2
  
  /** Accepts a list of (argument line, option description). 
    * Also accepts optional width, to which the result must be formatted.
    */
  def format(s: List[(String, String)], width: Option[Int] = None): String = {
    val neededWidth = width getOrElse DEFAULT_WIDTH
    val argWidth =  if (s.isEmpty) 0 else s.map(_._1).map(a => if (a.startsWith("--")) "    " + a else a).map(_.size).max
    s.flatMap { case (arg, descr) => 
      val argPadding = " " * (if (arg.trim.startsWith("--")) 4 else 0)
      val text = wrap(descr.split(" "), neededWidth - argWidth - COLUMN_PADDING).map(l => " " * (argWidth + COLUMN_PADDING + INDENT) + l)
      (" " * INDENT + argPadding + arg + text.head.drop(arg.size + argPadding.size + INDENT)) :: text.tail
    }.mkString("\n")
  }
  
  /** Carefully wraps the text to the needed width. */
  def wrap(s: Seq[String], width: Int): List[String] = {
    var text = List[String]("")
    s foreach { w =>
      if (text.last.size + 1 + w.size <= width) {
        text = text.init :+ (text.last + w + " ")
      } else if (text.last.size + w.size <= width) {
        text = text.init :+ (text.last + w)
      } else text :+= (w + " ")
    }
    text
  }

}
