package sourceNode

import org.chipsalliance.cde.config.Parameters
import chisel3._
import chisel3.util.Valid
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.Parameters
import chisel3.experimental.SourceInfo
import freechips.rocketchip.diplomacy._

abstract class BasicExuComplex(implicit p: Parameters) extends LazyModule {
  val issueNode = new ExuComplexIssueNode

  override def module: BasicExuComplexImp
}

abstract class BasicExuComplexImp(outer: BasicExuComplex, bypassNum: Int)
    extends LazyModuleImp(outer) {}
class ExuComplexIssueNode(implicit valName: ValName)
    extends MixedNexusNode(
      inner = ExuComplexIssueInwardNodeImpl,
      outer = ExuComplexIssueOutwardNodeImpl
    )(
      dFn = { p: Seq[Seq[RsParam]] =>
        {
          require(p.length == 1)
          p.head
        }
      },
      uFn = { p: Seq[TestParam] =>{ 

        UpwardParam(p.head.width)} }
    )

object ExuComplexIssueInwardNodeImpl
    extends InwardNodeImp[Seq[
      RsParam
    ], UpwardParam, (RsParam, UpwardParam, Parameters), IssueBundle] {
  override def edgeI(
      pd: Seq[RsParam],
      pu: UpwardParam,
      p: Parameters,
      sourceInfo: SourceInfo
  ): (RsParam, UpwardParam, Parameters) = {

    // (pd.head, pu, p)
    if(pu.width == 666){
      (pd.filter(_.width == 123).head, pu, p)
    }else {
      (pd.filter(_.width == 456).head, pu, p)
    }
  }
  override def bundleI(ei: (RsParam, UpwardParam, Parameters)): IssueBundle =
    new IssueBundle()
  override def render(ei: (RsParam, UpwardParam, Parameters)): RenderedEdge =
    RenderedEdge("#0000ff")
}
object ExuComplexIssueOutwardNodeImpl
    extends OutwardNodeImp[Seq[
      RsParam
    ], TestParam, (RsParam, TestParam, Parameters), IssueBundle] {
  override def edgeO(
      pd: Seq[RsParam],
      pu: TestParam,
      p: Parameters,
      sourceInfo: SourceInfo
  ): (RsParam, TestParam, Parameters) = {
    // (pd.head, pu, p)
    println(pd)
    if(pu.width == 666){
      (pd.filter(_.width == 123).head, pu, p)
    }else {
      (pd.filter(_.width == 456).head, pu, p)
    }
  }

  override def bundleO(eo: (RsParam, TestParam, Parameters)): IssueBundle =
    new IssueBundle()
}

class AluMulComplex(id: Int, bypassNum: Int)(implicit p: Parameters)
    extends BasicExuComplex {
  val alu = LazyModule(new AluExu(id, "AluMulComplex", bypassNum))
  val mul = LazyModule(new MulExu(id, "AluMulComplex", bypassNum))
  alu.issueNode :*= issueNode
  mul.issueNode :*= issueNode

  lazy val module = new AluMulCxImp(this, id, bypassNum)
}
class AluMulCxImp(outer: AluMulComplex, id: Int, bypassNum: Int)
    extends BasicExuComplexImp(outer, bypassNum) {

  val io = IO(new Bundle {
    // val bypassOut = Output(Valid(new ExuOutput))
    val csr_frm: UInt = Output(UInt(3.W))

  })

  val a = outer.alu
  val b = outer.mul
  io.csr_frm := DontCare

}
class AluDivComplex(id: Int, bypassNum: Int)(implicit p: Parameters)
    extends BasicExuComplex {
  val alu = LazyModule(new MulExu(id, "AluDivComplex", bypassNum))
  val div = LazyModule(new DivExu(id, "AluDivComplex", bypassNum))
  alu.issueNode :*= issueNode
  div.issueNode :*= issueNode

  lazy val module = new BasicExuComplexImp(this, bypassNum) {
    val io = IO(new Bundle {
      // val bypassOut = Output(Valid(new ExuOutput))
      val csr_frm: UInt = Output(UInt(3.W))
    })

    val a = alu.module
    val b = div.module

    io.csr_frm := DontCare
  }
}
