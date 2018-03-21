package com.liwei.design.controller;

import com.liwei.design.model.Share;
import com.liwei.design.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * 前端只显示文件名和后缀,路径隐藏.
     * @param path
     * @return
     */
    @RequestMapping("/getList")
    @ResponseBody
    public List<String> getFile(@RequestParam String path) {
        return fileService.getFileList(path);
    }

    /**
     * 获取上传图片时选择的目录列表
     * @return
     */
    @RequestMapping("/getDir")
    @ResponseBody
    public List<String> getDir(HttpServletRequest request) {
        return fileService.getDir(request);
    }

    /**
     * 若获取的是图片就输出图片流
     */
    @RequestMapping("/getView/**")
    public void getView(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PathMatcher pathMatcher = new AntPathMatcher();
        String path = pathMatcher.extractPathWithinPattern(
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
                request.getPathInfo() == null ? request.getServletPath() : request.getPathInfo());
        fileService.getView(response, path);
    }

    @RequestMapping("/download")
    public void responseDownload(HttpServletResponse response, @RequestParam String path, String other)
            throws UnsupportedEncodingException {
        fileService.downloadFile(response, path, other);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, String> deleteFile(@RequestParam String path) {
        Map<String, String> res = new HashMap<String, String>();
        res.put("res", fileService.deleteFile(path));
        return res;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(String path, HttpServletRequest request,
                                          @RequestParam("uploadFile") MultipartFile uploadFile)
            throws IOException {
        return fileService.uploadFile(uploadFile, path, request);
    }

    @RequestMapping("/getShareList")
    @ResponseBody
    public List<String> getShareList(HttpServletRequest request) {
        return fileService.getShareList(request);
    }

    @RequestMapping("/getsShareList")
    @ResponseBody
    public List<Share> getsShareList(HttpServletRequest request) {
        return fileService.getsShareList(request);
    }

    @RequestMapping("/cancelShare")
    @ResponseBody
    public String cancelShare(String path) {
        return fileService.cancelShare(path);
    }

    @RequestMapping("/sSharePath")
    @ResponseBody
    public Map<String, String> getSharePassword(String url, HttpServletRequest request) {
        return fileService.sSharePath(url, request);
    }

    @RequestMapping("/sharePath")
    @ResponseBody
    public Map<String, String> applyForShare(String url, HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
        res.put("sPath", fileService.sharePath(url, request));
        return res;
    }

    @RequestMapping("/createDir")
    @ResponseBody
    public Map<String, String> createDir(String url) {
        return fileService.createDir(url);
    }

    @RequestMapping("/downloadZip")
    @ResponseBody
    public Map<String, String> downloadFolder(String url, String other, HttpServletResponse response)
            throws IOException {
        return fileService.downloadFolder(url, response, other);
    }

    @RequestMapping("/getEditorHtml")
    @ResponseBody
    public Map<String, Object> getEditorHtml(String path) throws IOException {
        return fileService.getEditorHtml(path);
    }

    @RequestMapping("/saveEditorHtml")
    @ResponseBody
    public Map<String, String> saveEditorHtml(String path, String content) throws IOException {
        return fileService.saveEditorHtml(path, content);
    }
}
