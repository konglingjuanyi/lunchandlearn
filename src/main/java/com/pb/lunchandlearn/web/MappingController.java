package com.pb.lunchandlearn.web;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DE007RA on 7/31/2015.
 */
@Controller("mappingController")
@RequestMapping("mapping")
public class MappingController {

	ArrayList<Point> gpsCoordinates = new ArrayList<Point>();

	;
	ArrayList<Point> gpsPLineCoordinates = new ArrayList<Point>();
	@Value("${image.file.fullpath}")
	private String fileFullPath;

	@RequestMapping(value = "/UploadImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean saveImage(@RequestParam(value = "data") String file) {
		try {
			if (file.length() > 0) {
				try {
					byte[] valueDecoded = Base64.decode(file);
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(valueDecoded));
					ImageIO.write(image, "jpg", new File(fileFullPath));
				} catch (Exception e) {
					return false;
				}
			}
		} catch (Exception exp) {
			System.out.println("Error saving image: " + exp);
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/PullLocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Point> PullLocation() {
		ArrayList<Point> gpsToReturn = new ArrayList<Point>();
		gpsToReturn.addAll(gpsCoordinates);
		gpsCoordinates.clear();
		return gpsToReturn;
	}

	@RequestMapping(value = "/PullPLineLocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Point> PullPLineLocation() {
		ArrayList<Point> gpsToReturn = new ArrayList<Point>();
		gpsToReturn.addAll(gpsPLineCoordinates);
		gpsPLineCoordinates.clear();
		return gpsToReturn;
	}

	@RequestMapping(value = "/PushLocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean PushLocation(@RequestParam(value = "location") String location) {
		String[] values = location.split(",");
		Point pt = new Point();
		pt.x = Double.parseDouble(values[0]);
		pt.y = Double.parseDouble(values[1]);
		gpsCoordinates.add(pt);
		return true;
	}

	@RequestMapping(value = "/PushPLineLocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean PushPLineLocation(@RequestParam(value = "location") String location) {
		String[] values = location.split(",");
		Point pt = new Point();
		pt.x = Double.parseDouble(values[0]);
		pt.y = Double.parseDouble(values[1]);
		gpsPLineCoordinates.add(pt);
		return true;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadAttachment(HttpServletResponse response) {
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//		response.setHeader("Content-Disposition", "attachment;filename=filename.jpg");

		try {
			ServletOutputStream out = response.getOutputStream();
			File initialFile = new File(fileFullPath);
			InputStream targetStream = new FileInputStream(initialFile);
			byte[] content = IOUtils.toByteArray(targetStream);
			out.write(content);
			targetStream.close();
			out.flush();
		} catch (Exception e) {
			System.out.println("Error while downloading: " + e);
		}
	}
	class Point {
		double x;
		double y;
	}

}