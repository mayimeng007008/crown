package org.crown.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.crown.common.annotations.Resources;
import org.crown.common.utils.JsonUtil;
import org.crown.common.utils.UploadUtil;
import org.crown.enums.AuthTypeEnum;
import org.crown.framework.controller.SuperController;
import org.crown.framework.responses.ApiResponses;
import org.crown.model.dto.WebFootDTO;
import org.crown.model.entity.WebFoot;
import org.crown.model.parm.WebFootPARM;
import org.crown.service.IWebFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 网站底部表 前端控制器
 * </p>
 *
 * @author zqchen
 */
@Api(tags = {"Webfoot"}, description = "网站底部相关接口")
@RestController
@RequestMapping(value = "/webfoots", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class WebFootRestController extends SuperController {

    @Autowired
    private IWebFootService webFootService;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询所有网站底部(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressZhCn", value = "需要查询的网站底部名", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<WebFootDTO>> page(@RequestParam(value = "addressZhCn", required = false) String addressZhCn) {
        return success(webFootService.pageWebFootDTO(this.<WebFoot>getPage(), addressZhCn));
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询所有网站底部")
    @GetMapping("/webfoots")
    public ApiResponses<List<WebFoot>> list() {
    	//不带where条件查询
        //return success(webFootService.list());
        return success(webFootService.list(new QueryWrapper<WebFoot>().eq("isdataeffid", 1)));
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询单个网站底部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "网站底部ID", required = true, paramType = "path")
    })
    @GetMapping("/{id}")
    public ApiResponses<WebFoot> get(@PathVariable("id") Integer id) {
        WebFoot webFoot = webFootService.getById(id);
        return success(webFoot);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "添加网站底部")
    @PostMapping
    public ApiResponses<Void> create(@RequestBody @Validated(WebFootPARM.Create.class) WebFootPARM webFootPARM) {
        webFootService.save(webFootPARM.convert(WebFoot.class));
        return success(HttpStatus.CREATED);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "修改网站底部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "网站ID", required = true, paramType = "path")
    })
    @PutMapping("/{id}")
    public ApiResponses<Void> update(@PathVariable("id") Integer id, @RequestBody @Validated(WebFootPARM.Update.class) WebFootPARM webFootPARM) {
        WebFoot webFoot = webFootPARM.convert(WebFoot.class);
        webFoot.setId(id);
        webFootService.updateById(webFoot);
        return success();
    }

    @Autowired
    UploadUtil uploadUtil;

    /**
     * 头像上传 目前首先相对路径
     */
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "上传logo图片")
    @PostMapping(value = "/upload")
    public ApiResponses<JsonUtil> imgUpload(HttpServletRequest req, @RequestParam("file") MultipartFile[] file,
                              ModelMap model) {
    	Map<String, Object> map = uploadUtil.upload(file,"",0);
        JsonUtil j = new JsonUtil();
        j.setMsg(map);
        //j.setData(map);
        return success(j);
    }
    
   
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "删除网站底部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "网站底部ID", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public ApiResponses<Void> delete(@PathVariable("id") Integer id) {
    	//真删除
        //webFootService.removeById(id);
    	
    	//假删除
    	WebFoot webFoot = new WebFoot();
    	webFoot.setId(id);
    	webFoot.setIsdataeffid(0);
        webFootService.updateById(webFoot);
        return success(HttpStatus.NO_CONTENT);
    }
}

