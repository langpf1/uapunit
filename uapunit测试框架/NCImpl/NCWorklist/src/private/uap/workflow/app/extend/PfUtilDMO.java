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
 * 流程平台DMO类
 * 
 * @author fangj 2001-10-8 16:04:02
 * @modifier leijun 2005-10-10 适配到NC5
 */
public class PfUtilDMO {

  public PfUtilDMO() {
    super();
  }

  /**
   * 查找该动作是否该单据的结束动作
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
   * 通过单据类型、业务类型和动作、公司Id、操作人的查询动作前约束数组 <li>操作人或组无关，则返回该纪录。 <li>
   * 操作人有关，则返回跟该操作人有关的约束条件。 <li>组有关，则返回该操作人的组有关的约束条件。
   * 
   * @param billType
   * @param businessType
   * @param actionName
   * @param corpId
   * @param operator
   * @return
   * @throws DbException
   * 
   * @modifier 雷军 2004-03-11 增加获取一个字段 errhintmsg
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
      // 业务类型,封存业务类型不起作用
      sql += " and c.pk_businesstype='" + businessType + "'";
    }
    sql +=
        " and (configflag="
            + IPFConfigInfo.UserNoRelation// 和操作员无关
            + " or (configflag="
            + IPFConfigInfo.UserRelation
            + " and operator=?) "// 和操作员有关
            + " or (configflag="
            + IPFConfigInfo.RoleRelation// 和角色有关,需要增加用户在公司中角色查询
            + " and operator in(select pk_role from sm_user_role where cuserid=?)) "
            + " ) order by c.sysindex";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();

      // 设置参数
      // para.addParam(corpId); //公司Id
      para.addParam(billType); // 单据类型
      para.addParam(actionName); // 动作类型

      para.addParam(operator); // 操作员
      para.addParam(operator); // 和角色有关的操作员
      // para.addParam(corpId); //和角色有关的公司Id
      List lResult =
          (List) jdbc.executeQuery(sql, para, new ConstirctProcessor());

      if (PfUtilBaseTools.isTranstype(billType)) {
        // 交易类型没找到，找单据类型
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
   * 查询单据的动作执行前的提示语句
   * 
   * @param billType
   *          单据类型PK
   * @param actionType
   *          动作名称
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
      // 单据类型（无交易类型）配置的上游
    }
    else {// 交易类型
      if (includeBillType) {
        strField =
            "((a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype='" + billOrTranstype + "')"; // 单据类型+交易类型配置的上游
        strField +=
            " or (a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype = '~')"; // 单据类型（无交易类型）配置的上游
      }
      else {
        strField =
            "(a.pk_billtype='"
                + PfUtilBaseTools.getRealBilltype(billOrTranstype)
                + "' and a.transtype='" + billOrTranstype + "')"; // 单据类型+交易类型配置的上游
      }

    }
    return strField;
  }

  /**
   * 根据公司Id,单据类型(或交易类型)及业务类型查找该单据类型的来源
   * 
   * <li>如果是自制单据，也额外作为一个VO返回 <li>也从集团获取流程配置的来源单据lj+
   * 
   * @param groupId
   *          公司PK
   * @param billOrTranstype
   *          当前单据类型(或交易类型)PK
   * @param businessType
   *          业务类型PK
   * @return
   * @throws DbException
   */
  public BillbusinessVO[] queryBillSource(final String groupId,
      final String billOrTranstype, String businessType, boolean includeBillType)
      throws DbException {
    // 需要判定传入的是单据类型还是交易类型
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

      // 设置参数
      para.addParam(groupId); // 公司编码
      if (businessType != null)
        para.addParam(businessType); // 单据类型

      List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
        public Object processResultSet(ResultSet rs) throws SQLException {
          ArrayList<BillbusinessVO> al = new ArrayList<BillbusinessVO>();

          if (!rs.next())
            return al;
          BillbusinessVO referVo = null;
          if (rs.getString("makebillflag").equals("Y")) {
            referVo = new BillbusinessVO();
            referVo.setBilltypename(NCLangRes4VoTransl.getNCLangRes()
                .getStrByID("busitype", "busitypehint-000000")/*自制单据*/);
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
              // XXX:避免重复的来源单据
              if (!isAlreadyExist(al, referVo.getPk_billtype()))
                al.add(referVo);
            }
          }
          return al;
        }

