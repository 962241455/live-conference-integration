package com.cmnt.dbpick.manager.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.entity.SysDictData;
import com.cmnt.dbpick.manager.server.service.ISysDictDataService;
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
@Api(tags = "字典数据控制器")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @GetMapping("/list")
    public AjaxResult list(SysDictData dictData)
    {
        PageUtil.startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return AjaxResult.success(PageUtil.getDataTable(list));
    }

//    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:dict:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysDictData dictData)
//    {
//        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
//        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
//        return util.exportExcel(list, "字典数据");
//    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode)
    {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/dictType/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        return AjaxResult.success(dictDataService.selectDictDataByType(dictType));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/public/dictType/{dictType}")
    public AjaxResult publicDictType(@PathVariable String dictType)
    {
        return dictType(dictType);
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());

        int rows = dictDataService.insertDictData(dict);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改保存字典类型
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());

        int rows = dictDataService.updateDictData(dict);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes)
    {
        int rows = dictDataService.deleteDictDataByIds(dictCodes);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
