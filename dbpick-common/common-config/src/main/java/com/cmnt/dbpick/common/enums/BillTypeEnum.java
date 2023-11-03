package com.cmnt.dbpick.common.enums;

public enum BillTypeEnum {

  BILL_TYPE_INCOME (1,"订单收入"),
  BILL_TYPE_EXPENDITURE (2,"订单支出-钱包账户"),
  BILL_TYPE_REFUND(3,"订单退款-钱包账户"),
  BILL_TYPE_RECHARGE (4,"微信充值"),
  BILL_TYPE_TRANSFER(5,"提现-微信"),
  BILL_TYPE_CANCEL_CERT(6,"提取保证金-微信"),
  BILL_TYPE_CERT(7,"提现-微信"),
  BILL_TYPE_BONUS (8,"活动奖励"),
  BILL_TYPE_REDISTER_BONUS (9,"用户注册福利"),
  BILL_TYPE_ILLEGAL_CHARGE_BONUS(10,"违规扣款"),
  BILL_TYPE_ILLEGAL_CHARGE_BALANCE (11,"违规扣款-钱包余额扣款"),
  BILL_TYPE_WALFARE_BALANCE(12,"福利金"),
  BILL_TYPE_RECHARGE_ALIPAY (13,"支付宝充值"),
  BILL_TYPE_REFUND_ALI(14,"订单退款-支付宝"),
  BILL_TYPE_RECHARGE_ALIPAY_ORDER(15,"订单支出-支付宝"),
  BILL_TYPE_RECHARGE_WX_ORDER(16,"订单支出-微信"),
  BILL_TYPE_REFUND_WX(17,"订单退款-微信"),
  BILL_TYPE_PREMADE_PAY_ALIPAY (18,"车队支出-支付宝"),
  BILL_TYPE_PREMADE_PAY_WX (19,"车队支出-微信"),
  BILL_TYPE_PREMADE_PAY_BALANCE (20,"车队支出-钱包账户"),
  BILL_TYPE_PREMADE_PAY_ALIPAY_REFUND (21,"车队退款-支付宝"),
  BILL_TYPE_PREMADE_PAY_WX_REFUND (22,"车队退款-微信"),
  BILL_TYPE_PREMADE_PAY_BALANCE_REFUND (23,"车队退款-钱包账户"),
  BILL_TYPE_INCOME_PREMADE(24,"车队收入"),
  BILL_TYPE_CERT_ALIPAY (25,"支付保证金"),
  BILL_TYPE_CANCEL_CERT_ALIPAY(26,"提取保证金"),
  BILL_TYPE_WORK_SHOP (27,"工作室扣款"),
  BILL_TYPE_STUDIO_TRANSFER (28,"工作室提现"),

  BILL_TYPE_ACCOMPANY_REFUND (29,"陪玩订单退款-零钱"),
  BILL_TYPE_ACCOMPANY_REFUND_ALI (30,"陪玩订单退款-支付宝"),
  BILL_TYPE_ACCOMPANY_REFUND_WX (31,"陪玩订单退款-微信"),
  BILL_TYPE_ACCOMPANY_PAY (32,"陪玩订单支出-零钱"),
  BILL_TYPE_ACCOMPANY_PAY_ALI (33,"陪玩订单支出-支付宝"),
  BILL_TYPE_ACCOMPANY_PAY_WX (34,"陪玩订单支出-微信"),
  BILL_TYPE_ACCOMPANY_INCOME(35,"陪玩订单收入"),

  BILL_TYPE_INVITE_REFUND (36,"约玩订单退款-零钱"),
  BILL_TYPE_INVITE_REFUND_ALI (37,"约玩订单退款-支付宝"),
  BILL_TYPE_INVITE_REFUND_WX (38,"约玩订单退款-微信"),
  BILL_TYPE_INVITE_PAY (39,"约玩订单支出-零钱"),
  BILL_TYPE_INVITE_PAY_ALI (40,"约玩订单支出-支付宝"),
  BILL_TYPE_INVITE_PAY_WX (41,"约玩订单支出-微信"),
  BILL_TYPE_INVITE_INCOME (42,"约玩订单收入"),
      //系统充值
  BILL_TYPE_SYS_COMPLAIN (43,"投诉补偿"),
  BILL_TYPE_SYS_ORDER (44,"订单补偿"),
  BILL_TYPE_SYS_ACTIVITY (45,"活动补偿"),
      //提现
  BILL_TYPE_TRANSFER_FAIL (46,"提现驳回"),

    // 系统扣款
  BILL_TYPE_PREMADE_DEDUCT (47,"车队订单违规扣款"),
  BILL_TYPE_FEED_DEDUCT (48,"发现订单违规扣款"),
  BILL_TYPE_WORK_SHOP_DEDUCT (49,"工作室提现扣款"),
  BILL_TYPE_SCALP_DEDUCT (50,"刷单扣款"),
  BILL_TYPE_IRREGULARITY_DEDUCT (51,"违规扣款"),
  BILL_TYPE_SYS_DEDUCT (52,"系统回收"),
  BILL_TYPE_GAME_ORDER_DEDUCT (53,"普通订单违规扣款"),

