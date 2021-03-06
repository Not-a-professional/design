package com.liwei.design.service;

import com.liwei.design.model.Trash;
import com.liwei.design.model.Share;
import com.liwei.design.model.User;
import com.liwei.design.model.ticketShare;
import com.liwei.design.repo.TrashRepository;
import com.liwei.design.repo.ShareRepository;
import com.liwei.design.repo.UserRepository;
import com.liwei.design.repo.ticketShareRepository;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    @Autowired
    private ShareRepository sr;
    @Autowired
    private ticketShareRepository ts;
    @Autowired
    private UserRepository ur;
    @Autowired
    private TrashRepository dr;

    private final static String root = "/Users/liwei/Desktop/design";

    public void downloadFile(HttpServletResponse response, String path, String other)
            throws UnsupportedEncodingException {
        String[] temp = path.split("/");
        String filename = temp[temp.length - 1];
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename="
                + new String(filename.getBytes(), "iso-8859-1"));
        //4.获取要下载的文件输入流
        try (InputStream in = new FileInputStream(root + path)){

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
                if (temp.getAbsolutePath().endsWith(".DS_Store") || !dr.findAllByPath(temp.getAbsolutePath()).isEmpty()) {

                } else {
                    fileList.add(temp.getAbsolutePath());
                }
            }
        } else {
            fileList.add(file.getAbsolutePath());
        }
        return fileList;
    }

    public List<Map<String, String>> getAdminFileList(String path) {
        List<Map<String, String>> list = new ArrayList<>();
        File file;
        if (path.startsWith(root)) {
            file = new File(path);
        } else {
            file = new File(root + path);
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File temp : files) {
                Map<String, String> map = new HashMap<>();
                if (temp.getAbsolutePath().endsWith(".DS_Store")) {

                } else {
                    map.put("path",temp.getAbsolutePath());
                    list.add(map);
                }
            }
        }
        return list;
    }

    public String rollBack(String id) {
        dr.delete(Long.valueOf(id));
        return "success";
    }

    public String deleteForTrash(HttpServletRequest request, String path) {
        Trash trash = new Trash();
        trash.setDate(new Timestamp(new Date().getTime()));
        trash.setPath(root + path);
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        trash.setUser(username);
        if (dr.saveAndFlush(trash) != null) {
            return "success";
        }
        return "fail";
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

    public Map<String, Object> uploadDir(HttpServletRequest request,
                                         HttpServletResponse response)
        throws IOException  {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = params.getFiles("uploadDir");
        String parentPath = params.getParameter("dirPath");
        double sizeMB = 0.0;
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        User user = ur.findOne(username);
        Map<String, Object> res = new HashMap<>();
        for (MultipartFile file: files) {
            if (!file.getOriginalFilename().endsWith("jpg")
                && !file.getOriginalFilename().endsWith("png")
                && !file.getOriginalFilename().endsWith("mp4")
                && !file.getOriginalFilename().endsWith("txt")
                && !file.getOriginalFilename().endsWith(".DS_Store")) {
                res.put("res", "fail");
                res.put("msg", "目前只接受jpg,png,mp4,txt格式文件上传！");
                return res;
            }
            sizeMB = sizeMB + file.getBytes().length / 1024.00;
            if (user.getUsedVolume().add(BigDecimal.valueOf(sizeMB))
                .compareTo(user.getVolume()) == 1) {
                res.put("res", "fail");
                res.put("msg", "存储空间不足，上传失败\r\n请申请提高存储空间！");
                return res;
            }
        }

        // 保存目录结构上传文件夹
        // https://blog.csdn.net/bedisdover/article/details/52579713
        //https://blog.csdn.net/wenzhihui_2010/article/details/39397837
            try {
                for (MultipartFile file: files) {
                    String fileName = file.getOriginalFilename();
                    List<String> separatedPath = getSeparatedPath(fileName);
                    StringBuilder temp = new StringBuilder(root + parentPath);
                        for (int i = 0; i < separatedPath.size() - 1; i++) {
                            temp.append("/").append(separatedPath.get(i));
                            // 若父级目录目录不存在，创建之
                            if (!new File(temp.toString()).exists()) {
                                new File(temp.toString()).mkdir();
                            }
                        }

                        // 写入文件
                        uploadFile(file, parentPath, request);
                }
              } catch (Exception e) {
                e.printStackTrace();
            }

        return res;
    }

    public Map<String, Object> uploadFile(MultipartFile uploadFile, String path, HttpServletRequest request)
            throws IOException  {
        Map<String, Object> res = new HashMap<String, Object>();
        String filename = uploadFile.getOriginalFilename();
        if (!uploadFile.isEmpty()) {
            // 为user表设置存储容量字段，判断上传的文件是否会超过限制容量,单位kb
            double sizeMB = uploadFile.getBytes().length / 1024.00;
            SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                    .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            Authentication authentication = securityContextImpl.getAuthentication();
            String username = authentication.getName();
            User user = ur.findOne(username);
            if (user.getUsedVolume().add(BigDecimal.valueOf(sizeMB))
                    .compareTo(user.getVolume()) == 1) {
                res.put("res", "fail");
                res.put("msg", "存储空间不足，上传失败\r\n请申请提高存储空间！");
                return res;
            } else {
                user.setUsedVolume(user.getUsedVolume().add(BigDecimal.valueOf(sizeMB)));
                ur.saveAndFlush(user);
            }

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
        dirList.add(file.getAbsolutePath());
        getAllDir(dirList, file);
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
        return sr.getListByUserAndSpath(username);
    }

    public List<Share> getsShareList(HttpServletRequest request) {
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

    public Map<String, String> sSharePath(String url, HttpServletRequest request, String expireTime) {
        Map<String, String> res = new HashMap<String, String>();
        if (isExist(url)) {
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
        String sPath = MD5(url).substring(8, 24);
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
        if (getExpire(expireTime) != null) {
            ticketShare.setExpire(new Timestamp(getExpire(expireTime)));
        }
        ts.saveAndFlush(ticketShare);
        res.put("sPath", sPath);
        res.put("secret", secret);
        return res;
    }

    public String sharePath(String url, HttpServletRequest request, String expireTime) {
        if (isExist(url)) {
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
        if (getExpire(expireTime) != null) {
            ticketShare.setExpire(new Timestamp(getExpire(expireTime)));
        }
        ts.saveAndFlush(ticketShare);
        return "=w=";
    }

    private Long getExpire(String expireTime) {
        if (expireTime.equals("1")) { //有效期7天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
            return calendar.getTime().getTime();
        } else if (expireTime.equals("2")) { //有效期1天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            return calendar.getTime().getTime();
        }
        return null;
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

    // 下载文件夹
    public Map<String, String> downloadFolder(String url, HttpServletResponse response, String other)
            throws IOException {
        Map<String, String> res = new HashMap<String, String>();
        response.setContentType("application/zip");
        String[] temp = url.split("/");
        String zipName = temp[temp.length - 1];
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(zipName.getBytes(), "iso-8859-1") + ".zip");
        toZip(root + url, response.getOutputStream(), true);
        if (other.equals("other")) {
            Share share = sr.findOne(url);
            share.setDownload(share.getDownload() + 1);
            sr.saveAndFlush(share);
        }
        return res;
    }

    public Map<String, Object>  getEditorHtml(String path) throws IOException {
        String location = root + path;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(location));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            br.close();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("res", sb.toString());
        return res;
    }

    public Map<String, String> saveEditorHtml(String path, String content) throws IOException {
        BufferedWriter bw = null;
        String location = root + path;
        Map<String, String> res = new HashMap<>();
        try {
            bw = new BufferedWriter(new FileWriter(location));
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            res.put("res", "fail");
            return res;
        } finally {
            bw.close();
        }
        res.put("res", "success");
        return res;
    }

    public void getAllDir(List<String> dirList, File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File temp : files) {
                if (temp.isDirectory() && dr.findAllByPath(temp.getAbsolutePath()).isEmpty()) {
                    dirList.add(temp.getAbsolutePath());
                    getAllDir(dirList, temp);
                }
            }
        }
    }

    /**
     * 对字符串md5加密(大写+数字)
     *
     * @param s 传入要加密的字符串
     * @return  MD5加密后的字符串
     */
    public String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //判断分享是否已经存在
    public Boolean isExist(String url) {
        if (sr.findOne(url) != null || ts.findOneByPath(url) != null) {
            return true;
        }
        return false;
    }

    //文件夹下载
    private static final int  BUFFER_SIZE = 2 * 1024;

    public void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException{

        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 获取request对象中包含的文件列表
     *
     * @throws FileUploadException
     */
    private List<FileItem> getFileItem(HttpServletRequest request)
        throws FileUploadException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存中存储文件的最大值

        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大上传的文件大小

        upload.setHeaderEncoding("UTF-8");

        return upload.parseRequest(request);
    }

    /**
     * 向磁盘写入文件
     *
     * @param fileItem 文件项，包含文件内容
     * @param filePath 文件路径，不包含文件名
     * @param fileName 文件名，不包含路径
     */
    private void writeFile(FileItem fileItem, String filePath, String fileName) throws Exception {
        File file = new File(filePath, fileName);

        fileItem.write(file);
    }

    /**
     * 从文件路径中获取文件名及各级父目录
     *
     * @param filePath 文件相对项目目录的路径
     * @return List<String> (0 ~ n - 2) :各级父目录名称
     * n - 1       :不包含路径的文件名
     * 示例：filePath --- taskName/src/main/java/test.java
     * 返回["taskName", "src", "main", "java", "test.java"]
     */
    private List<String> getSeparatedPath(String filePath) {
        List<String> result = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(filePath, "/");

        while (tokenizer.hasMoreElements()) {
            result.add(tokenizer.nextToken());
        }

        return result;
    }
}
