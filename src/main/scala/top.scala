

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import scala.reflect.runtime.universe._
import freechips.rocketchip.diplomacy.AdapterNode
import sourceNode._
import org.chipsalliance.cde.config.Parameters
class top()() extends Module {
  val io = IO(new Bundle {})
  
  val l_soc = LazyModule(new ExecuteBlock()(Parameters.empty))
  val soc = Module(l_soc.module)


  dontTouch(soc.io)
  dontTouch(soc.rs1.io)
  dontTouch(soc.rs2.io)
  val rf = l_soc.regFile.issueNode
  val exu = l_soc.exuBlocks.head.issueNode
  val one = l_soc.integerBlock.aluDiv.head.issueNode
  val two = l_soc.integerBlock.aluMul.head.issueNode


  // println(rf.iPorts)
  println(rf.in)

  println(rf.out)

  println("===================================================================")
  // println(exu.iPorts)
  println(exu.out)
  println("===================================================================")
  // println(one)

  // println(exu.in)
  // println(exu.out)
  //   val s = l_soc.graphML
  //   println(s)
  // val f = new java.io.File("./test.graphml")
  // val fw = new java.io.FileWriter(f)
  // fw.write(s)
  // fw.close()
}

