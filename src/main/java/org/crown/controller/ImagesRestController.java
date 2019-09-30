package org.crown.controller;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.crown.common.annotations.Resources;
import org.crown.enums.AuthTypeEnum;
import org.crown.framework.controller.SuperController;
import org.crown.framework.responses.ApiResponses;
import org.crown.model.dto.ImagesDTO;
import org.crown.model.entity.Images;
import org.crown.service.IImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * 系统照片表 前端控制器
 * </p>
 *
 * @author Caratacus
 */
@Api(tags = {"Images"}, description = "照片操作相关接口")
@RestController
@RequestMapping(value = "/images", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ImagesRestController extends SuperController {

	@Value("${lycm.imagePath}")
    private String imagePath;
	
    @Autowired
    private IImagesService imagesService;
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("查询所有照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "galleryUuid", value = "需要查询的照片名称", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<ImagesDTO>> page(@RequestParam(value = "uuid", required = true) String galleryUuid) {
        Integer isOk = 0;
        
        return success(
                imagesService.query().like(StringUtils.isNotEmpty(galleryUuid), Images::getGalleryUuid, galleryUuid)
                        .eq(true, Images::getDeleted, isOk).orderByDesc(Images::getCreateTime)
                        .page(this.<Images>getPage())
                        .convert(
                        		entity -> {
                        			
                        			ImagesDTO dto = entity.convert(ImagesDTO.class);
                        			int count=imagesService.findCount(galleryUuid);
                        			dto.setCount(count);
                        			System.out.println(dto);
                                    return dto;
                                }
                        		)
        );
    }
    
    
    
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "照片访问量")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "图片的id", required = true, paramType = "path")
    })
    @GetMapping("/imgVisits")
    public int imgVisits(@PathParam("id") int id ) {
    		imagesService.addImgVisits(id);
    		Images img= imagesService.findImageById(id);
    		int visits = img.getVisits();
    		return visits;
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "获取照片点赞量")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "图片的id", required = true, paramType = "path")
    })
    @GetMapping("/getImgThumbsUp")
    public int getImgThumbsUp(@PathParam("id") int id ) {
    	
    	Images img= imagesService.findImageById(id);
    	int thumbsUp = img.getThumbsUp();
    	return thumbsUp;
    }
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "添加照片点赞量")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "图片的id", required = true, paramType = "path")
    })
    @GetMapping("/addImgThumbsUp")
    public void addImgThumbsUp(@PathParam("id") int id,@PathParam("num") int num ) {
    	
    	imagesService.addImageThumbsUpById(id,num);
    	
    	
    }
    
    
    
	/*
	 * @Resources(auth = AuthTypeEnum.AUTH)
	 * 
	 * @ApiOperation("查询单个照片")
	 * 
	 * @ApiImplicitParams({
	 * 
	 * @ApiImplicitParam(name = "id", value = "照片uuID", required = true, paramType =
	 * "path") })
	 * 
	 * @GetMapping("/{id}") public ApiResponses<ImagesDTO> get(@PathVariable("id")
	 * Integer id) { Images Images = imagesService.getById(id);
	 * ApiAssert.notNull(ErrorCodeEnum.USER_NOT_FOUND, Images);
	 * 
	 * ImagesDTO ImagesDTO = Images.convert(ImagesDTO.class); return
	 * success(ImagesDTO); }
	 * 
	 * @Resources(auth = AuthTypeEnum.AUTH)
	 * 
	 * @ApiOperation("创建照片")
	 * 
	 * @PostMapping public ApiResponses<Void> create(@RequestBody ImagesDTO
	 * imagesDTO) { Images Images = imagesDTO.convert(Images.class);
	 * imagesService.save(Images); return success(HttpStatus.CREATED); }
	 * 
	 * @Resources(auth = AuthTypeEnum.AUTH)
	 * 
	 * @ApiOperation("修改照片")
	 * 
	 * @ApiImplicitParams({
	 * 
	 * @ApiImplicitParam(name = "id", value = "照片uuID", required = true, paramType =
	 * "path") })
	 * 
	 * @PutMapping("/{id}") public ApiResponses<Void> update(@PathVariable("id")
	 * Integer id, @RequestBody ImagesDTO imagesDTO) { Images Images =
	 * imagesDTO.convert(Images.class); Images.setId(id);
	 * imagesService.updateById(Images); return success(); }
	 */

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "删除照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "照片uuID", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public ApiResponses<Void> delete(@PathVariable("id") Integer id) {
    	
    	Images at = new Images();
    	at.setId(id);
    	at.setDeleted(1);
    	imagesService.updateById(at);
        return success(HttpStatus.NO_CONTENT);
    }
    
    //@Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "下载选中的照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgPath", value = "高清图照片存放文件夹", required = true, paramType = "path")
    })
    /**
	 * 图片下载
	 * @param url 图片url
	 * @param formatName 文件格式名称
	 * @param localFile 下载到本地文件
	 * @return 下载是否成功
	 */
    @GetMapping(value = "/download")
    public void downLoad(@RequestParam(value = "imgPath", required = true)String path, HttpServletResponse response) {
        try {
            //path = "http://localhost:8084/file2/1.jpg";
			/*
			 * String imageUrl = path; String formatName = ""; File localFile = "";
			 * 
			 * ImageUtil.downloadImage(imageUrl, formatName, localFile);
			 */
        	int index = path.lastIndexOf("/");
            path = path.substring(index+1);
            String url=imagePath+path;
            File file = new File(url);
 
            FileImageInputStream  fs = new FileImageInputStream (file);
            int streamLength = (int)fs.length();
            byte[] image = new byte[streamLength ];
            fs.read(image,0,streamLength );
            fs.close();
            

            //response.serHeader("Content-Disposition","attachment;filename=图片名字.图片类型后缀");
            
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Type","application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename="+path);
            response.getOutputStream().write(image);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //return response;
    }
    
}

