package cn.luischen.api;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.exception.BusinessException;
import cn.luischen.utils.TaleUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by winterchen on 2018/5/1.
 */
@Component
public class QiniuCloudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuCloudService.class);

    @Value("${qiniu.accesskey}")
    private String ACCESS_KEY;
    @Value("${qiniu.serectkey}")
    private String SECRET_KEY;
    /**
     * 仓库
     */
    @Value("${qiniu.bucket}")
    private String BUCKET;
    /**
     * 七牛云外网访问地址
     */
    @Value("${qiniu.cdn.url}")
    public String QINIU_UPLOAD_SITE;

    public String upload(MultipartFile file, String fileName) {

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = null;

            response = uploadManager.put(file.getInputStream(), fileName, upToken, null, null);

            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(r.toString());
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(ex.getMessage());
        } catch (IOException e) {
            LOGGER.error("file upload failed", e);
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(e.getMessage());
        }
        
    }

    public void delete(String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
            throw BusinessException.withErrorCode(ErrorConstant.Att.DELETE_ATT_FAIL).withErrorMessageArguments(ex.getMessage());
        }
    }

}
