import chisel3._
object run extends App{
    println("hello world!")
    emitVerilog(new  top(),  Array("--target-dir", "generated"))
    

}