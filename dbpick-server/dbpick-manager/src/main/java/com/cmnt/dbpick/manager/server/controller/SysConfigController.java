package com.cmnt.dbpick.manager.server.controller;
import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.entity.SysConfig;
import com.cmnt.dbpick.manager.server.service.ISysConfigService;
import com.cmnt.dbpick.manager.server.util.PageUtil;
import com.cmnt.dbpick.manager.server.util.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 *  *
 */
@Api(tags = "参数配置")
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysConfig config)
    {
        PageUtil.startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return AjaxResult.success(PageUtil.getDataTable(list));
    }

//    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:config:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysConfig config)
//    {
//        List<SysConfig> list = configService.selectConfigList(config);
//        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
//        return util.exportExcel(list, "参数数据");
//    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return AjaxResult.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return AjaxResult.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (Constants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());

        int rows = configService.insertConfig(config);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改参数配置
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (Constants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());

        int rows = configService.updateConfig(config);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除参数配置
     */
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        int rows = configService.deleteConfigByIds(configIds);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
