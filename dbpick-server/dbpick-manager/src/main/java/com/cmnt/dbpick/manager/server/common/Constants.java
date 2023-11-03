package com.cmnt.dbpick.manager.server.common;

import java.util.Arrays;
import java.util.List;

/**
 * 通用常量信息
 *
 */
public class Constants {

    public interface OssCloud{

        /**
         * OSS模块KEY
         */
        String SYS_OSS_KEY = "sys_oss:";

        /**
         * 云存储配置KEY
         */
        String CLOUD_STORAGE_CONFIG_KEY = "CloudStorageConfig";

        /**
         * 缓存配置KEY
         */
        String CACHE_CONFIG_KEY = SYS_OSS_KEY + CLOUD_STORAGE_CONFIG_KEY;

        /**
         * 预览列表资源开关Key
         */
        String PEREVIEW_LIST_RESOURCE_KEY = "sys.oss.previewListResource";

        /**
         * 系统数据ids
         */
        List<Integer> SYSTEM_DATA_IDS = Arrays.asList(1, 2, 3, 4);
    }

    public interface SysConfigKey {
        String 短信配置 = "sys_sms_config";
        String 微信消息模板 = "wechat.msg.template.test";
    }

    /**
     * 全局是否
     */
    public interface YseNo {
        String NO = "0";
        String YSE = "1";
    }

    /**
     * 站内信状态
     */
    public interface InteriorMessageStatus {
        String 未读 = "0";
        String 已读 = "1";
    }

    public interface DataScope {
        String 所有数据权限 = "1";
        String 自定义数据权限 = "2";
        String 本部门数据权限 = "3";
        String 本部门及以下数据权限 = "4";
        String 仅本人 = "5";
        String 本销控区 = "6";
    }


    public static String TRIGGER_OPTIMISTIC_LOCK = "TriggerOptimisticLock";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";
    public static final String LOGOUT_ERROR = "Logout error";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码有效期（S）
     */
    public static final Long CAPTCHA_EXPIRATION = 120L;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    //public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";







    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 异常状态 */
    public static final String EXCEPTION = "1";

    /** 用户封禁状态 */
    public static final String USER_BLOCKED = "1";

    /** 角色封禁状态 */
    public static final String ROLE_BLOCKED = "1";

    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";

    /** 字典正常状态 */
    public static final String DICT_NORMAL = "0";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";




    /**
     * 单表（增删改查）
     */
    public final static String TPL_CRUD = "crud";

    /**
     * 树表（增删改查）
     */
    public final static String TPL_TREE = "tree";

    /**
     * 主子表（增删改查）
     */
    public final static String TPL_SUB = "sub";

    /**
     * 树编码字段
     */
    public final static String TREE_CODE = "treeCode";

    /**
     * 树父编码字段
     */
    public final static String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段
     */
    public final static String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段
     */
    public final static String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段
     */
    public final static String PARENT_MENU_NAME = "parentMenuName";

    /**
     * 数据库字符串类型
     */
    public final static String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    /**
     * 数据库文本类型
     */
    public final static String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /**
     * 数据库时间类型
     */
    public final static String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /**
     * 数据库数字类型
     */
    public final static String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};

    /**
     * BO对象 不需要添加字段
     */
    public final static String[] COLUMNNAME_NOT_ADD = {"create_by", "create_time", "del_flag", "update_by",
            "update_time", "version"};

    /**
     * BO对象 不需要编辑字段
     */
    public final static String[] COLUMNNAME_NOT_EDIT = {"create_by", "create_time", "del_flag", "update_by",
            "update_time", "version"};

    /**
     * VO对象 不需要返回字段
     */
    public final static String[] COLUMNNAME_NOT_LIST = {"create_by", "create_time", "del_flag", "update_by",
            "update_time", "version"};

    /**
     * BO对象 不需要查询字段
     */
    public final static String[] COLUMNNAME_NOT_QUERY = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark", "version"};

    /**
     * Entity基类字段
     */
    public final static String[] BASE_ENTITY = {"createBy", "createTime", "updateBy", "updateTime"};

    /**
     * Tree基类字段
     */
    public final static String[] TREE_ENTITY = {"parentName", "parentId", "children"};

    /**
     * 文本框
     */
    public final static String HTML_INPUT = "input";

    /**
     * 文本域
     */
    public final static String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    public final static String HTML_SELECT = "select";

    /**
     * 单选框
     */
    public final static String HTML_RADIO = "radio";

    /**
     * 复选框
     */
    public final static String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    public final static String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件
     */
    public final static String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    public final static String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    public final static String HTML_EDITOR = "editor";

    /**
     * 字符串类型
     */
    public final static String TYPE_STRING = "String";

    /**
     * 整型
     */
    public final static String TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public final static String TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public final static String TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public final static String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    public final static String TYPE_DATE = "Date";

    /**
     * 模糊查询
     */
    public final static String QUERY_LIKE = "LIKE";

    /**
     * 需要
     */
    public final static String REQUIRE = "1";

}
