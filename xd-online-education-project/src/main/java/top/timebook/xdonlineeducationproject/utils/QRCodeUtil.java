package top.timebook.xdonlineeducationproject.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QRCodeUtil
 * @Description 二维码工具类
 * @Author xiongzl
 * @Date 2019/5/14 12:05
 * @Version 1.0
 **/
public class QRCodeUtil {


    private static final String QRCODE_CHARACTER_SET = "UTF-8";
    private static final Integer QRCODE_WIDTH = 400;
    private static final Integer QRCODE_HEIGHT = 400;
    private static final String QRCODE_FORMAT = "png";


    /**
     * @Author xiongzl
     * @Description 生成二维码
     * @Date 2019/5/14 12:14
     * @Param [codeUrl, response]
     * @Return void
     **/
    public static void generateQRCode(String codeUrl, HttpServletResponse response) {
        try {
            // 生成二维码的配置
            Map<EncodeHintType, Object> hints = new HashMap<>(10);
            // 设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            // 设置编码类型
            hints.put(EncodeHintType.CHARACTER_SET, QRCODE_CHARACTER_SET);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, hints);

            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, QRCODE_FORMAT, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
