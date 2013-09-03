package uap.workflow.app.extend;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pf.pub.PfUIDataCache;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pf.pub.INCConsts;
import nc.vo.pf.pub.util.ArrayUtil;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.PfAddInfo;
import nc.vo.pub.pf.PfPOArriveVO;
import nc.vo.pub.pf.PfUtilActionConstrictVO;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pfflow01.BillFlowParnterVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.pfflow05.ActionconstrictVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.rbac.UserGroupVO;
import nc.vo.uap.rbac.excp.RbacException;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;
import nc.vo.wfengine.definition.WorkflowDefStatusEnum;

/**
 * ����ƽ̨DMO��
 * 
 * @author fangj 2001-10-8 16:04:02
 * @modifier leijun 2005-10-10 ���䵽NC5
 */
public class PfUtilDMO {

  public PfUtilDMO() {
    super();
  }

  /**
   * ���Ҹö����Ƿ�õ��ݵĽ�������
   * 
   * @param billType
   * @param actionName
   * @return
   * @throws DbException
   */
  public boolean queryLastStep(String billType, String actionName)
      throws DbException {
    boolean retflag = false;
    String sql =
        "select finishflag from pub_billaction where pk_billType=? and actiontype=?";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();

      SQLParameter para = new SQLParameter();
      para.addParam(billType);
      para.addParam(actionName);
      Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor(1));
      if (String.valueOf(obj).equals("Y")) {
        retflag = true;
      }
    }
    finally {
      if (persist != null)
        persist.release();
    }

    return retflag;
  }

  /**
   * ͨ���������͡�ҵ�����ͺͶ�������˾Id�������˵Ĳ�ѯ����ǰԼ������ <li>�����˻����޹أ��򷵻ظü�¼�� <li>
   * �������йأ��򷵻ظ��ò������йص�Լ�������� <li>���йأ��򷵻ظò����˵����йص�Լ��������
   * 
   * @param billType
   * @param businessType
   * @param actionName
   * @param corpId
   * @param operator
   * @return
   * @throws DbException
   * 
   * @modifier �׾� 2004-03-11 ���ӻ�ȡһ���ֶ� errhintmsg
   */
  public PfUtilActionConstrictVO[] queryActionConstrict(String billType,
      String businessType, String actionName, String operator, String pk_group)
      throws DbException {
    String sql =
        "select configflag,operator,a.classname as aclassname,"
            + " a.returntype as areturntype,a.functionnote as afunctionnote, "
            + " a.methodname as amethod,a.arguments as aparameter,ysf,value, "
            + " b.classname as bclassname,b.functionnote as bfunctionnote, "
            + " b.methodname as bmethod,b.arguments as bparameter,errhintmsg,c.isbefore "
            + " from pub_actionconstrict c inner join pub_function a on "
            + " c.functionid=a.pk_function left join pub_function b on c.value=b.pk_function "
            + " left join bd_busitype d on c.pk_businesstype=d.pk_busitype"
            + " where c.pk_billtype=? and actiontype=?";
    if (businessType == null
        || IPFConfigInfo.STATEBUSINESSTYPE.equals(businessType)) {
      sql =
          sql + " and pk_businesstype='" + IPFConfigInfo.STATEBUSINESSTYPE
              + "'" + " and (c.pk_group='~' or c.pk_group='"
              + pk_group + "')";
    }
    else {
      // ҵ������,���ҵ�����Ͳ�������
      sql += " and c.pk_businesstype='" + businessType + "'";
    }
    sql +=
        " and (configflag="
            + IPFConfigInfo.UserNoRelation// �Ͳ���Ա�޹�
            + " or (configflag="
            + IPFConfigInfo.UserRelation
            + " and operator=?) "// �Ͳ���Ա�й�
            + " or (configflag="
            + IPFConfigInfo.RoleRelation// �ͽ�ɫ�й�,��Ҫ�����û��ڹ�˾�н�ɫ��ѯ
            + " and operator in(select pk_role from sm_user_role where cuserid=?)) "
            + " ) order by c.sysindex";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();

      // ���ò���
      // para.addParam(corpId); //��˾Id
      para.addParam(billType); // ��������
      para.addParam(actionName); // ��������

      para.addParam(operator); // ����Ա
      para.addParam(operator); // �ͽ�ɫ�йصĲ���Ա
      // para.addParam(corpId); //�ͽ�ɫ�йصĹ�˾Id
      List lResult =
          (List) jdbc.executeQuery(sql, para, new ConstirctProcessor());

      if (PfUtilBaseTools.isTranstype(billType)) {
        // ��������û�ҵ����ҵ�������
        boolean isNeedQueryParentConsrict_before = false;
        boolean isNeedQueryParentConsrict_after = false;
        int beforeConstirctCount = 0;
        int afterConstirctCount = 0;
        for (PfUtilActionConstrictVO vo : (ArrayList<PfUtilActionConstrictVO>) lResult) {
          if (vo.getIsBefore().equals(ActionconstrictVO.TYPE_BEFORE)) {
            beforeConstirctCount++;
          }
          if (vo.getIsBefore().equals(ActionconstrictVO.TYPE_AFTER)) {
            afterConstirctCount++;
          }
        }

        if (beforeConstirctCount == 0) {
          isNeedQueryParentConsrict_before = true;
        }
        if (afterConstirctCount == 0) {
          isNeedQueryParentConsrict_after = true;
        }

        if (isNeedQueryParentConsrict_before || isNeedQueryParentConsrict_after) {
          String parentbilltype = PfUtilBaseTools.getRealBilltype(billType);
          para.clearParams();
          para.addParam(parentbilltype);
          para.addParam(actionName);
          para.addParam(operator);
          para.addParam(operator);
          ArrayList<PfUtilActionConstrictVO> lParentResult =
              (ArrayList<PfUtilActionConstrictVO>) jdbc.executeQuery(sql, para,
                  new ConstirctProcessor());
          for (PfUtilActionConstrictVO vo : lParentResult) {
            if (isNeedQueryParentConsrict_before) {
              if (vo.getIsBefore().equals(ActionconstrictVO.TYPE_BEFORE)) {
                lResult.add(vo);
              }
            }
            if (isNeedQueryParentConsrict_after) {
              if (vo.getIsBefore().equals(ActionconstrictVO.TYPE_AFTER)) {
                lResult.add(vo);
              }
            }
          }

        }

      }
      return (PfUtilActionConstrictVO[]) lResult
          .toArray(new PfUtilActionConstrictVO[lResult.size()]);
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  /**
   * ��ѯ���ݵĶ���ִ��ǰ����ʾ���
   * 
   * @param billType
   *          ��������PK
   * @param actionType
   *          ��������
   * @return
   * @throws DbException
   */
  public String queryActionHint(String billType, String actionType)
      throws DbException {
    String sql =
        "select showhint from pub_billaction where pk_billtype=? and actiontype=?";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();

      para.addParam(billType);
      para.addParam(actionType);
      Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor(1));
      return String.valueOf(obj);
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  private String createBilltypeSql(String billOrTranstype,
      boolean includeBillType) {
    String strField = "";
    if (!PfUtilBaseTools.isTranstype(billOrTranstype)) {
      strField += "  (a.pk_billtype='" + billOrTranstype + "')"; // and
      // a.transtype
      // is
      // null
      // �������ͣ��޽������ͣ����õ�����
    }
    else {// ��������
      if (includeBillType) {
        strField =
            "((a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype='" + billOrTranstype + "')"; // ��������+�����������õ�����
        strField +=
            " or (a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype = '~')"; // �������ͣ��޽������ͣ����õ�����
      }
      else {
        strField =
            "(a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype='" + billOrTranstype + "')"; // ��������+�����������õ�����
      }

    }
    return strField;
  }

  /**
   * ���ݹ�˾Id,��������(��������)��ҵ�����Ͳ��Ҹõ������͵���Դ
   * 
   * <li>��������Ƶ��ݣ�Ҳ������Ϊһ��VO���� <li>Ҳ�Ӽ��Ż�ȡ�������õ���Դ����lj+
   * 
   * @param groupId
   *          ��˾PK
   * @param billOrTranstype
   *          ��ǰ��������(��������)PK
   * @param businessType
   *          ҵ������PK
   * @return
   * @throws DbException
   */
  public BillbusinessVO[] queryBillSource(final String groupId,
      final String billOrTranstype, String businessType, boolean includeBillType)
      throws DbException {
    // ��Ҫ�ж�������ǵ������ͻ��ǽ�������
    String strField = createBilltypeSql(billOrTranstype, includeBillType);

    String sql =
        "select a.makebillflag,a.pk_businesstype,a.pk_billtype,b.referbilltype,b.pk_businesstype "
            + "from pub_billbusiness a left join pub_billsource b on a.pk_billbusiness=b.billbusinessid "
            + "where a.pk_businesstype=b.pk_businesstype and a.pk_group=?  and "
            + strField;

    if (businessType != null)
      sql += " and a.pk_businesstype=?";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();

      // ���ò���
      para.addParam(groupId); // ��˾����
      if (businessType != null)
        para.addParam(businessType); // ��������

      List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
        public Object processResultSet(ResultSet rs) throws SQLException {
          ArrayList<BillbusinessVO> al = new ArrayList<BillbusinessVO>();

          if (!rs.next())
            return al;
          BillbusinessVO referVo = null;
          if (rs.getString("makebillflag").equals("Y")) {
            referVo = new BillbusinessVO();
            referVo.setBilltypename(NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("busitype", "busitypehint-000000")/*���Ƶ���*/);
            referVo.setPk_billtype("makeflag");
            referVo.setPk_businesstype(rs.getString("pk_businesstype"));
            al.add(referVo);
          }
          String tmpString = rs.getString("referbilltype");
          if (!StringUtil.isEmptyWithTrim(tmpString)) {
            referVo = new BillbusinessVO();
            String realBilltype =
                PfUtilBaseTools.getRealBilltype(tmpString.trim());
            referVo.setPk_billtype(realBilltype);
            tmpString = rs.getString("pk_businesstype");
            tmpString = (tmpString == null ? "" : tmpString.trim());
            referVo.setPk_businesstype(tmpString);
            referVo.setBilltypename(PfUIDataCache
                .getBillTypeNameByCode(realBilltype));
            al.add(referVo);
          }
          while (rs.next()) {
            tmpString = rs.getString("referbilltype");
            if (!StringUtil.isEmptyWithTrim(tmpString)) {
              referVo = new BillbusinessVO();
              String realBilltype =
                  PfUtilBaseTools.getRealBilltype(tmpString.trim());
              referVo.setPk_billtype(realBilltype);
              tmpString = rs.getString("pk_businesstype");
              tmpString = (tmpString == null ? "" : tmpString);
              referVo.setPk_businesstype(tmpString);
              referVo.setBilltypename(PfUIDataCache
                  .getBillTypeNameByCode(realBilltype));
              // XXX:�����ظ�����Դ����
              if (!isAlreadyExist(al, referVo.getPk_billtype()))
                al.add(referVo);
            }
          }
          return al;
        }

        /**
         * �ж���Դ�����Ƿ��Ѿ�����
         * 
         * @param alList
         * @param srcBilltype
         * @return
         */
        private boolean isAlreadyExist(ArrayList<BillbusinessVO> alList,
            String srcBilltype) {
          for (BillbusinessVO bbVO : alList) {
            if (bbVO.getPk_billtype().equals(srcBilltype))
              return true;
          }
          return false;
        }
      });
      return (BillbusinessVO[]) lResult.toArray(new BillbusinessVO[lResult
          .size()]);
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * ��ѯĳҵ��������ĳ��������(��������)�����ε���VO����
   * 
   * @param billType
   *          ��������(��������)PK
   * @param busiType
   *          ҵ������PK�����Ϊ�գ��򷵻��������������иõ��ݵ����ε���
   * @return
   * @throws DbException
   */
  public BillbusinessVO[] queryBillDest(String billType, String busiType)
      throws DbException {
    String groupid = InvocationInfoProxy.getInstance().getGroupId();
    if (groupid.equals(INCConsts.GROUP_CODE)) {
      groupid = INCConsts.COMMONCORP;
    }
    boolean isNull = StringUtil.isEmptyWithTrim(busiType);
    String sql =
        "select a.pk_billbusiness,a.pk_group,a.pk_billtype,a.transtype,a.pk_businesstype,a.referbillflag,a.makebillflag "
            + "from pub_billbusiness a left join pub_billsource b on a.pk_billbusiness=b.billbusinessid "
            + "where a.pk_group=? "
            + (isNull ? "" : " and a.pk_businesstype=?");

    if (PfUtilBaseTools.isTranstype(billType)) {
      // ����ǽ�������
      sql += " and b.referbilltype='" + billType + "'";
    }
    else {
      sql +=
          " and (b.referbilltype='"
              + billType
              + "' or b.referbilltype in (select pk_billtypecode from bd_billtype where parentbilltype='"
              + billType + "' and istransaction='Y'))";
    }
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(groupid);
      // para.addParam(billType);
      if (!isNull)
        para.addParam(busiType);
      ArrayList alRet =
          (ArrayList) jdbc.executeQuery(sql, para, new BeanListProcessor(
              BillbusinessVO.class));
      return (BillbusinessVO[]) alRet.toArray(new BillbusinessVO[alRet.size()]);
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  /**
   * ���ݹ�˾PK,��������(��������)PK�������ù���ҵ������ <li>Ҳ���ؼ������õ�ҵ������,�������õ�ҵ����������ǰ�� <li>
   * ���ң�ҵ�����Ͱ��ձ�������
   * 
   * @param groupId
   * @param billType
   *          �������ͻ�������
   * @return
   * @throws DbException
   */
  public BusitypeVO[] queryBillBusinessType(String groupId, String billType)
      throws DbException {
    // XXX::��Ҫ�ж�������ǵ������ͻ��ǽ�������
    String strField;
    if (PfUtilBaseTools.isTranstype(billType))
      strField = "a.transtype";
    else
      strField = "a.pk_billtype";
    String sql =
        "select a.pk_businesstype,b.businame,b.busicode,a.pk_group from pub_billbusiness a,bd_busitype b"
            + " where a.pk_businesstype=b.pk_busitype and b.validity="
            + WorkflowDefStatusEnum.Valid.getIntValue()
            + " and "
            + strField
            + "=? and a.pk_group=? order by b.busicode";

    // ���Ʊ�2006-4-7������sqlserver����@@@@��1001���ַ�������Ĵ���պú�oracle�෴�������������ݿ�Ĳ����Ե��²���ʹ�����򣬸�Ϊ�ɳ���������
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(billType); // ��������
      para.addParam(groupId); // ���ű���
      List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
        public Object processResultSet(ResultSet rs) throws SQLException {
          ArrayList<BusitypeVO> al = new ArrayList<BusitypeVO>();
          while (rs.next()) {
            BusitypeVO btVO = new BusitypeVO();
            String tmpString = rs.getString("pk_businesstype");
            if (tmpString == null) {
              btVO.setPrimaryKey(null);
            }
            else {
              btVO.setPrimaryKey(tmpString.trim());
            }
            tmpString = rs.getString("businame");
            if (tmpString == null) {
              btVO.setBusiname(null);
            }
            else {
              btVO.setBusiname(tmpString.trim());
            }
            tmpString = rs.getString("busicode");
            if (tmpString == null) {
              btVO.setBusicode(null);
            }
            else {
              btVO.setBusicode(tmpString.trim());
            }
            tmpString = rs.getString("pk_group");
            if (tmpString == null) {
              btVO.setPk_group(null);
            }
            else {
              btVO.setPk_group(tmpString.trim());
            }

            // XXX:�����ظ���ҵ������
            if (!isAlreadyExist(al, btVO.getPrimaryKey()))
              al.add(btVO);
          }
          return al;
        }

        /**
         * �ж��Ƿ��Ѿ������˸�ҵ������
         * 
         * @param al
         * @param pk_busitype
         * @return
         */
        private boolean isAlreadyExist(ArrayList<BusitypeVO> al,
            String pk_busitype) {
          if (StringUtil.isEmptyWithTrim(pk_busitype))
            return false;
          for (BusitypeVO btVO : al) {
            if (pk_busitype.equals(btVO.getPrimaryKey()))
              return true;
          }
          return false;
        }
      });
      BusitypeVO[] voRet = null;
      if (lResult != null && lResult.size() > 0) {
        voRet = new BusitypeVO[lResult.size()];
        lResult.toArray(voRet);
        VOUtil.descSort(voRet, new String[] {
          "pk_group"
        });// ����pk_corp�����ţ���֤@@@@����ǰ�棬����ʹ�����ݿ������
      }
      return voRet;
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * ͨ���������͡�ҵ�����͡���˾Id������Ա��ѯ���������Ķ��� �����м�¼VO����
   * 
   * @param billType
   * @param businessType
   * @param groupId
   * @param sourceAction
   * @param operator
   * @return
   * @throws DbException
   * @�޸��ߣ����Ʊ�20060430�����Ӳ���Ա������ֱ�ӹ��˳����ϲ���Ա�Ķ�������
   */
  public PfUtilActionVO[] queryDriveAction(final String billType,
      String businessType, String groupId, String sourceAction, String operator)
      throws DbException {
    String sql =
        "select pk_driveconfig,configflag,operator,pk_billtype,pk_sourcebusinesstype,actiontype "
            + " from pub_messagedrive where pk_sourcebilltype=? and sourceaction=? ";
    if (businessType == null) {
      sql += " and pk_businesstype ='~' ";
    }
    else {
      sql += " and pk_businesstype=? ";
      // // ���ҵ�����Ͳ�������
      // sql +=
      // " and exists (select 1 from bd_busitype where bd_busitype.pk_busitype = pub_messagedrive.pk_businesstype and validity = "
      // + WorkflowDefStatusEnum.Valid.getIntValue() + " ) ";
    }

    sql +=
        " and ( configflag="
            + IPFConfigInfo.UserNoRelation// �Ͳ���Ա�޹�
            + " or (configflag="
            + IPFConfigInfo.UserRelation
            + " and operator=?) "// �Ͳ���Ա�й�
            + " or (configflag="
            + IPFConfigInfo.RoleRelation// �ͽ�ɫ�й�,��Ҫ�����û��ڹ�˾�н�ɫ��ѯ
            + "     and operator in(select pk_role from sm_user_role where cuserid=? )) "
            + " )order by sysindex desc ,pk_billtype";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      // ���ò���
      // para.addParam(groupId);
      para.addParam(billType);
      para.addParam(sourceAction);
      if (businessType != null) {
        para.addParam(businessType); // ҵ������
      }
      para.addParam(operator); // ����Ա
      para.addParam(operator); // �ͽ�ɫ�йصĲ���Ա
      // para.addParam(groupId); //�ͽ�ɫ�йصĹ�˾Id

      List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
        public Object processResultSet(ResultSet rs) throws SQLException {
          ArrayList<PfUtilActionVO> al = new ArrayList<PfUtilActionVO>();
          while (rs.next()) {
            PfUtilActionVO action = new PfUtilActionVO();
            String tmpString = null;

            tmpString = rs.getString("pk_driveconfig");
            action.setPkMessageDrive(tmpString.trim());

            int configFlag = rs.getInt("configflag");
            action.setConfigFlag(Integer.valueOf(configFlag));

            tmpString = rs.getString("operator");
            if (tmpString != null) {
              action.setRelaPK(tmpString.trim());
            }
            else {
              action.setRelaPK(null);
            }

            // ������������
            tmpString = rs.getString("pk_billtype");
            if (tmpString == null) {
              action.setBillType(null);
            }
            else {
              action.setBillType(tmpString.trim());
            }
            // ��������ҵ������
            tmpString = rs.getString("pk_sourcebusinesstype");
            if (tmpString == null) {
              action.setBusinessType(null);
            }
            else {
              action.setBusinessType(tmpString.trim());
            }
            // ������������
            tmpString = rs.getString("actiontype");
            if (tmpString == null) {
              action.setActionName(null);
            }
            else {
              action.setActionName(tmpString.trim());
            }
            // ��������������
            action.setDriveBillType(billType);
            al.add(action);
          }
          return al;
        }
      });
      return (PfUtilActionVO[]) lResult.toArray(new PfUtilActionVO[lResult
          .size()]);
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * ��ѯ��ǰҵ�������Ƿ����ָ���ĵ�����������
   * 
   * @param billTypeAry
   * @param busiType
   * @return
   * @throws DbException
   */
  public PfPOArriveVO queryIsSameByBillTypeAry(String billTypeAry,
      String busiType) throws DbException {
    String sql =
        " SELECT a.pk_billtype,a.pk_businesstype,b.busicode,b.businame "
            + "FROM pub_billbusiness a LEFT OUTER JOIN bd_busitype b "
            + "ON a.pk_businesstype = b.pk_busitype WHERE a.pk_businesstype=? AND "
            + "a.pk_billtype IN (" + billTypeAry + ")";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(busiType);
      PfPOArriveVO retVo = null;
      retVo = (PfPOArriveVO) jdbc.executeQuery(sql, para, new BaseProcessor() {
        public Object processResultSet(ResultSet rs) throws SQLException {
          PfPOArriveVO arriveVo = null;
          // ���Ұ����ɹ�������ҵ������
          while (rs.next()) {
            if (arriveVo == null) {
              arriveVo = new PfPOArriveVO();
            }
            String tmpString = rs.getString(1);
            // FIXME::��ô����̫�����ˣ�
            if ("23".equals(tmpString)) {
              arriveVo.setArriveFlag(new UFBoolean("Y"));
            }
            if ("45".equals(tmpString)) {
              arriveVo.setStoreFlag(new UFBoolean("Y"));
            }

            tmpString = rs.getString(2);
            if (tmpString == null) {
              arriveVo.setPk_busiType(null);
            }
            else {
              arriveVo.setPk_busiType(tmpString.trim());
            }
            tmpString = rs.getString(3);
            if (tmpString == null) {
              arriveVo.setBusiCode(null);
            }
            else {
              arriveVo.setBusiCode(tmpString.trim());
            }
            tmpString = rs.getString(4);
            if (tmpString == null) {
              arriveVo.setBusiName(null);
            }
            else {
              arriveVo.setBusiName(tmpString.trim());
            }
          }
          return arriveVo;
        }
      });
      return retVo;
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * @param busiPks
   * @param billtype
   * @return
   * @throws BusinessException
   */
	public String[] getBillbusiPKsOfBilltype(String[] busiPks, String billtype) throws BusinessException {
		if (busiPks == null || busiPks.length == 0)
			return null;

		// XXX:Ҫ���ִ�����ǵ������ͻ��ǽ�������
		String strField;
		if (PfUtilBaseTools.isTranstype(billtype))
			strField = "transtype";
		else
			strField = "pk_billtype";

		StringBuffer sWhere = new StringBuffer();
		sWhere.append(strField);
		sWhere.append("='");
		sWhere.append(billtype);
		sWhere.append("' and (pk_billbusiness='");
		sWhere.append(busiPks[0]);
		sWhere.append("'");

		for (int i = 1; i < busiPks.length; i++) {
			sWhere.append(" or pk_billbusiness='");
			sWhere.append(busiPks[i]);
			sWhere.append("'");
		}

		sWhere.append(")");
		Collection collection = new BaseDAO().retrieveByClause(BillbusinessVO.class, sWhere.toString(),
				new String[] { "pk_businesstype" });
		if (collection == null || collection.size() == 0)
			return null;
		String[] sRet = new String[collection.size()];
		BillbusinessVO voTmp = null;
		int i = 0;
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			voTmp = (BillbusinessVO) iter.next();
			sRet[i] = voTmp.getPk_businesstype();
			i++;
		}
		return sRet;
	}

  /**
   * ���ݹ�˾PK,��������(��������)PK����û�н���Ȩ�޿��Ƶ�ҵ������ <li>Ҳ���ؼ������õ�ҵ������
   * 
   * @param groupId
   * @param billType
   *          ��������(��������)PK
   * @return
   * @throws DbException
   * @author ���Ʊ�2006-03-29
   */
  public String[] queryBusitypeOfNoControl(String groupId, String billType)
      throws DbException {
    // XXX:Ҫ���ִ�����ǵ������ͻ��ǽ�������
    String strField;
    if (PfUtilBaseTools.isTranstype(billType))
      strField = "transtype";
    else
      strField = "pk_billtype";

    String sql =
        "select pk_businesstype from pub_billbusiness where pk_group=? and "
            + strField + "=? ";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      // ���ò���
      para.addParam(groupId); // ��˾����
      para.addParam(billType); // ��������
      List list =
          (List) jdbc.executeQuery(sql, para, new ColumnListProcessor(1));
      String[] sRet = null;
      if (list != null && list.size() > 0) {
        sRet = new String[list.size()];
        list.toArray(sRet);
      }
      return sRet;
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  /**
   * ����ĳ��ҵ�������µ�������������VO���� <li>����һЩ������Ϣ
   * 
   * @param biztype
   *          ҵ������pK
   * @param pk_group
   *          ����PK
   * @return
   * @throws DbException
   */
  public BillbusinessVO[] queryBillbusiVOs(String biztype, String pk_group)
      throws DbException {
    String sql = "select a.* from pub_billbusiness a where a.pk_businesstype=?";

    if (!StringUtil.isEmptyWithTrim(pk_group))
      sql += " and a.pk_group='" + pk_group + "'";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(biztype);
      List list =
          (List) jdbc.executeQuery(sql, para, new BeanListProcessor(
              BillbusinessVO.class));
      BillbusinessVO[] vos =
          (BillbusinessVO[]) list.toArray(new BillbusinessVO[list.size()]);
      for (BillbusinessVO billbusiVO : vos) {
        String billType = billbusiVO.getPk_billtype();
        String transType = billbusiVO.getTranstype();
        BilltypeVO btVO =
            PfDataCache.getBillType(new BillTypeCacheKey().buildBilltype(
                billType).buildPkGroup(pk_group));
        billbusiVO.setBilltypename(Pfi18nTools.i18nBilltypeName(billType,
            btVO == null ? null : btVO.getBilltypename()));
        if (!StringUtil.isEmptyWithTrim(transType)) {
          BilltypeVO transtypeVO =
              PfDataCache.getBillType(new BillTypeCacheKey().buildBilltype(
                  transType).buildPkGroup(pk_group));
          billbusiVO.setTranstypename(Pfi18nTools.i18nBilltypeName(transType,
              transtypeVO == null ? null : transtypeVO.getBilltypename()));
        }
      }
      return vos;
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * ��ѯĳ��ҵ��������ָ���������ͻ������͵�Ŀ��ҵ������
   * 
   * @param busitype
   * @param billtype
   * @return
   * @throws DbException
   */
  public String queryDestBusitype(String busitype, String billtype)
      throws DbException {
    boolean isTranstype = PfUtilBaseTools.isTranstype(billtype);
    String sql =
        "select destbiztype from pub_billbusiness where pk_businesstype=? ";
    if (isTranstype)
      sql += "and transtype=?";
    else
      sql += "and pk_billtype=?";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(busitype);
      para.addParam(billtype);
      Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor());
      return (String) obj;
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  /**
   * ����ĳ�����ݻ������Ϳ�ʹ�õġ������������˵���Ϣ <li>�������ơ���Դ����
   * 
   * @param billtype
   * @param transtype
   * @param pk_group
   * @param userId
   * @param includeBillType
   * @return
   * @throws BusinessException
   */
  public PfAddInfo[] retAddInfo(String billtype, String transtype,
      String pk_group, String userId, boolean includeBillType)
      throws BusinessException {
    // ����������ȷ��
    checkParams(billtype, transtype);
    // 1.��ѯ��ǰ���ݻ������ͣ�ĳ�û�����ġ�ĳ��֯������ҵ������
    ArrayList<String> alBusitypes =
        findRelatedBusitype(billtype, transtype, pk_group, null, userId,
            includeBillType);
    if (alBusitypes == null || alBusitypes.size() == 0)
      return null;

    // 2.��ѯ�Ƿ����á����ơ�
    HashMap<String, PfAddInfo> hmRet = new HashMap<String, PfAddInfo>();
    ArrayList<String> alBusitypesMakebill =
        canMakeBillBusi(billtype, transtype, alBusitypes, includeBillType);
    if (alBusitypesMakebill != null && alBusitypesMakebill.size() > 0) {
      PfAddInfo ai = new PfAddInfo();
      ai.setMakeflag(true);
      hmRet.put("MAKEFLAG", ai);
    }

    // 3.��ѯ��ǰ���ݻ������� ����Դ��ϵ
    ArrayList alSrcTypes =
        findBillsrc(billtype, transtype, alBusitypes, includeBillType);

    // �����ظ�����Դ����
    for (Object srcType : alSrcTypes) {
      String[] strTypes = new String[((Object[]) srcType).length];
      System.arraycopy(srcType, 0, strTypes, 0, strTypes.length);
      String strRealBilltype = PfUtilBaseTools.getRealBilltype(strTypes[0]); // ֻ�赥������
      if (!hmRet.containsKey(strRealBilltype)) {
        PfAddInfo ai = new PfAddInfo();
        // ��Դ��������
        ai.setSrc_billtype(strRealBilltype);
        BilltypeVO btVO =
            PfDataCache.getBillTypeInfo(new BillTypeCacheKey()
                .buildBilltype(strRealBilltype));
        // ��Դ������������
        ai.setSrc_billtypename(Pfi18nTools.i18nBilltypeName(strRealBilltype,
            btVO.getBilltypename()));
        // ҵ������
        ArrayList<String> busiTypes = new ArrayList<String>();
        busiTypes.add(strTypes[1]);
        ai.setBusitypes(busiTypes);
        // ��������
        ArrayList<String> transTypes = new ArrayList<String>();
        if (PfUtilBaseTools.isTranstype(strTypes[0])) {
          transTypes.add(strTypes[0]);
        }
        else {
          // ֻ���˵������ͣ�û���佻������
          transTypes.add("");
        }
        ai.setTranstypes(transTypes);
        hmRet.put(strRealBilltype, ai);
      }
      else {
        hmRet.get(strRealBilltype).getBusitypes().add(strTypes[1]);
        if (PfUtilBaseTools.isTranstype(strTypes[0]))
          hmRet.get(strRealBilltype).getTranstypes().add(strTypes[0]);
        else
          hmRet.get(strRealBilltype).getTranstypes().add("");
      }
    }
    return hmRet.values().toArray(new PfAddInfo[0]);
  }

  /**
   * �����ܲ�����������̣������ҳ����
   * 
   * @param billtype
   * @param transtype
   * @param pk_group
   * @param pk_org
   * @param userId
   * @param includeBillType
   * @return
   * @throws BusinessException
   * @throws RbacException
   */
  private ArrayList<String> findRelatedBusitype(String billtype,
      String transtype, String pk_group, String pk_org, String userId,
      boolean includeBillType) throws RbacException, BusinessException {
    boolean hasTranstype = false;
    if (!StringUtil.isEmptyWithTrim(transtype))
      hasTranstype = true;

    String sql =
        " select pk_businesstype from pub_billbusiness a inner join bd_busitype b on a.pk_businesstype=b.pk_busitype where b.validity != "
            + WorkflowDefStatusEnum.Init.getIntValue()
            + " and b.pk_group ='"
            + pk_group + "'";
    sql += " and a.pk_billtype='" + billtype + "'";
    if (hasTranstype) {
      // ����Ľ������ͷǿ�
      if (includeBillType)
        sql +=
            " and ( a.transtype = '~' or a.transtype='" + transtype
                + "')";
      else
        sql += " and a.transtype='" + transtype + "'";
    }
    // �����֯���� modified by zhangrui 
    if(!StringUtil.isEmptyWithTrim(pk_org)) {
    	sql += " and exists (select 1 from pub_billflow_org bo where bo.pk_busitype = b.pk_busitype and bo.pk_org = '" + pk_org + "')";
    }

    // ��ȥ��Щ �����ɸ��û�����ģ��������˲����ߣ����ǲ�������û��ָ�����û�
    String sql_partner =
        "select busitype from pub_billflow_partner where billtype='" + billtype
            + "'";
    if (hasTranstype) {
      if (includeBillType)
        sql_partner +=
            " and (isnull(transtype,'~')='~' or transtype='" + transtype + "')";
      else
        sql_partner += " and transtype='" + transtype + "'";
    }
    String inRole = getRoleSqlWithComma(userId);
    String inUser = "'" + userId + "'";
    String inUserGrp = getUserGroupSqlWithComma(userId);
    String inRoleGrp = getRoleGroupSqlWithComma(userId);
    String clause = inUser + inRole + inUserGrp + inRoleGrp;
    String sql_in = sql_partner + " and parnter_pk  in(" + clause + ") ";
    sql_partner += " and parnter_pk not in(" + clause + ") ";

    // partner����pk_org�ֶ�û�ã���ȥ�� modified by zhangrui 2012-3-30
//    if (!StringUtil.isEmptyWithTrim(pk_org)) {
//      sql_partner += " and pk_org='" + pk_org + "'";
//      sql_in += " and pk_org='" + pk_org + "'";
//    }

    sql =
        sql + "and ( pk_businesstype not in (" + sql_partner
            + " ) or pk_businesstype  in (" + sql_in + "))";

    return (ArrayList<String>) new BaseDAO().executeQuery(sql,
        new ColumnListProcessor());
  }

  private ArrayList findBillsrc(String billtype, String transtype,
      ArrayList<String> alBusitypes, boolean includeBillType)
      throws DAOException {

    String inBusitype = getInBusitype(alBusitypes);
    String whereSql = "";
    if (StringUtil.isEmptyWithTrim(transtype)) {
      // ���ݵ������� ��ѯ��������̣�Ҳ�������ӽ������͵�
      whereSql =
          "(pk_billtype='"
              + billtype
              + "' or pk_billtype in(select pk_billtypecode from bd_billtype where istransaction='Y' and parentbilltype='"
              + billtype + "'))";
    }
    else {
      // ����Ľ������ͷǿ�
      if (includeBillType) {
        // ���ݵ��ݻ������� ��ѯ���������
        whereSql =
            "(pk_billtype='" + transtype + "' or pk_billtype='" + billtype
                + "')";
      }
      else {
        // ���ݽ������� ��ѯ���������
        whereSql = "pk_billtype='" + transtype + "'";
      }
    }

    String sql_billsrc =
        "select referbilltype, pk_businesstype from pub_billsource where "
            + whereSql + " and pk_businesstype in(" + inBusitype + ")";

    return (ArrayList) new BaseDAO().executeQuery(sql_billsrc,
        new ArrayListProcessor());

  }

	private String getInBusitype(ArrayList<String> alBusitypes) {
		// �ڵ��ø÷��������������Ѿ���֤��alBusitypes != null && alBusityoes.size()>0
		StringBuffer inBusitype = new StringBuffer();
		if (alBusitypes == null || alBusitypes.size() == 0)
			return inBusitype.toString();
		for (String busitype : alBusitypes) {
			inBusitype.append(",'");
			inBusitype.append(busitype);
			inBusitype.append("'");
		}
		return inBusitype.substring(1);
	}

  private void checkParams(String billtype, String transtype)
      throws PFBusinessException {
    if (StringUtil.isEmptyWithTrim(billtype))
      throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("busitype", "busitypehint-000001")/*�����������billtype����Ϊ��*/);
    if (PfUtilBaseTools.isTranstype(billtype))
      throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("busitype", "busitypehint-000002", null, new String[] {
            billtype
          })/*�����������billtype={0}Ӧ��Ϊ��������*/);
    if (!StringUtil.isEmptyWithTrim(transtype)) {
      if (!PfUtilBaseTools.isTranstype(transtype))
        throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("busitype", "busitypehint-000003", null, new String[] {
              transtype
            })/*�����������transtype={0}Ӧ��Ϊ��������*/);
      if (!PfUtilBaseTools.getRealBilltype(transtype).equals(billtype))
        throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("busitype", "busitypehint-000004", null, new String[] {
              transtype, billtype
            })/*����������󣺽�������={0}�ĵ������Ͳ�����{1}*/);
    }
  }

  /**
   * ��ѯĳ���û���ĳ�����ݻ������ͣ���ĳ��֯�� ��������ҵ������
   * 
   * @param billtype
   * @param transtype
   * @param pk_org
   * @param userId
   * @return
   * @throws BusinessException
   */
  public String retBusitypeCanStart(String billtype, String transtype,
      String pk_org, String userId) throws BusinessException {
    // ����������ȷ��
    checkParams(billtype, transtype);

    boolean includeBillType = true; // FIXME::Ĭ���ǰ����������͵�

    // 1.��ѯ��ǰ���ݻ������ͣ�ĳ�û�����ġ�ĳ��֯������ҵ������
    String pk_group = InvocationInfoProxy.getInstance().getGroupId();
    ArrayList<String> alBusitypes =
        findMatchestBusitype(billtype, transtype, pk_group, pk_org, userId,
            includeBillType);

    // 2. �������֯���Ҳ������ٰ���֯ȥ����
    if (alBusitypes.size() == 0 && !StringUtil.isEmptyWithTrim(pk_org)) {
      alBusitypes =
          findMatchestBusitype(billtype, transtype, pk_group, null, userId,
              includeBillType);
    }

    if (alBusitypes == null || alBusitypes.size() == 0) {
      return null; // asked by fanjh
      // throw new PFBusinessException("�û�" + userId + "����֯" + pk_org +
      // "���Ҳ�����������ҵ������");
    }
    // else if (alBusitypesCanStart.size() > 1) {
    // UserVO user = (UserVO)new BaseDAO().retrieveByPK(UserVO.class,
    // userId);
    // String username = user==null? userId:user.getUser_name();
    // OrgVO org = (OrgVO)new BaseDAO().retrieveByPK(OrgVO.class, pk_org);
    // String orgname = org==null? pk_org: org.getName();
    // String busiNames =generateExpInfo(alBusitypesCanStart);
    // throw new PFBusinessException("�û�" + username + "����֯" + orgname +
    // "���ж����������ҵ������:"+busiNames);
    // }
    // �������ȼ���ߵ�ҵ�����̣�����ж�����ȼ���ͬ�Ķ��壬���������
    return alBusitypes.get(0);
  }

  /**
   * �õ��������ʱ�򷵻���Ϣ�а�����������
   * 
   * @return String
   * */
  private String generateExpInfo(ArrayList<String> alBusitypesCanStart)
      throws DAOException {
    String inBusitype = getInBusitype(alBusitypesCanStart);
    Collection<BusitypeVO> busitypevos =
        new BaseDAO().retrieveByClause(BusitypeVO.class, "pk_busitype in("
            + inBusitype + ")");
    StringBuffer sb = new StringBuffer("\n");
    for (BusitypeVO vo : busitypevos) {
      sb.append(vo.getBusiname() + "\n");
    }
    return sb.toString();
  }

  /**
   * ������ƥ������̣�Ӧ��ֻ���ҳ�һ��
   * 
   * @param billtype
   * @param transtype
   * @param pk_group
   * @param pk_org
   * @param userId
   * @param includeBillType
   * @return
   * @throws BusinessException
   */
  private ArrayList<String> findMatchestBusitype(String billtype,
      String transtype, String pk_group, String pk_org, String userId,
      boolean includeBillType) throws BusinessException {

    String sql_partner =
        "select busitype from pub_billflow_partner a inner join bd_busitype b on a.busitype=b.pk_busitype where b.validity ="
            + WorkflowDefStatusEnum.Valid.getIntValue()
            + " and b.pk_group ='"
            + pk_group + "' and a.billtype='" + billtype + "'";

    String sql_partner_trans = sql_partner;
    String sql_primarybill = "";
    boolean hasTranstype = false;
    if (!StringUtil.isEmptyWithTrim(transtype)) {
      hasTranstype = true;
      sql_primarybill = " and b.primarybilltype='" + transtype + "'";
      sql_partner_trans =
          sql_partner + " and a.transtype='" + transtype + "'"
              + sql_primarybill;
    }
    else {
      sql_primarybill = " and b.primarybilltype='" + billtype + "'";
      sql_partner_trans =
          sql_partner + " and isnull(a.transtype,'~')='~'" + sql_primarybill;
    }
    //��Ϊ������֯ƥ��
    String sql_org = "";
    if (!StringUtil.isEmptyWithTrim(pk_org)) {
      sql_org = " and exists (select 1 from pub_billflow_org bo where bo.pk_busitype = b.pk_busitype and bo.pk_org = '" + pk_org + "') ";
    }
    else {
      sql_org = " and isnull(b.orgcount, 0) = 0 ";
    }

    String sql_order = " order by b.priority desc";

    // 1. ������ϸ��
    ArrayList<String> alBusitypes = new ArrayList<String>();
    alBusitypes =
        findMatchestBusitypeWithParnter(billtype, transtype, pk_group, pk_org,
            userId, includeBillType);

    // 2.�������˲����ߵ�������û���ҵ������������û�����ò����ߵ�����
    String sql_nopartner = "";
    if (alBusitypes.size() == 0) {
      sql_nopartner =
          " select pk_businesstype from pub_billbusiness a inner join bd_busitype b on a.pk_businesstype=b.pk_busitype where b.validity = "
              + WorkflowDefStatusEnum.Valid.getIntValue()
              + " and b.pk_group ='"
              + pk_group
              + "' and a.pk_billtype='"
              + billtype
              + "' and a.pk_businesstype not in ("
              + sql_partner_trans + ")";
      String sql_nopartner_trans = sql_nopartner;
      if (hasTranstype) {
        // ����н������ͣ�Ҫ���ݽ���������
        sql_nopartner_trans =
            sql_nopartner_trans + " and a.transtype='" + transtype + "'";
      }
      else {
        sql_nopartner_trans =
            sql_nopartner_trans + " and isnull(a.transtype,'~')='~'";
      }
      // ���Ϻ��ĵ�������
      sql_nopartner_trans =
          sql_nopartner_trans + sql_primarybill + sql_org + sql_order;
      alBusitypes =
          (ArrayList<String>) new BaseDAO().executeQuery(sql_nopartner_trans,
              new ColumnListProcessor());
    }

    // 3. ��������˽��������Ҳ鲻�������ٸ��ݵ������Ͳ�
    if (alBusitypes.size() == 0 && hasTranstype) {
      alBusitypes =
          findMatchestBusitype(billtype, null, pk_group, pk_org, userId,
              includeBillType);
    }

    return alBusitypes;
  }

  /**
   * �������˲����ߵ������в��ҷ���������ҵ����
   * 
   * @param isSearchBilltype
   *          �Ƿ���ҵ��������ϵ�ҵ����
   * @return
   * */
  private ArrayList<String> findMatchestBusitypeWithParnter(String billtype,
      String transtype, String pk_group, String pk_org, String userId,
      boolean includeBillType) throws BusinessException, DAOException {
    ArrayList<String> alBusitypes = new ArrayList<String>();
    String sql_partner =
        "select busitype from pub_billflow_partner a inner join bd_busitype b on a.busitype=b.pk_busitype where b.validity = "
            + WorkflowDefStatusEnum.Valid.getIntValue()
            + " and b.pk_group ='"
            + pk_group + "' and a.billtype='" + billtype + "'";

    String sql_partner_trans = sql_partner;
    if (!StringUtil.isEmptyWithTrim(transtype)) {
      sql_partner_trans =
          sql_partner + " and a.transtype='" + transtype
              + "' and b.primarybilltype='" + transtype + "'";
    }
    else {
      sql_partner_trans =
          sql_partner
              + " and isnull(a.transtype,'~')='~' and b.primarybilltype='"
              + billtype + "'";
    }

    String sql_order = " order by b.priority desc";

    // 1.����userid���ҡ�
    String sql_partner_user = " and a.parnter_pk in('" + userId + "') ";
    String sql1 = sql_partner_trans + sql_partner_user + sql_order;
    alBusitypes =
        (ArrayList<String>) new BaseDAO().executeQuery(sql1,
            new ColumnListProcessor());

    // 2.���Ҳ�����������û����ڵ��û������
    if (alBusitypes == null || alBusitypes.size() == 0) {
      String userGrpId = getUserGroupSqlWithComma(userId);
      if (!StringUtil.isEmptyWithTrim(userGrpId)) {
        String groupid = userGrpId.substring(1);
        String sql_partner_usergroup = " and a.parnter_pk in(" + groupid + ") ";
        String sql2 = sql_partner_trans + sql_partner_usergroup + sql_order;
        alBusitypes =
            (ArrayList<String>) new BaseDAO().executeQuery(sql2,
                new ColumnListProcessor());
      }
    }
    // 3.�����û����ڵĽ�ɫ����
    if (alBusitypes == null || alBusitypes.size() == 0) {
      String inRole = getRoleSqlWithComma(userId);
      if (!StringUtil.isEmptyWithTrim(inRole)) {
        String role = inRole.substring(1);
        String sql_partner_role = " and a.parnter_pk in(" + role + ") ";
        String sql3 = sql_partner_trans + sql_partner_role + sql_order;
        alBusitypes =
            (ArrayList<String>) new BaseDAO().executeQuery(sql3,
                new ColumnListProcessor());
      }
    }

    // 4.�����û����ڵĽ�ɫ����ң��Ƿ���ҵ������
    if (alBusitypes == null || alBusitypes.size() == 0) {
      String roleGrp = getRoleGroupSqlWithComma(userId);
      if (!StringUtil.isEmptyWithTrim(roleGrp)) {
        String rolegroupid = roleGrp.substring(1);
        String role_parnter_rolegroup =
            " and a.parnter_pk in(" + rolegroupid + ") ";
        String sql4 = sql_partner_trans + role_parnter_rolegroup + sql_order;
        alBusitypes =
            (ArrayList<String>) new BaseDAO().executeQuery(sql4,
                new ColumnListProcessor());
      }
    }

    return alBusitypes;
  }

	private String getUserGroupSqlWithComma(String userId) throws BusinessException {
		IUserManageQuery umQuery = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserGroupVO[] users = umQuery.queryUserGroupByUser(userId);
		StringBuffer inUser = new StringBuffer();
		if (users != null && users.length != 0) {
			for (int i = 0; i < users.length; i++) {
				inUser.append(",'");
				inUser.append(users[i].getPrimaryKey());
				inUser.append("'");
			}
			// inUser = inUser.substring(1);
		}
		return inUser.toString();
	}

	private String getRoleGroupSqlWithComma(String userId) throws BusinessException {
		IRoleManageQuery roleQuery = (IRoleManageQuery) NCLocator.getInstance()
				.lookup(IRoleManageQuery.class.getName());
		RoleGroupVO[] rolegourpVOs = roleQuery.queryRoleGroupByUserId(userId);
		StringBuffer inRoles = new StringBuffer();
		if (rolegourpVOs != null && rolegourpVOs.length != 0) {
			for (int i = 0; i < rolegourpVOs.length; i++) {
				inRoles.append(",'");
				inRoles.append(rolegourpVOs[i].getPrimaryKey());
				inRoles.append("'");
			}
			// inRoles = inRoles.substring(1);
		}
		return inRoles.toString();
	}

	private String getRoleSqlWithComma(String userId) throws RbacException, PFBusinessException {
		// 1.��ѯ��ǰ���ݻ������ͣ�ĳ�û�����ġ�ĳ��֯������ҵ������
		IRoleManageQuery rmQry = NCLocator.getInstance().lookup(IRoleManageQuery.class);
		RoleVO[] roles = rmQry.queryRoleByUserID(userId, null);
		StringBuffer inRole = new StringBuffer();
		if (roles != null && roles.length != 0) {
			for (int i = 0; i < roles.length; i++) {
				inRole.append(",'");
				inRole.append(roles[i].getPrimaryKey());
				inRole.append("'");
			}
			// inRole = inRole.substring(1);
		}
		return inRole.toString();
	}

  private ArrayList<String> canMakeBillBusi(String billtype, String transtype,
      ArrayList<String> alBusitypes, boolean includeBillType)
      throws DAOException {
    String inBusitype = getInBusitype(alBusitypes);
    String billtypeWhere4Billbusi = "";
    if (StringUtil.isEmptyWithTrim(transtype)) {
      // ���ݵ������� ��ѯ��������
      billtypeWhere4Billbusi = "pk_billtype='" + billtype + "'";
    }
    else {
      // ����Ľ������ͷǿ�
      if (includeBillType) {
        // ���ݵ��ݻ������� ��ѯ��������
        billtypeWhere4Billbusi =
            "(pk_billtype='" + billtype + "' or transtype='" + transtype + "')";
      }
      else {
        // ���ݽ������� ��ѯ��������
        billtypeWhere4Billbusi = "transtype='" + transtype + "'";
      }
    }

    // 2.��ѯ��ǰ���ݻ������ͣ��Ƿ������С����ơ�
    // 3.���ȼ��Ӹߵ��ͽ�������
    String sql_billbusi =
        "select pk_businesstype from pub_billbusiness ,bd_busitype b where b.pk_busitype =pk_businesstype and makebillflag='Y' and "
            + billtypeWhere4Billbusi
            + " and pk_businesstype in("
            + inBusitype
            + ") order by b.priority desc";

    Object ret =
        new BaseDAO().executeQuery(sql_billbusi, new ColumnListProcessor());
    if (ret == null)
      return null;

    return (ArrayList<String>) ret;
  }

  public String[] queryBusitypeNotConsiderTrans(String billType, String pk_org,
      String userId) throws BusinessException, DbException {
	//��Ϊ����֯ƥ��
	String orgSQL = " exists (select 1 from pub_billflow_org bo where bo.pk_busitype = a.pk_busitype and bo.pk_org = ?)";
    String sql =
        "select distinct a.pk_busitype from bd_busitype a left outer join pub_billflow_partner b on a.pk_busitype = b.busitype and (a.primarybilltype=b.billtype or  b.transtype= a.primarybilltype ) "
            + " left outer join bd_billtype billtype on billtype.pk_billtypecode=a.primarybilltype "
            + " where a.pk_group=? and " 
            + " ( " + orgSQL + " or a.orgcount = 0) and (a.primarybilltype =?  or billtype.parentbilltype =? ) and a.validity= "
            + WorkflowDefStatusEnum.Valid.getIntValue() + "  and ";
    
    String inRole = getRoleSqlWithComma(userId);
    String inUser = "'" + userId + "'";
    String inUserGrp = getUserGroupSqlWithComma(userId);
    String inRoleGrp = getRoleGroupSqlWithComma(userId);
    String clause = inUser + inRole + inUserGrp + inRoleGrp;
    String sqlpartner =
        " (( b.parnter_pk in("
            + clause
            + ")) or a.pk_busitype not in (select busitype from pub_billflow_partner where billtype=?)) ";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      // ���ò���
      para.addParam(InvocationInfoProxy.getInstance().getGroupId()); // ��˾����
      para.addParam(pk_org);
      para.addParam(billType); // ��������
      para.addParam(billType);
      para.addParam(billType); // ��������
      List list =
          (List) jdbc.executeQuery(sql + sqlpartner, para,
              new ColumnListProcessor(1));
      String[] sRet = null;
      if (list != null && list.size() > 0) {
        sRet = new String[list.size()];
        list.toArray(sRet);
      }
      return sRet;
    }
    finally {
      if (persist != null)
        persist.release();
    }
  }

  /**
   * @param pk_group
   * @param userid
   * @param src_billtype
   * @param dest_billtype
   * @param dest_transtype
   * @return
   * @throws BusinessException
   */
  public String[] getBillpksByBillsourceConfig(String pk_group, String userid,
      String src_billtype, String dest_billtype, String dest_transtype)
      throws BusinessException {

    String inRole = getRoleSqlWithComma(userid);
    String inUser = "'" + userid + "'";
    String inUserGrp = getUserGroupSqlWithComma(userid);
    String inRoleGrp = getRoleGroupSqlWithComma(userid);
    String clause = inUser + inRole + inUserGrp + inRoleGrp;

    String sql =
        " select referbilltype from pub_billsource , (select distinct busitype from pub_billflow_partner where  parnter_pk in("
            + clause
            + ")) temp where pk_billtype = ? and pk_businesstype = temp.busitype and referbilltype in (select pk_billtypecode from bd_billtype where pk_billtypecode = ? or parentbilltype = ?) and exists (select 1 from bd_busitype where pk_busitype = pub_billsource.pk_businesstype and pk_group = ? ) ";
    SQLParameter sp = new SQLParameter();
    sp.addParam(userid);
    sp.addParam(StringUtil.isEmptyWithTrim(dest_transtype) ? dest_billtype
        : dest_transtype);
    sp.addParam(src_billtype);
    sp.addParam(src_billtype);
    sp.addParam(pk_group);
    ArrayList<String> alSrcTypes =
        (ArrayList<String>) new BaseDAO().executeQuery(sql, sp,
            new ColumnListProcessor());
    alSrcTypes.remove(src_billtype);
    return alSrcTypes.toArray(new String[0]);
  }

  /**
   * @param pk_group
   * @param pk_billtype
   * @param userid
   * @return
   * @throws BusinessException
   */
  public String[] getBilltypeByBillsourceConfig(String pk_group,
      String pk_billtype, String userid) throws BusinessException {
    String inRole = getRoleSqlWithComma(userid);
    String inUser = "'" + userid + "'";
    String inUserGrp = getUserGroupSqlWithComma(userid);
    String inRoleGrp = getRoleGroupSqlWithComma(userid);
    String clause = inUser + inRole + inUserGrp + inRoleGrp;

    String sql =
        " select referbilltype from pub_billsource , (select distinct busitype from pub_billflow_partner where parnter_pk in("
            + clause
            + ") ) temp where pk_billtype = ? and pk_businesstype = temp.busitype and exists (select 1 from bd_busitype where pk_busitype = pub_billsource.pk_businesstype and pk_group = ? ) ";
    SQLParameter sp = new SQLParameter();
    sp.addParam(pk_billtype);
    sp.addParam(pk_group);
    ArrayList<String> alSrcTypes =
        (ArrayList<String>) new BaseDAO().executeQuery(sql, sp,
            new ColumnListProcessor());
    return alSrcTypes.toArray(new String[0]);
  }

	/**
	 * @param billtype
	 * @param transtype
	 * @param pk_org
	 * @param userIds
	 * @return
	 * @throws BusinessException
	 * @throws DbException
	 */
	public boolean existDuplicateBusiFlow(String billtype, String transtype,
			String[] pk_orgs, String pk_busitype) throws BusinessException {
		boolean isDuplicate = false;
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();

		String billbusinessSql = "select pk_busitype from bd_busitype where validity= "
				+ WorkflowDefStatusEnum.Valid.getIntValue()
				+ " and primarybilltype=? and pk_group =? ";
		SQLParameter sp = new SQLParameter();
		if (StringUtil.isEmptyWithTrim(transtype))
			sp.addParam(billtype);
		else
			sp.addParam(transtype);
		sp.addParam(pk_group);

		// ����֯ƥ�� add by zhangrui
		int orgCount = ArrayUtil.getLength(pk_orgs);
		billbusinessSql += " and orgcount = " + orgCount;
		if (orgCount > 0) {
			// ��������㣬��ѡ����֯����ǰ��ȫƥ��
			// ��֯��ȫƥ�䣬����ζ�ţ�org������ȣ��Ҳ����������ļ�¼��pk_org���ڵ�ǰpk_org������
			billbusinessSql += " and not exists (select 1 from pub_billflow_org bo " +
					" where bo.pk_busitype = bd_busitype.pk_busitype and not "
					+ SQLUtil.buildSqlForIn("bo.pk_org", pk_orgs) + ")";
		}

		// ���ĵ�������
		ArrayList<String> lResult = (ArrayList<String>) new BaseDAO()
				.executeQuery(billbusinessSql, sp, new ColumnListProcessor());

		// �Ҳ������������ģ�˵�����ظ���ֱ�ӷ���
		if (lResult == null || lResult.size() == 0)
			return isDuplicate;
		else if (!lResult.contains(pk_busitype)) {
			// ��������
			lResult.add(pk_busitype);
		}
		if (lResult.size() == 1 && lResult.get(0).equals(pk_busitype))
			// �ų����Լ�
			return isDuplicate;

		// �ж��Ƿ������Ҳ��ͬ
		String inBusitype = getInBusitype(lResult);
		// Ҫ�����Լ�
		String flowParnterSql = "select * from pub_billflow_partner where busitype in("
				+ inBusitype + ") and billtype =? ";
		SQLParameter sp2 = new SQLParameter();
		sp2.addParam(billtype);
		if (!StringUtil.isEmptyWithTrim(transtype)) {
			flowParnterSql += " and transtype = ? ";
			sp2.addParam(transtype);
		}

		List<BillFlowParnterVO> lParnter = (List<BillFlowParnterVO>) new BaseDAO()
				.executeQuery(flowParnterSql, sp2, new BeanListProcessor(
						BillFlowParnterVO.class));
		if (lParnter == null || lParnter.size() == 0) {
			// ˵����û��������ߣ���������3Ҫ����ͬ�����ظ�
			isDuplicate = true;
		} else if (lParnter.size() >= 1) {
			Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			for (BillFlowParnterVO vo : lParnter) {
				String key = vo.getBusitype();
				if (map.get(key) == null) {
					map.put(key, new ArrayList<String>());
				}
				map.get(key).add(vo.getParnter_pk());
			}
			ArrayList<String> useridArray = map.get(pk_busitype);
			// ��ǰ����Ϊ���ò����ߡ�
			if (useridArray == null && lResult.size() > map.keySet().size()) {
				return true;
			}
			for (String key : map.keySet()) {
				if (key.equals(pk_busitype))
					continue;
				ArrayList<String> parnters = map.get(key);
				if (parnters.equals(useridArray)) {
					isDuplicate = true;
					break;
				}
			}
		} else {
			isDuplicate = false;
		}
		return isDuplicate;

	}

  /**
   * ���ð汾�� ��ͬ������ҵ�����ı�������Ʊ���Ψһ�����������ͬ��������Ƶ�ҵ�����������һ�����̵Ĳ�ͬ�汾
   * 
   * @param busicode ҵ��������
   * @param businame ҵ��������
   * @throws BusinessException
   * */
  public String getMaxVersion(String busicode, String businame)
      throws BusinessException {
    String maxVersion = "1.0";
    String pk_group = InvocationInfoProxy.getInstance().getGroupId();
    BaseDAO dao = new BaseDAO();
    String maxVersion_sql =
        "select max(version) from bd_busitype where busicode ='" + busicode
            + "'and businame ='" + businame + "'";
    Object version = dao.executeQuery(maxVersion_sql, new BaseProcessor() {

      @Override
      public Object processResultSet(ResultSet rs) throws SQLException {
        if (rs.next()) {
          String version = rs.getString(1);
          return version;
        }
        return null;
      }

    });

    if (version != null) {
      version =
          String.valueOf(new BigDecimal(version.toString()).add(new BigDecimal(
              "0.1")));
      maxVersion = version.toString();
    }
    else {
      maxVersion = "1.0";
    }
    return maxVersion;
  }

}

