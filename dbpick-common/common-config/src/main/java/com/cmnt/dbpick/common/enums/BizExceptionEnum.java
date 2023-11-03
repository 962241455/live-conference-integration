package com.cmnt.dbpick.common.enums;

/**
 * 业务异常类型 范围定义: 5001-5999 公共错误码 6001-6999 车队错误码 7001-7999 订单错误码 8001-8999 用户错误码 120001-120999 支付项目-内购打赏相关错误码 ...
 *
 * @author
 */
public enum BizExceptionEnum {

    // 公共错误码
    /**
     * 返回成功
     */
    SUCCESS(0, "success"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-1, "未知错误"),

    DB_FAIL(-2, "数据库异常"),
    /**
     * 服务器内部异常
     */
    INTERNAL_SERVER_ERROR(500, "服务器正在打排位赛"),
    URL_NOT_FOUND(404, "访问地址不存在"),
    INVALID_TOKEN(401, "token非法"),
    INVALID_REFRESH_TOKEN(402, "refresh_token非法"),
    PERMISSIONS_ACCESS_DENIED(403, "权限不足，访问拒绝"),
    INVALID_PY_TOKEN(4001, "token校验失败"),
    BAD_REQUEST(400, "非法请求"),
    BLACK_LIST(423, "当前请求IP已被拉入黑名单"),
    TOO_MANY_REQUEST(429, "官人!用力太猛了，系统开启大招，排会儿队歇息一下吧"),
    READ_LIMIT_LUA_ERROR(600, "读取限流lua脚本配置异常"),
    REDIS_LOCK_PARAM_ERR(601, "Redis分布式锁参数有误"),
    /**
     * 参数验证失败
     */
    PARAM_VALID_FAIL(5001, "参数验证失败 [%s]"),
    /**
     * 文件上传
     */
    FILE_UPLOAD_FAIL(5002, "文件[%s]上传失败"),
    /**
     * 生成二维码
     */
    CREATE_TWO_CODE_FAIL(5003, "内容[%s]生成二维码失败"),
    CREATE_ELASTIC_JOB_FAIL(5004, "添加任务[%s]失败"),
    FILE_DOWNLOAD_FAIL(5006, "文件下载失败"),
    DICT_NOT_FOUND(5100, "未找到数据字典的数据"),
    DICT_EVALUATE_STAR_TIPS_NOT_FOUND(5101, "未找到星级评价提示文案配置"),
    DICT_EVALUATE_STAR_TAG_NOT_FOUND(5102, "未找到星级评价标签配置"),
    DICT_USER_HOME_TAG_SHOW_COUNT_NOT_FOUND(5103, "未找用户主页形象标签展示数量配置"),
    DICT_USER_HOME_TAG_SHOW_COUNT_ILLEGAL(5104, "用户主页形象标签展示数量不能小于0"),
    DICT_EVALUATE_TAG_NOT_FOUND(5106, "未找到评价标签"),
    DICT_EVALUATE_TAG_EXISTED(5107, "标签名重复"),
    DICT_EVALUATE_TIPS_ILLEGAL(5108, "星级评价提示文案不能为空"),
    /**
     * 服务熔断
     */
    HYSTRIX_SERVER(5555, "服务熔断"),
    GATEWAY_FALLBACK(5556, "系统繁忙, 请稍后再试!"),
    APP_VERSION_CONFIG_NOT_FOUND(5557, "[%s]版本信息未配置, 请稍后再试!"),

    // 车队错误码
    /**
     * 车队不存在
     */
    TEAM_NOT_EXIST(6001, "车队不存在"),
    /**
     * 创建车队失败
     */
    TEAM_CREATE_FAILED(6002, "创建车队失败"),
    /**
     * 不是车队队长不能踢出队员、解散车队、立即开车、结束车队
     */
    TEAM_NOT_LEADER(6003, "只有队长才能操作哦"),
    /**
     * 不是暴鸡身份无权创建车队
     */
    TEAM_CREATE_NOT_BAOJI(6004, "暴鸡方可创建车队"),

    /**
     * 车队不在准备中
     */
    TEAM_IS_NOT_PREPARING(6005, "车队不在准备中"),
    /**
     * 车队进行中
     */
    TEAM_IS_RUNNING(6006, "车队正在进行中"),
    /**
     * 车队已解散
     */
    TEAM_HAS_DISMISSED(6007, "车队已解散"),
    /**
     * 车队已结束
     */
    TEAM_HAS_COMPLETED(6008, "车队已结束"),
    /**
     * 车队已满员
     */
    TEAM_MEMBER_IS_FULL(6009, "车队已满员"),
    /**
     * 立即开车时, 车队未满员
     */
    TEAM_MEMBER_NOT_FULL(6010, "车队未满员"),
    /**
     * 车队中无暴鸡队员
     */
    TEAM_MEMBER_HAS_NO_BAOJI(6011, "车队中无暴鸡队员"),
    /**
     * 车队中无老板队员
     */
    TEAM_MEMBER_HAS_NO_BOSS(6012, "车队中无老板队员"),
    /**
     * 车队中无任何队员
     */
    TEAM_HAS_NO_MEMBER(6013, "车队中无任何队员"),
    /**
     * 车队队员不存在
     */
    TEAM_MEMBER_NOT_EXIST(6014, "该队员已离开车队"),
    /**
     * 当前队员已离开车队
     */
    TEAM_CURRENT_MEMBER_HAS_LEAVED(6015, "你已离开车队"),
    /**
     * 车队位置数不能大于副本支持上限
     */
    TEAM_SEAT_UPPER_LIMIT(6016, "车队位置数大于设定上限"),
    /**
     * 车队位置数不能小于已入队位置数
     */
    TEAM_SEAT_DOWN_LIMIT(6017, "车队位置数不能小于已有队员数"),
    /**
     * 更新车队错误
     */
    TEAM_UPDATE_ERROR(6018, "更新车队失败"),
    /**
     * 车队队员不是老板身份
     */
    TEAM_MEMBER_IS_NOT_BOSS(6019, "该队员不是老板"),
    /**
     * 非车队老板创建订单
     */
    TEAM_MEMBER_NOT_BOSS(6020, "老板方可支付订单"),
    /**
     * 老板支付校验失败
     */
    TEAM_BOSS_PAYMENT_CALLBACK_ERROR(6021, "支付校验失败"),
    /**
     * 立即开车时, 并非所有车队队员的状态都是已入团
     */
    TEAM_MEMBER_NOT_ALL_JOINED_TEAM(6022, "车队还有尚未确认入团的队员"),
    /**
     * 立即开车时, 创建暴鸡订单车队状态错误(车队状态不是进行中...)
     */
    TEAM_CREATE_BAOJI_ORDER_STATUS_ERROR(6023, "车队创建暴鸡订单失败"),
    /**
     * 车队进行中队员状态错误
     */
    TEAM_RUNNING_MEMBER_STATUS_ERROR(6024, "队员状态错误"),
    /**
     * 车队进行中解散车队和车队正常结束的游戏结果不能为默认的(未知)
     */
    TEAM_RUNNING_GAME_RESULT_ERROR(6025, "比赛结果提交失败"),
    /**
     * 车队准备中解散车队的游戏结果肯定为默认的(未知)
     */
    TEAM_PREPARING_GAME_RESULT_ERROR(6026, "比赛结果提交失败"),
    /**
     * 结束车队时车队状态错误(车队状态不是进行中)
     */
    TEAM_END_STATUS_ERROR(6027, "比赛结果提交失败"),
    /**
     * 车队状态异常
     */
    TEAM_STATUS_ERROR(6028, "车队[%s]"),
    /**
     * 车队队员退出时, 车队状态错误(车队状态不是准备中或进行中...)
     */
    TEAM_MEMBER_QUIT_STATUS_ERROR(6029, "退出车队失败"),
    /**
     * 踢出队员时, 车队状态错误(车队状态不是准备中...)
     */
    TEAM_KICKOUT_MEMBER_STATUS_ERROR(6030, "踢出队员失败"),
    /**
     * 开车到解散/结束操作太快(30s内)
     */
    TEAM_OPERATE_TOO_FAST(6031, "您的操作太快了, 请稍后再试"),
    /**
     * 操作频繁(其实是 mq 消息发送失败了...)
     */
    TEAM_MQ_SEND_FAIL(6032, "您的操作太快了, 请稍后再试!"),
    /**
     * 踢出队员时, 发生IM消息原因不能为空
     */
    TEAM_MEMBER_OUT_REASON_NOT_EMPTY(6033, "成员被踢出原因不能为空"),
    /**
     * 车队类型的创建者使用的身份与车队类型支持的身份不同
     */
    TEAM_CREATOR_NOT_MATCH(6034, "车队类型与创建者身份不匹配"),

    /**
     * 车队非全员都在准备状态
     */
    TEAM_MEMBER_NOT_ALL_ONREADY(6035, "车队还有未准备的队员"),

    /**
     * 车队状态不在开始状态
     */
    TEAM_IS_NOT_RUNNING(6036, "车队未处于进行中"),
    /**
     * 不规范操作
     */
    TEAM_UNSUPPORTED_OPERATION(6037, "不支持的操作"),
    /**
     * 免费次数不足
     */
    TEAM_FREE_CHANCE_NOT_ADEQATE(6038, "您今天的免费机会已用完，请明天再来。也可以体验更优质的金牌车队~"),

    /**
     * 用户获取匹配信息时, 未找到匹配的历史记录
     */
    TEAM_FREE_MATCHING_HISTORY_NOT_FOUND(6039, "匹配失败,请重新点击匹配"),

    /**
     * 免费次数不足
     */
    TEAM_MEMBER_FREE_CHANCE_NOT_ADEQATE(6040, "%s 免费次数不足，无法开车"),

    TEAM_HAS_MEMBER_FREE_CHANCE_NOT_ADEQATE(6041, "存在免费次数不足的队员，无法开车"),

    TEAM_JOIN_DAN_NOT_MATCH(6042, "车队段位不匹配"),

    /**
     * 车队被冻结
     */
    TEAM_HAS_FREEZED(6043, "车队已被冻结"),

    TEAM_RESULT_EMPTY(6044, "请填写比赛结果"),

    /**
     * 免费次数不足，扣减失败
     */
    SYSTEM_CHANCE_DEDUCT_FAIL(6045, "免费次数不足，扣减失败"),

    /**
     * 需等到全部队员准备才可以支付
     */
    TEAM_NOT_ALL_READY(6046, "需等到全部队员准备才可以支付"),

    /**
     * 已完成支付
     */
    HAS_PAYED(6047, "已完成支付"),

    CANNOT_KICK_YOURSELF(6048, "不能将自己踢出车队"),
    PREMADE_EVALUATED(6049, "已评价成功。"),
    PREMADE_CAN_NOT_EVALUATE(6050, "暂不能评价。"),

    CANNOT_FIND_ORDER(6051,"未找到相关订单信息"),

    ORDER_HAS_COMPLAINT(6052,"订单已投诉"),
    ORDER_COMPLAINT_OUTTIME(6053, "订单已超过投诉时间"),
    PREMADE_RESULT_SCREENSHOTS_EMPTY(6054,"游戏截图不能为空"),

    PREMADE_INVITE_MEMBER_NOT_IN_ROOM(6055,"只有车队成员才可以邀请好友"),

    PREMADE_INVITE_ROLE_ERR(6056,"车队邀请role参数错误"),

    //匹配
    MATCHING_EXISTS(6100, "您已经在匹配中了"),
    MATCHING_FREECOUPONS_NONE(6101, "非常抱歉，您已经没有免单次数了"),
    MATCHING_JOIN_TEAM_FAILED(6102, "加入车队失败，请重新匹配再试"),
    MATCHING_PUNISHMENT(6103, "由于你频繁退出车队，暂时不能加入匹配队列"),
    MATCHING_TODAY_MATCHED(6104, "您今天已经使用过免费匹配了,明天再来吧"),

    // 游戏数据
    GAME_NOT_EXISTS(6200, "游戏信息不存在"),
    GAME_DAN_NOT_EXISTS(6201, "游戏段位信息不存在"),
    GAME_NOT_CERTIFIED(6202, "您尚未认证游戏，请先进行游戏认证"),
    GAME_CERTIFY_CONFIG_EXISTED(6203, "该游戏的认证配置已存在"),
    GAME_CERTIFY_CONFIG_NOT_EXISTED(6204, "该游戏认证信息不存在"),

