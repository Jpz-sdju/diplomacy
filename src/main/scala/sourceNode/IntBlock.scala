package sourceNode

import freechips.rocketchip.diplomacy._
import chisel3.UInt
import org.chipsalliance.cde.config.Parameters
import chisel3.Bundle
import chisel3.experimental.SourceInfo
import chisel3.util._
import chisel3._

case class UpwardParam(width: Int)
case class DownwardParam(width: Int)
case class EdgeParam(width: Int)
case class TestParam(width: Int)
case class RsParam(width: Int)
class IssueBundle extends Bundle {
  val a = UInt(2.W)
}
object ExuBlockIssueNodeImpl
    extends SimpleNodeImp[Seq[
      RsParam
    ], UpwardParam, (RsParam, UpwardParam, Parameters), IssueBundle] {
  override def edge(
      pd: Seq[RsParam],
      pu: UpwardParam,
      p: Parameters,
      sourceInfo: SourceInfo
  ): (RsParam, UpwardParam, Parameters) = {
    (RsParam(3), pu, p)
  }
  override def bundle(e: (RsParam, UpwardParam, Parameters)): IssueBundle =
    new IssueBundle()
  override def render(e: (RsParam, UpwardParam, Parameters)): RenderedEdge =
    RenderedEdge("#0000ff", "Issue")
}

class ExuBlockIssueNode(implicit valName: ValName)
    extends AdapterNode(ExuBlockIssueNodeImpl)({ p => p }, { p => p })

class IntegerBlock(implicit p: Parameters) extends LazyModule {

  val issueNode = new ExuBlockIssueNode()
  val aluMul = Seq(LazyModule(new AluMulComplex(1, 0)))
  val aluDiv = Seq(LazyModule(new AluDivComplex(2, 0)))
  lazy val module = new IntegerBlockImpl(this)

  val intComplexes = aluMul ++ aluDiv

  intComplexes.foreach(execu => {
    execu.issueNode :*= issueNode
  })
}

class IntegerBlockImpl(outer: IntegerBlock) extends LazyModuleImp(outer) {
  val io = IO(new Bundle {
    val s = Output(UInt(1.W))
  })
  io.s := DontCare

  val a = outer.aluDiv
  val b = outer.aluMul
}