        /**
         * 判定来源单据是否已经存在
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
   * 查询某业务流程中某单据类型(或交易类型)的下游单据VO数组
   * 
   * @param billType
   *          单据类型(或交易类型)PK
   * @param busiType
   *          业务类型PK，如果为空，则返回所有流程配置中该单据的下游单据
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
      // 如果是交易类型
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
   * 根据公司PK,单据类型(或交易类型)PK查找配置过的业务类型 <li>也返回集团配置的业务类型,集团配置的业务类型排在前面 <li>
   * 而且，业务类型按照编码排序
   * 
   * @param groupId
   * @param billType
   *          单据类型或交易类型
   * @return
   * @throws DbException
   */
  public BusitypeVO[] queryBillBusinessType(String groupId, String billType)
      throws DbException {
    // XXX::需要判定传入的是单据类型还是交易类型
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

    // 周善保2006-4-7：由于sqlserver对于@@@@和1001等字符串排序的处理刚好和oracle相反，所以这种数据库的差异性导致不能使用排序，改为由程序处理排序
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(billType); // 单据类型
      para.addParam(groupId); // 集团编码
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

            // XXX:避免重复的业务类型
            if (!isAlreadyExist(al, btVO.getPrimaryKey()))
              al.add(btVO);
          }
          return al;
        }

        /**
         * 判定是否已经加入了该业务类型
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
        });// 按照pk_corp降序排，保证@@@@排在前面，不能使用数据库的排序
      }
      return voRet;
    }
    finally {
      if (persist != null)
        persist.release();
    }

  }

  /**
   * 通过单据类型、业务类型、公司Id、操作员查询单据驱动的动作 的所有记录VO数组
   * 
   * @param billType
   * @param businessType
   * @param groupId
   * @param sourceAction
   * @param operator
   * @return
   * @throws DbException
   * @修改者：周善保20060430，增加操作员参数，直接过滤出符合操作员的动作驱动
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
      // // 封存业务类型不起作用
      // sql +=
      // " and exists (select 1 from bd_busitype where bd_busitype.pk_busitype = pub_messagedrive.pk_businesstype and validity = "
      // + WorkflowDefStatusEnum.Valid.getIntValue() + " ) ";
    }

    sql +=
        " and ( configflag="
            + IPFConfigInfo.UserNoRelation// 和操作员无关
            + " or (configflag="
            + IPFConfigInfo.UserRelation
            + " and operator=?) "// 和操作员有关
            + " or (configflag="
            + IPFConfigInfo.RoleRelation// 和角色有关,需要增加用户在公司中角色查询
            + "     and operator in(select pk_role from sm_user_role where cuserid=? )) "
            + " )order by sysindex desc ,pk_billtype";
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      // 设置参数
      // para.addParam(groupId);
      para.addParam(billType);
      para.addParam(sourceAction);
      if (businessType != null) {
        para.addParam(businessType); // 业务类型
      }
      para.addParam(operator); // 操作员
      para.addParam(operator); // 和角色有关的操作员
      // para.addParam(groupId); //和角色有关的公司Id

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

            // 驱动单据类型
            tmpString = rs.getString("pk_billtype");
            if (tmpString == null) {
              action.setBillType(null);
            }
            else {
              action.setBillType(tmpString.trim());
            }
            // 驱动单据业务类型
            tmpString = rs.getString("pk_sourcebusinesstype");
            if (tmpString == null) {
              action.setBusinessType(null);
            }
            else {
              action.setBusinessType(tmpString.trim());
            }
            // 驱动动作类型
            tmpString = rs.getString("actiontype");
            if (tmpString == null) {
              action.setActionName(null);
            }
            else {
              action.setActionName(tmpString.trim());
            }
            // 发起驱动的类型
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
   * 查询当前业务类型是否包括指定的单据类型数组
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
          // 查找包含采购订单的业务类型
          while (rs.next()) {
            if (arriveVo == null) {
              arriveVo = new PfPOArriveVO();
            }
            String tmpString = rs.getString(1);
            // FIXME::这么处理，太死板了？
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

		// XXX:要区分传入的是单据类型还是交易类型
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
   * 根据公司PK,单据类型(或交易类型)PK查找没有进行权限控制的业务类型 <li>也返回集团配置的业务类型
   * 
   * @param groupId
   * @param billType
   *          单据类型(或交易类型)PK
   * @return
   * @throws DbException
   * @author 周善保2006-03-29
   */
  public String[] queryBusitypeOfNoControl(String groupId, String billType)
      throws DbException {
    // XXX:要区分传入的是单据类型还是交易类型
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
      // 设置参数
      para.addParam(groupId); // 公司编码
      para.addParam(billType); // 单据类型
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
   * 返回某个业务类型下的所有流程配置VO数组 <li>包括一些名称信息
   * 
   * @param biztype
   *          业务类型pK
   * @param pk_group
   *          集团PK
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
   * 查询某个业务类型下指定单据类型或交易类型的目的业务流程
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
   * 返回某个单据或交易类型可使用的“新增”下拉菜单信息 <li>包括自制、来源单据
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
    // 检查参数的正确性
    checkParams(billtype, transtype);
    // 1.查询当前单据或交易类型，某用户参与的、某组织的所有业务流程
    ArrayList<String> alBusitypes =
        findRelatedBusitype(billtype, transtype, pk_group, null, userId,
            includeBillType);
    if (alBusitypes == null || alBusitypes.size() == 0)
      return null;

    // 2.查询是否配置“自制”
    HashMap<String, PfAddInfo> hmRet = new HashMap<String, PfAddInfo>();
    ArrayList<String> alBusitypesMakebill =
        canMakeBillBusi(billtype, transtype, alBusitypes, includeBillType);
    if (alBusitypesMakebill != null && alBusitypesMakebill.size() > 0) {
      PfAddInfo ai = new PfAddInfo();
      ai.setMakeflag(true);
      hmRet.put("MAKEFLAG", ai);
    }

    // 3.查询当前单据或交易类型 的来源关系
    ArrayList alSrcTypes =
        findBillsrc(billtype, transtype, alBusitypes, includeBillType);

    // 忽略重复的来源单据
    for (Object srcType : alSrcTypes) {
      String[] strTypes = new String[((Object[]) srcType).length];
      System.arraycopy(srcType, 0, strTypes, 0, strTypes.length);
      String strRealBilltype = PfUtilBaseTools.getRealBilltype(strTypes[0]); // 只需单据类型
      if (!hmRet.containsKey(strRealBilltype)) {
        PfAddInfo ai = new PfAddInfo();
        // 来源单据类型
        ai.setSrc_billtype(strRealBilltype);
        BilltypeVO btVO =
            PfDataCache.getBillTypeInfo(new BillTypeCacheKey()
                .buildBilltype(strRealBilltype));
        // 来源单据类型名称
        ai.setSrc_billtypename(Pfi18nTools.i18nBilltypeName(strRealBilltype,
            btVO.getBilltypename()));
        // 业务流程
        ArrayList<String> busiTypes = new ArrayList<String>();
        busiTypes.add(strTypes[1]);
        ai.setBusitypes(busiTypes);
        // 交易类型
        ArrayList<String> transTypes = new ArrayList<String>();
        if (PfUtilBaseTools.isTranstype(strTypes[0])) {
          transTypes.add(strTypes[0]);
        }
        else {
          // 只配了单据类型，没有配交易类型
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
   * 查找能参与的所有流程，可以找出多个
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
      // 传入的交易类型非空
      if (includeBillType)
        sql +=
            " and ( a.transtype = '~' or a.transtype='" + transtype
                + "')";
      else
        sql += " and a.transtype='" + transtype + "'";
    }
    // 添加组织条件 modified by zhangrui 
    if(!StringUtil.isEmptyWithTrim(pk_org)) {
    	sql += " and exists (select 1 from pub_billflow_org bo where bo.pk_busitype = b.pk_busitype and bo.pk_org = '" + pk_org + "')";
    }

    // 除去那些 不能由该用户参与的，即：配了参与者，但是参与者中没有指定的用户
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

    // partner表里pk_org字段没用，遂去掉 modified by zhangrui 2012-3-30
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
      // 根据单据类型 查询参与的流程，也包括其子交易类型的
      whereSql =
          "(pk_billtype='"
              + billtype
              + "' or pk_billtype in(select pk_billtypecode from bd_billtype where istransaction='Y' and parentbilltype='"
              + billtype + "'))";
    }
    else {
      // 传入的交易类型非空
      if (includeBillType) {
        // 根据单据或交易类型 查询参与的流程
        whereSql =
            "(pk_billtype='" + transtype + "' or pk_billtype='" + billtype
                + "')";
      }
      else {
        // 根据交易类型 查询参与的流程
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
		// 在调用该方法的上下文中已经保证了alBusitypes != null && alBusityoes.size()>0
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
          .getStrByID("busitype", "busitypehint-000001")/*传入参数错误：billtype不可为空*/);
    if (PfUtilBaseTools.isTranstype(billtype))
      throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("busitype", "busitypehint-000002", null, new String[] {
            billtype
          })/*传入参数错误：billtype={0}应该为单据类型*/);
    if (!StringUtil.isEmptyWithTrim(transtype)) {
      if (!PfUtilBaseTools.isTranstype(transtype))
        throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("busitype", "busitypehint-000003", null, new String[] {
              transtype
            })/*传入参数错误：transtype={0}应该为交易类型*/);
      if (!PfUtilBaseTools.getRealBilltype(transtype).equals(billtype))
        throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("busitype", "busitypehint-000004", null, new String[] {
              transtype, billtype
            })/*传入参数错误：交易类型={0}的单据类型并不是{1}*/);
    }
  }

  /**
   * 查询某个用户对某个单据或交易类型，在某组织下 可启动的业务流程
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
    // 检查参数的正确性
    checkParams(billtype, transtype);

    boolean includeBillType = true; // FIXME::默认是包括单据类型的

    // 1.查询当前单据或交易类型，某用户参与的、某组织的所有业务流程
    String pk_group = InvocationInfoProxy.getInstance().getGroupId();
    ArrayList<String> alBusitypes =
        findMatchestBusitype(billtype, transtype, pk_group, pk_org, userId,
            includeBillType);

    // 2. 如果有组织且找不到，再把组织去掉查
    if (alBusitypes.size() == 0 && !StringUtil.isEmptyWithTrim(pk_org)) {
      alBusitypes =
          findMatchestBusitype(billtype, transtype, pk_group, null, userId,
              includeBillType);
    }

    if (alBusitypes == null || alBusitypes.size() == 0) {
      return null; // asked by fanjh
      // throw new PFBusinessException("用户" + userId + "在组织" + pk_org +
      // "下找不到可启动的业务流程");
    }
    // else if (alBusitypesCanStart.size() > 1) {
    // UserVO user = (UserVO)new BaseDAO().retrieveByPK(UserVO.class,
    // userId);
    // String username = user==null? userId:user.getUser_name();
    // OrgVO org = (OrgVO)new BaseDAO().retrieveByPK(OrgVO.class, pk_org);
    // String orgname = org==null? pk_org: org.getName();
    // String busiNames =generateExpInfo(alBusitypesCanStart);
    // throw new PFBusinessException("用户" + username + "在组织" + orgname +
    // "下有多个可启动的业务流程:"+busiNames);
    // }
    // 返回优先级最高的业务流程，如果有多个优先级相同的定义，则随机返回
    return alBusitypes.get(0);
  }

  /**
   * 得到多个流程时候返回信息中包含流程名称
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
   * 查找最匹配的流程，应该只能找出一个
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
    //改为按多组织匹配
    String sql_org = "";
    if (!StringUtil.isEmptyWithTrim(pk_org)) {
      sql_org = " and exists (select 1 from pub_billflow_org bo where bo.pk_busitype = b.pk_busitype and bo.pk_org = '" + pk_org + "') ";
    }
    else {
      sql_org = " and isnull(b.orgcount, 0) = 0 ";
    }

    String sql_order = " order by b.priority desc";

    // 1. 按最明细查
    ArrayList<String> alBusitypes = new ArrayList<String>();
    alBusitypes =
        findMatchestBusitypeWithParnter(billtype, transtype, pk_group, pk_org,
            userId, includeBillType);

    // 2.在配置了参与者的流程中没有找到，则继续查找没有配置参与者的流程
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
        // 如果有交易类型，要根据交易类型找
        sql_nopartner_trans =
            sql_nopartner_trans + " and a.transtype='" + transtype + "'";
      }
      else {
        sql_nopartner_trans =
            sql_nopartner_trans + " and isnull(a.transtype,'~')='~'";
      }
      // 加上核心单据条件
      sql_nopartner_trans =
          sql_nopartner_trans + sql_primarybill + sql_org + sql_order;
      alBusitypes =
          (ArrayList<String>) new BaseDAO().executeQuery(sql_nopartner_trans,
              new ColumnListProcessor());
    }

    // 3. 如果配置了交易类型且查不到，则再根据单据类型查
    if (alBusitypes.size() == 0 && hasTranstype) {
      alBusitypes =
          findMatchestBusitype(billtype, null, pk_group, pk_org, userId,
              includeBillType);
    }

    return alBusitypes;
  }

  /**
   * 在配置了参与者的流程中查找符合条件的业务流
   * 
   * @param isSearchBilltype
   *          是否查找单据类型上的业务流
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

    // 1.根据userid查找。
    String sql_partner_user = " and a.parnter_pk in('" + userId + "') ";
    String sql1 = sql_partner_trans + sql_partner_user + sql_order;
    alBusitypes =
        (ArrayList<String>) new BaseDAO().executeQuery(sql1,
            new ColumnListProcessor());

    // 2.查找不到，则根据用户所在的用户组查找
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
    // 3.根据用户所在的角色查找
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

    // 4.根据用户所在的角色组查找，是否有业务流程
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
		// 1.查询当前单据或交易类型，某用户参与的、某组织的所有业务流程
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
      // 根据单据类型 查询流程配置
      billtypeWhere4Billbusi = "pk_billtype='" + billtype + "'";
    }
    else {
      // 传入的交易类型非空
      if (includeBillType) {
        // 根据单据或交易类型 查询流程配置
        billtypeWhere4Billbusi =
            "(pk_billtype='" + billtype + "' or transtype='" + transtype + "')";
      }
      else {
        // 根据交易类型 查询流程配置
        billtypeWhere4Billbusi = "transtype='" + transtype + "'";
      }
    }

    // 2.查询当前单据或交易类型，是否配置有“自制”
    // 3.优先级从高到低进行排序
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
	//改为多组织匹配
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
      // 设置参数
      para.addParam(InvocationInfoProxy.getInstance().getGroupId()); // 公司编码
      para.addParam(pk_org);
      para.addParam(billType); // 单据类型
      para.addParam(billType);
      para.addParam(billType); // 单据类型
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

		// 多组织匹配 add by zhangrui
		int orgCount = ArrayUtil.getLength(pk_orgs);
		billbusinessSql += " and orgcount = " + orgCount;
		if (orgCount > 0) {
			// 如果大于零，则选出组织跟当前完全匹配
			// 组织完全匹配，则意味着：org个数相等，且不存在这样的记录：pk_org不在当前pk_org数组中
			billbusinessSql += " and not exists (select 1 from pub_billflow_org bo " +
					" where bo.pk_busitype = bd_busitype.pk_busitype and not "
					+ SQLUtil.buildSqlForIn("bo.pk_org", pk_orgs) + ")";
		}

		// 核心单据链表
		ArrayList<String> lResult = (ArrayList<String>) new BaseDAO()
				.executeQuery(billbusinessSql, sp, new ColumnListProcessor());

		// 找不到满足条件的，说明不重复，直接返回
		if (lResult == null || lResult.size() == 0)
			return isDuplicate;
		else if (!lResult.contains(pk_busitype)) {
			// 加上自身
			lResult.add(pk_busitype);
		}
		if (lResult.size() == 1 && lResult.get(0).equals(pk_busitype))
			// 排除掉自己
			return isDuplicate;

		// 判断是否参与者也相同
		String inBusitype = getInBusitype(lResult);
		// 要加上自己
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
			// 说明都没有配参与者，但是其它3要素相同，故重复
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
			// 当前流程为配置参与者。
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
   * 设置版本号 ，同集团下业务流的编码和名称必须唯一，如果存在相同编码和名称的业务流，则必是一个流程的不同版本
   * 
   * @param busicode 业务流编码
   * @param businame 业务流名称
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

      // 类名称
      tmpString = rs.getString("aclassname");
      if (tmpString == null) {
        actionConstrict.setFunClassName(null);
      }
      else {
        actionConstrict.setFunClassName(tmpString.trim());
      }
      // 返回类型
      tmpString = rs.getString("areturntype");
      if (tmpString == null) {
        actionConstrict.setFunReturnType(null);
      }
      else {
        actionConstrict.setFunReturnType(tmpString.trim());
      }
      // 功能摘要
      tmpString = rs.getString("afunctionnote");
      if (tmpString == null) {
        actionConstrict.setFunNote(null);
      }
      else {
        actionConstrict.setFunNote(tmpString.trim());
      }
      // 方法名称
      tmpString = rs.getString("amethod");
      if (tmpString == null) {
        actionConstrict.setMethod(null);
      }
      else {
        actionConstrict.setMethod(tmpString.trim());
      }
      // 参数名称
      tmpString = rs.getString("aparameter");
      if (tmpString == null) {
        actionConstrict.setParameter(null);
      }
      else {
        actionConstrict.setParameter(tmpString.trim());
      }
      // 运算符
      tmpString = rs.getString("ysf");
      if (tmpString == null) {
        actionConstrict.setYsf(null);
      }
      else {
        actionConstrict.setYsf(tmpString.trim());
      }
      // 值
      tmpString = rs.getString("value");
      if (tmpString == null) {
        actionConstrict.setValue(null);
      }
      else {
        actionConstrict.setValue(tmpString.trim());
      }
      // 类名称
      tmpString = rs.getString("bclassname");
      if (tmpString == null) {
        actionConstrict.setClassName2(null);
      }
      else {
        actionConstrict.setClassName2(tmpString.trim());
      }
      // 功能摘要
      tmpString = rs.getString("bfunctionnote");
      if (tmpString == null) {
        actionConstrict.setFunNote2(null);
      }
      else {
        actionConstrict.setFunNote2(tmpString.trim());
      }
      // 方法名称
      tmpString = rs.getString("bmethod");
      if (tmpString == null) {
        actionConstrict.setMethod2(null);
      }
      else {
        actionConstrict.setMethod2(tmpString.trim());
      }
      // 参数名称
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
