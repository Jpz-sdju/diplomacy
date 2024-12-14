import chisel3._
object run extends App{
    println("safasfd")
    emitVerilog(new  top(),  Array("--target-dir", "generated"))
}