  BILL_TYPE_TRANSFER_CERT (54,"提取保证金-余额提现"),
      // QQ支付
 BILL_TYPE_RECHARGE_QQPAY (55,"QQ钱包充值"),
  BILL_TYPE_REFUND_QQ (56,"订单退款-QQ钱包"),
  BILL_TYPE_RECHARGE_QQ_ORDER (57,"订单支出-QQ钱包"),
  BILL_TYPE_PREMADE_PAY_QQPAY_REFUND (58,"车队退款-QQ钱包"),
  BILL_TYPE_PREMADE_PAY_QQ_REFUND (59,""),
  BILL_TYPE_PREMADE_PAY_QQ (60,"车队支出-QQ钱包"),
  BILL_TYPE_ACCOMPANY_REFUND_QQ (61,"陪玩订单退款-QQ钱包"),
  BILL_TYPE_ACCOMPANY_PAY_QQ (62,"陪玩订单支出-QQ钱包"),
  BILL_TYPE_INVITE_REFUND_QQ (63,"约玩订单退款-QQ钱包"),
  BILL_TYPE_INVITE_PAY_QQ (64,"约玩订单支出-QQ钱包"),

      //云账户提现
  BILL_TYPE_TRANSFER_YUN_ALI (65,"提现-支付宝"),
  BILL_TYPE_TRANSFER_YUN_WX (66,"提现-微信红包提现"),
  BILL_TYPE_TRANSFER_YUN_WX_TIMEOUT (67,"微信提现确认超时"),
  BILL_TYPE_TRANSFER_YUN_WX_REFUND (68,"提现失败-退回钱包账户") ,

      //周卡幸运礼包活动
  BILL_TYPE_WEEK_GIFT_PKG (69,"周卡活动") ,
  BILL_TYPE_ACTIVITY_GOODS (70,"") ,
      // 服务悬赏 start

  BILL_TYPE_SERVICE_PAY_QQ (71,"自定义技能订支出-QQ钱包") ,
  BILL_TYPE_SERVICE_PAY_BALANCE (72,"自定义技能订支出-钱包账户") ,

  BILL_TYPE_SERVICE_REFUND_WX (73,"自定义技能订退款-微信"),
  BILL_TYPE_SERVICE_REFUND_ALI (74,"自定义技能订退款-支付宝"),
  BILL_TYPE_SERVICE_REFUND_QQ (75,"自定义技能订退款-QQ钱包"),
  BILL_TYPE_SERVICE_REFUND_BALANCE (76,"自定义技能订退款-钱包账户"),
  BILL_TYPE_SERVICE_INCOME (77,"自定义技能订单收入"),

  BILL_TYPE_DISCOVERY_RED_PACKET_REFUND_BALANCE (78,"悬赏订单退款-钱包账户"),
  BILL_TYPE_DISCOVERY_RED_PACKET_REFUND_ALI (79,"悬赏订单退款-支付宝"),
  BILL_TYPE_DISCOVERY_RED_PACKET_REFUND_WX (80,"悬赏订单退款-微信"),
  BILL_TYPE_DISCOVERY_RED_PACKET_PAY_BALANCE (81,"悬赏订单支出-钱包账户"),
  BILL_TYPE_DISCOVERY_RED_PACKET_PAY_ALI (82,"悬赏订单支出-支付宝"),
  BILL_TYPE_DISCOVERY_RED_PACKET_PAY_WX (83,"悬赏订单支出-微信"),
  BILL_TYPE_DISCOVERY_RED_PACKET_INCOME (84,"悬赏订单收入"),
  BILL_TYPE_DISCOVERY_RED_PACKET_PAY_QQ (85,"悬赏订单支出-QQ钱包"),
  BILL_TYPE_DISCOVERY_RED_PACKET_REFUND_QQ (86,"悬赏订单退款-QQ钱包") ,
  BILL_TYPE_SERVICE_PAY_WX (87,"自定义技能订支出-微信") ,
  BILL_TYPE_SERVICE_PAY_ALI (88,"自定义技能订支出-支付宝"),
  BILL_TYPE_RPGCERT_CANCEL_CERT (94,"RPG取消保证金订单") ,
    // 服务悬赏 end
  BILL_TYPE_MINIPROGRAM_PAY_WX (89,"DNF车队订单支出-微信") ,
  BILL_TYPE_MINIPROGRAM_REFUND_WX (90,"DNF车队订单退款-微信") ,
  BILL_TYPE_MINIPROGRAM_INCOME (93,"DNF车队小程序收入"),

  BILL_TYPE_GCOIN_RECHARGE_BALANCE (91,""),
  BILL_TYPE_STARLIGHT_CONVERT (93,"");

  private final int code;
  private final String desc;


  BillTypeEnum(int core, String desc) {
    this.code = core;
    this.desc = desc;
  }

    public int getCode() {
        return code;
    }

    public String getCesc() {
        return desc;
    }



  public static String getDescByCode(int code){
    for(BillTypeEnum types: values()){
      if(types.code == code){
        return types.desc;
      }
    }
    return "";
  }

  public static BillTypeEnum getEnumByCode(int code){
    for(BillTypeEnum types: values()){
      if(types.code == code){
        return types;
      }
    }
    return null;
  }
}