class ConstirctProcessor extends BaseProcessor {
  public Object processResultSet(ResultSet rs) throws SQLException {
    ArrayList<PfUtilActionConstrictVO> al =
        new ArrayList<PfUtilActionConstrictVO>();
    while (rs.next()) {
      PfUtilActionConstrictVO actionConstrict = new PfUtilActionConstrictVO();
      String tmpString = null;

      int configFlag = rs.getInt("configflag");
      actionConstrict.setConfigFlag(configFlag);

      tmpString = rs.getString("operator");
      if (tmpString != null) {
        actionConstrict.setRelaPk(tmpString.trim());
      }
      else {
        actionConstrict.setRelaPk(null);
      }

      // ������
      tmpString = rs.getString("aclassname");
      if (tmpString == null) {
        actionConstrict.setFunClassName(null);
      }
      else {
        actionConstrict.setFunClassName(tmpString.trim());
      }
      // ��������
      tmpString = rs.getString("areturntype");
      if (tmpString == null) {
        actionConstrict.setFunReturnType(null);
      }
      else {
        actionConstrict.setFunReturnType(tmpString.trim());
      }
      // ����ժҪ
      tmpString = rs.getString("afunctionnote");
      if (tmpString == null) {
        actionConstrict.setFunNote(null);
      }
      else {
        actionConstrict.setFunNote(tmpString.trim());
      }
      // ��������
      tmpString = rs.getString("amethod");
      if (tmpString == null) {
        actionConstrict.setMethod(null);
      }
      else {
        actionConstrict.setMethod(tmpString.trim());
      }
      // ��������
      tmpString = rs.getString("aparameter");
      if (tmpString == null) {
        actionConstrict.setParameter(null);
      }
      else {
        actionConstrict.setParameter(tmpString.trim());
      }
      // �����
      tmpString = rs.getString("ysf");
      if (tmpString == null) {
        actionConstrict.setYsf(null);
      }
      else {
        actionConstrict.setYsf(tmpString.trim());
      }
      // ֵ
      tmpString = rs.getString("value");
      if (tmpString == null) {
        actionConstrict.setValue(null);
      }
      else {
        actionConstrict.setValue(tmpString.trim());
      }
      // ������
      tmpString = rs.getString("bclassname");
      if (tmpString == null) {
        actionConstrict.setClassName2(null);
      }
      else {
        actionConstrict.setClassName2(tmpString.trim());
      }
      // ����ժҪ
      tmpString = rs.getString("bfunctionnote");
      if (tmpString == null) {
        actionConstrict.setFunNote2(null);
      }
      else {
        actionConstrict.setFunNote2(tmpString.trim());
      }
      // ��������
      tmpString = rs.getString("bmethod");
      if (tmpString == null) {
        actionConstrict.setMethod2(null);
      }
      else {
        actionConstrict.setMethod2(tmpString.trim());
      }
      // ��������
      tmpString = rs.getString("bparameter");
      if (tmpString == null) {
        actionConstrict.setParameter2(null);
      }
      else {
        actionConstrict.setParameter2(tmpString.trim());
      }
      // added by leijun 20040308
      tmpString = rs.getString("errhintmsg");
      if (tmpString == null) {
        actionConstrict.setErrHintMsg(null);
      }
      else {
        actionConstrict.setErrHintMsg(tmpString.trim());
      }
      // added by leijun 20070426
      tmpString = rs.getString("isbefore");
      if (tmpString == null) {
        actionConstrict.setIsBefore(ActionconstrictVO.TYPE_BEFORE);
      }
      else {
        actionConstrict.setIsBefore(tmpString);
      }

      al.add(actionConstrict);
    }
    return al;
  }
}
