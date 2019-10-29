package org.crown.common.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class AddWaterMarkUtil {
	
	@Value("${lycm.imagePath}")
	private String imagePath;
	
	 public  void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		 iconPath=imagePath+iconPath;
		 srcImgPath=imagePath+srcImgPath;
		 targerPath=imagePath+targerPath;
	        OutputStream os = null;
	        try {
	            Image srcImg = ImageIO.read(new File(srcImgPath));

	            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
	                    BufferedImage.TYPE_INT_RGB);

	            // 得到画笔对象
	            // Graphics g= buffImg.getGraphics();
	            Graphics2D g = buffImg.createGraphics();

	            // 设置对线段的锯齿状边缘处理
	            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

	            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
	                    0, null);

			/*
			 * if (null != degree) { // 设置水印旋转 g.rotate(Math.toRadians(degree), (double)
			 * buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2); }
			 */

	            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
	            ImageIcon imgIcon = new ImageIcon(iconPath);

	            // 得到 水印 Image对象。
	            Image img = imgIcon.getImage();
	            int watermarkImageWidth=img.getWidth(null);
	            int watermarkImageHeight=img.getHeight(null);
	            int width=srcImg.getWidth(null)/3;
	            double dmarkWidth = width;// 设置水印的宽度为图片宽度的0.4倍
				double dmarkHeight = dmarkWidth * ((double)watermarkImageHeight/(double)watermarkImageWidth);//强转为double计算宽高比例
				int imarkWidth = (int) dmarkWidth;
				int imarkHeight = (int) dmarkHeight;
	            float alpha =1f; // 透明度
	            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

	            // 表示水印图片的位置
	            g.drawImage(img, srcImg.getWidth(null)*13/20, srcImg.getHeight(null)*17/20, imarkWidth,imarkHeight,null);

	            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

	            g.dispose();

	            os = new FileOutputStream(targerPath);

	            // 生成图片
	            ImageIO.write(buffImg, "JPG", os);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (null != os)
	                    os.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	 
	        }
	
	 }
}
