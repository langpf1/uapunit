/*// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2012-8-30 15:59:56
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames ansi 
// Source File Name:   N_20_APPROVE.java

package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.pu.m20.IPraybillApprove;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

public class N_20_APPROVE extends nc.bs.pub.compiler.AbstractCompiler2
{

    public N_20_APPROVE()
    {
    }

    public java.lang.String getCodeRemark()
    {
        return "\tsuper.m_tmpVo = paraVo;\n      // ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n      nc.vo.pu.m20.entity.PraybillVO[] inObject = (nc.vo.pu.m20.entity.PraybillVO[]) getVos();\n      Object retValue =nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.pu.m20.IPraybillApprove.class).approve(inObject,this);\n      return retValue;\n";
    }

    public java.lang.Object runComClass(nc.vo.pub.compiler.PfParameterVO vo)
        throws nc.vo.pub.BusinessException
    {
        java.lang.Object retValue;
        super.m_tmpVo = vo;
        nc.vo.pu.m20.entity.PraybillVO inObject[] = (nc.vo.pu.m20.entity.PraybillVO[])(nc.vo.pu.m20.entity.PraybillVO[])getVos();
        retValue = null;//((nc.itf.pu.m20.IPraybillApprove)nc.bs.framework.common.NCLocator.getInstance().lookup(IPraybillApprove.class)).approve(inObject, this);
        //return retValue;
        try {
			procActionFlow(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return inObject;
    }
}*/