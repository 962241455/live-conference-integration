package com.cmnt.dbpick.manager.server.controller;


import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.entity.SysDictType;
import com.cmnt.dbpick.manager.server.service.ISysDictTypeService;
import com.cmnt.dbpick.manager.server.util.PageUtil;
import com.cmnt.dbpick.manager.server.util.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 *  *
 */
@Api(tags = "字典类型控制器")
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @GetMapping("/list")
    public AjaxResult list(SysDictType dictType)
    {
        PageUtil.startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return AjaxResult.success(PageUtil.getDataTable(list));
    }

//    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:dict:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysDictType dictType)
//    {
//        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
//        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
//        return util.exportExcel(list, "字典类型");
//    }

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId)
    {
        return AjaxResult.success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict)
    {
        if (Constants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());

        int rows = dictTypeService.insertDictType(dict);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改字典类型
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict)
    {
        if (Constants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return AjaxResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());

        int rows = dictTypeService.updateDictType(dict);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds)
    {
        int rows = dictTypeService.deleteDictTypeByIds(dictIds);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return AjaxResult.success(dictTypes);
    }
}
