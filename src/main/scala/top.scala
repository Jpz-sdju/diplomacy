import timer2iuncache.{IUnCache, TLTimer}

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import chipsalliance.rocketchip.config._
import scala.reflect.runtime.universe._
class top()(implicit p: Parameters) extends Module {
  val io = IO(new Bundle {})
  val l_soc = LazyModule(new sfs())
  val soc = Module(l_soc.module)

  val target = l_soc.iun.clientNode
  val target_1 = l_soc.timer.node

  def getParent(cls: Class[_]): Unit = {
    cls match {
      case null => return
      case _: Class[_] => {
        println(cls.getSuperclass())
        getParent(cls.getSuperclass())
      }
    }

  }
//   getParent(target.getClass())
//   getParent(target_1.getClass())

  def listPrint(tgt: List[_], indent: Int = 0): Unit = {
    tgt.foreach {
      case ls: List[_] => {
        print(" " * indent + "[")
        listPrint(ls, indent + 4)
        println(" " * indent + "]")
      }
      case other => println(" "* indent + other)
    }
  }

    val ss = List(target.edges, target.in, target.out, target.inward, target.outward)  
//   listPrint(ss)
println(target.edges)

}

class sfs()(implicit p: Parameters) extends LazyModule {
  val iun = LazyModule(new IUnCache())
  val timer = LazyModule(new TLTimer())
//    val xbar = TLXbar()
//    xbar := TLTempNode() := iun.clientNode
  timer.node := iun.clientNode

  lazy val module = new sfsImpl(this)

}

class sfsImpl(outer: sfs) extends LazyModuleImp(outer) {

  val iun = outer.iun.module
  val timer = outer.timer.module

}
