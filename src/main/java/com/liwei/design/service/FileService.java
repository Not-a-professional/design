package com.liwei.design.service;

import com.liwei.design.model.Share;
import com.liwei.design.model.ticketShare;
import com.liwei.design.repo.ShareRepository;
import com.liwei.design.repo.ticketShareRepository;
import com.liwei.design.tool.fileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class FileService {
    @Autowired
    private ShareRepository sr;
    @Autowired
    private ticketShareRepository ts;

    private final static String root = "/Users/liwei/Desktop/design";
    fileTool fileTool = new fileTool();

    public void downloadFile(HttpServletResponse response, String path, String other)
            throws UnsupportedEncodingException {
        String[] temp = path.split("/");
        String filename = temp[temp.length - 1];
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename="
                + new String(filename.getBytes(), "iso-8859-1"));
        //4.获取要下载的文件输入流
        try {
            InputStream in = new FileInputStream(root + path);

            int len = 0;
            //5.创建书缓冲区
            byte[] buffer = new byte[1024];
            //6.通过response对象获取OutputStream输出流对象
            OutputStream os = response.getOutputStream();
            //7.将FileInputStream流对象写入到buffer缓冲区
            while ((len = in.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            //8.关闭流
            in.close();
            os.close();
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }
        if (other.equals("other")) {
            Share share = sr.findOne(path);
            share.setDownload(share.getDownload() + 1);
            sr.saveAndFlush(share);
        }
    }

    /**
     * 前端只显示文件名和后缀,路径隐藏.
     * @param path
     * @return
     */
    public List<String> getFileList(String path) {
        List<String> fileList = new ArrayList<String>();
        File file = new File(root + path);
        File[] files = file.listFiles();
        if (files != null) {
            for (File temp : files) {
                if (temp.getAbsolutePath().endsWith(".DS_Store")) {

                } else {
                    fileList.add(temp.getAbsolutePath());
                }
            }
        }
        return fileList;
    }

    public String deleteFile(String path) {
        File file = new File(root + path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
                sr.deleteByUrl(file.getAbsolutePath().substring(27));
                return "success";
            } else {
                for (File temp : files) {
                    if (temp.isDirectory()) {
                        delete(temp.getAbsolutePath());
                    } else {
                        temp.delete();
                        sr.deleteByUrl(temp.getAbsolutePath().substring(27));
                    }
                }
            }
            file.delete();
            return "success";
        } else {
            if (file.delete()) {
                return "success";
            } else {
                return "fail";
            }
        }
    }

    public void delete(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
                sr.deleteByUrl(file.getAbsolutePath().substring(27));
            } else {
                for (File temp : files) {
                    if (temp.isDirectory()) {
                        delete(temp.getAbsolutePath());
                    } else {
                        file.delete();
                        sr.deleteByUrl(temp.getAbsolutePath().substring(27));
                    }
                }
            }
        } else {
            file.delete();
        }
    }

    public Map<String, Object> uploadFile(MultipartFile uploadFile, String path) throws IOException  {
        Map<String, Object> res = new HashMap<String, Object>();
        String filename = uploadFile.getOriginalFilename();
        if (!uploadFile.isEmpty()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                File serverFile = new File(root + path +"/" + filename);
                in = uploadFile.getInputStream();
                out = new FileOutputStream(serverFile);
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) > 0) {
                    out.write(b, 0, len);
                }
                out.close();
                in.close();
                res.put("res","success");
                return res;
            } catch (Exception e) {
                res.put("res","fail");
                return res;
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }

                if (in != null) {
                    in.close();
                    in = null;
                }
            }
        } else {
            res.put("res","fail");
            return res;
        }
    }

    public List<String> getDir(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        File file = new File(root + "/" + username);
        List<String> dirList = new ArrayList<String>();
        fileTool.getAllDir(dirList, file);
        return dirList;
    }

    public void getView(HttpServletResponse response, String path)
            throws IOException {
        BufferedImage buffImg = ImageIO.read(new FileInputStream(root + "/" + path));
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream os = response.getOutputStream();
        if (path.endsWith("jpg")) {
            response.setContentType("image/jpeg");
            ImageIO.write(buffImg, "jpeg", os);
        } else if (path.endsWith("png")) {
            response.setContentType("image/png");
            ImageIO.write(buffImg, "png", os);
        }
        os.close();
    }

    public List<String> getShareList(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        return sr.getListByUser(username);
    }

    public String cancelShare(String path) {
        sr.deleteByUrl(path);
        return "1";
    }

    public Map<String, String> sSharePath(String url, HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        if (fileTool.isExist(url)) {
            res.put("res", "fail");
            return res;
        }

        Random random = new Random();
        String index = "123456789zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            sb.append(index.charAt(random.nextInt(index.length())));
        }

        String secret = sb.toString();
        String sPath = fileTool.MD5(url).substring(8, 24);
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();

        ticketShare ticketShare = new ticketShare();
        ticketShare.setStatus("0");
        ticketShare.setUser(username);
        ticketShare.setPath(url);
        ticketShare.setSpath(sPath);
        ticketShare.setSecret(secret);
        ts.saveAndFlush(ticketShare);
        res.put("sPath", sPath);
        res.put("secret", secret);
        return res;
    }

    public String sharePath(String url, HttpServletRequest request) {
        if (fileTool.isExist(url)) {
            return "fail";
        }
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        ticketShare ticketShare = new ticketShare();
        ticketShare.setStatus("0");
        ticketShare.setUser(username);
        ticketShare.setPath(url);
        ts.saveAndFlush(ticketShare);
        return "=w=";
    }

    public Map<String, String> createDir(String url) {
        Map<String, String> res = new HashMap<String, String>();
        File dir = new File(root + url);
        if (dir.exists()) {
            res.put("res", "fail");
            res.put("error", "文件夹已存在！");
            return res;
        } else {
            dir.mkdir();
            res.put("error", "文件夹创建成功！");
            res.put("res", "success");
            return res;
        }
    }

    //TODO 下载文件夹
    public Map<String, String> downloadFolder(String url, HttpServletResponse response, String other)
            throws IOException {
        Map<String, String> res = new HashMap<String, String>();
        response.setContentType("application/zip");
        String[] temp = url.split("/");
        String zipName = temp[temp.length - 1];
        response.setHeader("Content-Disposition", "attachment; filename="+ zipName +".zip");
        fileTool.toZip(root + url, response.getOutputStream(), true);
        if (other.equals("other")) {
            Share share = sr.findOne(url);
            share.setDownload(share.getDownload() + 1);
            sr.saveAndFlush(share);
        }
        return res;
    }

}