    //订单错误码
    ORDER_UNKNOW_USER_STATUS(7000, "车队队员订单状态错误"),
    ORDERED(7001, "该游戏角色已接单"),
    ORDER_NOT_PAY(7002, "该用户[%s]未支付"),
    ORDER_NOT_EXIST(7003, "订单不存在"),
    ORDER_TEAM_BAOJI_EMPTY(7004, "车队[%s]暴鸡订单列表为空"),
    ORDER_TEAM_USER_EMPTY(7005, "车队[{0}]的用户[{1}]订单列表为空"),
    SINGLE_GAME_RAID_FAIL(7006, "获取单个游戏副本失败"),
    BATCH_BAOJI_LEVEL_RATE(7007, "批量获取暴鸡等级系数失败"),
    BATCH_BAOJI_RATE(7008, "获取副本内暴鸡占比失败"),
    ORDER_FEFUND_NOTIFY_FAIL(7009, "退款回调订单更新失败"),
    ORDER_PAID_NOTIFY_FAIL(7010, "支付回调订单更新失败"),
    ORDER_PAID_CONFIRM_NOTIFY_FAIL(7011, "支付确认回调订单更新失败"),
    ORDER_PROFIT_BAOJI_EMPTY(7012, "暴鸡数量为零"),
    PROFIT_DPS_FAIL(7013, "结算DPS收益失败"),
    PROFIT_ASSIST_FAIL(7014, "结算辅助收益失败"),
    INVALID_BAOJI_LEVEL(7015, "错误的暴鸡等级[{0}]"),
    ORDER_PAYED_EMPTY(7016, "老板[{0}]无已支付的订单"),
    ORDER_TEAM_ITEM_EMPTY(7017, "车队订单列表为空"),
    SUM_BOSS_AMOUT_FAIL(7018, "统计老板订单总金额失败"),
    COUPON_CANNOT_USED(7018, "优惠券不可用"),
    COUPON_USED_FAIL(7019, "优惠券消费失败"),
    INVALID_PAYED_AMOUT(7020, "订单已支付金额异常：[%s]"),
    TEAM_BAOJI_ORDER_ALREADY_CREATE(7021, "该车队暴鸡订单已经创建，请勿重复创建"),
    REFUND_MQ_SEND_FAIL(7022, "退款mq消息发送失败"),
    ORDER_STATUS_NOPAY(7023, "老板[{}]不存在未支付订单"),
    ORDER_PAYED_AND_LEAVETEAM_EMPTY(7024, "车队已支付订单和中途退出的老板订单列表为空"),
    PROFIT_INVALID(7025, "暴鸡收益结算错误：收益总和大于老板支付总额"),
    ORDER_STATUS_NOT_EXIST(7026, "订单不存在"),
    ORDER_WX_INFO_FAIL(7027, "获取微信支付订单失败"),
    ORDER_STATUS_NOT_PAID(7028, "订单[%s]支付状态不是已支付"),
    ORDER_CATEGORY_ERROR(7029, "游戏订单分类错误"),
    ORDER_ALREADY_PAID(7030, "订单已支付,请勿重复支付"),
    ORDER_ALREADY_FINISH(7031, "订单已完成,请勿重复支付"),
    ORDER_NOT_SETTLE_COMPLETE(7032, "订单还未结算完成，请稍后重试"),
    ORDER_FEOM_PAYMENT_FAIL(7034, "获取支付系统订单失败"),
    ORDER_TYPE_NOT_SUPPORT(7035, "不支持的订单类型"),
    ORDER_SEQUENCE_EMPTY(7035, "订单号不能为空"),
    ORDER_HASE_BEEN_GRABED(7036, "该订单已被抢走"),
    ORDER_PAYMENT_NOT_EXIST(7037, "订单支付信息不存在"),

    ROCKETMQ_ORDER_STATISTIC_SEND_FAIL(7038, "订单统计MQ发送失败"),
    ORDER_NOT_IN_PENDING_CONFIRM(7039, "订单不处于待确认状态"),

    PREMADE_STATUS_ERR(7040, "车队[%s]状态错误，无法结算"),

    PREMADE_ONLY_END_RUNNING(7041, "只能结束进行中的车队"),

    ORDER_RESULT_SCREENSHOTS_NOT_ADAPTAT(7201, "订单结果与截图数量不匹配"),
    /**
     * 提交结果数与订单不符
     */
    ORDER_RESULT_SETTLEMENTNUM_NOT_ADAPTAT(7202, "提交结果数与订单不符"),
    /**
     * 图片数量与已打局数不匹配
     */
    ORDER_RESULT_NEED_MORE_PHOTOS(7203, "你需上传已打的 %s 张比赛结果截图"),
    ORDER_BUSINESS_TYPE_ERROR(7204, "不支持的订单类型"),
    ORDER_NOT_IN_COMPLAINT(7205, "订单不处于投诉中状态，不能进行投诉处理"),
    ORDER_REPORT_BAOJI_INCOME_ERROR(7206, "订单服务上报暴击暴击值收益失败"),
    ORDER_ALEADY_CONFIRM(7207, "订单已确认"),
    // 用户错误码
    /**
     * 游戏角色不存在
     */
    USER_GAME_ROLE_NOT_EXIST(8001, "游戏角色不存在"),
    /**
     * 游戏角色错误码
     */
    USER_GAME_ROLE_CERT_NOT_ALLOW_DELETE(8002, "请先解除认证，再删除角色"),
    /**
     * 操作人不一致
     */
    OPERATE_PERSON_DIFFER(8003, "非本人操作，拒绝"),
    USER_ROLE_NOT_CERT(8004, "游戏角色没有经过认证"),
    USER_ROLE_CREATE_FAST(8005, "请勿频繁创建角色"),
    /**
     * 用户头像不存在
     */
    USER_AVATAR_NOT_EXIST(8006, "用户头像不存在"),
    USER_SYSTEM_MAINTAINING(8007, "服务器维护中, 请稍后再试~"),
    USER_GAME_ROLE_IS_NOT_BOSS(8008, "该角色在此副本中不是老板"),
    USER_SYSTEM_THIRDPART_VALID_FAIL(8009, "第三方登录验证失败"),
    USER_MAX_REGIST_PER_DEVICE_LIMIT(8010, "该设备注册账户次数已超过上限，请使用其他设备注册。"),
    USER_SYSTEM_LOGIN_LOCK(8011, "此账号登录功能已被限制，请联系客服qq:2188245056"),
    USER_MAX_LOGIN_PER_DEVICE_LIMIT(8012, "该设备登录账户次数已超过上限，请使用其他设备登录。"),
    USER_SYSTEM_PHONE_REGIST_FAIL(8013, "手机号注册失败"),
    USER_VALIDATE_SENSITIVE_WORD(8014, "用户名含有敏感词，请修改后重新提交"),
    USER_GENERATE_UID_FAIL(8015, "用户分配uid失败"),
    USER_VALIDATE_DESC_SENSITIVE_WORD(8016, "个人简介含有敏感词，请修改后重新提交"),

    USER_AVATAR_VERIFY(8017, "你的头像正在审核中，请稍后查看"),
    USER_AVATAR_VERIFY_FAIL(8018, "你的头像不符合规范，请重新上传"),

    USER_BAOJI_INFO_NOT_EXIST(8019, "该账号不是暴鸡账号"),
    USER_UNION_NOT_EXIT(8020, "工会不存在"),
    USER_HAS_UNION(8021,"已加入其它工会， 不能加入该工会"),

    USERS_CERT_INFO_NOT_EXIT(8600, "暴鸡%s未认证过该游戏[%s]"),
    USER_CERT_INFO_NOT_EXIT(8601, "用户未认证过该游戏"),
    USER_GAME_CERT_QUERY_ERROR(8602, "查询游戏认证信息失败,请稍后再试!"),
    USER_NOT_IN_WHITE_LIST(8701, "用户不在组建暴鸡车队白名单里"),
    USER_NOT_IN_WHITE_LIST_APP(8702, "抱歉，你未能入选首期开车暴鸡名单，暂时无法开车，敬请期待第二期！"),
    USER_BASE_INFO_FAIL(8703, "查询暴鸡基础信息失败"),
    USER_CERT_ANY_INFO_NOT_EXIT(8704, "用户未认证过任何游戏"),
    USER_CERT_INFO_EXIT(8705, "用户已认证过游戏"),
    USER_CERT_NUMBER_INFO_EXIT(8706, "该身份证已被其他用户实名认证"),
    USER_NOT_CERT(8706, "您还未进行暴鸡/暴娘认证，请打开暴暴APP-我的-成为暴鸡/暴娘进行认证"),
    USER_MARGIN_ORDER_NOT_EXIST(8707, "认证保证金订单不存在"),
    USER_MARGIN_NOT_PAY(8708, "您尚未缴纳过保证金"),
    USER_POINT_NOT_CLEAR(8709, "您还有鸡分未提现，无法提取保证金"),
    USER_MARGIN_FROZEN(8710, "您的保证金已冻结，无法提取保证金。如有问题请联系客服"),
    USER_MARGIN_EXTRACT_ORDER_NOT_EXIST(8711, "认证保证金提取订单不存在"),
    USER_MARGIN_EXTRACT_AT_TEAM(8712, "正在车队中，不能提取保证金"),
    USER_MARGIN_EXTRACT_AT_ORDER(8713, "订单进行中，不能提取保证金"),
    USER_MARGIN_EXTRACT_PROCESSING(8714, "保证金退款中"),
    USER_MARGIN_STARLIGHT_NOT_EMPTY(8715, "你还有%s打赏收益未提现，请提现后再提取保证金"),
    USER_MARGIN_STARLIGHT_NOT_order_EMPTY(8727, "你还有%s订单收益未提现，请提现后再提取保证金"),
    USER_MARGIN_STARLIGHT_NOT_EMPTY_AND_LESS(8716, "你还有%s打赏收益未提现，继续提取保证金将清空剩余打赏收益，是否继续提取？"),
    USER_POINT_NOT_CLEAR_AND_LESS(8717, "你还有%s鸡分未兑现，继续提取保证金将清空剩余鸡分，是否继续提取？"),
    USER_BAOJI_ROOMID_EXIST(8718, "该房间号已存在"),
    USER_BAOJI_ROOMID_DATA_ERROR(8719, "暴鸡认证数据异常"),
    USER_BAOJI_ROOMID_UPDATE_ERROR(8721, "更新暴鸡房间号失败"),
    USER_BAOJI_ROOMID_UPDATE_GAMETEAM_SERVICE_ERROR(8722, "调用车队服务更新暴鸡房间号失败"),
    USER_BAOJI_SCORE_GET_RESOCURE_CONFIG_FAIL(8720, "暴鸡积分变更拉取配置失败"),
    USER_BAOJI_UPDATE_SCORE_CHANGE_RECORD_FAIL(8723, "更新暴鸡等级积分更改历史失败"),
    USER_BAOJI_UPDATE_SCORE_FAIL(8724, "更新暴鸡等级积分失败"),
    USER_BAOJI_UPDATE_FINISH_ORDER_FAIL(8725, "更新暴鸡完成订单数据失败"),
    USER_BAOJI_UPDATE_FINISH_ORDER_WEEK_STATISTIC_FAIL(8726, "更新暴鸡周订单数据统计失败"),
    USER_BAOJI_PRIORITY_DISPATCH_EXIST(8728, "此暴鸡优先派单白名单已存在"),


    UNION_FIND_NOT_INFO(87091, "未来查询到工作室信息"),
    UNION_FIND_EXPORT_BANK_ERROR(87092, "工作室订单收益提现失败"),
    UNION_FIND_EXPORT_REWARD_ERROR(87093, "工作室打赏收益提现失败"),
    UNION_FIND_ERROR(87094, "工作室提现导失败"),
    UNION_FIND_USER_BANK_NOT_INFO(87095, "工作室订单收益暴鸡暴娘没有可导出的数据"),
    UNION_FIND_USER_REWARD_NOT_INFO(87096, "工作室打赏收益暴鸡暴娘没有可导出的数据"),

    TIME_PATTERN(8886, "时间格式不正确！"),
    USER_EXIST(8887, "用户名已存在！"),
    UID_TOO_LONG(8888, "用户uid %s 长度错误,uid为8位字符!"),
    PHONE_HAS_REGIST_USER(8890, "手机号已注册"),

