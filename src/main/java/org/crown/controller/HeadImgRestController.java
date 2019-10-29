package org.crown.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.crown.common.annotations.Resources;
import org.crown.common.utils.CompareHeadImgUtil2;
import org.crown.common.utils.CutHeadImgUtil2;
import org.crown.enums.AuthTypeEnum;
import org.crown.framework.controller.SuperController;
import org.crown.framework.responses.ApiResponses;
import org.crown.model.dto.HeadImgDTO;
import org.crown.model.entity.HeadImg;
import org.crown.service.IHeadImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 系统截取的头像表 前端控制器
 * </p>
 *
 * @author Caratacus
 */
@Api(tags = {"HeadImg"}, description = "截取的头像操作相关接口")
@RestController
@RequestMapping(value = "/headImg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@Transactional
public class HeadImgRestController extends SuperController {
    @Autowired
    private IHeadImgService headImgService;
    @Autowired
    private CompareHeadImgUtil2 compareHeadImgUtil2;
    @Autowired
    CutHeadImgUtil2 cutHeadImgUtil;
    
    @Value("${lycm.imagePath}")
    private String imagePath;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("通过头像的id集合查询某张照片所有截取的头像,")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要查询的截取的头像id集合", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<HeadImgDTO>> page(@RequestParam(value = "ids", required = true) String ids) {
        //Integer isOk = 0;
        
        return success(
            headImgService.query().in(StringUtils.isNotEmpty(ids), HeadImg::getId, ids)
                //.eq(true, HeadImg::getDeleted, isOk).orderByDesc(HeadImg::getCreateTime)
                .page(this.<HeadImg>getPage())
                .convert(
            		entity -> {
            			HeadImgDTO dto = entity.convert(HeadImgDTO.class);
                        return dto;
                    }
                )
    		);
    }
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("获取已经确认头像(ma)")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "galleryUuid", value = "需要查询的截取的头像所属活动UUID", paramType = "query")
    })
    @GetMapping("/getAllImgs")
    public ApiResponses<IPage<HeadImgDTO>> getAllHeadImgs(@RequestParam(value = "uuid", required = true) String galleryUuid,int needCheack) {
    	return success(
    			headImgService.query().eq(StringUtils.isNotEmpty(galleryUuid), HeadImg::getGalleryUuid, galleryUuid)
    			.eq(true, HeadImg::getNeedCheack, needCheack)
    			.orderByDesc(HeadImg::getCreateTime)
    			.page(this.<HeadImg>getPage())
    			.convert(
    					entity -> {
    						
    						HeadImgDTO dto = entity.convert(HeadImgDTO.class);
    						return dto;
    					}
    					)
    			);
    }
    
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("获取所有参加此活动的人物的头像,")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "galleryUuid", value = "需要查询的截取的头像所属活动UUID", paramType = "query")
    })
    @GetMapping("/allImgs")
    public ApiResponses<IPage<HeadImgDTO>> getAllImgs(@RequestParam(value = "galleryUuid", required = true) String galleryUuid) {
    	Integer isOk = 1;
    	
    	return success(
    			headImgService.query().eq(StringUtils.isNotEmpty(galleryUuid), HeadImg::getGalleryUuid, galleryUuid)
    			.eq(true, HeadImg::getDeleted, 2)
    			.eq(true, HeadImg::getNeedCheack, 2)
    			.orderByDesc(HeadImg::getCreateTime)
    			.page(this.<HeadImg>getPage())
    			.convert(
    					entity -> {
    						
    						HeadImgDTO dto = entity.convert(HeadImgDTO.class);
    						return dto;
    					}
    					)
    			);
    }
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "删除截取的头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "截取的头像ID", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public ApiResponses<Void> delete(@PathVariable("id") Integer id) {
    	
    	HeadImg at = new HeadImg();
    	at.setId(id);
    	at.setDeleted(1);
    	headImgService.updateById(at);
        return success(HttpStatus.NO_CONTENT);
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "移除与头像不匹配的照片")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "截取的头像ID", required = true, paramType = "path")
    })
    @GetMapping("/removeImgsId")
    public  ApiResponses<Integer> removeImgsId(@PathParam("id") Integer id,@PathParam("imgId") Integer imgId) {
    	int status=headImgService.removeImgsId(id,imgId);
    		return success(status);
    }
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "批量删除头像")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "ids", value = "头像ID数组", required = true, paramType = "path")
    })
    @GetMapping("/deleteSelectedHeadImgs")
    public void deleteSelectedHeadImgs(@PathParam("ids") Integer[] ids) {
    	for (Integer id : ids) {
    	
    		headImgService.deleteSelectedHeadImgs(id);
		}
    	
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "获取头像数量")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "uuid", value = "相册uuid", required = true, paramType = "path")
    })
    @GetMapping("/getHeadCount")
    public ApiResponses<List<Integer>>  getHeadCount(@PathParam("uuid") String uuid) {
    	return success(headImgService.getHeadCount(uuid));
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "批量修改头像状态needcheack(mayimeng)")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "ids", value = "头像ID数组", required = true, paramType = "path")
    })
    @GetMapping("/updateCheack")
    public void updateCheack(@PathParam("ids") Integer[] ids) {
    	for (Integer id : ids) {
    	
    		headImgService.updateCheack(id);
		}
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "批量修改人物分类(maymeng)")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "ids", value = "头像ID数组", required = true, paramType = "path")
    })
    @GetMapping("/person")
    public void person(@PathParam("ids") Integer[] ids) {
    		headImgService.person(ids);
    }
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "比对头像")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "uuid", value = "相册的uuid", required = true, paramType = "path")
    })
    @GetMapping("/compareHead")
    public List<String> compareHead(@PathParam("uuid") String uuid) {
    	String msg;
    	List<String> list = new ArrayList<>();
    	try {
    		msg=compareHeadImgUtil2.CompareHeadImg(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			msg="人脸比对出错，请重试！";
		};
		list.add(msg);
		return list;
    }
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "截取头像接口")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "uuid", value = "相册的uuid", required = true, paramType = "path")
    })
    @GetMapping("/doCutHeadImg")
    public List<String> cutHeadImgs(@PathParam("uuid") String uuid) {
    	List<String> list = new ArrayList<String>();
    	String msg;
    	try {
    		msg=cutHeadImgUtil.cutHeadImg(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			msg="检测头像出错，请重试！";
		}
    	list.add(msg);
    	return list;
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "获取截图百分比和状态")
    @GetMapping("/getCutStatus")
    public List<Integer> getCutStatus() {
    	return cutHeadImgUtil.getCutStatus();
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "获取人脸比对百分比和状态")
    @GetMapping("/getCompareStatus")
    public List<Integer> getCompareStatus() {
    	System.err.println("收到请求比对");
    	System.err.println(compareHeadImgUtil2.getCompareStatus().get(1));
    	return compareHeadImgUtil2.getCompareStatus();
    }
    
}

