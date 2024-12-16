package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._

object ExuInwardImpl extends SimpleNodeImp[Seq[RsParam],TestParam,(RsParam, TestParam, Parameters),IssueBundle]{
  override def edge(pd: Seq[RsParam], pu: TestParam, p: Parameters, sourceInfo: SourceInfo):(RsParam, TestParam, Parameters) = {
    (RsParam(3), pu, p)
  }
  override def bundle(e: (RsParam, TestParam, Parameters)): IssueBundle = new IssueBundle()
  override def render(e: (RsParam, TestParam, Parameters)) = RenderedEdge("#00ff00")
}

class ExuInputNode(exuConfig: TestParam)(implicit valName: ValName) extends SinkNode(ExuInwardImpl)(Seq(exuConfig))

abstract class BasicExu(implicit p:Parameters) extends LazyModule{
  def issueNode: ExuInputNode

}

abstract class BasicExuImpl(outer:BasicExu) extends LazyModuleImp(outer){


}

class AluExu(id:Int, complexName:String, val bypassInNum:Int)(implicit p:Parameters) extends BasicExu{
  private val cfg  = TestParam(666)
  val issueNode = new ExuInputNode(cfg)

  lazy val module = new AluExuImpl(this, cfg)
}

class AluExuImpl(outer:AluExu, exuCfg:TestParam)(implicit p:Parameters) extends BasicExuImpl(outer){
  val io = IO(new Bundle{
    val bypassIn = Output(Vec(2, UInt(1.W))) //Alu does not need bypass out for its latency is 0. Bypassing in regfile is enough.
  })

  io.bypassIn := DontCare

}

class MulExu(id:Int, complexName:String, val bypassInNum:Int)(implicit p:Parameters) extends BasicExu{
  private val cfg  = TestParam(777)
  val issueNode = new ExuInputNode(cfg)

  lazy val module = new MulExuImpl(this, cfg)
}
class MulExuImpl(outer:MulExu, exuCfg:TestParam)(implicit p:Parameters) extends BasicExuImpl(outer){
  val io = IO(new Bundle{
    val bypassIn = Output(Vec(2, UInt(1.W))) //Alu does not need bypass out for its latency is 0. Bypassing in regfile is enough.
  })
  io.bypassIn := DontCare
}

class DivExu(id:Int, complexName:String, val bypassInNum:Int)(implicit p:Parameters) extends BasicExu{
  private val cfg  = TestParam(777)
  val issueNode = new ExuInputNode(cfg)
  lazy val module = new DivExuImpl(this, cfg)
}

class DivExuImpl(outer:DivExu, exuCfg:TestParam) extends BasicExuImpl(outer){
  val io = IO(new Bundle {
    val bypassIn = Output(Vec(2, Valid(UInt(2.W))))
  })
 
  io.bypassIn := DontCare
}