    /**
     * 入参缺失
     */
    PARAM_ENTRY_ERROR(8889, "参数错误！"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(8888, "用户不存在"),

    USER_INFO_ERR(8891, "获取用户信息失败"),
    /**
     * 用户已在其他车队中
     */
    USER_IN_OTHER_TEAM(8100, "您已加入了其他车队"),
    /**
     * 用户不在当前车队中
     */
    USER_NOT_IN_CURRENT_TEAM(8200, "您不在当前车队中"),
    USER_UPDATE_AVATAR_FAIL(8201, "修改用户头像链接失败"),
    USER_PHONE_CODE_FAIL(8202, "验证码错误"),
    /**
     * 用户账号已绑定
     */
    USER_AUTH3_ACCOUNT_IS_BIND(8401, "该第三方账号已绑定过暴鸡账号"),
    USER_AUTH3_USERNAME_IS_INVALID(8401, "该第三方账号绑定用户名非法"),
    USER_AUTH3_PHONE_IS_INVALID(8402, "手机号格式错误"),
    USER_AUTH3_PHONE_IS_BIND(8403, "该手机号已绑定过暴鸡账号"),
    USER_AUTH3_ACCOUNT_BIND_FAIL(8404, "绑定第三方账号失败"),
    USER_AUTH3_PHONE_BIND_FAIL(8405, "绑定手机号码失败"),
    USER_AUTH3_GET_BIND_LIST(8406, "获取绑定列表失败"),
    USER_AUTH3_PLATFORM_UNBIND_EMPTY(8407, "未绑定对应的第三方账号"),
    USER_AUTH3_PLATFORM_UNBIND_FAIL(8408, "解绑第三方账号失败"),
    USER_AUTH3_UPDATEPHONE_CODE_LENGTH(8409, "请填写四位数字验证码"),
    USER_AUTH3_UPDATEPHONE_FAIL(8410, "更换绑定手机号码失败"),
    USER_AUTH3_VERIFYPHONE_NO_PHONE(8411, "当前账号未绑定手机号码"),

    /**
     * 用户实名认证
     */
    USER_REALNAME_VERIFY_IDCARDNUMBER_INVAILD(8412, "实名信息有误，请重新填写"),
    USER_REALNAME_VERIFY_EXIST_RECORD(8413, "您已经提交了实名认证申请，请勿重复提交"),
    USER_REALNAME_VERIFY_IDCARD_NUMBER_EXIST(8414, "此实名信息已被使用，请更换身份信息"),
    USER_REALNAME_VERIFY_APPLY_SUBMIT_FAILED(8415, "提交实名认证申请失败"),
    USER_REALNAME_CHECK_FAILED(8416, "用户未实名或已加入黑名单"),
    USER_REALNAME_VERIFY_FAILED_REMARK_NOTNULL(8416, "实名认证审核不通过，需填写推送消息"),
    USER_REALNAME_VERIFY_SUBMIT_FAILED(8417, "提交实名认证结果失败"),
    USER_REALNAME_BLACKLIST_ADD_FAILED(8418, "拉入黑名单失败"),
    USER_REALNAME_BLACKLIST_REMOVE_FAILED(8419, "移出黑名单失败"),
    USER_REALNAME_BLACKLIST_UNBIND_BLACKLIST_FAILED(8420, "您的实名信息已被拉黑，不能解绑"),
    USER_REALNAME_BLACKLIST_UNBIND_BAOJI_FAILED(8420, "您有已认证或待审核的暴鸡/暴娘信息，不能解绑"),
    USER_REALNAME_BLACKLIST_UNBIND_FAILED(8421, "解绑实名认证信息失败"),
    USER_BAOJI_SUBMIT_FAILED(8422, "未查询到暴鸡信息或待缴纳保证金，请勿重复提交"),

    /**
     * 用户鸡分错误码
     */
    USER_POINT_EXCHANGE_CONFIG_EMPTY(8301, "鸡分兑换配置未配置"),
    USER_POINT_EXCHANGE_SWITCH_DISABLE(8302, "鸡分兑换通道已关闭"),
    USER_POINT_EXCHANGE_TIME_FRAME_ERROR(8303, "当前时间不在鸡分兑换时间范围内"),
    USER_POINT_EXCHANGE_AMOUNT_FRAME_ERROR(8304, "当前兑换暴鸡值不在兑换范围内"),
    USER_POINT_EXCHANGE_AVAILABLE_AMOUNT(8305, "可用鸡分不足"),
    USER_AWARD_POINT_NOT_FOUND(8306, "未获取到车队对应鸡分配置奖励值"),
    STAR_AWARD_POINT_NOT_FOUND(8307, "未匹配到星值对应鸡分配置奖励值"),
    INCR_POINT_FAIL(8307, "插入鸡分明细失败"),

    ACTION_NO_FIND_METHOD(4002, "未找到该方法"),

    /**
     * 照片
     */
    PICTURE_NO_FIND(8500, "未找到需要替换的图片"),
    PICTURE_VERIFYING(8501, "照片正在审核中，无法修改"),
    PICTURE_URL_UNLAWFUL(8502, "非法URL"),
    PICTURE_OVER_MAX(8503, "已超过最大数量限制"),
    PICTURE_OVER_MIN(8504, "至少保留一张照片"),
    PICTURE_VERIFY_FAILED(8505, "审核头像失败"),

    /**
     * 微信小程序
     */
    WX_MP_AUTH_FAIL(8308, "微信小程序认证失败"),
    WX_PHONE_HAS_BINDED(8309, "该微信已经绑定手机"),
    PHONE_HAS_BINDED_WX(8310, "手机号已经绑定微信"),

    /**
     * 用户分享拉新错误码
     */
    USER_AWARD_BAOJICOIN_ERROR(8206, "增加暴击币失败"),

    USER_SHARE_NOT_ERROR(8207, "该分享类型不存在"),

    //API鉴权
    TOKEN_EXPIRED(11000, "Token过期"),
    AUTH_REVOKE_FAIL(11002, "Token注销失败,已经被注销"),
    EMPTY_TOKEN(11003, "Authorization 参数为空"),
    DECODE_TOKEN_FAIL(11004, "解析BASIC TOKEN失败"),
    INVALID_BASIC_TOKEN(11005, "错误的BASIC TOKEN"),
    INVALID_CLIENT(11005, "错误的ClientId或ClientSecret"),
    INIT_AUTHURL_ERROR(11007, "初始化权限异常"),
    LOAD_AUTHURL_ERROR(11008, "获取权限异常"),
    REFRESH_TOKEN_ERROR(11009, "token 刷新失败"),
    UID_INFO_EMPTY(11010, "数据异常，找不到用户信息，需重新登陆。Token:[{0}]"),
    INIT_CLIENT_ERROR(110011, "预热客户端信息异常"),
    ERROR_TOKEN(110012, "Authorization 参数错误"),

    // 游戏资源错误码
    /**
     * 游戏副本不存在
     */
    GAME_RAID_NOT_EXIST(9001, "游戏副本不存在"),
    /**
     * 游戏职业不匹配
     **/
    GAME_CAREER_NOT_MATCH(9002, "游戏职业不匹配"),
    /**
     * 游戏服务器不匹配
     **/
    ZONE_SERVER_NOT_MATCH(9003, "游戏服务器不匹配"),

    ZONE_SERVER_NOT_EXIST(9004, "游戏服务器不存在"),
    RAID_CERT_NOT_EXIST_RAID(9005, "认证的副本没有可加入副本"),
    //客户中心错误码
    /**
     * 同一个车队只能被同一个老板投诉一次
     */
    COMPLAINT_NOT_ALLOWED_TWICE(9901, "同一个车队只能被同一个老板投诉一次"),

    /**
     * 评价星评必须在1、2、3、4、5范围内
     */
    EVALUATE_STAR_ILLEGAL(9902, "请给出星评价哦"),
    /**
     * 评价内容最多只能输入1000汉字
     */
    EVALUATE_CONTENT_LENGTH_OVER(9903, "最多只能输入1000字哦~"),
    /**
     * 评价内容检测有违规内容
     */
    EVALUATE_CONTENT_ILLEGAL(9904, "评价内容含有敏感词，请修改后重新提交"),
    /**
     * 只有已服务订单才能评价
     */
    EVALUATE_SERVICE_NOT_END(9905, "只有已确认服务的服务订单才可以评价哦~"),
    /**
     * 只有车队成员才能评价车队订单
     */
    EVALUATE_NOT_TEAM_MEMBER(9906, "只有车队成员才能评价车队订单哦~"),
    /**
     * 1,2星评分至少需要选择一个标签或填写评论内容
     */
    EVALUATE_CONTENT_MISSING(9907, "请选择评论标签或填写评论内容"),
    /**
     * 缺少车队号
     */
    EVALUATE_TEAMSEQUENCE_MISSING(9908, "缺少车队号"),
    /**
     * 选择了多个标签
     */
    EVALUATE_ILLEGAL_SEARCH_TYPE(9909, "只能选择一个标签"),
    /**
     * 没有选择标签
     */
    EVALUATE_MISSING_SEARCH_TYPE(9910, "至少选择一个标签"),

    /**
     * 不支持评论的订单类型（目前只支持暴鸡车队单）
     */
    EVALUATE_ILLEGAL_ORDER_TYPE(9911, "错误的订单类型"),
    /**
     * 星评分必填(目前2个维度)
     */
    EVALUATE_ILLEGAL_STAR(9912, "请从实力、服务两个维度进行评价"),
    /**
     * 星评分必填(目前2个维度)
     */
    EVALUATE_UID_CHICKENID_MISSING(9913, "请填写uid或鸡牌号"),
    /**
     * 星评分必填(目前2个维度)
     */
    EVALUATE_ILLEGAL_DATE_DIFFERENCE(9914, "不合法的时间范围"),
    /**
     * 错误的暴鸡uid
     */
    EVALUATE_ILLEGAL_BAOJI_UID(9915, "暴鸡信息有误"),
    /**
     * 不支持评价的订单类型
     */
    EVALUATE_UNSUPPORTED_ORDER_TYPE(9916, "不支持评价的订单类型"),
    /**
     * 不支持评价的订单类型
     */
    EVALUATE_ITERATIVE(9917, "请勿重复评价"),
    /**
     * 没有订单结束时间
     */
    EVALUATE_ORDER_FINISHI_TIME_MISSING(9918, "订单未结束，不能评价"),
    /**
     * 查询其他服务出错，无法获取gameId
     */
    EVALUATE_FETCH_RESOURCE_FAIL(9919, "获取资源失败，请稍后重试"),

    //阿里云过滤错误码
    /**
     * 需要人工审核
     */
    ALI_SCAN_SUGGESTION_REVIEW(10001, "需要人工审核"),
    /**
     * 文本违规
     */
    ALI_SCAN_SUGGESTION_BLOCK(10002, "文本违规"),
    /**
     * 任务解析失败
     */
    ALI_SCAN_TASK_PROCESS_FAIL(10003, "任务解析失败"),
    /**
     * 响应包处理失败
     */
    ALI_SCAN_DETECT_NOT_SUCCESS(10004, "响应包处理失败"),
    /**
     * HTTP请求响应失败
     */
    ALI_SCAN_RESPONSE_NOT_SUCCESS(10005, "HTTP请求响应失败"),

    //支付项目-内购打赏相关错误码
    /**
     * 暴鸡币打赏和用户钱包流水相关-异常相关提示编码
     */
    USER_WALLETS_SELECT(120001, "用户余额查询服务接口异常"),
    GCOIN_BILL_SELECT(120002, "暴鸡币流水查询服务接口异常"),
    STARLIGHT_BILL_SELECT(120003, "暴击值流水查询服务接口异常"),
    GCOIN_CONSUME(120004, "暴鸡币兑换暴击值服务接口异常"),
    CREATE_GCOIN_ORDER_FAILURE(120005, "暴鸡币订单创建服务接口异常"),
    BILL_SAVE(120006, "流水保存接口异常"),
    ORDER_ENTITY_TYPE_EMPTY(120007, "订单实体类型不存在,流水保存失败！"),
    CURRENCY_TYPE_EMPTY(120008, "扣款货币类型类型不存在,扣款订单流水保存失败！"),
    USER_STARLIGHT_SELECT(120009, "用户累计暴击值查询服务接口异常"),

    /**
     * 暴鸡币打赏和用户钱包流水相关-业务处理条件不足的相关提示编码
     */
    USER_WALLETS_TYPE_EMPTY(120010, "用户钱包类型不能为空！"),
    WALLETS_BILL_NO_DATA(120011, "流水查询无数据！"),
    USER_GCOIN_NOT_ENOUGH(120012, "当前用户暴鸡币余额数不足！"),
    CONSUME_ORDER_NON_EXIST(120013, "打赏订单不存在，请重新下单！"),
    CONSUME_ORDER_STATE_PAYSUCCESS(120014, "打赏订单已处理，请不要重复请求！"),
    BILL_TIME_PATTERN(120015, "时间格式不正确"),
    BILL_TIME_DEFICIENCY(120016, "缺少开始或结束时间"),
    SCORE_BILL_FAIL(120017, "鸡分流水查询接口异常！"),
    USER_SCORE_BALANCE_FAIL(120018, "获取用户鸡分信息接口异常"),
    USER_FREE_GIFT_NOT_ENOUGH(120019, "当前用户剩余免费礼物数不足"),

    /**
     * 充值和兑现-业务处理条件不足的相关提示错误码
     */
    // 充值错误码错误码
    PAYMENT_ORDER_NOT_EXIST(120021, "对应的订单不存在"),
    //暴鸡币余额账户不可用
    GACCOUNT_UNAVALIABLE(120022, "暴鸡币余额账户不可用"),

    PAYMENT_FINISH(120023, "订单已经充值成功"),
    WITHDRAW_FINISH(120024, "订单已经提现成功"),
    EXCHANGE_FINISH(120028, "订单已经兑换完成"),

    STARTACCOUNT_UNAVALIABLE(120025, "暴击值余额账户不可用"),
    STARTNUMBER_LESS(120026, "兑换金额不能大于暴击值余额"),

    EXCHANGE_SERVICE_REJECT(121001, "您是暴鸡用户，不可进行暴击值兑换哦~"),

    COMSUMER_LOCK(120029, "该消息正在处理中"),
    APPLE_RECIPT_NOT_EXIST(120030, "APPLE票据信息不能为空"),
    MESSAGE_IS_CONSUMING(120031, "该消息正在处理中"),

    STARACCOUNT_NOT_EXIST(120032, "暴击值账户不存在"),
    BALANCE_WITHDRAW__FAIL(120033, "可使用暴击值余额不足，提现失败"),
    WITHDRAW_REPEAT__FAIL(120036, "提现请求正在处理中，请勿重复提交"),
    GCOINCCOUNT_NOT_EXIST(120037, "暴鸡币账户不存在"),
    WITHDRAW_BOTH__NULL__FAIL(120038, "订单号及流水号不能同时为空"),
    WITHDRAW_PARAMS_NOT_MATCH(120039, "提现参数类型不匹配"),

    //暴鸡币支付
    GCOINPAYMENT_CLOSE(120034, "暴鸡币支付订单已经超时关闭"),
    GCOINPAYMENT_FINISH(120035, "暴鸡币支付订单已经支付成功"),
    /**
     * 充值和兑现-异常相关提示编码
     */
    //回调接口异常
    RECALL_API_EXCEPTION(120041, "回调接口异常"),
    //链接错误
    CONNECTION_EXCEPTION(120042, "链接错误"),
    PAYMENT_API_EXCEPTION(120043, "回调发送充值订单异常"),
    WITHDRAW_API_EXCEPTION(120044, "回调发送提现订单异常"),

    /**
     * 充值和兑现-消息队列相关提示编码
     */
    ROCKETMQ_PRODUCER_ERROR(120050, "rocketMq生产者出问题了~"),
    ROCKETMQ_COMSUMER_ERROR(120051, "rocketMq消费者出问题了~"),

    //金额入参必须是大于0的整数
    INPUT_PARAM_NUMBER_ERROR(120100, "金额入参必须是大于0的整数"),

    //暴鸡币账号相关错误码
    GCOIN_ACCOUNT_FROZEN(120101, "暴鸡币账号已冻结"),
    GCOIN_ACCOUNT_UNAVAILABLE(120102, "暴鸡币账号不可用"),
    GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH(120103, "暴鸡币账户余额不足"),
    GCOIN_ACCOUNT_BALANCE_NOT_EXIST(120104, "暴鸡币账户不存在"),

    //暴击值账号相关错误
    STAR_ACCOUNT_FROZEN(120105, "暴击值账户已冻结"),
    STAR_ACCOUNT_UNAVAILABLE(120106, "暴击值账户不可用"),
    STAR_ACCOUNT_BALANCE_NOT_ENOUGH(120107, "暴击值账户余额不足"),

    ACCOUNT_CHECK_PARAM_ERROR(120108, "查询账号信息时，用户类型不匹配"),

    //暴鸡币充值相关错误码
    GCOIN_RECHARGE_ORDER_NOT_EXIST(120201, "暴鸡币充值订单不存在"),
    GCOIN_RECHARGE_ORDER_CLOSED(120202, "暴鸡币充值订单已关闭"),
    GCOIN_RECHARGE_ORDER_FINISHED(120203, "暴鸡币充值订单已完成 "),
    GCOIN_RECHARGE_ORDER_NO_IS_EMPTY(120204, "暴鸡币充值,充值订单不能为空"),
    GCOIN_RECHARGE_ORDER_NO_FORMAT_EXCEPTION(120205, "暴鸡币充值,订单号格式不对"),
    GCOIN_RECHARGE_APPLE_PAY_ORDER_EMPTY(120206, "暴鸡币充值,apple支付时充值订单号-交易订单号不存在"),
    GCOIN_RECHARGE_PATH_ORDERS_NO_MATCH_ITEMS_ORDER(120207, "暴鸡币充值,PATH中orderId与items中的orderId 数量不匹配"),

    //暴鸡币充值-苹果支付相关错误码
    GCOIN_RECHARGE_APPLEPAY_SERVER_EXCEPTION(120300, "暴鸡币充值-苹果支付-苹果服务器异常"),
    GCOIN_RECHARGE_APPLEPAY_RECIPT_NOT_EXIST(120301, "暴鸡币充值-苹果支付-票据不存在"),
    GCOIN_RECHARGE_APPLEPAY_DEVICEID_NOT_EXIST(120302, "暴鸡币充值-苹果支付-设备ID不存在"),
    GCOIN_RECHARGE_APPLEPAY_CURRENCYTYPE_NOT_EXIST(120303, "暴鸡币充值-苹果支付-货币类型不存在"),
    GCOIN_RECHARGE_APPLEPAY_PRODUCT_SEARCH_ERROR(120304, "暴鸡币充值-苹果支付-查询APPLIE支付商品信息失败"),
    GCOIN_RECHARGE_APPLEPAY_RECIPT_IS_USED(120305, "暴鸡币充值-苹果支付-票据已经使用过"),

    //暴鸡币支付相关错误码
    GCOIN_PAYMENT_ORDER_CREATING(120400, "暴鸡币支付，该暴鸡币支付订单正在处理中"),
    GCOIN_PAYMENT_ORDER_NOT_EXIST(120401, "暴鸡币支付，该暴鸡币支付订单不存在"),
    GCOIN_PAYMENT_ORDER_CLOSED(120402, "暴鸡币支付，该暴鸡币支付订单支付超时关闭或结清，无法再支付"),
    GCOIN_PAYMENT_ORDER_PAIED(120403, "暴鸡币支付，该暴鸡币支付订单已支付完成 "),
    GCOIN_PAYMENT_ORDER_EXIST(120404, "暴鸡币支付，该暴鸡币支付订单已经存在"),

    GCOIN_PAYMENT_SEARCH_PARAM_ERROR(120405, "查询暴鸡币支付订单时，参数不完整（orderId或者orderType和outTradeNo二选一)"),
    GCOIN_PAYMENT_SEARCH_ORDER_NOT_FOUND(120406, "查询暴鸡币支付订单时，订单不存在"),

    //暴鸡币退款时，关联的支付订单的相关错误码
    GCOIN_REFUND_PAYMENT_ORDER_NOT_EXIST(120501, "暴鸡币退款，该支付订单不存在"),
    GCOIN_REFUND_PAYMENT_ORDER_NOT_PAY(120502, "暴鸡币退款，该支付订单未支付"),
    GCOIN_REFUND_PAYMENT_ORDER_SETTLED(120503, "暴鸡币退款，该支付订单已经退款完毕结清"),
    GCOIN_REFUND_PAYMENT_ORDER_AMOUNT_NOT_ENOUGH(120504, "暴鸡币退款，要退款金额大于可退金额"),
    GCOIN_REFUND_PAYMENT_ORDER_EXIST(120505, "暴鸡币退款，该支付订单已存在"),

    //暴鸡币退款时，退款订单的相关错误码
    GCOIN_REFUND_ORDER_CREATING(120600, "暴鸡币退款，该订单正在处理中"),
    GCOIN_REFUND_ORDER_NOT_EXIST(120601, "暴鸡币退款，该退款订单不存在"),
    GCOIN_REFUND_ORDER_NOT_PAY(120602, "暴鸡币退款，该退款订单未支付"),
    GCOIN_REFUND_ORDER_FINISHED(120603, "暴鸡币退款，该退款订单完成"),
    GCOIN_REFUND_ORDER_CLOSED(120604, "暴鸡币退款，订单支付超时已关闭、或结清，无法再退款"),
    GCOIN_REFUND_ORDER_FAIL(120605, "暴鸡币退款，订单退款失败"),

    GCOIN_REFUND_SEARCH_PARAM_ERROR(120605, "查询暴鸡币退款订单时，参数不完整（orderId或者orderType和outRefundNo二选一)"),
    GCOIN_REFUND_SEARCH_ORDER_NOT_FOUND(120606, "查询暴鸡币退款订单时，订单不存在"),
    CALL_PAY_SYSTEM_FAIL(120607, "调用支付系统失败"),
    CALL_GAMINGTEAM_SYSTEM_FAIL(120607, "调用车队系统失败"),
    CALL_TRADE_SYSTEM_FAIL(120608, "调用trade系统失败"),
    CALL_RESOURCE_SYSTEM_FAIL(120609, "调用resource系统失败"),
    CALL_RISK_SYSTEM_FAIL(120610, "调用风控系统失败"),

    //扣款相关错误码
    //扣款货币类型错误
    DEDUCT_CURRENCY_TYPE_ERROR(120700, "扣款货币类型错误"),
    //后台暴鸡币扣款操作相关错误
    GCOIN_DEDUCT_ORDER_EXIST(120701, "暴鸡币扣款订单已经存在"),
    //后台暴击值扣款相关错误
    STAR_DEDUCT_ORDER_EXIST(120702, "暴击值扣款订单已经存在"),
    //后台暴鸡币充值相关错误
    BACK_STAGE_GCOIN_PAYMENT_ORDER_EXIST(120703, "暴鸡币后台充值，订单已经存在"),

    //微信支付相关错误码
    TENPAY_PAY_SETTING_NOT_EXISTS(120910, "微信/QQ支付-支付配置没有配置该项"),
    TENPAY_PAY_SETTING_NULL_EXISTS(120911, "微信/QQ支付-支付配置为空"),
    TENPAY_PREPAY_ORDER_EXISTS(120912, "微信/QQ支付-预支付订单已经存在"),
    TENPAY_PREPAY_ORDER_POST_ERROR(120913, "微信/QQ支付-预支付订单网络请求错误"),
    TENPAY_PREPAY_ORDER_POST_CLOSE_ERROR(120914, "微信/QQ支付-预支付订单网络关闭错误"),
    TENPAY_PREPAY_ORDER_RETURN_ERROR(120915, "微信/QQ支付-预支付订单创建响应错误"),
    TENPAY_PREPAY_ORDER_PARAM_ERROR(120916, "微信/QQ支付--预支付订单参数错误"),
    TENPAY_PREPAY_ORDER_NET_ERROR(120917, "微信/QQ支付-预支付订单下单请求网络错误"),
    TENPAY_PREPAY_ORDER_CLOSE_TIME_ERROR(120918, "微信/QQ支付-订单关闭需在支付5分钟之后关闭"),
    TENPAY_PAY_ORDER_QUERY_RETURN_ERROR(120919, "微信/QQ支付-支付查询返回失败"),
    TENPAY_PREPAY_ORDER_QUERY_RETURN_SYNC_ERROR(120920, "微信/QQ支付-支付查询通同步数据异常"),
    TENPAY_PREPAY_ORDER_REFUND_AGAIN_ERROR(120921, "微信/QQ支付-已经存在退款记录"),
    TENPAY_PREPAY_ORDER_REFUND_MONEY_ERROR(120922, "微信/QQ支付-退款金额错误"),
    TENPAY_PREPAY_ORDER_REFUND_MONEY_OUT_ERROR(120923, "微信/QQ支付-退款金额超过订单总金额"),
    TENPAY_PREPAY_ORDER_REFUND_SYNC_ERROR(120926, "微信/QQ支付-退款同步数据库异常"),
    TENPAY_PREPAY_ORDER_REFUND_PARAM_ERROR(120927, "微信/QQ支付-退款参数构建异常"),
    TENPAY_PREPAY_ORDER_DO_REFUND_ERROR(120928, "微信/QQ支付-发起退款业务失败"),
    TENPAY_PREPAY_ORDER_REFUND_RETURN_ERROR(120929, "微信/QQ支付-退款业务返回失败"),
    TENPAY_PREPAY_ORDER_QUERY_CONS_ERROR(120930, "微信/QQ支付-构建查询参数异常"),
    TENPAY_PREPAY_ORDER_QUERY_DO_ERROR(120931, "微信/QQ支付-支持查询订单异常"),
    TENPAY_PREPAY_ORDER_REFUND_DO_ERROR(120932, "微信/QQ支付-执行退款异常"),
    TENPAY_PREPAY_ORDER_REFUND_DB_ERROR(120933, "微信/QQ支付-退款同步数据库失败"),
    TENPAY_PREPAY_ORDER_CONS_REFUND_QUERY_ERROR(120934, "微信/QQ支付-退款查询参数构建异常"),
    TENPAY_REFUND_ORDER_NOT_EXITS_ERROR(120935, "微信/QQ支付-不存在该笔退款"),
    TENPAY_RETURN_APP_PARAM_ERROR(120936, "微信/QQ支付-返回APP参数错误"),

    //支付宝-支付相关错误码
    ALIPAY_PAY_ORDER_EXIST(121101, "支付宝支付-支付订单已经存在"),
    ALIPAY_PAY_ORDER_CLOSED(121102, "支付宝支付-支付订单已经关闭"),
    ALIPAY_PAY_ORDER_CANCEL(121103, "支付宝支付-支付订单已经取消"),
    ALIPAY_PAY_ORDER_SUCCESS(121104, "支付宝支付-支付订单已经支付"),
    ALIPAY_PAY_ORDER_NOT_FOUND(121105, "支付宝支付-支付订单不存在"),
    ALIPAY_PAY_ORDER_CLOSED_NO_REFUND(121106, "支付宝支付-支付订单已经关闭无法退款"),
    ALIPAY_PAY_ORDER_CLOSING(121107, "支付宝支付-支付订单正在申请关闭中"),
    ALIPAY_PAY_ORDER_CANCELING(121108, "支付宝支付-支付订单正在申请取消中"),
    ALIPAY_PAY_ORDER_CREATE_EXCEPTION(121109, "支付宝支付-支付订单数据加密异常"),
    ALIPAY_PAY_ORDER_MONEY_NOT_EQUAL(121110, "支付宝回调-支付订单金额不一致"),
    ALIPAY_PAY_ORDER_APPID_NOT_EQUAL(121111, "支付宝回调-appId不一致"),

    //支付宝-退款相关错误码
    ALIPAY_REFUND_PAYORDER_UNPAIED(121201, "支付宝退款-支付订单未支付"),
    ALIPAY_REFUND_PAYORDER_NOT_EXIST(121202, "支付宝退款-支付订单不存在"),
    ALIPAY_REFUND_PAYORDER_CLOSED(121203, "支付宝退款-支付订单已经关闭"),
    ALIPAY_REFUND_PAYORDER_CANCEL(121204, "支付宝退款-支付订单已经取消"),
    ALIPAY_REFUND_PAYORDER_FINISHED(121205, "支付宝退款-支付交易结束，不可退款"),
    ALIPAY_REFUND_ORDER_NOT_EXIST(121206, "支付宝退款-退款订单不存在"),
    ALIPAY_REFUND_ORDER_EXIST(121207, "支付宝退款-退款订单已经存在"),

    ALIPAY_REFUND_ORDER_CREATE_EXCEPTION(121209, "支付宝退款-退款订单创建异常"),
    ALIPAY_REFUND_ORDER_MONEY_NOT_EQUAL(121210, "支付宝回调-退款订单金额不一致"),

    //支付宝-关闭订单相关错误码
    ALIPAY_CLOSE_PAYORDER_PAIED(121301, "支付宝关闭-支付订单已经支付"),
    ALIPAY_CLOSE_PAYORDER_NOT_EXIST(121302, "支付宝关闭-支付订单不存在"),
    ALIPAY_CLOSE_PAYORDER_CLOSING(121303, "支付宝关闭-支付订单正在申请取消中"),
    ALIPAY_CLOSE_PAYORDER_CLOSED(121304, "支付宝关闭-支付订单已经关闭"),
    ALIPAY_CLOSE_PAYORDER_CANCELING(121305, "支付宝关闭-支付订单正在申请取消中"),
    ALIPAY_CLOSE_PAYORDER_CANCEL(121306, "支付宝关闭-支付订单已经取消"),
    ALIPAY_CLOSE_PAYORDER_FINISHED(121307, "支付宝关闭-支付交易结束，不可关闭"),
    ALIPAY_CLOSE_ORDER_EXCEPTION(121308, "支付宝关闭-关闭订单异常"),
    ALIPAY_CLOSE_RETURN_OUT_TRADE_NO_NOT_EQUAL(121309, "支付宝关闭-关闭返回支付订单号不一致"),
    ALIPAY_CLOSE_RETURN_TRADE_NO_NOT_EQUAL(121310, "支付宝关闭-关闭返回支付宝交易订单号不一致"),

    //支付宝-撤销订单相关错误码
    ALIPAY_CANCEL_PAYORDER_SUCCESS(121401, "支付宝撤销-支付订单已支付，请调用退款接口进行处理"),
    ALIPAY_CANCEL_PAYORDER_NOT_EXIST(121402, "支付宝撤销-支付订单不存在"),
    ALIPAY_CANCEL_PAYORDER_CLOSING(121403, "支付宝撤销-支付订单正在申请关闭中"),
    ALIPAY_CANCEL_PAYORDER_CLOSED(121404, "支付宝撤销-支付订单已经关闭"),
    ALIPAY_CANCEL_PAYORDER_CANCELING(121405, "支付宝撤销-支付订单正在申请撤销中"),
    ALIPAY_CANCEL_PAYORDER_CANCEL(121406, "支付宝撤销-支付订单已经撤销"),
    ALIPAY_CANCEL_PAYORDER_FINISHED(121407, "支付宝撤销-支付交易结束，不可撤销"),
    ALIPAY_CANCEL_CALL_EXCEPTION(121408, "支付宝撤销-调用撤销订单异常"),
    ALIPAY_CANCEL_CREATE_EXCEPTION(121409, "支付宝撤销-创建撤销订单异常"),
    ALIPAY_CANCEL_PAYORDER_UNPAIED(121410, "支付宝撤销-该订单未支付，请调用关闭接口进行处理"),
    ALIPAY_CANCEL_PAYORDER_NOT_EXIST_IN_ALI(121411, "支付宝撤销-该订单不存在于支付宝系统中，请调用关闭接口进行处理"),

    //支付宝查询相关错误码
    ALIPAY_PAY_ORDER_SEARCH_EXCEPTION(122301, "支付宝支付-支付订单查询异常"),
    ALIPAY_REFUND_SEARCH_EXCEPTION(122302, "支付宝退款-退款订单查询异常"),
    ALIPAY_REFUND_SEARCH_CALL_EXCEPTION(122303, "支付宝退款-调用退款接口异常"),
    ALIPAY_REFUND_SEARCH_RETURN_PARAMS_EXCEPTION(122304, "支付宝退款-退款查询没有对应的订单信息，表示退款失败"),
    ALIPAY_REFUND_SEARCH_REQUEST_NOT_EQUAL(122305, "支付宝退款-退款查询返回的退款订单号与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_PAY_TRADE_NO_NOT_EQUAL(122306, "支付宝退款-退款查询返回的支付订单号与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_REFUND_MONEY_NOT_EQUAL(122307, "支付宝退款-退款查询返回的退款金额与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_TRANSACTION_ID_NO_EQUAL(122308, "支付宝退款-退款查询返回的交易订单号与商户第一次收到的不一致"),
    ALIPAY_REFUND_REFUND_ORDER_NOT_EXIST(122309, "支付宝退款-退款订单不存在"),
    ALIPAY_REFUND_REFUND_ORDER_FAIL(122310, "支付宝退款-该退款订单退款失败"),
    ALIPAY_REFUND_REFUND_ORDER_SUCCESS(122311, "支付宝退款-该退款订单已经退款成功"),

    //支付配置相关错误码
    EXTERNAL_APP_CHANNEL_NOT_FOUND(122401, "没有查到对应的App-渠道配置信息"),
    EXTERNAL_APP_CHANNEL_DISABLE(122402, "该APP暂不支持该渠道的支付方式"),
    EXTERNAL_ALIPAY_CONFIG_NOT_EXIST(122403, "支付宝支付配置信息不存在"),
    EXTERNAL_TENPAY_CONFIG_NOT_EXIST(122404, "QQ支付、微信支付配置信息不存在"),
    EXTERNAL_CAPAY_CONFIG_NOT_EXIST(122405, "云账户配置信息不存在"),
    EXTERNAL_CHANNEL_NOT_FOUND(122406, "没有对应的渠道信息"),
    EXTERNAL_PAY_SETTING_CONFIG_NOT_EXIST(122407, "该支付配置信息不存在"),

    MONGODB_ALIPAY_PAYMENT_ORDER_NOT_EXITST(122601, "mongoDB中查不到对应的支付宝支付订单信息"),

    TENCENT_PAY_ORDERCREATE(123101, "腾讯支付-创建订单异常"),
    TENCENT_PAY_ORDERCLOSE_1(123102, "腾讯支付-关闭订单异常：订单已经存在"),
    TENCENT_PAY_ORDERCLOSE_2(123103, "腾讯支付-微信订单失败"),

    //第三方支付流水支付错误码
    EXTERNAL_TRADE_BILL_DATA_OUT_OF_RANGE(124101, "查询时间超出范围"),

    //APPSetting设置相关错误码
    APP_SETTING_APP_NAME_IS_EXIST(125001, "AppSetting配置-appName已经存在"),
    APP_SETTING_APP_ID_IS_NOT_EXIST(125002, "AppSetting配置-没有对应AppId的配置信息"),
    APP_SETTING_NOT_EXIST(125003, "AppSetting配置-没有对应的配置信息"),

    //苹果支付
    APPLE_PAY_FAILED(125101, "苹果支付失败"),
    APPLE_PAY_SETTING_NOT_EXIST(125102, "苹果支付-配置信息不存在"),
    APPLE_PAY_PRODUCT_NOT_EXIST(125103, "苹果支付-产品不存在"),

    //支付回调业务方失败
    EXTERNAL_PAY_CALL_BACK_FAILED(125201, "支付回调也业务方失败"),
    //退款回调业务方失败
    EXTERNAL_REFUND_CALL_BACK_FAILED(125202, "退款回调也业务方失败"),

    //支付订单相关错误码
    //支付时支付订单校验
    EXTERNAL_PAY_ORDER_EXIST(126101, "第三方支付-支付-支付订单已经存在"),
    EXTERNAL_PAY_ORDER_CLOSED(126102, "第三方支付-支付-支付订单已经全额退款关闭"),
    EXTERNAL_PAY_ORDER_CANCEL(126103, "第三方支付-支付-支付订单已经取消"),
    EXTERNAL_PAY_ORDER_SUCCESS(126104, "第三方支付-支付-支付订单已经支付"),
    EXTERNAL_PAY_ORDER_NOT_FOUND(126105, "第三方支付-支付-支付订单不存在"),
    EXTERNAL_PAY_ORDER_CLOSED_NO_REFUND(126106, "第三方支付-支付-支付订单已经关闭无法退款"),
    EXTERNAL_PAY_ORDER_CLOSING(126107, "第三方支付-支付-支付订单正在申请关闭中"),
    EXTERNAL_PAY_ORDER_CANCELING(126108, "第三方支付-支付-支付订单正在申请取消中"),
    EXTERNAL_PAY_ORDER_CREATE_EXCEPTION(126109, "第三方支付-支付-支付订单数据加密异常"),
    EXTERNAL_PAY_ORDER_MONEY_NOT_EQUAL(126110, "第三方支付-支付-支付订单金额不一致"),
    EXTERNAL_PAY_ORDER_MONEY_NOT_EQUAL_TRADE_MONEY(126111, "第三方支付-支付-支付金额与业务订单金额不一致"),
    EXTERNAL_PAY_VALIDATE_ORDER_CALL_FAIL(126112, "第三方支付-校验订单信息,接口调用失败"),
    EXTERNAL_LIVE_TICKET_PAY_VALIDATE_ORDER_CALL_FAIL(1261121, "第三方支付-直播老板上座支付订单不一致或不存在"),
    EXTERNAL_LIVE_BID_PAY_VALIDATE_ORDER_CALL_FAIL(1261122, "第三方支付-直播老板竞价支付订单不一致或不存在"),
    EXTERNAL_PAY_VALIDATE_ORDER_OUT_TRADE_NO_NOT_EQUAL(126113, "第三方支付-校验订单信息,业务订单号不一致"),

    //退款订单状态校验
    EXTERNAL_REFUND_ORDER_CANCEL(126114, "第三方支付-退款-退款订单已经取消"),
    EXTERNAL_REFUND_ORDER_SUCCESS(126115, "第三方支付-退款-退款订单已经完成"),
    EXTERNAL_REFUND_ORDER_FAIL(126116, "第三方支付-退款-退款订单已经失败"),
    EXTERNAL_REFUND_ORDER_NOT_ENOUGH_REFUND(126117, "申请退款-本次退款金额大于可退金额"),
    EXTERNAL_REFUND_ORDER_NOT_FOUND(126118, "第三方支付-退款订单不存在"),

    //退款时支付订单校验
    EXTERNAL_REFUND_PAY_ORDER_UNPAID(126121, "第三方支付-退款-支付订单未支付"),
    EXTERNAL_REFUND_PAY_ORDER_NOT_EXIST(126122, "第三方支付-退款-支付订单不存在"),
    EXTERNAL_REFUND_PAY_ORDER_CLOSING(126123, "第三方支付-退款-支付订单关闭中"),
    EXTERNAL_REFUND_PAY_ORDER_CLOSED(126124, "第三方支付-退款-支付订单已经关闭"),
    EXTERNAL_REFUND_PAY_ORDER_CANCELING(126125, "第三方支付-退款-支付订单撤销中"),
    EXTERNAL_REFUND_PAY_ORDER_CANCEL(126126, "第三方支付-退款-支付订单已经撤销"),
    EXTERNAL_REFUND_PAY_ORDER_FINISHED(126127, "第三方支付-退款-支付交易结束，不可退款"),
    EXTERNAL_REFUND_ORDER_NOT_EXIST(126128, "第三方支付-退款订单不存在"),

    //关闭时，支付订单校验
    EXTERNAL_CLOSE_PAY_ORDER_NOT_EXIST(126131, "第三方支付-关闭-支付订单不存在"),
    EXTERNAL_CLOSE_PAY_ORDER_SUCCESS(126132, "第三方支付-关闭-支付订单已经支付"),
    EXTERNAL_CLOSE_PAY_ORDER_CLOSING(126133, "第三方支付-关闭-支付订单正在申请取消中"),
    EXTERNAL_CLOSE_PAY_ORDER_CLOSED(126134, "第三方支付-关闭-支付订单已经关闭"),
    EXTERNAL_CLOSE_PAY_ORDER_CANCELING(126135, "第三方支付-关闭-支付订单正在申请取消中"),
    EXTERNAL_CLOSE_PAY_ORDER_CANCEL(126136, "第三方支付-关闭-支付订单已经取消"),
    EXTERNAL_CLOSE_PAY_ORDER_FINISHED(126137, "第三方支付-关闭-支付交易结束，不可关闭"),

    //撤销时，支付订单状态校验
    EXTERNAL_CANCEL_PAY_ORDER_NOT_EXIST(126141, "第三方支付-撤销-支付订单不存在"),
    EXTERNAL_CANCEL_PAY_ORDER_SUCCESS(126142, "第三方支付-撤销-支付订单已支付，请调用退款接口进行处理"),
    EXTERNAL_CANCEL_PAY_ORDER_CLOSING(126143, "第三方支付-撤销-支付订单正在申请关闭中"),
    EXTERNAL_CANCEL_PAY_ORDER_CLOSED(126144, "第三方支付-撤销-支付订单已经关闭"),
    EXTERNAL_CANCEL_PAY_ORDER_CANCELING(126145, "第三方支付-撤销-支付订单正在申请撤销中"),
    EXTERNAL_CANCEL_PAY_ORDER_CANCEL(126146, "第三方支付-撤销-支付订单已经撤销"),
    EXTERNAL_CANCEL_PAY_ORDER_FINISHED(126147, "第三方支付-撤销-支付交易结束，不可撤销"),

    //结算时，支付订单状态校验
    EXTERNAL_SETTLEMENT_PAY_ORDER_UNPAID(126151, "第三方支付-结算-订单未支付"),
    EXTERNAL_SETTLEMENT_PAY_ORDER_CLOSING(126152, "第三方支付-结算-订单关闭中"),
    EXTERNAL_SETTLEMENT_PAY_ORDER_CLOSED(126153, "第三方支付-结算-订单已经关闭"),
    EXTERNAL_SETTLEMENT_PAY_ORDER_CANCELING(126154, "第三方支付-结算-订单撤销中"),
    EXTERNAL_SETTLEMENT_PAY_ORDER_CANCEL(126155, "第三方支付-结算-订单已撤销"),
    EXTERNAL_SETTLEMENT_PAY_ORDER_FINISHED(126156, "第三方支付-结算-订单结束，无法结算"),

    //重构相关错误码
    EXTERNAL_BUSINESS_TYPE_NOT_SUPPORT(126100, "不支持该类型业务"),
    EXTERNAL_OPERATE_TYPE_NOT_SUPPORT(126101, "不支持该操作业务"),

    //支付宝支付
    EXTERNAL_ALIPAY_RESPONSE_IS_NULL(126200, "支付宝-返回response为空"),
    EXTERNAL_ALIPAY_CALL_FAIL(126201, "支付宝-接口调用失败"),
    EXTERNAL_ALIPAY_SEARCH_PAYMENT_CALL_FAIL(126202, "支付宝-查询支付订单调用失败"),
    EXTERNAL_ALIPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH(126203, "支付宝-查询支付订单信息业务订单号不匹配"),
    EXTERNAL_ALIPAY_REFUND_CALL_FAIL(126204, "支付宝-退款接口调用失败"),
    EXTERNAL_ALIPAY_CANCEL_CALL_FAIL(126205, "支付宝-撤销接口调用失败"),
    EXTERNAL_ALIPAY_NOTIFY_VALIDATE_FAIL(126206, "支付宝-回调验签失败"),
    EXTERNAL_ALIPAY_REFUND_SYSTEM_EXCEPTION(126207, "支付宝-退款接口，系统异常，请再次提交重试"),

    EXTERNAL_TENPAY_RESPONSE_ERROR(126300, "微信/QQ支付-返回数据错误"),
    EXTERNAL_TENPAY_SIGN_INVALIDATION(126301, "微信/QQ支付-签名无效"),
    EXTERNAL_TENPAY_RETURN_CODE_INVALIDATION(126302, "微信/QQ支付-returnCode值无效"),
    EXTERNAL_TENPAY_SIGNATURE_INVALIDATION(126303, "微信/QQ支付-无效的签名算法"),
    EXTERNAL_TENPAY_HMACSHA256_ERROR(126304, "微信/QQ支付-生成 HMACSHA256 加密异常"),
    EXTERNAL_TENPAY_MD5_ERROR(126305, "微信/QQ支付-生成 MD5 加密异常"),
    EXTERNAL_TENPAY_XML_2_MAP_ERROR(126306, "微信/QQ支付-将xml报文转化为map异常"),
    EXTERNAL_TENPAY_SANDBOX_SIGNATURE_FAIL(126307, "微信/QQ支付-微信获取沙箱秘钥，生成签名失败"),
    EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_FAIL(126308, "微信/QQ支付-微信获取沙箱秘钥，请求失败"),
    EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_RESPONSE_FAIL(126308, "微信/QQ支付-微信获取沙箱签名响应解析失败"),
    EXTERNAL_TENPAY_MAP_2_XML_ERROR(126309, "微信/QQ支付-将map转XML异常"),

    EXTERNAL_TENPAY_CREATE_PAYMENT_FAIL(126311, "微信/QQ支付-创建预支付订单失败"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_RETURN_ERROR(126312, "微信/QQ支付-预支付订单返回错误"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_CALL_FAIL(126313, "微信/QQ支付-预支付订单创建调用失败"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_SERVICE_FAIL(126314, "微信/QQ支付-预支付订单业务失败"),

    EXTERNAL_TENPAY_QUERY_PAYMENT_FAIL(126321, "微信/QQ支付-查询支付订单失败"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_RETURN_ERROR(126322, "微信/QQ支付-查询支付订单返回错误"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_CALL_FAIL(126323, "微信/QQ支付-查询支付订单创建调用失败"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_SERVICE_FAIL(126324, "微信/QQ支付-查询支付订单业务失败"),

    EXTERNAL_TENPAY_REFUND_FAIL(126331, "微信/QQ支付-发起退款失败"),
    EXTERNAL_TENPAY_REFUND_RETURN_ERROR(126332, "微信/QQ支付-发起退款返回错误"),
    EXTERNAL_TENPAY_REFUND_CALL_FAIL(126333, "微信/QQ支付-发起退款调用失败"),
    EXTERNAL_TENPAY_REFUND_SERVICE_FAIL(126334, "微信/QQ支付-发起退款业务失败"),

    EXTERNAL_TENPAY_QUERY_REFUND_FAIL(126341, "微信/QQ支付-查询退款失败"),
    EXTERNAL_TENPAY_QUERY_REFUND_RETURN_ERROR(126342, "微信/QQ支付-查询退款返回错误"),
    EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL(126343, "微信/QQ支付-查询退款调用失败"),
    EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL(126344, "微信/QQ支付-查询退款业务失败"),

    EXTERNAL_TENPAY_CLOSE_PAYMENT_FAIL(126351, "微信/QQ支付-关闭订单失败"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_RETURN_ERROR(126352, "微信/QQ支付-关闭订单返回错误"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_CALL_FAIL(126353, "微信/QQ支付-关闭订单调用失败"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_SERVICE_FAIL(126354, "微信/QQ支付-关闭订单业务失败"),

    EXTERNAL_TENPAY_SEARCH_PAYMENT_ORDER_FAIL(126360, "微信/QQ支付-查询支付订单信息业务订单号失败"),
    EXTERNAL_TENPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH(126361, "微信/QQ支付-查询支付订单信息业务订单号不匹配"),

    EXTERNAL_TENPAY_NOTIFY_REQUEST_FAIL(126371, "微信/QQ支付-回调获取参数异常"),
    EXTERNAL_TENPAY_PAYMENT_NOTIFY_FAIL(126372, "微信/QQ支付-支付回调结果失败"),

    UNSUPPORT_PAY_CHANNEL(129999, "暂未支持该支付渠道"),

    UNAUTH_P_CHANNEL(129998, "未授权的应用"),

    PAY_CHANNEL_IS_DISABLE(129997, "该支付渠道已被禁用"),

    APP_PAY_CHANNEL_IS_CLOSED(129996, "该应用的支付权限已被关闭"),

    PAY_CHANNEL_SETTING_NOT_CONFIG(129995, "暂未设置该支付渠道的配置信息"),

    APPID_AND_SOURCE_IS_NOT_MATCH(129994, "该APPID没有对应的sourceID可以匹配"),

    //风控错误码
    RISK_DICT_NOT_INIT(130001, "风控数据字典没有初始化~"),
    RISK_RATING_SERVICE_FAIL(130002, "风控服务不可用"),
    RISK_RATING_VALIDATE_FAILED(130003, "风控校验失败: %s"),
    GLOBAL_RISK_SWITCH_CLOSED(133333, "风控总关开为关闭状态"),
    FREEGE_USER_ALREADY_EXISTED(133334, "冻结失败，用户已进入冻结黑名单"),
    LOGIN_CHANCE_GIVEN_REJECT(133335, "您今日登录获得免费机会已达上限"),

    //订单收益分成结算
    ORDER_INCOME_REPEAT(140001, "订单重复结算"),

    ORDER_INCOME_RECODR_NULL(140002, "订单不存在"),
    ORDER_INCOME_RATIO(140003, "订单分成比例查询异常"),
    INVALID_DEDUCT_RATIO_VALUE(140004, "抽成比例值异常，只能为0-1之间"),

    LIVE_INCOME_RATIO(1400031, "直播分成比例查询异常"),

    INVALID_LIVE_RATIO_VALUE(1400032, "直播分成类型异常,类型只能为8-9之间"),

    BANNER_CONFIG_NOT_EXIST(150001, "banner配置不存在"),

    CHICKENPOINT_CONFIG_NOT_EXIST(150002, "积分任务不存在"),

    SHARE_INVITE_ALREADY_ONLINE(150003, "邀请分享任务已经上架，请勿重复上架"),

    FREE_TEAM_HOME_ADVERTISE_NOT_EXIST(150004, "暴鸡车队该首页宣传图不存在"),

    FREE_TEAM_HOME_SCROLL_TEMPLATE_NOT_EXIST(150005, "暴鸡车队该滚动模板不存在"),

    //底部tab参数列表为空
    FOOT_TAB_ARGS_EMPTY(160000, "底部tab参数列表为空"),

    FOOT_TAB_INSIDE_ARGS_EMPTY(160001, "底部tab内部服务参数列表为空"),

    DUPLICATE_TAB_ORDER_INDEX(160003, "排序号[%d]重复"),

    DUPLICATE_TAB_ITEM_ORDER_INDEX(160004, "所有Tab[{0}]中，排序号[{1}]重复"),

    // 暴鸡车队
    FREE_TEAM_TYPE_NOT_FOUND(170001, "暴鸡车队类型[%s]不存在"),

    BAOJI_DAN_RANGE_NOT_FOUND(170002, "暴鸡接单范围不存在"),

    BAOJI_IDENTITY_NOT_MATCH_FREE_TEAM(170003, "可组建的身份不符合该车队要求"),

    FREE_TEAM_TIMES_ERROR(170004, "暴鸡车队上车次数异常"),
    FREE_TEAM_DEVICE_WHITE_ERROR(170005, "该设备已在白名单中，请勿重复添加"),
    COIN_CONSUME_AWARD_PAY_ORDERNO_DUPLICATE(17006, "消费暴鸡币订单重复"),
    FREE_TEAM_DEVICE_WHITE_NOT_EXIST(170007, "该设备不存在"),
    FREE_TEAM_UID_NOT_NULL(170008, "暴鸡车队成员uid不能为空"),
    SHUMEI_API_CALL_FAIL(170009, "调用数美API返回结果异常"),
    IMMACHINE_DEVICE_ALREADY_EXIST(170010, "该设备已在黑名单中，无需重复添加"),
    FREE_TEAM_COUPONS_ERROR(170011, "插入免费券失败"),
    FREE_TEAM_TYPE_ERROR_FOUND(170012, "查询免费车队类型失败,请稍后再试"),

    //专属单
    EXCLUSIVE_INVITE_FAIL(180000, "邀约失败"),
    EXCLUSIVE_ORDER_EXIST(180001, "邀约的专属订单已存在"),
    EXCLUSIVE_FREE_CHANCE_FAIL(180002, "消耗免费次数异常"),
    EXCLUSIVE_FREE_CHANCE_NOT_ADEQATE(180003, "当前免费次数不足，去邀请好友获得更多免费次数吧"),
    EXCLUSIVE_CACHE_ORDER_FAIL(180005, "缓存专属订单信息失败"),
    EXCLUSIVE_GET_CACHE_ORDER_FAIL(180006, "获取缓存的专属订单信息失败"),
    EXCLUSIVE_CACHE_RELATION_FAIL(180007, "缓存暴鸡与老板订单关系失败"),
    EXCLUSIVE_GET_CACHE_ALL_RELATION_FAIL(180008, "获取暴鸡与所有老板订单关系失败"),
    EXCLUSIVE_GET_CACHE_RELATION_FAIL(180009, "获取暴鸡与老板订单关系失败"),
    EXCLUSIVE_CACHE_WAIT_AGREE_TIMEOUT_FAIL(180010, "缓存超时未接单的过期时间失败"),
    EXCLUSIVE_GET_CACHE_WAIT_AGREE_TIMEOUT_FAIL(180011, "获取超时未接单的过期时间失败"),
    EXCLUSIVE_RETURN_FREE_COUNTS_FAIL(180012, "返还消费的优惠券失败"),
    EXCLUSIVE_AGREE_TIMEOUT_FAIL(180013, "暴鸡待超时未接单，处理异常"),
    EXCLUSIVE_HAS_RELATION(180014, "对方还没有结束之前的订单，请稍后再邀约"),
    EXCLUSIVE_CREATE_ORDER_FAIL(180015, "生成专属订单信息失败"),
    EXCLUSIVE_GAME_CODE_EMPTY(180016, "数字字典库中无此游戏Code"),
    EXCLUSIVE_ORDER_NOT_STARTED(180017, "订单不在进行中"),
    EXCLUSIVE_GAME_CONFIG_FAIL(180012, "获取专属单所属游戏配置失败"),
    EXCLUSIVE_GAME_NOT_CERTIFIED(180019, "对方尚未认证游戏哦~"),
    EXCLUSIVE_ITSELF(180022, "老鬼你邀约自己了"),
    EXCLUSIVE_REFUND_FAIL(180021, "返还支付的金额或暴鸡币失败"),
    EXCLUSIVE_SETTLEMENT_TYPE_ERR(180022, "专属单配置无此游戏的结算类型"),
    EXCLUSIVE_REMAINING_AMOUNT(180023, "获取余额失败"),
    EXCLUSIVE_CALC_AFTER_VALUE_ERR(180024, "获取抽成失败"),
    EXCLUSIVE_REFUND_ERR(180025, "发起退款请求失败"),
    EXCLUSIVE_EXIST_UNCONFIRM_ORDER(180026, "您有订单等确认，请先确认之前的订单"),
    EXCLUSIVE_EXIST_COMPLAIN_ORDER(180027, "您有订单在投诉中，请耐心等待投诉结果"),

    // 云账户提现 19打头
    CLOUDPAY_ORDER_EXISTS(190001, "云账户提现-提现订单已经存在"),
    CLOUDPAY_ORDER_ENCRYPT_ERROR(190002, "云账户提现-构建加密参数异常"),
    CLOUDPAY_ORDER_NETWORK_ERROR(190003, "云账户提现-网络异常"),
    CLOUDPAY_BALANCE_ENCRYPT_ERROR(190004, "查询商户余额-构建加密参数异常"),
    CLOUDPAY_BALANCE_NETWORK_ERROR(190005, "查询商户余额-网络异常"),
    CLOUDPAY_ORDER_NOT_FUND(190006, "云账户提现-提现订单不存在"),
    CLOUDPAY_ORDER_AMOUNT_NOTEQUAL(190007, "云账户提现-提现订单金额不一致"),
    CLOUDPAY_ORDER_RETURN_ERROR(190008, "云账户提现-提交下单请求返回失败"),
    CLOUDPAY_ORDER_QUERY_NETWORK_ERROR(190009, "查询提现订单状态-网络异常"),
    CLOUDPAY_ORDER_QUERY_RETURN_ERROR(190010, "云账户提现-查询订单请求返回失败"),
    CLOUDPAY_ORDER_RETURN_NULL(190011, "云账户提现-下单请求返回为空"),
    CLOUDPAY_BALANCE_RETURN_NULL(190012, "云账户提现-查询商户余额请求返回为空"),
    CLOUDPAY_QUERY_RETURN_NULL(190013, "云账户提现-查询订单状态请求返回为空"),
    CLOUDPAY_MONEY_LESSTHAN_1_YUAN(190014, "云账户提现-提现至微信钱包金额不能小于1元"),
    CLOUDPAY_CHANNEL_NOT_SUPPORT(190015, "云账户提现-暂不支持该渠道提现"),
    WITHDRAW_OVER_LIMIT(190016, "每天订单收益只能提现1次，请明天再来哦~"),
    WITHDRAW_GIFT_LIMIT(1900161, "每天打赏收益只能提现1次，请明天再来哦~"),
    WITHDRAW_AMOUNT_MIN(190017, "提现金额最低为[%s]元"),
    WITHDRAW_AMOUNT_MAX(190018, "单次提现金额不能超过2000哦~"),
    WITHDRAW_AUDIT_NOT_EXIST(190019, "提现审批记录不存在"),
    WITHDRAW_AUDIT_STATE_NOT_WAIT(190020, "提现申请已审批完成，请勿重复审批"),
    WITHDRAW_BLOCK_STATE_NOT_YET(190021, "提现申请已被截停，审批失败"),
    WITHDRAW_BLOCK_STATE_NOT_WAIT(190022, "提现已审批完成，截停失败"),
    WITHDRAW_BLOCK_MIS_OPERATION(190023, "错误操作"),
    WITHDRAW_FUNCTION_CLOSED(190024, "提现功能已关闭"),
    WITHDRAW_AUDIT_QUERY_ALREADY(190025, "该提现记录已执行过查询操作"),
    WITHDRAW_QUERY_USER_IDENTITY_ERROR(190026, "提现时查询用户身份信息，user服务返回失败"),
    WITHDRAW_IDENTITY_STUDIO_ERROR(190027, "工作室账户无法提现，请与工作室管理员联系"),
    WITHDRAW_CALL_RISKRATING_ERROR(190028, "提现时查询提现黑名单，risk服务返回失败"),
    WITHDRAW_QUERY_USER_BLACKITEM_ERROR(190029, "该用户在提现黑名单中，已限制提现"),
    WITHDRAW_CALL_USER_ERROR(190030, "提现时验证用户实名信息，user服务返回失败"),
    WITHDRAW_QUERY_USER_IDCARD_ERROR(190031, "请先实名认证"),
    WITHDRAW_QUERY_USER_BOSS_ERROR(190032, "您不是暴鸡身份，不可进行暴鸡值提现哦~"),
    WITHDRAW_CALL_USER_BIND_ERROR(190033, "提现时查询用户账号绑定信息，user服务返回失败"),
    WITHDRAW_QUERY_USER_BIND_ERROR(190034, "您尚未绑定提现微信账号，请先进行绑定~"),
    WITHDRAW_QUERY_USER_STUDIO_ERROR(190035, "您当前为工作室暴鸡身份，不可进行暴鸡值提现哦~"),
    WITHDRAW_QUERY_USER_FREEZE_ERROR(190036, "您的账号被冻结，请联系官方客服"),
    // 暴击值兑换订单-新增校验
    COMPLAIN_ORDER_EXIST_FAIL(300001, "兑换失败，存在进行中的订单"),
    EXCHANGE_AUTH_USER_ERROR(300002, "暴击值兑换暴鸡币，查询用户身份失败"),

    // 活动相关
    ACTIVITY_ALERT_CONFIG_NOT_FOUND(350000, "未查询到活动弹窗配置"),

    //消息推送相关错误码
    PUSH_MESSAGE_FAIL(400000, "消息推送，消息推送失败"),
    PUSH_MESSAGE_URL_EMPTY(400001, "消息推送，后续动作不为APP首页、不跳转时，跳转URL不能为空"),
    PUSH_MESSAGE_TAG_HAS_NO_USER(400002, "消息推送，tagName没有关联的用户id"),
    PUSH_MESSAGE_FILE_USERID_IS_EMPTY(400003, "消息推送，上传的文件用户id为空"),
    USER_TAG_NOT_EXIST(400004, "用户标签，用户标签不存在"),
    USER_TAG_USER_ID_IS_EMPTY(400005, "用户标签，上传文件中用户ID为空"),
    USER_TAG_NAME_IS_EXIST(400006, "用户标签，用户标签已经存在"),
    USER_TAG_FILE_READ_EXCEPTION(400007, "用户标签，读取文件异常"),
    USER_TAG_FILE_SERVER_EXCEPTION(400008, "用户标签，无法连接到文件所在的服务器"),
    USER_TAG_FILE_IS_EMPTY(400009, "用户标签，新增标签时，标签文件不能为空"),
    USER_TAG_FAIL(400010, "用户标签，打标签失败"),
    SYSTEM_MESSAGE_CALL_FAIL(400011, "发送系统消息失败"),
    SYSTEM_MESSAGE_OVER_RATE_LIMIT(400012, "发送系统消息超频"),
    GET_USER_RONG_YUN_ID_FAIL(400013, "获取用户融云ID失败"),
    USER_TAG_TAG_NAME_ERROR(400014, "用户标签，用户标签名只能由字母、数字、中文组成，长度不能超过20"),
    PUSH_MESSAGE_UPDATE_FAILED(400015, "消息推送，更新失败"),
    GET_USER_TAG_FAILED(40016, "获取用户标签失败"),
    PUSH_MESSAGE_TAG_IS_EMPTY(400017, "消息推送，非全量推送时，用户标签不能为空"),
    PUSH_MESSAGE_NO_USER(400018, "消息推送，没有接收消息的用户id"),
    PUSH_MESSAGE_NOT_SUPPORT_TARGET(400019, "消息推送，暂不支持该种推送目标"),
    PUSH_MESSAGE_SET_TEMP_TAG_FAIL(400020, "消息推送，发送临时tag失败"),
    USER_NOT_EXIST_THIS_TAG(400021, "消息推送，用户不存在这个标签"),
    USER_TAG_NOT_EFFECTIVE(400022, "消息推送，用户标签还未全部生效，请稍等"),


    //礼物配置
    GIFT_CONFIG_NOT_EXIST(400101, "礼物配置，该ID对应的礼物配置不存在"),

    RESOURCE_GIFT_NOT_SET(400102, "礼物配置，没有设置礼物信息"),

    RESOURCE_GIFT_NOT_AVAILABLE(400103, "礼物配置，没有可用的礼物！"),

    RESOURCE_GIFT_AMOUNT_NOT_SET(400104, "礼物配置，礼物数量配置不存在"),

    RESOURCE_GIFT_OBTAIN_FAIL(400105, "礼物配置，获取礼物配置失败"),

    RESOURCE_GIFT_NAME_EXIST(400106, "礼物配置，礼物名称已经存在"),

    RESOURCE_REWARD_ENTRANCE_NOT_SET(400201, "打赏入口配置,没有设置打赏入口信息"),

    RESOURCE_REWARD_ENTRANCE_NAME_EXIST(400202, "打赏入口配置,打赏入口名称已经存在"),

    //暴鸡币支付配置
    ANDROID_PAY_RATE_CONFIG_NOT_EXIST(400301, "安卓支付，没有设置暴鸡币支付比例配置"),

    ANDROID_PAY_AMOUNT_CONFIG_NOT_EXIST(400302, "安卓支付，没有设置暴鸡币支付数量配置"),

    ANDROID_PAY_RATE_CONFIG_FAIL(400303, "安卓支付，获取暴鸡币支付比例配置失败"),

    DICT_CATEGORY_CONFIG_NOT_INIT(180001, "数据字典分类没有初始化"),

    DICT_CATEGORY_CONFIG_NOT_MATCH(180002, "数据字典分类不匹配"),

    DICT_POSITION_ERROR(180003, "投放位置代码错误"),

    DICT_USER_TYPE_ERROR(180004, "用户类型错误"),

    IM_MSG_SEND_ERROR(200001, "im消息发送失败"),
    IM_MSG_STATE_ENUM_ERROR(200002, "im消息状态枚举错误"),

    //core相关错误码
    CORE_CREATE_VERSION_LOG_FAILED(210001, "插入版本日志记录失败"),

    CORE_UPDATE_VERSION_LOG_FAILED(210002, "更新版本日志记录失败"),

    CORE_DISABLE_VERSION_LOG_FAILED(210003, "停用版本日志记录失败"),

    CORE_ENABLE_VERSION_LOG_FAILED(210004, "启用版本日志记录失败"),

    CORE_DELETE_VERSION_LOG_FAILED(210005, "删除版本日志记录失败"),

    CORE_VERSION_LOG_HAS_DELETED(210006, "删除版本日志记录失败"),

    CORE_FEEDBACK_INSERT_FAILED(200007, "插入反馈信息失败"),

    CORE_VERSION_NUMBER_ILLEGAL(200008, "版本号非法"),

    CORE_FEEDBACK_REPLY_REPEAT(200009, "该条反馈建议已回复，请勿重新提交"),

    CORE_FEEDBACK_REPLY_FAILED(200010, "提交反馈回复内容失败"),

    //market相关错误码
    MARKET_CREATE_CHANNEL_LOG_FAILED(220001, "插入渠道推广url失败"),

    MARKET_UPDATE_CHANNEL_LOG_FAILED(220002, "更新渠道推广url失败"),

    MARKET_COLLECT_USER_PHONE_FAILED(220003, "请填写正确的手机号"),

    MARKET_COLLECT_USER_PHONE_REPEAT(220004, "该手机号已经参加活动，请勿重复提交"),

    MARKET_NEW_YEAR_CALL_RESOURCE_FAIL(2200005, "调用资源服务获取皮肤奖励配置失败"),

    MARKET_NEW_YEAR_REWARD_RESOURCE_EMPTY(2200006, "调用资源服务获取皮肤奖励配置为空"),

    MARKET_NEW_YEAR_REWARD_STATUS_INIT_FAIL(2200007, "初始化用户的奖励状态数据失败"),

    MARKET_NEW_YEAR_CURRENT_TEAM_MEMBER_FAIL(2200008, "获取最新的奖励组队列表异常"),

    MARKET_NEW_YEAR_CREATE_NEW_TEAM_FAIL(2200009, "创建新的组队失败"),

    //后台管理
    BACKEND_SYSTEM_CREATE_DINGDING_INVALID_UNIONID(200001, "unionId不能为空"),

    BACKEND_SYSTEM_CREATE_USER_HAS_CREATED(200002, "用户已经创建，请勿重复创建"),

    BACKEND_SYSTEM_LOGIN_DINGDING_FAILED(200003, "钉钉账户尚未绑定或注册"),

    BACKEND_SYSTEM_LOGIN_PASSWORD_ERROR_MAX_TIME(200004, "您好，您今日输错密码次数超过3次，已自动冻结您的账号，请联系管理员！"),

    BACKEND_SYSTEM_LOGIN_ERROR_PASSWORD(200005, "密码错误，请重试"),

    BACKEND_SYSTEM_TOKEN_ENCRYPT_FAILED(200006, "token加密失败"),

    BACKEND_SYSTEM_EMAIL_INAVLID_FORMAT(200007, "邮箱格式不正确"),

    BACKEND_SYSTEM_USER_NOT_REGISTER(200008, "该用户尚未注册"),

    BACKEND_SYSTEM_USER_STATUS_ILLEGAL(200009, "用户状态非法"),

    //打赏
    REWARD_ITERATIVE(230001, "请勿重复提交"),

    REWARD_INVALID_PRE_ID(230002, "无效的预打赏单号"),

    REWARD_MISSING_SEQUENCE(230003, "缺少序列号"),

    REWARD_MISSING_ROOM_NUM(230004, "缺少房间号"),

    REWARD_MISSING_GROUP_ID(230005, "缺少群组ID"),

    REWARD_MISSING_GAME_CODE(230006, "缺少游戏编码"),

    REWARD_ILLEGAL_SCENE(230007, "不支持的打赏场景"),

    REWARD_MISSING_ORDER_TYPE(230009, "缺少订单类型"),

    REWARD_ILLEGAL_REWARD_YOURSELF(230010, "不能给自己打赏"),

    //market新年拉新活动
    NEW_YEAR_TEAM_NON_EXIST(240001, "助力车队不存在"),

    NEW_YEAR_TEAM_CONFIG_EXIST(240002, "资源配置不存在"),

    NEW_YEAR_USER_NOT_QUALIFICATION(240003, "无助力资格"),

    NEW_YEAR_USER_NOT_QUALIFICATION_RECEIVE(240004, "无领取资格"),

    // 键盘快捷语
    CUSTOM_KEYBOARD_QUICK_REPLY_OUT_OF_MAX(250000, "最多只能自定义%s条快捷语哦"),

    CUSTOM_KEYBOARD_QUICK_REPLY_NOT_FOUND(250001, "未查询到您的自定义快捷语"),

    /**
     * 暴鸡房间已创建
     */
    BAOJI_ROOM_HAS_CREATED(260000, "暴鸡房间已创建"),

    /**
     * 暴鸡房间创建失败
     */
    BAOJI_ROOM_CREATE_FAIL(260001, "暴鸡房间创建失败"),
    /**
     * 用户在其他房间
     */
    USER_IN_OTHER_ROOM(260002, "您已在房间[%s]内"),

    /**
     * 用户不在当前房间
     */
    USER_NOT_IN_CURRENT_ROOM(260003, "您不在当前房间"),
    /**
     * 房间已满员
     */
    BAIJI_ROOM_ISFULLED(260004, "房间已满员"),

    /**
     * 房间位置不足
     */
    ROOM_POSITION_NOT_ADEQUATE(260005, "房间位置不足"),

    /**
     * 房间位置不能少于房间人数
     */
    ROOM_SIZE_CANNOT_LT_USERS(260006, "房间位置不能少于房间人数"),
    /**
     * 房主才可以进行的操作
     */
    ROOM_NOT_OWNER_WARNING(260007, "房主才可以进行的操作"),

    /**
     * 不支持的操作
     */
    ROOM_OPT_NOT_SUPORT(260008, "不支持的操作"),

    /**
     * 房间有其他车队
     */
    ROOM_HAS_OTHER_BUSNESS(260009, "房间有其他正在进行的业务"),

    /**
     * 房间人数大于免费车队最大人数配置
     */
    ROOM_USER_GT_MAX_TEAM_SIZE(260010, "当前房间人数大于免费车队最大人数配置"),

    /**
     * 队长不能在有车队的情况下离开房间
     */
    ROOM_LEADER_CANNOT_LEAVE_ON_BUSINESS(260011, "队长不能离开车队"),

    BAOJI_ROOM_DATA_ERROR(260020, "暴鸡房间数据异常"),

    /**
     * 老板已不在房间内
     */
    BOSS_NOT_IN_ROOM(260021, "%s 已不在房间内"),

    /**
     * 老板未选择段位
     */
    DAN_IS_NOT_CHOSEN(260022, "您还没有选择段位"),

    BAOJI_ROOM_NOT_EXISITS(260023, "暴鸡房间不存在"),

    /**
     * 用户不在任何房间
     */
    USER_NOT_IN_ANY_ROOM(260099, "您不在任何房间内"),

    //投诉申诉 [270000-272000)
    COMPLAIN_BAOJI_IS_MISSING(270000, "请填写暴鸡uid"),
    COMPLAIN_TIME_EXPIRED(270001, "已过投诉有效时间，无法投诉"),
    COMPLAIN_BAOJI_MISMATCH(270002, "投诉的暴鸡不在车队中，无法投诉"),
    COMPLAIN_ITEM_EXISTS(270003, "投诉项已存在"),
    COMPLAIN_ITEM_NOT_EXIST(270004, "投诉项不存在"),
    COMPLAIN_RECORD_NOT_EXIST(270005, "投诉记录不存在"),
    COMPLAIN_ITEM_STATUS_ERROR(270006, "已超过可申诉时间，无法提交"),
    COMPLAIN_APPEAL_OPERATION_DATE_INVALID(270007, "投诉截止时间[%s]未到，暂不能处理"),
    COMPLAIN_APPEAL_OPERATION_NOT_ALL(270008, "投诉未全部处理完成，暂不能提交"),
    COMPLAIN_APPEAL_DEAL_ERROR(270009, "队长或暴鸡尚未提交全部申诉资料，暂不能处理"),
    COMPLAIN_RECORD_ALREADY_FINISH(270010, "投诉单已处理完成，不能再次修改"),
    COMPLAIN_REFUND_AMOUNT_ERROR(270011, "退款数量不能大于下单数量"),
    COMPLAIN_RANGE_DAN_IS_NULL(270012, "接单范围不能为空"),
    COMPLAIN_PLAYER_MISMATCH(270010, "投诉的老板不在车队中，无法投诉"),
    COMPLAIN_APPEAL_ITEM_EXISTS(2701000, "申诉项已存在"),
    COMPLAIN_APPEAL_USER_INVALID(2701001, "暴鸡账号不匹配，无权申诉"),
    COMPLAIN_APPEAL_USER_NOT_LEADER(2701002, "该暴鸡不是队长，无权申诉"),


    //付费车队 [273000-274000)
    PREMADE_IN_OTHER_ROOM(273000, "已经在其他车队中"),
    RREMADE_SENSITIVE_WORDS(273001, "车队标题中含有敏感信息"),

    RREMADE_ILLEGAL_IDENTITY_CREATE(273002, "当前身份[%s]不能创建车队"),

    RREMADE_HAS_NO_GAME_CERTIFICATIONS(273003, "很抱歉，你当前身份没有认证该游戏"),

    RREMADE_ILLEGAL_GAME_DAN(273004, "注意！左侧段位不可高于右侧段位"),

    PREMADE_LEAVE_ERROR(273005, "注意，你在App中已经创建车队，请回到App进行操作"),

    PREMADE_LEAVE_WHEN_RUNNING(273006, "车队已经发车"),

    RREMADE_ILLEGAL_IDENTITY_LEAVE(273007, "状态错误，不能离开车队"),

    RREMADE_ILLEGAL_STATUS_PAY(273008, "车队状态[%s]，不能进行支付"),

    RREMADE_NOT_ALL_IN_PAY(273009, "需等到全部队员准备才可以支付"),

    RREMADE_USER_NOT_IN(273010, "当前用户不是车队成员"),

    RREMADE_ILLEGAL_IDENTITY_PAY(273011, "用户身份[%s]不能支付"),

    RREMADE_ALREADY_PAY(273012, " 已完成支付！"),

    RREMADE_IN_FREE_TEAM(273013, "您正在免费车队中，不能同时使用金牌车队"),
    PREMADE_IN_PAY_TEAM(273014, "您正在金牌车队中，不能同时使用免费车队"),
    PREMADE_REMOTE_INVOKE_FAILED(273016, "远程接口调用失败 invoke=%s"),
    PREMADE_REMOTE_BIZ_FAILED(273017, "远程接口业务失败 errMsg=[%s]"),


    PREMADE_TOKEN_ERROR(2800000, "车队，用户登录超时"),
    PREMADE_ROLE_GAME_ERROR(2800001, "你当前身份没有认证该游戏"),
    PREMADE_GAME_MISSING(2800002, "暴鸡电竞缺少游戏配置"),
    PREMADE_BAOJI_GAME_ERROR(2800003, "暂不支持暴鸡进入该类型车队"),
    PREMADE_GAME_DAN_ERROR(2800004, "你的游戏段位不符合车队要求"),
    PREMADE_FULL_ERROR(2800005, "车队已满"),
    PREMADE_BN_GAME_ERROR(2800006, "暂不支持暴娘进入该上分车队"),
    PREMADE_GAME_TYPE_ERROR(2800007, "游戏类型错误"),
    PREMADE_GAME_ZONE_ERROR(2800008, "游戏大区错误"),
    PREMADE_NULL_ERROR(2800009, "车队已发车"),
    PREMADE_DISCOUNT_ERROR(2800010, "该游戏折扣信息没配"),
    PREMADE_STATUS_ERROR(2800011, "开车后不能调整车队位置"),
    ONLY_CAPTAIN_CAN_BAN(2800012, "只有队长能关闭车位"),
    ONLY_CAPTAIN_CAN_OPEN(2800013, "只有队长能打开车位"),
    PREMADE_POSTION_LESS(2800014, "房间至少需要保留两个位置"),
    NO_BOSS_POSITION(2800015, "至少有一名老板才能开车"),
    NO_CAN_OPNE_POSITION(2800016, "无可打开车位"),
    NO_POSITION_BAN(2800017, "无位置可关闭"),
    PREMADE_JOIN_ERROR(2800018, "不能重复上车"),
    PREMADE_ORDER_NOT_FOUND(2800019, "车队订单不存在"),
    PREMADE_IM_GROUP_ERROR(2800020, "IM群组加入失败"),
    PREMADE_ILLEGAL_PAY_CHANNEL(2800021, "不支持的支付渠道[%s]"),
    PREMADE_RESULT_UNCOMMIT(2800022, "还有车队单未填写结果"),
    PREMADE_BB_GAME_MISSING(2800023, "缺少游戏[%s]配置"),
    CHANNEL_RECEIVED(2800024, "今日已经领取过了"),
    CHECK_SIGN_FAIL(2800025, "签名校验失败！"),


    //直播拍卖 [2900000-300000)
    AUCTION_STATUS_ILLEGAL(2900000, "错误的状态"),
    AUCTION_ITEM_MISSING(2900001, "错误的拍卖号[%s]"),
    AUCTION_ENDED(2900002, "拍卖已结束"),
    AUCTION_NO_PERMIT(2900003, "请刷票上座"),
    AUCTION_BID_ILLEGAL_LAST_BID(2900004, "不能低于上次出价"),
    AUCTION_STATUS_ILLEGAL_TO_PREHEAT(2900005, "拍卖[%s]状态[%s]不能进行预热"),
    AUCTION_MAX_PRICE(2900006, "超过最高起拍价限制"),
    AUCTION_MAX_BID(2900007, "超过最高出价限制"),
    AUCTION_ILLEGAL_ROOM(2900008, "直播房间[%s]信息错误"),
    AUCTION_EXISTS_ACTIVE_TO_APPLY(2900009, "还有未完成的拍卖"),
    AUCTION_STATUS_ILLEGAL_TO_HANDLE_APPLY(2900010, "拍卖状态发生变化，不能处理上麦申请"),
    AUCTION_UID_ILLEGAL_TO_AUCTIONING(2900011, "你不是主播本人，不能处理上麦申请"),
    AUCTION_STATUS_WAITING(2900012, "拍卖还没开始惹"),
    AUCTION_STATUS_DONE(2900012, "拍卖已结束"),
    AUCTION_STATUS_CLOSED(2900012, "拍卖已关闭"),
    AUCTION_BID_ILLEGAL_STATING_PRICE(2900013, "不能低于起拍价"),
    AUCTION_STATUS_TO_COUNTDOWN(2900014, "拍卖[%s]状态[%s]不能开启倒计时"),
    AUCTION_ILLEGAL_PRESENTER(2900015, "你不是当前房间主持人"),
    AUCTION_IDENTITY_ILLEGAL_JOIN(2900016, "错误的身份类型"),
    AUCTION_IDENTITY_ILLEGAL_PRESENTER_JOIN(2900017, "没有获取当前房间的主持资格"),
    AUCTION_EXISTS_PRESENTER_JOIN(2900018, "当前房间已有主持在直播"),
    AUCTION_IDENTITY_ILLEGAL_STREAMER_JOIN(2900019, "主播上麦功能暂未全面开放，请加Q群【996276959】申请主播权限"),
    AUCTION_EXISTS_STREAMER_JOIN(2900020, "当前房间已有主播在直播"),
    AUCTION_UID_ILLEGAL_TO_APPLY(2900021, "你已经是当前房间的主持，不能直播"),
    AUCTION_UID_ILLEGAL_TO_PRESIDE(2900022, "你已经申请进行直播，不能主持"),
    AUCTION_EXISTS_ONLINE_TO_PRESIDE(2900023, "你已经在其他房间主持"),
    AUCTION_PRESENTER_TO_TICKET(2900024, "主持人不能刷票上座"),
    AUCTION_STREAMER_TO_TICKET(2900025, "自己不能刷票上座"),
    AUCTION_START_COUNTDOWN_FAILED(2900026, "开启倒计时失败"),
    AUCTION_STARTING(2900027, "当前房间有正在进行中的拍卖"),
    AUCTION_STATUS_TO_INVITE(2900028, "拍卖[%s]状态[%s]不能邀请上麦"),
    AUCTION_STATUS_TO_CHANCEL_INVITE(2900029, "拍卖[%s]状态[%s]不能取消邀请上麦"),
    AUCTION_WAITING(2900030, "拍卖未开始"),
    AUCTION_IDENTITY_PERMISSION_DENIED(2900031, "权限不足"),
    AUCTION_LIVE_AUCTION_COMMISSION_CONFIG_NOT_EXIST(2900032, "缺少直播抽成配置"),
    AUCTION_DESCRIPTION_ILLEGAL(2900033, "服务内容含有敏感词，请修改后重新提交"),
    AUCTION_UID_TO_UPDATE(2900034, "你不是当前拍卖的主播，不能进行修改"),
    AUCTION_STATUS_TO_UPDATE(2900035, "拍卖已开始，不能进行修改"),
    AUCTION_WAITING_MISSING_IN_ROOM(2900036, "你在当前房间没有等待中的拍卖"),
    AUCTION_PRESENTER_OFFLINE_TO_TICKET(2900037, "主持人不在线不能刷票上座"),
    AUCTION_STATUS_TO_CANCEL(2900038, "拍卖已开始，不能取消"),

    AUCTION_PERSON_NOT_EXIST(2910000, "未找到对应的数据"),
    AUCTION_PERSON_EXISTS(2910001, "数据已存在"),
    AUCTION_CREATE_CHAT_ROOM_FAIL(2910002, "创建融云聊天室失败"),

    AUCTION_ILLEGAL_TICKET_NO(2920000, "错误的票号[%s]"),
    AUCTION_ILLEGAL_BID_NO(2920001, "错误的出价单号[%s]"),

    ;


    private int errCode;
    private String errMsg;

    BizExceptionEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static BizExceptionEnum fromCode(int code) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errCode == code) {
                return c;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }

    public static BizExceptionEnum fromDesc(String desc) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errMsg.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN_ERROR;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
